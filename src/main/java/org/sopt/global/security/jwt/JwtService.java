package org.sopt.global.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import org.sopt.domain.auth.exception.AuthErrorCode;
import org.sopt.global.exception.CustomException;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtProperties jwtProperties;
    private final Algorithm algorithm;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.algorithm = Algorithm.HMAC256(jwtProperties.getSecret());
    }

    // Access Token 생성
    public String generateAccessToken(Long memberId, String email) {
        Instant now = Instant.now();
        return JWT.create()
                .withSubject(String.valueOf(memberId))
                .withClaim("email", email)
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(now.plusSeconds(jwtProperties.getExpiration().getAccessTokenExpiresInSeconds())))
                .sign(algorithm);
    }

    // Refresh Token 생성
    public String generateRefreshToken(Long memberId) {
        Instant now = Instant.now();
        return JWT.create()
                .withSubject(String.valueOf(memberId))
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(now.plusSeconds(jwtProperties.getExpiration().getRefreshTokenExpiresInSeconds())))
                .sign(algorithm);
    }

    // 토큰 검증 후 memberId 반환
    public Long verifyAndGetMemberId(String token) {
        if (token == null || token.isBlank()) {
            throw new CustomException(AuthErrorCode.EMPTY_TOKEN);
        }
        try {
            DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
            return Long.parseLong(jwt.getSubject());
        } catch (TokenExpiredException e) {
            // 만료된 토큰은 별도 에러코드로 구분 (클라이언트가 재발급 요청을 보낼 수 있도록)
            throw new CustomException(AuthErrorCode.EXPIRED_TOKEN);
        } catch (JWTVerificationException | NumberFormatException e) {
            throw new CustomException(AuthErrorCode.INVALID_TOKEN);
        }
    }

    // Authorization 헤더에서 Bearer 토큰 추출
    public Optional<String> extractToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith(BEARER_PREFIX)) {
            return Optional.of(header.substring(BEARER_PREFIX.length()).trim());
        }
        return Optional.empty();
    }
}