package com.jjery.springcrud.post.repository;

import com.jjery.springcrud.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public class PostRepository extends JpaRepository<Post, Long> {
}
