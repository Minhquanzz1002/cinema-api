package vn.edu.iuh.modules.zalopay;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.edu.iuh.modules.zalopay.order.OrderZaloAPI;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ZaloPay {
    private final OrderZaloAPI orderZaloAPI;

    public Map<String, Object> createOrderZaloPay(String appTransId ,String appUser, long amount) throws IOException {
        return orderZaloAPI.createOrder(appTransId , appUser, amount);
    }

    public Map<String, Object> getOrderZaloPay(String appTransId) throws IOException, URISyntaxException {
        return orderZaloAPI.getOrder(appTransId);
    }
}
