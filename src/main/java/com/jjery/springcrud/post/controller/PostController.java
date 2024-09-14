package com.jjery.springcrud.post.controller;

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
    public ResponseEntity<List<Post>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @Operation(summary = "특정 게시글 조회", description = "특정 게시글 조회")
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @Operation(summary = "게시글 작성", description = "게시글 작성")
    @PostMapping("/")
    public ResponseEntity<Post> createPost(
            @RequestHeader("Authorization") String bearerToken,
            @RequestBody Post post) {
        try {
            String token = bearerToken.substring(7);
            Claims claims = jwtUtil.getAllClaimsFromToken(token);
            String loginId = claims.getId(); // 로그인 아이디

            postService.createPost(post, loginId);
            return ResponseEntity.ok(post);
        } catch (RuntimeException e) {
            log.error(
                    "Error", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "게시글 수정", description = "게시글 수정")
    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(
            @RequestHeader("Authorization") String bearerToken,
            @PathVariable Long id, @RequestBody Post post){
        try {
            String token = bearerToken.substring(7);
            Claims claims = jwtUtil.getAllClaimsFromToken(token);
            String loginId = claims.getId(); // 로그인 아이디

            postService.updatePost(id, post, loginId);
            return ResponseEntity.ok(post);
        } catch (RuntimeException e) {
            log.error(
                    "Error", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Post> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok().build();
    }
}
