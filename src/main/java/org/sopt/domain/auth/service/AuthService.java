package org.sopt.domain.auth.service;

import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.sopt.domain.auth.dto.response.TokenResponse;
import org.sopt.domain.auth.entity.RefreshToken;
import org.sopt.domain.auth.exception.AuthErrorCode;
import org.sopt.domain.auth.exception.AuthException;
import org.sopt.domain.auth.repository.RefreshTokenRepository;
import org.sopt.domain.user.dto.response.UserResponse;
import org.sopt.domain.user.entity.User;
import org.sopt.domain.user.exception.UserErrorCode;
import org.sopt.domain.user.exception.UserException;
import org.sopt.domain.user.repository.UserRepository;
import org.sopt.global.jwt.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    @Value("${security.jwt.refresh-token-expires-in-seconds:1209600}")
    private long refreshTokenExpiresInSeconds;

    public UserResponse loginWithCredentials(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        if (!user.getPassword().equals(password)) {
            throw new AuthException(AuthErrorCode.INVALID_CREDENTIALS);
        }

        return UserResponse.from(user);
    }

    @Transactional
    public TokenResponse login(String email, String password) {
        UserResponse user = loginWithCredentials(email, password);

        String accessToken = jwtService.generateAccessToken(user.id(), user.email());
        String refreshToken = jwtService.generateRefreshToken(user.id());

        // 기존 Refresh Token 삭제 후 새로 저장
        refreshTokenRepository.deleteByUserId(user.id());
        refreshTokenRepository.save(
                RefreshToken.of(user.id(), refreshToken, refreshTokenExpiresInSeconds)
        );

        return TokenResponse.of(accessToken, refreshToken);
    }

    @Transactional
    public TokenResponse reissue(String refreshTokenValue) {
        // DB에서 Refresh Token 조회
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenValue)
                .orElseThrow(() -> new AuthException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND));

        // 만료 여부 확인
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
        refreshToken.rotate(newRefreshToken, refreshTokenExpiresInSeconds);

        return TokenResponse.of(newAccessToken, newRefreshToken);
    }

    public UserResponse getMemberById(Long memberId) {
        User member = userRepository.findById(memberId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        return UserResponse.from(member);
    }
}