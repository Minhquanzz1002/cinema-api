package vn.edu.iuh.projections.admin.v1;

import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.models.enums.PromotionLineType;

import java.time.LocalDate;
import java.util.List;

public interface AdminPromotionLineOverviewProjection {
    int getId();
    String getCode();
    String getName();
    LocalDate getStartDate();
    PromotionLineType getType();
    LocalDate getEndDate();
    BaseStatus getStatus();
    List<AdminPromotionDetailOverviewProjection> getPromotionDetails();
}
