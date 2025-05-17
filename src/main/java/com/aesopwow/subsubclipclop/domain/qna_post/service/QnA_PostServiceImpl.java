package com.aesopwow.subsubclipclop.domain.qna_post.service;

import com.aesopwow.subsubclipclop.domain.qna_post.dto.QnA_PostRequestDto;
import com.aesopwow.subsubclipclop.domain.qna_post.dto.QnA_PostResponseDto;
import com.aesopwow.subsubclipclop.domain.qna_post.repository.QnA_PostRepository;
import com.aesopwow.subsubclipclop.entity.QnaPost;
import com.aesopwow.subsubclipclop.entity.User;
import com.aesopwow.subsubclipclop.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QnA_PostServiceImpl implements QnA_PostService {

    private final QnA_PostRepository qnA_PostRepository;
    private final UserRepository userRepository;

    @Override
    public QnA_PostResponseDto getQnAPost(Long qnaPostNo) {
        QnaPost qnaPost = qnA_PostRepository.findById(qnaPostNo)
                .orElseThrow(() -> new IllegalArgumentException("QnA 게시글이 존재하지 않습니다."));

        return new QnA_PostResponseDto(
                qnaPost.getQnaPostNo(),
                qnaPost.getUser().getUserNo(),
                qnaPost.getTitle(),
                qnaPost.getContent()
        );
    }

    @Override
    public QnA_PostResponseDto createQnAPost(QnA_PostRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserNo())
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        QnaPost qnaPost = QnaPost.builder()
                .user(user)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .build();

        QnaPost saved = qnA_PostRepository.save(qnaPost);

        return new QnA_PostResponseDto(
                saved.getQnaPostNo(),
                saved.getUser().getUserNo(),
                saved.getTitle(),
                saved.getContent()
        );
    }

    @Override
    public QnA_PostResponseDto updateQnAPost(QnA_PostRequestDto requestDto) {
        QnaPost qnaPost = qnA_PostRepository.findById(requestDto.getQnaPostNo())
                .orElseThrow(() -> new IllegalArgumentException("QnA 게시글이 존재하지 않습니다."));

        qnaPost = QnaPost.builder()
                .qnaPostNo(qnaPost.getQnaPostNo())
                .user(qnaPost.getUser())
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .build();

        QnaPost updated = qnA_PostRepository.save(qnaPost);

        return new QnA_PostResponseDto(
                updated.getQnaPostNo(),
                updated.getUser().getUserNo(),
                updated.getTitle(),
                updated.getContent()
        );
    }

    @Override
    public void deleteQnAPost(Long qnaPostNo) {
        QnaPost qnaPost = qnA_PostRepository.findById(qnaPostNo)
                .orElseThrow(() -> new IllegalArgumentException("QnA 게시글이 존재하지 않습니다."));
        qnA_PostRepository.delete(qnaPost);
    }
}