package com.aesopwow.subsubclipclop.domain.comment.repository;

import com.aesopwow.subsubclipclop.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}