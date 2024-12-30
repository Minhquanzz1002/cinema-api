package vn.edu.iuh.dto.client.v1.order.req;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderProductRequest {
    @NotEmpty(message = "Danh sách sản phẩm không được rỗng")
    private List<Product> products;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Product {
        @NotNull(message = "ID không được rỗng")
        private Integer id;
        @Min(value = 1, message = "Số lượng tối thiểu phải lớn hơn 0")
        private int quantity;
    }
}
