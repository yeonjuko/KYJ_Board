package com.example.board.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class BoardController {
    @GetMapping("/")
    public String list(){
        return "board/list";
    }

    @GetMapping("/write")
    public String write(){
        return "board/write";
    }

}
