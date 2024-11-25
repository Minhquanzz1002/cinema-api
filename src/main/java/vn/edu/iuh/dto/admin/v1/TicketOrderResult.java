package vn.edu.iuh.dto.admin.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.iuh.models.Order;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketOrderResult {
    private Order order;
    private float totalPrice;
}
