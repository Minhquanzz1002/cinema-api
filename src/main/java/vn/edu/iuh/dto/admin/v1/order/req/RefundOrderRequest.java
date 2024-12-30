package vn.edu.iuh.dto.admin.v1.order.req;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefundOrderRequest {
    @NotBlank(message = "Lý do không được để trống")
    private String reason;
}
