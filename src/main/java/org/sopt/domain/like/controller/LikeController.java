package org.sopt.domain.like.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sopt.domain.like.service.LikeService;
import org.sopt.global.api.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Like", description = "게시글 좋아요 API")
@RestController
@RequestMapping("/api/v1/posts/{postId}/likes/{userId}")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @Operation(summary = "좋아요 추가", description = "게시글에 좋아요를 추가합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "좋아요 추가 성공"),
            @ApiResponse(responseCode = "404", description = "게시글 또는 유저를 찾을 수 없음"),
            @ApiResponse(responseCode = "409", description = "이미 좋아요를 누른 게시글 / 일시적인 오류 발생")
    })
    @PostMapping
    public ResponseEntity<CommonResponse<Void>> addLike(
            @Parameter(description = "유저 ID", example = "1", required = true)
            @PathVariable Long userId,
            @Parameter(description = "게시글 ID", example = "1", required = true)
            @PathVariable Long postId
    ) {
        likeService.addLike(postId, userId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CommonResponse.created("좋아요 추가 완료!"));
    }

    @Operation(summary = "좋아요 취소", description = "게시글에 좋아요를 취소합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "좋아요 취소 성공"),
            @ApiResponse(responseCode = "404", description = "게시글, 유저 또는 좋아요를 찾을 수 없음"),
            @ApiResponse(responseCode = "409", description = "일시적인 오류 발생")
    })
    @DeleteMapping
    public ResponseEntity<CommonResponse<Void>> cancelLike(
            @Parameter(description = "유저 ID", example = "1", required = true)
            @PathVariable Long userId,
            @Parameter(description = "게시글 ID", example = "1", required = true)
            @PathVariable Long postId
    ) {
        likeService.cancelLike(postId, userId);
        return ResponseEntity.ok(CommonResponse.ok("좋아요 취소 완료!"));
    }
}