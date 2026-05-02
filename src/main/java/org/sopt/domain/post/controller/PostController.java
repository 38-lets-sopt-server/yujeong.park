package org.sopt.domain.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sopt.domain.post.dto.response.*;
import org.sopt.global.api.CommonResponse;
import org.sopt.domain.post.dto.request.CreatePostRequest;
import org.sopt.domain.post.dto.request.UpdatePostRequest;
import org.sopt.domain.post.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Post", description = "게시글 API")
@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Operation(summary = "게시글 작성", description = "새로운 게시글을 작성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "게시글 작성 성공"),
            @ApiResponse(responseCode = "400", description = "유효성 검증 실패 (제목/내용 누락 또는 제목 50자 초과)"),
            @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음")
    })
    @PostMapping
    public ResponseEntity<CommonResponse<CreatePostResponse>> createPost(
            @Valid @RequestBody CreatePostRequest request
    ) {
        CreatePostResponse response = postService.createPost(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CommonResponse.created("게시글 등록 완료!", response));
    }

    @Operation(summary = "게시글 목록 조회", description = "전체 게시글 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping
    public ResponseEntity<CommonResponse<List<PostListResponse>>> getAllPosts() {
        List<PostListResponse> posts = postService.getAllPosts();
        return ResponseEntity
                .ok(CommonResponse.ok("게시글 목록 조회 완료!", posts));
    }

    @Operation(summary = "게시글 단건 조회", description = "게시글 ID로 특정 게시글을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<PostResponse>> getPost(
            @Parameter(description = "조회할 게시글 ID", example = "1", required = true)
            @PathVariable Long id
    ) {
        PostResponse post = postService.getPost(id);
        return ResponseEntity
                .ok(CommonResponse.ok("게시글 단건 조회 완료!", post));
    }

    @Operation(summary = "게시글 수정", description = "게시글 ID로 특정 게시글을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "400", description = "유효성 검증 실패 (제목/내용 누락 또는 제목 50자 초과)"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<UpdatePostResponse>> updatePost(
            @Parameter(description = "수정할 게시글 ID", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody UpdatePostRequest request
    ) {
        UpdatePostResponse response = postService.updatePost(id, request);
        return ResponseEntity
                .ok(CommonResponse.ok("게시글 수정 완료!", response));
    }

    @Operation(summary = "게시글 삭제", description = "게시글 ID로 특정 게시글을 삭제합니다. soft delete 처리됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<Void>> deletePost(
            @Parameter(description = "삭제할 게시글 ID", example = "1", required = true)
            @PathVariable Long id
    ) {
        postService.deletePost(id);
        return ResponseEntity.ok(CommonResponse.ok("게시글 삭제 완료!"));
    }

    @Operation(summary = "게시글 검색", description = "제목 키워드 또는 작성자 닉네임으로 게시글을 검색합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "검색 성공")
    })
    @GetMapping("/search")
    public ResponseEntity<CommonResponse<List<PostListResponse>>> searchPosts(
            @Parameter(description = "제목 키워드", example = "학식")
            @RequestParam(required = false) String title,
            @Parameter(description = "작성자 닉네임", example = "익명")
            @RequestParam(required = false) String nickname
    ) {
        List<PostListResponse> posts = postService.search(title, nickname);
        return ResponseEntity.ok(CommonResponse.ok("게시글 검색 완료!", posts));
    }
}