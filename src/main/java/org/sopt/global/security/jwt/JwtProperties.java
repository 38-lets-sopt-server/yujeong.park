package org.sopt.global.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {

    private String secret;
    private Expiration expiration;

    @Getter
    @Setter
    public static class Expiration {
        private long accessTokenExpiresInSeconds;
        private long refreshTokenExpiresInSeconds;
    }
}