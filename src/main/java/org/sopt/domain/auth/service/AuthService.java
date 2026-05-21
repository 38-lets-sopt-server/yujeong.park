package org.sopt.domain.auth.service;

import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.sopt.domain.auth.dto.TokenResult;
import org.sopt.domain.auth.dto.request.SignUpRequest;
import org.sopt.domain.auth.dto.response.SignUpResponse;
import org.sopt.domain.auth.entity.RefreshToken;
import org.sopt.domain.auth.exception.AuthErrorCode;
import org.sopt.domain.auth.exception.AuthException;
import org.sopt.domain.auth.repository.RefreshTokenRepository;
import org.sopt.domain.user.entity.User;
import org.sopt.domain.user.exception.UserErrorCode;
import org.sopt.domain.user.exception.UserException;
import org.sopt.domain.user.repository.UserRepository;
import org.sopt.global.security.jwt.JwtProperties;
import org.sopt.global.security.jwt.JwtService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;
    private final BCryptPasswordEncoder passwordEncoder;

    // 회원가입
    @Transactional
    public SignUpResponse signUp(SignUpRequest request) {
        // 이메일 중복 확인
        if (userRepository.existsByEmail(request.email())) {
            throw new AuthException(AuthErrorCode.EMAIL_DUPLICATED);
        }

        // 비밀번호 암호화 후 저장
        User user = new User(
                request.nickname(), request.email(), passwordEncoder.encode(request.password())
        );
        userRepository.save(user);

        return SignUpResponse.from(user);
    }

    // 이메일/비밀번호 검증 후 유저 반환
    private User loginWithCredentials(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        // 평문 비밀번호와 암호화된 비밀번호 비교
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthException(AuthErrorCode.INVALID_CREDENTIALS);
        }

        return user;
    }

    // 로그인
    @Transactional
    public TokenResult login(String email, String password) {
        User user = loginWithCredentials(email, password);

        // Access Token, Refresh Token 발급
        String accessToken = jwtService.generateAccessToken(user.getId(), user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getId());

        // 기존 Refresh Token 삭제 후 새로 저장
        refreshTokenRepository.deleteByUserId(user.getId());
        refreshTokenRepository.save(
                RefreshToken.of(user.getId(), refreshToken,
                        jwtProperties.getExpiration().getRefreshTokenExpiresInSeconds())
        );

        return TokenResult.of(accessToken, refreshToken);
    }

    // 토큰 재발급
    @Transactional
    public TokenResult reissue(String refreshTokenValue) {
        // DB에서 Refresh Token 조회
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenValue)
                .orElseThrow(() -> new AuthException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND));

        // Refresh Token 만료 여부 확인
        if (refreshToken.getExpiresAt().isBefore(Instant.now())) {
            throw new AuthException(AuthErrorCode.REFRESH_TOKEN_EXPIRED);
        }

        // JWT 서명 검증 후 유저 조회
        Long userId = jwtService.verifyAndGetMemberId(refreshTokenValue);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        // 새 Access Token + 새 Refresh Token 발급 후 Rotate
        String newAccessToken = jwtService.generateAccessToken(user.getId(), user.getEmail());
        String newRefreshToken = jwtService.generateRefreshToken(user.getId());
        refreshToken.rotate(newRefreshToken,
                jwtProperties.getExpiration().getRefreshTokenExpiresInSeconds());

        return TokenResult.of(newAccessToken, newRefreshToken);
    }
}