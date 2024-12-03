package vn.edu.iuh.dto.admin.v1.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminMovieRevenueSummaryResponseDTO {
    private String movieName;
    private float totalRevenue;
}
