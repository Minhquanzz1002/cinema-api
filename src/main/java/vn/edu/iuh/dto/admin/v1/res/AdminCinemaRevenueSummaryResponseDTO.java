package vn.edu.iuh.dto.admin.v1.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminCinemaRevenueSummaryResponseDTO {
    private String cinemaName;
    private float totalRevenue;
}
