package vn.edu.iuh.dto.admin.v1.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.models.enums.SeatType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTicketPriceDetailRequestDTO {
    private SeatType seatType;
    private float price;
    private BaseStatus status;
}
