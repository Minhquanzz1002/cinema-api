package vn.edu.iuh.dto.admin.v1.ticketprice.req;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CopyTicketPriceRequest {
    @NotNull(message = "Vui lòng chọn ngày bắt đầu")
    private LocalDate startDate;
    @NotNull(message = "Vui lòng chọn ngày kết thúc")
    private LocalDate endDate;
}
