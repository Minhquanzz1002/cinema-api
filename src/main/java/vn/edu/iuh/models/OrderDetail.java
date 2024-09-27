package vn.edu.iuh.models;

import jakarta.persistence.*;
import lombok.*;

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
    @Column(nullable = false)
    private int quantity;
    @Column(nullable = false)
    private float price;
    @ManyToOne
    private Product product;
    @ManyToOne
    private Order order;
}
