package vn.edu.iuh.dto.admin.v1.req;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.iuh.models.enums.BaseStatus;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTicketPriceRequestDTO {
    @NotBlank(message = "Tên không được để trống")
    private String name;
    @NotNull(message = "Ngày bắt đầu không được để trống")
    @FutureOrPresent(message = "Ngày bắt đầu phải là một ngày trong tương lai hoặc hiện tại")
    private LocalDate startDate;
    @NotNull(message = "Ngày kết thúc không được để trống")
    @FutureOrPresent(message = "Ngày kết thú phải là một ngày trong tương lai")
    private LocalDate endDate;
    @NotNull(message = "Trạng thái không được để trống")
    private BaseStatus status;
}
