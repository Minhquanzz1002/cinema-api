package vn.edu.iuh.dto.admin.v1.req;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequestDTO {
    private UUID customerId;
    @NotEmpty(message = "Danh sách ghế không được rỗng")
    private List<Integer> seatIds;
    @NotNull(message = "ID lịch chiếu không được rỗng")
    private UUID showTimeId;
}
