package com.aesopwow.subsubclipclop.domain.qna_post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QnA_PostResponseDto {
    private Long qnaPostNo;
    private Long userNo;
    private String title;
    private String content;
}