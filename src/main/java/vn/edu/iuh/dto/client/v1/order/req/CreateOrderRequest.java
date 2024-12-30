package vn.edu.iuh.dto.client.v1.order.req;

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
public class CreateOrderRequest {
    @NotNull(message = "ID lịch chiếu không được rỗng")
    private UUID showTimeId;
    @NotEmpty(message = "Danh sách ghế không được rỗng")
    private List<Integer> seatIds;
}
