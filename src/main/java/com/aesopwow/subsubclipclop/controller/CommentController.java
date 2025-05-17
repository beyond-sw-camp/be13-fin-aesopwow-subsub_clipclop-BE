package com.aesopwow.subsubclipclop.controller;

import com.aesopwow.subsubclipclop.domain.comment.dto.CommentRequestDto;
import com.aesopwow.subsubclipclop.domain.comment.dto.CommentResponseDto;
import com.aesopwow.subsubclipclop.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{commentNo}")
    public ResponseEntity<CommentResponseDto> getComment(@PathVariable Long commentNo) {
        return ResponseEntity.ok(commentService.getComment(commentNo));
    }

    @PostMapping("")
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentRequestDto requestDto) {
        return ResponseEntity.ok(commentService.createComment(requestDto));
    }

    @PutMapping("/{commentNo}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable Long commentNo,
            @RequestBody CommentRequestDto requestDto
    ) {
        if (!commentNo.equals(requestDto.getCommentNo())) {
            throw new IllegalArgumentException("URL과 Body의 댓글 번호가 일치하지 않습니다.");
        }

        return ResponseEntity.ok(commentService.updateComment(requestDto));
    }

    @DeleteMapping("/{commentNo}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentNo) {
        commentService.deleteComment(commentNo);
        return ResponseEntity.noContent().build();
    }
}