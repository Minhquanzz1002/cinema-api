package vn.edu.iuh.dto.admin.v1.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCinemaRequestDTO {
    @NotBlank(message = "Tên rạp không được để trống")
    @Size(min = 5, max = 255, message = "Tên rạp phải từ 5 đến 255 ký tự")
    private String name;

    @NotBlank(message = "Địa chỉ không được để trống")
    @Size(max = 500, message = "Địa chỉ không được vượt quá 500 ký tự")
    private String address;

    @NotBlank(message = "Phường/Xã không được để trống")
    @Size(max = 100, message = "Tên phường/xã không được vượt quá 100 ký tự")
    private String ward;

    @NotBlank(message = "Quận/Huyện không được để trống")
    @Size(max = 100, message = "Tên quận/huyện không được vượt quá 100 ký tự")
    private String district;

    @NotNull(message = "ID thành phố không được để trống")
    private Integer cityId;

    @Size(max = 10, message = "Không được vượt quá 10 ảnh")
    private List<String> images;

    private String hotline;

    // Custom validation method nếu cần
    public boolean isValid() {
        // Kiểm tra các điều kiện logic phức tạp hơn nếu cần
        return true;
    }
}