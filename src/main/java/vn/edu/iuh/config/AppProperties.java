package vn.edu.iuh.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private final Auth auth = new Auth();

    @Getter
    public static class Auth {
        private String tokenSecret;
        private long refreshTokenExpirationMilliseconds;
        private long accessTokenExpirationMilliseconds;
        private long resetTokenExpirationMilliseconds;
        private long registerTokenExpirationMilliseconds;
    }
}
