package com.jjery.springcrud.post.service;

import com.jjery.springcrud.post.dto.PostRequestDTO;
import com.jjery.springcrud.post.dto.PostResponseDTO;
import com.jjery.springcrud.post.entity.Post;
import com.jjery.springcrud.post.repository.PostRepository;
import com.jjery.springcrud.user.entity.User;
import com.jjery.springcrud.user.repository.UserRepository;
import com.jjery.springcrud.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<PostResponseDTO> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        List<PostResponseDTO> postResponseDTOs = new ArrayList<>();

        for (Post post : posts) {
            PostResponseDTO dto = convertToPostResponseDTO(post);
            postResponseDTOs.add(dto);
        }
        return postResponseDTOs;
    }

    public PostResponseDTO getPostById(Long id) {
        Post post = postRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Not found post with id: " + id));

        return convertToPostResponseDTO(post);
    }

    @Transactional
    public Post createPost(PostRequestDTO postRequestDTO, String userId) {
        log.info("게시글 작성 요청 수신: 제목 - {}, 작성자 - {}", postRequestDTO.getTitle(), userId);

        User user = userRepository.findByLoginId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = new Post();
        post.setUserId(user);
        post.setAuthor(user.getLoginId());
        post.setTitle(postRequestDTO.getTitle());
        post.setContent(postRequestDTO.getContent());
        post.setCategory(postRequestDTO.getCategory()); // 카테고리 필드 설정
        post.setCreatedAt(LocalDateTime.now());

        Post savedPost = postRepository.save(post);
        log.info("게시글 저장 완료: 게시글 ID - {}, 작성자 - {}", savedPost.getId(), savedPost.getAuthor());

        return savedPost;
    }

    @Transactional
    public PostResponseDTO updatePost(Long id, PostRequestDTO postRequestDTO, String userId) {
        Post postToUpdate = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found post with id: " + id));

        if (!postToUpdate.getAuthor().equals(userId)) {
            throw new RuntimeException("You are not the author of this post");
        }

        postToUpdate.setTitle(postRequestDTO.getTitle());
        postToUpdate.setContent(postRequestDTO.getContent());
        postToUpdate.setCategory(postRequestDTO.getCategory()); // 카테고리 필드 업데이트
        postToUpdate.setUpdatedAt(LocalDateTime.now());

        Post updatedPost = postRepository.save(postToUpdate);
        return convertToPostResponseDTO(updatedPost);
    }

    @Transactional
    public void deletePost(Long id, String userId) {
        Post postToDelete =
                postRepository
                        .findById(id)
                        .orElseThrow(() -> new RuntimeException("Post not found"));
        // 작성자와 로그인한 사용자가 같은지 확인
        if (!postToDelete.getAuthor().equals(userId)) {
            throw new RuntimeException("You are not the author of this post");
        }
        postRepository.delete(postToDelete);
    }

    private PostResponseDTO convertToPostResponseDTO(Post post) {
        PostResponseDTO postResponseDTO = new PostResponseDTO();
        postResponseDTO.setId(post.getId());
        postResponseDTO.setAuthor(post.getAuthor());
        postResponseDTO.setCreatedAt(post.getCreatedAt());
        postResponseDTO.setUpdatedAt(post.getUpdatedAt());
        postResponseDTO.setTitle(post.getTitle());
        postResponseDTO.setContent(post.getContent());

        return postResponseDTO;
    }

    // 카테고리별 게시글 조회 메서드
    public List<PostResponseDTO> getPostsByCategory(String category) {
        List<Post> posts = postRepository.findByCategory(category);
        return posts.stream()
                .map(this::convertToPostResponseDTO)
                .collect(Collectors.toList());
    }
}

