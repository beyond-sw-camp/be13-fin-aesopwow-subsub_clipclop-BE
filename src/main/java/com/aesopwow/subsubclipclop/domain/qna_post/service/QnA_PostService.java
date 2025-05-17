package com.aesopwow.subsubclipclop.domain.qna_post.service;

import com.aesopwow.subsubclipclop.domain.qna_post.dto.QnA_PostRequestDto;
import com.aesopwow.subsubclipclop.domain.qna_post.dto.QnA_PostResponseDto;

public interface QnA_PostService {
    QnA_PostResponseDto getQnAPost(Long qnaPostNo);

    QnA_PostResponseDto createQnAPost(QnA_PostRequestDto requestDto);

    QnA_PostResponseDto updateQnAPost(QnA_PostRequestDto requestDto);

    void deleteQnAPost(Long qnaPostNo);
}