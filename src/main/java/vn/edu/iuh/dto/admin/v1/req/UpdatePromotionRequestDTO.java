package vn.edu.iuh.dto.admin.v1.req;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;
import vn.edu.iuh.models.enums.BaseStatus;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePromotionRequestDTO {
    @NotBlank(message = "Tên khuyến mãi là bắt buộc")
    private String name;
    @URL(message = "Link ảnh không hợp lệ")
    private String imagePortrait;
    private LocalDate startDate;
    @NotNull(message = "Ngày kết thúc là bắt buộc")
    @FutureOrPresent(message = "Ngày kết thúc phải ở hiện tại hoặc tương lai")
    private LocalDate endDate;
    private BaseStatus status;
    private String description;
}
