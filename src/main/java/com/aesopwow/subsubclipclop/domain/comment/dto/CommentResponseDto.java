package com.aesopwow.subsubclipclop.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {
    private Long commentNo;
    private Long qnaPostNo;
    private Long userNo;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}