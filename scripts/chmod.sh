#!/bin/bash

# 전체 app 디렉토리 소유권 변경
chown -R ec2-user:ec2-user /home/ec2-user/app

# scripts 폴더 내 쉘 스크립트들 소유권과 실행권한 설정
chown ec2-user:ec2-user /home/ec2-user/app/scripts/*.sh
chmod +x /home/ec2-user/app/scripts/*.sh
