package vn.edu.iuh.dto.admin.v1.actor.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.validation.NullOrNotBlank;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateActorRequest {
    @NotBlank(message = "Tên không được để trống")
    private String name;
    @Past(message = "Ngày sinh phải là một ngày trong quá khứ")
    private LocalDate birthday;
    @NullOrNotBlank(message = "Quốc gia phải là null hoặc không được để trống")
    private String country;
    @NullOrNotBlank(message = "Ảnh phải là null hoặc URL")
    @URL(message = "Ảnh không đúng định dạng URL")
    private String image;
    private String bio;
    @NotNull(message = "Trạng thái không được để trống")
    private BaseStatus status;
}
