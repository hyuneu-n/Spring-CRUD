package com.jjery.springcrud.post.controller;

import com.jjery.springcrud.post.dto.PostRequestDTO;
import com.jjery.springcrud.post.dto.PostResponseDTO;
import com.jjery.springcrud.post.entity.Post;
import com.jjery.springcrud.post.service.PostService;
import com.jjery.springcrud.user.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "전체 게시글 조회", description = "전체 게시글 조회")
    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getAllPosts() {
        List<PostResponseDTO> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @Operation(summary = "특정 게시글 조회", description = "특정 게시글 조회")
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Long id) {
        PostResponseDTO post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    @Operation(summary = "게시글 작성", description = "게시글 작성")
    @PostMapping("/")
    public ResponseEntity<Post> createPost(
            @RequestHeader("Authorization") String bearerToken,
            @RequestBody PostRequestDTO postRequestDTO) {
        try {
            String token = bearerToken.substring(7);
            Claims claims = jwtUtil.getAllClaimsFromToken(token);
            String loginId = claims.getId(); // 로그인 아이디

            Post createdPost = postService.createPost(postRequestDTO, loginId);
            return ResponseEntity.ok(createdPost);
        } catch (RuntimeException e) {
            log.error(
                    "Error", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "게시글 수정", description = "게시글 수정")
    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDTO> updatePost(
            @RequestHeader("Authorization") String bearerToken,
            @PathVariable Long id, @RequestBody PostRequestDTO postRequestDTO){
        try {
            String token = bearerToken.substring(7);
            Claims claims = jwtUtil.getAllClaimsFromToken(token);
            String loginId = claims.getId(); // 로그인 아이디

            PostResponseDTO updatedPost = postService.updatePost(id, postRequestDTO, loginId);
            return ResponseEntity.ok(updatedPost);
        } catch (RuntimeException e) {
            log.error("Error", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "게시글 삭제", description = "게시글 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<Post> deletePost(
            @RequestHeader("Authorization") String bearerToken,
            @PathVariable Long id) {
        try {
            String token = bearerToken.substring(7);
            Claims claims = jwtUtil.getAllClaimsFromToken(token);
            String loginId = claims.getId(); // 로그인 아이디

            postService.deletePost(id, loginId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
