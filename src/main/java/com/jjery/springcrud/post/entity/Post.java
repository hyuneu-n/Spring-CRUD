package com.jjery.springcrud.post.entity;

import com.jjery.springcrud.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

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
    @JoinColumn(name = "user_id", referencedColumnName = "login_id")
    private User userId;
    //게시물 제목
    @Column(name = "title")
    private String title;
    //게시물 내용
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;
    //작성자
    @Column(name = "author")
    private String author;
    //게시물 작성 날짜
    @Column
    @CreatedDate
    private LocalDateTime createdAt;
    //수정 날짜
    @Column
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
