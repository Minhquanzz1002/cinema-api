package vn.edu.iuh.dto.admin.v1.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import vn.edu.iuh.models.Product;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.models.enums.ProductStatus;

import java.time.LocalDate;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AdminProductPriceOverviewResponseDTO {
    private int id;
    private LocalDate startDate;
    private LocalDate endDate;
    private float price;
    private BaseStatus status;
    private ProductDTO product;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductDTO {
        private int id;
        private String code;
        private String name;
        private String description;
        private String image;
        private ProductStatus status;
    }
}
