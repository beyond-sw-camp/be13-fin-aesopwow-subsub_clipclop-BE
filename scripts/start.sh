#!/bin/bash

set -e

echo "Start deployment script..."

#  기본 경로 설정
APP_DIR=/home/ec2-user/app
BUILD_DIR=$APP_DIR/build/libs
SCRIPTS_DIR=$APP_DIR/scripts
DEPLOY_LOG=$APP_DIR/deploy.log
APP_LOG=$APP_DIR/app.log
ENV_FILE=$APP_DIR/.env

TIME_NOW=$(date '+%Y-%m-%d %H:%M:%S')

# sha.txt 파일 존재 확인 (이 파일로 어떤 ZIP을 받을지 판단)
if [ ! -f "$SCRIPTS_DIR/sha.txt" ]; then
  echo "$TIME_NOW > ERROR: $SCRIPTS_DIR/sha.txt 파일이 없습니다." >> $DEPLOY_LOG
  exit 1
fi

# sha.txt에서 SHA 값 읽기 → ZIP 파일 이름 구성
SHA=$(cat $SCRIPTS_DIR/sha.txt)
ZIP_NAME="${SHA}.zip"
JAR_NAME="subsubclipclop-0.0.1-SNAPSHOT.jar"
JAR_PATH="$BUILD_DIR/$JAR_NAME"

# 빌드 디렉토리 생성 (없으면)
mkdir -p "$BUILD_DIR"

# S3에서 ZIP 파일 다운로드
echo "$TIME_NOW > Downloading $ZIP_NAME from S3" >> $DEPLOY_LOG
aws s3 cp s3://aesop-be/$ZIP_NAME $BUILD_DIR/$ZIP_NAME >> $DEPLOY_LOG 2>&1
if [ $? -ne 0 ]; then
  echo "$TIME_NOW > Failed to download ZIP file from S3" >> $DEPLOY_LOG
  exit 1
fi

# ZIP 파일 압축 해제
echo "$TIME_NOW > Unzipping $ZIP_NAME" >> $DEPLOY_LOG
unzip -o $BUILD_DIR/$ZIP_NAME -d $BUILD_DIR >> $DEPLOY_LOG 2>&1
if [ $? -ne 0 ]; then
  echo "$TIME_NOW > Failed to unzip file" >> $DEPLOY_LOG
  exit 1
fi

# .env 파일을 읽어서 환경 변수 export
if [ -f "$ENV_FILE" ]; then
  echo "$TIME_NOW > Loading environment variables from .env" >> $DEPLOY_LOG
  export $(grep -v '^#' "$ENV_FILE" | xargs)  # 주석 제외한 라인을 환경 변수로 export
else
  echo "$TIME_NOW > ERROR: $ENV_FILE not found" >> $DEPLOY_LOG
  exit 1
fi

# 기존 실행 중인 애플리케이션 프로세스 종료
EXISTING_PID=$(pgrep -f "$JAR_NAME")
if [ -n "$EXISTING_PID" ]; then
  echo "$TIME_NOW > Stopping existing process (PID: $EXISTING_PID)" >> $DEPLOY_LOG
  kill $EXISTING_PID
  sleep 5
  # 혹시 아직도 살아있으면 강제 종료
  if ps -p $EXISTING_PID > /dev/null; then
    echo "$TIME_NOW > Force killing process (PID: $EXISTING_PID)" >> $DEPLOY_LOG
    kill -9 $EXISTING_PID
  fi
fi

# 새 애플리케이션 실행 (백그라운드)
echo "$TIME_NOW > Starting JAR file" >> $DEPLOY_LOG
nohup java -jar "$JAR_PATH" --spring.profiles.active=prod >> $APP_LOG 2>&1 &
disown  # 부모 프로세스 종료되어도 계속 실행되도록

# 새 프로세스 PID 로그에 기록
NEW_PID=$!
echo "$TIME_NOW > New process started (PID: $NEW_PID)" >> $DEPLOY_LOG
