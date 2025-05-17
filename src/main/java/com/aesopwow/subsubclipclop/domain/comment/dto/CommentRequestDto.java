package com.aesopwow.subsubclipclop.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {
    private Long commentNo;
    private Long qnaPostNo;
    private Long userNo;
    private String content;
}