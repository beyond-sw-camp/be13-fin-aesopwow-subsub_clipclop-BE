package com.aesopwow.subsubclipclop.domain.comment.service;

import com.aesopwow.subsubclipclop.domain.comment.dto.CommentRequestDto;
import com.aesopwow.subsubclipclop.domain.comment.dto.CommentResponseDto;

public interface CommentService {
    CommentResponseDto getComment(Long commentNo);
    CommentResponseDto createComment(CommentRequestDto requestDto);
    CommentResponseDto updateComment(CommentRequestDto requestDto);
    void deleteComment(Long commentNo);
}