package vn.edu.iuh.dto.admin.v1.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.iuh.models.enums.BaseStatus;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductPriceRequestDTO {
    private LocalDate startDate;
    private LocalDate endDate;
    private BaseStatus status;
    private List<ProductDTO> products;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductDTO {
        private int id;
        private float price;
    }
}
