package vn.edu.iuh.projections.admin.v1;

import vn.edu.iuh.models.enums.BaseStatus;

import java.time.LocalDate;

public interface AdminPromotionOverviewProjection {
    int getId();
    String getCode();
    String getName();
    LocalDate getStartDate();
    LocalDate getEndDate();
    BaseStatus getStatus();
}
