package vn.edu.iuh.dto.admin.v1.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.iuh.controllers.admin.v1.PromotionController;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.models.enums.ProductStatus;
import vn.edu.iuh.models.enums.SeatType;
import vn.edu.iuh.projections.admin.v1.BaseProductProjection;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminPromotionSummaryResponseDTO {
    private int id;
    private String code;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String type;
    private String status;
    private List<PromotionDetailDTO> promotionDetails;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PromotionDetailDTO {
        private int id;
        Float discountValue;
        Float maxDiscountValue;
        Float minOrderValue;
        int usageLimit;
        ProductDTO requiredProduct;
        ProductDTO giftProduct;
        int currentUsageCount;
        int giftQuantity;
        SeatType giftSeatType;
        int giftSeatQuantity;
        int requiredProductQuantity;
        int requiredSeatQuantity;
        SeatType requiredSeatType;
        BaseStatus status;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ProductDTO {
            int id;
            String code;
            String name;
            String description;
            String image;
            ProductStatus status;
        }
    }
}
