package vn.edu.iuh.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "zalo-pay")
public class ZaloPayProperties {
    private  String appId;
    private String key1;
    private String key2;
    private String redirectUrl;
    private String callbackUrl;
}
