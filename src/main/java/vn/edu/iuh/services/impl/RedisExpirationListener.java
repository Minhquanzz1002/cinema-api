package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;
import vn.edu.iuh.models.Order;
import vn.edu.iuh.models.enums.OrderStatus;
import vn.edu.iuh.repositories.OrderRepository;

import java.util.Optional;
import java.util.UUID;

import static vn.edu.iuh.services.impl.OrderServiceImpl.ORDER_KEY_PREFIX;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisExpirationListener implements MessageListener {
    private final OrderRepository orderRepository;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = new String(message.getBody());
        log.info("Received expiration event for key: {}", expiredKey);

        if (expiredKey.startsWith(ORDER_KEY_PREFIX)) {
            UUID orderId = UUID.fromString(expiredKey.substring(ORDER_KEY_PREFIX.length()));
            Optional<Order> optionalOrder = orderRepository.findById(orderId);
            if (optionalOrder.isPresent()) {
                Order order = optionalOrder.get();
                if (order.getStatus() == OrderStatus.PENDING) {
                    orderRepository.delete(order);
                    log.info("Order with ID: {} has been deleted due to expiration", orderId);
                }
            }
        }
    }
}
