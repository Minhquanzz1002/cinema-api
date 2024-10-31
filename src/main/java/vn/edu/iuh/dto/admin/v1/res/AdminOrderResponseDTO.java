package vn.edu.iuh.dto.admin.v1.res;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.iuh.models.enums.OrderStatus;
import vn.edu.iuh.models.enums.RefundStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminOrderResponseDTO {
    private UUID id;
    private String code;
    private float totalPrice;
    private float finalAmount;
    private float totalDiscount;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private UserDTO user;
    private String cancelReason;
    private Float refundAmount;
    private LocalDateTime refundDate;
    private RefundStatus refundStatus;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserDTO {
        private UUID id;
        private String name;
        private String email;
        private String phone;
    }
}
