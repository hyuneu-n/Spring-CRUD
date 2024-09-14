package com.jjery.springcrud.post.service;

import com.jjery.springcrud.post.repository.PostRepository;
import com.jjery.springcrud.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;

    public List<Post> findAll() {}


}
