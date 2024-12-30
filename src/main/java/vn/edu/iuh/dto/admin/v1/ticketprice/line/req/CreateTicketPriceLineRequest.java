package vn.edu.iuh.dto.admin.v1.ticketprice.line.req;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.models.enums.DayType;

import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTicketPriceLineRequest {
    @NotEmpty(message = "Áp dụng cho ngày không được để trống")
    private List<DayType> applyForDays;
    @NotNull(message = "Thời gian bắt đầu không được để trống")
    private LocalTime startTime;
    @NotNull(message = "Thời gian kết thúc không được để trống")
    private LocalTime endTime;
    private BaseStatus status;
    @NotNull(message = "Giá vé thường không được để trống")
    @Min(value = 0, message = "Giá vé thường phải lớn hơn hoặc bằng 0")
    private float normalPrice;
    @NotNull(message = "Giá vé VIP không được để trống")
    @Min(value = 0, message = "Giá vé VIP phải lớn hơn hoặc bằng 0")
    private float vipPrice;
    @NotNull(message = "Giá vé đôi không được để trống")
    @Min(value = 0, message = "Giá vé đôi phải lớn hơn hoặc bằng 0")
    private float couplePrice;
}
