package com.jjery.springcrud.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PostResponseDTO {
    private String title;
    private String content;
    private String author;

}
