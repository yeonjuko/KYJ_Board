package com.example.board.domain;

import com.p6spy.engine.logging.Category;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @CreatedDate
    private LocalDateTime createdDate;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "board_relation",
            joinColumns = @JoinColumn(name = "board_id"),
            inverseJoinColumns = @JoinColumn(name = "related_board_id")
    )
    private Set<Board> relatedBoards = new HashSet<>();

    public void addRelatedBoard(Board board){
        relatedBoards.add(board);
        board.getRelatedBoards().add(this);
    }

}
