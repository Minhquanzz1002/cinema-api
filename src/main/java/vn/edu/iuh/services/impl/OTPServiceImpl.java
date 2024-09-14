package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import vn.edu.iuh.services.OTPService;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class OTPServiceImpl implements OTPService {
    private final StringRedisTemplate redisTemplate;
    private static final String OTP_KEY_PREFIX = "otp:";

    @Async
    @Override
    public void saveOTP(String email, String otp) {
        String key = OTP_KEY_PREFIX + email;
        redisTemplate.opsForValue().set(key, otp);
        redisTemplate.expire(key, 5, TimeUnit.MINUTES);
    }

    @Override
    public boolean validateOTP(String email, String otp) {
        String key = OTP_KEY_PREFIX + email;
        String storedOtp = redisTemplate.opsForValue().get(key);
        if (storedOtp == null) {
            log.error("OTP not found or expired for email: {}", email);
            return false;
        }
        if (storedOtp.equals(otp)) {
            redisTemplate.delete(key);
            log.info("OTP validated successfully for email: {}", email);
            return true;
        } else {
            log.error("Invalid OTP for email: {}", email);
            return false;
        }
    }


}
