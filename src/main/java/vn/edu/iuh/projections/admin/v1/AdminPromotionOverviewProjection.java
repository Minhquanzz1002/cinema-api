package vn.edu.iuh.projections.admin.v1;

import vn.edu.iuh.models.enums.BaseStatus;

import java.time.LocalDate;
import java.util.List;

public interface AdminPromotionOverviewProjection {
    int getId();
    String getCode();
    String getName();
    String getImagePortrait();
    LocalDate getStartDate();
    LocalDate getEndDate();
    String getDescription();
    BaseStatus getStatus();
    List<AdminPromotionLineOverviewProjection> getPromotionLines();
}
