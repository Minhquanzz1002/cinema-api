package vn.edu.iuh.dto.admin.v1.cinema.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.iuh.models.enums.BaseStatus;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCinemaRequest {
    @NotBlank(message = "Tên rạp không được để trống")
    @Size(min = 5, max = 255, message = "Tên rạp phải từ 5 đến 255 ký tự")
    private String name;

    @NotBlank(message = "Địa chỉ không được để trống")
    @Size(max = 500, message = "Địa chỉ không được vượt quá 500 ký tự")
    private String address;

    @NotBlank(message = "Phường/Xã không được để trống")
    private String ward;

    @NotBlank(message = "Mã Phường/Xã không được để trống")
    private String wardCode;

    @NotBlank(message = "Quận/Huyện không được để trống")
    private String district;

    @NotBlank(message = "Mã Quận/Huyện không được để trống")
    private String districtCode;

    @NotNull(message = "Thành phố không được để trống")
    private String city;

    @NotNull(message = "Mã Thành phố không được để trống")
    private String cityCode;

    @Size(max = 10, message = "Không được vượt quá 10 ảnh")
    private List<String> images;

    private String hotline;

    @NotNull(message = "Trạng thái không được để trống")
    private BaseStatus status;
}