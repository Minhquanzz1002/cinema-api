package vn.edu.iuh.dto.common.zalopay.req;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderZaloPayRequestDTO {
    @NotNull(message = "Mã đơn hàng không được để trống")
    private UUID orderId;
}
