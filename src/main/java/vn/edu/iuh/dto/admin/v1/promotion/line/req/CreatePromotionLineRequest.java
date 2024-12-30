package vn.edu.iuh.dto.admin.v1.promotion.line.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.models.enums.PromotionLineType;
import vn.edu.iuh.models.enums.SeatType;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePromotionLineRequest {
    private String code;
    private String name;
    private PromotionLineType type;
    private LocalDate startDate;
    private LocalDate endDate;
    private BaseStatus status;
    private List<PromotionDetailDTO> promotionDetails;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PromotionDetailDTO {
        private Float discountValue;
        private Float maxDiscountValue;
        private Float minOrderValue;
        private SeatType requiredSeatType;
        private int requiredSeatQuantity;
        private SeatType giftSeatType;
        private int giftSeatQuantity;
        private int giftQuantity;
        private int giftProduct;
        private int usageLimit;
        private BaseStatus status;
    }
}
