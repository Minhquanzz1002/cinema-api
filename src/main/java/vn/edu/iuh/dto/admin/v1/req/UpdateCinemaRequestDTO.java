package vn.edu.iuh.dto.admin.v1.req;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.iuh.models.enums.BaseStatus;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCinemaRequestDTO {
    @NotBlank(message = "Tên rạp không được để trống")
    private String name;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

    @NotBlank(message = "Phường/Xã không được để trống")
    private String ward;

    @NotBlank(message = "Quận/Huyện không được để trống")
    private String district;

    private Integer cityId;

    private List<String> images;

    private String hotline;

    private BaseStatus status;
}