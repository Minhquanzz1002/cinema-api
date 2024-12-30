package vn.edu.iuh.dto.admin.v1.product.price.req;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.iuh.models.enums.BaseStatus;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductPriceRequest {
    @NotNull(message = "Trạng thái không được để trống")
    private BaseStatus status;
    @NotNull(message = "Giá không được để trống")
    @Min(value = 0, message = "Giá phải lớn hơn hoặc bằng 0")
    private Float price;
    @FutureOrPresent(message = "Ngày bắt đầu phải từ ngày hiện tại trở đi")
    private LocalDate startDate;
    @FutureOrPresent(message = "Ngày kết thúc phải từ ngày hiện tại trở đi")
    private LocalDate endDate;
}
