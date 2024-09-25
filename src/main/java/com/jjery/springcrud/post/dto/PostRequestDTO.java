package com.jjery.springcrud.post.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequestDTO {
    private String title;
    private String content;
    private String category; // 카테고리 필드 추가
}
