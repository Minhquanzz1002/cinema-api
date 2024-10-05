package vn.edu.iuh.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderUpdateDiscountDTO {
    @NotNull(message = "Mã giảm giá là bắt buộc")
    @NotBlank(message = "Mã giảm giá không được để trống")
    private String code;
}
