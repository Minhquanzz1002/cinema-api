package vn.edu.iuh.dto.req;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderUpdateSeatRequestDTO {
    @NotEmpty(message = "Danh sách ghế không được rỗng")
    private List<Integer> seatIds;
}
