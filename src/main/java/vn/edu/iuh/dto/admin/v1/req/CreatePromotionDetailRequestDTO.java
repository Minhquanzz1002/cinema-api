package vn.edu.iuh.dto.admin.v1.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.iuh.models.enums.SeatType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePromotionDetailRequestDTO {
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
}
