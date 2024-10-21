package vn.edu.iuh.projections.admin.v1;

import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.models.enums.SeatType;

public interface AdminPromotionDetailOverviewProjection {
    int getId();
    Float getDiscountValue();
    Float getMaxDiscountValue();
    Float getMinOrderValue();
    int getUsageLimit();
    BaseProductProjection getRequiredProduct();
    BaseProductProjection getGiftProduct();
    int getCurrentUsageCount();
    int getGiftQuantity();
    SeatType getGiftSeatType();
    int getGiftSeatQuantity();
    int getRequiredProductQuantity();
    int getRequiredSeatQuantity();
    SeatType getRequiredSeatType();
    BaseStatus getStatus();
}
