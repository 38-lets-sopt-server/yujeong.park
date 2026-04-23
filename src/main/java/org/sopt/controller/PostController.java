package org.sopt.controller;

import org.sopt.common.ApiResponse;
import org.sopt.dto.request.CreatePostRequest;
import org.sopt.dto.request.UpdatePostRequest;
import org.sopt.dto.response.*;
import org.sopt.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // POST /posts
    @PostMapping
    public ResponseEntity<ApiResponse<CreatePostResponse>> createPost(
            @RequestBody CreatePostRequest request
    ) {
        CreatePostResponse response = postService.createPost(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("게시글 등록 완료!", response));
    }

    // GET /posts
    @GetMapping
    public ResponseEntity<ApiResponse<List<PostListResponse>>> getAllPosts() {
        List<PostListResponse> posts = postService.getAllPosts();
        return ResponseEntity
                .ok(ApiResponse.success("게시글 목록 조회 완료!", posts));
    }

    // GET /posts/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> getPost(
            @PathVariable Long id
    ) {
        PostResponse post = postService.getPost(id);
        return ResponseEntity
                .ok(ApiResponse.success("게시글 단건 조회 완료!", post));
    }

    // PUT /posts/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UpdatePostResponse>> updatePost(
            @PathVariable Long id,
            @RequestBody UpdatePostRequest request
    ) {
        UpdatePostResponse response = postService.updatePost(id, request);
        return ResponseEntity
                .ok(ApiResponse.success("게시글 수정 완료!", response));
    }

    // DELETE /posts/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<DeletePostResponse>> deletePost(
            @PathVariable Long id
    ) {
        DeletePostResponse response = postService.deletePost(id);
        return ResponseEntity
                .ok(ApiResponse.success("게시글 삭제 완료!", response));
    }
}