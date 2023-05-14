package com.example.board.repository;

import com.example.board.domain.Board;

import com.p6spy.engine.logging.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BoardRepository extends JpaRepository<Board, Long> {
}
