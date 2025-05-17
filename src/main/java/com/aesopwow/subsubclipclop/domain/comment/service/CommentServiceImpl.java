package com.aesopwow.subsubclipclop.domain.comment.service;

import com.aesopwow.subsubclipclop.domain.comment.dto.CommentRequestDto;
import com.aesopwow.subsubclipclop.domain.comment.dto.CommentResponseDto;
import com.aesopwow.subsubclipclop.domain.comment.repository.CommentRepository;
import com.aesopwow.subsubclipclop.domain.qna_post.repository.QnA_PostRepository;
import com.aesopwow.subsubclipclop.domain.user.repository.UserRepository;
import com.aesopwow.subsubclipclop.entity.Comment;
import com.aesopwow.subsubclipclop.entity.QnaPost;
import com.aesopwow.subsubclipclop.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final QnA_PostRepository qnA_PostRepository;
    private final UserRepository userRepository;

    @Override
    public CommentResponseDto getComment(Long commentNo) {
        Comment comment = commentRepository.findById(commentNo)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
        return toDto(comment);
    }

    @Override
    public CommentResponseDto createComment(CommentRequestDto requestDto) {
        QnaPost post = qnA_PostRepository.findById(requestDto.getQnaPostNo())
                .orElseThrow(() -> new IllegalArgumentException("QnA 게시글이 존재하지 않습니다."));
        User user = userRepository.findById(requestDto.getUserNo())
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        Comment comment = Comment.builder()
                .qnaPost(post)
                .user(user)
                .content(requestDto.getContent())
                .build();

        return toDto(commentRepository.save(comment));
    }

    @Override
    public CommentResponseDto updateComment(CommentRequestDto requestDto) {
        Comment comment = commentRepository.findById(requestDto.getCommentNo())
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        Comment updated = Comment.builder()
                .commentNo(comment.getCommentNo())
                .qnaPost(comment.getQnaPost())
                .user(comment.getUser())
                .content(requestDto.getContent())
                .build();

        return toDto(commentRepository.save(updated));
    }

    @Override
    public void deleteComment(Long commentNo) {
        Comment comment = commentRepository.findById(commentNo)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
        commentRepository.delete(comment);
    }

    private CommentResponseDto toDto(Comment comment) {
        return new CommentResponseDto(
                comment.getCommentNo(),
                comment.getQnaPost().getQnaPostNo(),
                comment.getUser().getUserNo(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}