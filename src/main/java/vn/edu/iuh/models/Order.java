package vn.edu.iuh.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import vn.edu.iuh.models.enums.PaymentStatus;
import vn.edu.iuh.models.enums.OrderStatus;
import vn.edu.iuh.models.enums.PaymentMethod;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, unique = true, updatable = false)
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
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PaymentStatus paymentStatus = PaymentStatus.UNPAID;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails;
    @ManyToOne
    @JoinColumn(nullable = false)
    private ShowTime showTime;
    @ManyToOne
    private User user;
    @ManyToOne
    private PromotionLine promotionLine;
    @ManyToOne
    private PromotionDetail promotionDetail;
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Refund refund;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments;
}
