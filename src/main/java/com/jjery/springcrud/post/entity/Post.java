package com.jjery.springcrud.post.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Entity
@Slf4j
@Table(name = "post")
@Getter
@Setter
@NoArgsConstructor
public class Post {
    // 게시물 번호 pk
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private int postNum;
    //게시물 제목
    @Column(name = "title")
    private String title;
    //게시물 내용
    @Column(name = "content")
    private String content;
    //게시물 작성 날짜
    @Column
    private LocalDateTime createdAt;
    //수정 날짜
    @Column
    private LocalDateTime updatedAt;
}
