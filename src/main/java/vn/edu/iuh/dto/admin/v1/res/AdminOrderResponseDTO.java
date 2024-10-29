package vn.edu.iuh.dto.admin.v1.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.iuh.models.enums.OrderStatus;

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
