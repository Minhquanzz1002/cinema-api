package vn.edu.iuh.models;

import jakarta.persistence.*;
import lombok.*;
import vn.edu.iuh.models.enums.OrderStatus;
import vn.edu.iuh.models.enums.RefundStatus;

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
    private float finalAmount;
    @Column(nullable = false)
    @Builder.Default
    private float totalDiscount = 0;
    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime orderDate = LocalDateTime.now();
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails;
    @ManyToOne
    @JoinColumn(nullable = false)
    private ShowTime showTime;
    @ManyToOne
    @JoinColumn(nullable = true)
    private User user;
    @ManyToOne
    private PromotionLine promotionLine;
    @ManyToOne
    private PromotionDetail promotionDetail;
    @Column(length = 500)
    private String cancelReason;
    private Float refundAmount;
    private LocalDateTime refundDate;
    @Enumerated(EnumType.STRING)
    private RefundStatus refundStatus;
}
