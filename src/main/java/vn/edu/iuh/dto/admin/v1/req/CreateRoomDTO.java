package vn.edu.iuh.dto.admin.v1.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import vn.edu.iuh.models.enums.BaseStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRoomDTO {
    @NotBlank(message = "Tên phòng chiếu không được để trống")
    private String name;

    @NotNull(message = "Trạng thái không được để trống")
    private BaseStatus status;

    @NotNull(message = "Mã rạp không được để trống")
    private Integer cinemaId;
}
