package org.sopt.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.domain.auth.dto.request.LoginRequest;
import org.sopt.domain.auth.dto.request.ReissueRequest;
import org.sopt.domain.auth.dto.response.TokenResponse;
import org.sopt.domain.auth.service.AuthService;
import org.sopt.domain.user.dto.response.UserResponse;
import org.sopt.global.api.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "로그인 (Access Token + Refresh Token 발급)")
    @PostMapping("/login")
    public ResponseEntity<CommonResponse<TokenResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        TokenResponse tokens = authService.login(request.email(), request.password());
        return ResponseEntity.ok(CommonResponse.ok("로그인 완료!", tokens));
    }

    @Operation(summary = "토큰 재발급 (Refresh Token → 새 Access Token + Refresh Token)")
    @PostMapping("/reissue")
    public ResponseEntity<CommonResponse<TokenResponse>> reissue(
            @Valid @RequestBody ReissueRequest request
    ) {
        TokenResponse tokens = authService.reissue(request.refreshToken());
        return ResponseEntity.ok(CommonResponse.ok("토큰 재발급 완료!", tokens));
    }

    @Operation(summary = "내 정보 조회 (Access Token 검증)")
    @GetMapping("/me")
    public ResponseEntity<CommonResponse<UserResponse>> me(Authentication authentication) {
        Long memberId = Long.parseLong(authentication.getName());
        UserResponse user = authService.getMemberById(memberId);
        return ResponseEntity.ok(CommonResponse.ok("내 정보 조회 성공!", user));
    }
}