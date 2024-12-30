package vn.edu.iuh.dto.admin.v1.room.req;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import vn.edu.iuh.models.enums.BaseStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRoomRequest {
    @NotBlank(message = "Tên phòng chiếu không được rỗng")
    private String name;
    private BaseStatus status;
}
