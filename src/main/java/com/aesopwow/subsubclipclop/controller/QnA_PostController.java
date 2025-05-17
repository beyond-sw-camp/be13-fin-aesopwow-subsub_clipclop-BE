package com.aesopwow.subsubclipclop.controller;

import com.aesopwow.subsubclipclop.domain.qna_post.dto.QnA_PostRequestDto;
import com.aesopwow.subsubclipclop.domain.qna_post.dto.QnA_PostResponseDto;
import com.aesopwow.subsubclipclop.domain.qna_post.service.QnA_PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/qna-posts")
@RequiredArgsConstructor
public class QnA_PostController {

    private final QnA_PostService qnA_PostService;

    // ✅ 단건 조회
    @GetMapping("/{qnaPostNo}")
    public ResponseEntity<QnA_PostResponseDto> getQnAPost(@PathVariable Long qnaPostNo) {
        QnA_PostResponseDto response = qnA_PostService.getQnAPost(qnaPostNo);
        return ResponseEntity.ok(response);
    }

    // ✅ 생성
    @PostMapping
    public ResponseEntity<QnA_PostResponseDto> createQnAPost(@RequestBody QnA_PostRequestDto requestDto) {
        QnA_PostResponseDto response = qnA_PostService.createQnAPost(requestDto);
        return ResponseEntity.ok(response);
    }

    // ✅ 수정
    @PutMapping("/{qnaPostNo}")
    public ResponseEntity<QnA_PostResponseDto> updateQnAPost(
            @PathVariable Long qnaPostNo,
            @RequestBody QnA_PostRequestDto requestDto
    ) {
        // PathVariable과 DTO의 No가 일치하는지 검증(Optional)
        if (!qnaPostNo.equals(requestDto.getQnaPostNo())) {
            throw new IllegalArgumentException("URL과 Body의 QnA 게시글 번호가 일치하지 않습니다.");
        }

        QnA_PostResponseDto response = qnA_PostService.updateQnAPost(requestDto);
        return ResponseEntity.ok(response);
    }

    // ✅ 삭제
    @DeleteMapping("/{qnaPostNo}")
    public ResponseEntity<Void> deleteQnAPost(@PathVariable Long qnaPostNo) {
        qnA_PostService.deleteQnAPost(qnaPostNo);
        return ResponseEntity.noContent().build();
    }
}