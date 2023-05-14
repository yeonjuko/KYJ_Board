package com.example.board.controller;

import com.example.board.domain.Board;
import com.example.board.domain.BoardDTO;
import com.example.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class BoardController {

    @Autowired
    private final BoardService boardService;
    @GetMapping("/board")
    public String list(Model model){
        model.addAttribute("boards", boardService.getBoardList());
        return "board/list";
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable Long id, Model model){
        Board board = boardService.getBoard(id);
        Set<Board> relatedBoards = boardService.getRelatedBoards(id);
        model.addAttribute("board", board);
        model.addAttribute("relatedBoards", relatedBoards);
        return "board/detail";
    }


    @GetMapping("/board/register")
    public String registerGet(){
        return "board/register";
    }

    @PostMapping("/board/register")
    public String registerPost(Board board){
        boardService.createdBoard(board);
        return "redirect:/board";
    }


}
