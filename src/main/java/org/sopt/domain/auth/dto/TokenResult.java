package org.sopt.domain.auth.dto;

public record TokenResult(
        String accessToken,
        String refreshToken
) {
    public static TokenResult of(String accessToken, String refreshToken) {
        return new TokenResult(accessToken, refreshToken);
    }
}