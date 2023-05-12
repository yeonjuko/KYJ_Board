package com.example.board.web.dto;

import com.example.board.domain.Posts;

import java.time.LocalDateTime;

public class PostsResponseDto {

    private Long id;
    private String title;
    private String content;

    private LocalDateTime createdDate;

    public PostsResponseDto(Posts entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
    }
}
