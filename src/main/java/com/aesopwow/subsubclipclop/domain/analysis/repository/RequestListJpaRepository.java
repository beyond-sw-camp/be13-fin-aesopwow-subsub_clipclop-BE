package com.aesopwow.subsubclipclop.domain.analysis.repository;

import com.aesopwow.subsubclipclop.entity.RequireList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestListJpaRepository extends JpaRepository<RequireList, Long> {
}