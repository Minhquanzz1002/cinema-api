package vn.edu.iuh.dto.admin.v1.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardStatsResponseDTO {
    private float totalRevenue;
    private int moviesCount;
    private int showTimesToday;
    private int totalCustomers;
    private int totalEmployees;
    private int totalCinemas;
}
