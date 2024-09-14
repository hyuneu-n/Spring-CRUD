package com.jjery.springcrud.post.service;

import com.jjery.springcrud.post.entity.Post;
import com.jjery.springcrud.post.repository.PostRepository;
import com.jjery.springcrud.user.entity.User;
import com.jjery.springcrud.user.repository.UserRepository;
import com.jjery.springcrud.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(Long id) {
        return postRepository
                .findById(id)
                .orElseThrow(()->new RuntimeException("Not found post with id: " + id));
    }

    @Transactional
    public Post createPost(Post post, String userId) {
        User user =
                userRepository
                        .findByLoginId(userId)
                        .orElseThrow(()-> new RuntimeException("User not found"));
        post.setUserId(user);
        post.setAuthor(user.getLoginId());
        return postRepository.save(post);
    }

    @Transactional
    public Post updatePost(Long id, Post post, String userId) {
        Post postToUpdate = getPostById(id);
        postToUpdate.setTitle(post.getTitle());
        postToUpdate.setContent(post.getContent());
        return postRepository.save(postToUpdate);
    }

    @Transactional
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
