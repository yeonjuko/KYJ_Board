package com.example.board.domain;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class BoardDTO {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdDate;
    private List<Long> relatedBoardIds;


    public BoardDTO(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.createdDate = board.getCreatedDate();
        this.relatedBoardIds = board.getRelatedBoards()
                .stream()
                .map(Board::getId)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdDate;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdDate = createdAt;
    }

    public List<Long> getRelatedBoardIds() {
        return relatedBoardIds;
    }

    public void setRelatedBoardIds(List<Long> relatedBoardIds) {
        this.relatedBoardIds = relatedBoardIds;
    }
}
