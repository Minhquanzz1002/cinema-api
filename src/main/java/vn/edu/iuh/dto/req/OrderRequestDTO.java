package vn.edu.iuh.dto.req;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {
    @NotNull(message = "ID lịch chiếu không được rỗng")
    private UUID showTimeId;
    private List<Product> products;
    @NotEmpty(message = "Danh sách ghế không được rỗng")
    private List<Integer> seatIds;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Product {
        @NotNull(message = "ID không được rỗng")
        private Integer id;
        @Min(value = 1, message = "Số lượng tối thiểu phải lớn hơn 0")
        private int quantity;
    }
}
