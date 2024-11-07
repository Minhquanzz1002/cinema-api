package vn.edu.iuh.dto.admin.v1.res;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class AdminDailyReportResponseDTO {
    private String employeeCode;
    private String employeeName;
    private LocalDate date;
    private float totalPrice;
    private float totalDiscount;
    private float finalAmount;

    public AdminDailyReportResponseDTO(String employeeCode, String employeeName, LocalDate date, float totalPrice, float totalDiscount, float finalAmount) {
        this.employeeCode = employeeCode;
        this.employeeName = employeeName;
        this.date = date;
        this.totalPrice = totalPrice;
        this.totalDiscount = totalDiscount;
        this.finalAmount = finalAmount;
    }
}
