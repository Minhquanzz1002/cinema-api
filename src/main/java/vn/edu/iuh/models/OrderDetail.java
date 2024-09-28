package vn.edu.iuh.models;

import jakarta.persistence.*;
import lombok.*;
import vn.edu.iuh.models.enums.OrderDetailType;

import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_details")
public class OrderDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private int quantity;
    @Column(nullable = false)
    private float price;
    @ManyToOne
    private Product product;
    @ManyToOne
    private Seat seat;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Order order;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderDetailType type;
}
