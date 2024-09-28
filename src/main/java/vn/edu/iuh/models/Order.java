package vn.edu.iuh.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.core.Local;
import vn.edu.iuh.models.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, unique = true, updatable = false, columnDefinition = "CHAR(10)")
    private String code;
    @Column(nullable = false)
    private float totalPrice;
    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime orderDate = LocalDateTime.now();
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails;
    @ManyToOne
    @JoinColumn(nullable = false)
    private ShowTime showTime;
    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;
}
