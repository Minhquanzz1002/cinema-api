package vn.edu.iuh.dto.client.v1.order.req;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderSeatRequest {
    @NotEmpty(message = "Danh sách ghế không được rỗng")
    private List<Integer> seatIds;
}
