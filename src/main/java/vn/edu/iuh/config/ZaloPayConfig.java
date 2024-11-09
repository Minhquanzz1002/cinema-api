package vn.edu.iuh.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.edu.iuh.modules.zalopay.order.OrderZaloAPI;

@Configuration
public class ZaloPayConfig {
    @Bean
    public OrderZaloAPI orderZaloAPI(ZaloPayProperties zaloPayProperties, ObjectMapper objectMapper) {
        return new OrderZaloAPI(zaloPayProperties, objectMapper);
    }
}
