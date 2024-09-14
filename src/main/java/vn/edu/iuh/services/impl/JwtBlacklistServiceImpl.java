package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import vn.edu.iuh.services.JwtBlacklistService;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class JwtBlacklistServiceImpl implements JwtBlacklistService {
    private final StringRedisTemplate redisTemplate;

    @Override
    public void saveToken(String token) {
        String key = "blacklist" + token;
        redisTemplate.opsForValue().set(key, "blacklisted", Duration.ofSeconds(3600));
    }

}
