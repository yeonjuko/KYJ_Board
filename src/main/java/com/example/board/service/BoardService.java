package com.example.board.service;

import com.example.board.domain.Board;
import com.example.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    public void register(Board board){
        boardRepository.save(board);
    }
    public List<Board> list(){
        return boardRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public Board detail(Long id){
        return boardRepository.findById(id).orElse(null);
    }

}
