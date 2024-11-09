package vn.edu.iuh.modules.zalopay;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.edu.iuh.modules.zalopay.order.OrderZaloAPI;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ZaloPay {
    private final OrderZaloAPI orderZaloAPI;

    public Map<String, Object> createOrderZaloPay(String appUser, long amount) throws IOException {
        return orderZaloAPI.createOrder(appUser, amount);
    }
}
