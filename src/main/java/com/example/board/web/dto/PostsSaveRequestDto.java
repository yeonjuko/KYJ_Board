package com.example.board.web.dto;

import com.example.board.domain.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
public class PostsSaveRequestDto {
    private String title;
    private String content;
    private LocalDateTime createdDate;

    @Builder
    public PostsSaveRequestDto(String title, String content){
        this.title = title;
        this.content = content;
    }
    
    public Posts toEntity(){
        return Posts.builder().title(title).content(content).build();
    }
}
