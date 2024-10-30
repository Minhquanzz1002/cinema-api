package vn.edu.iuh.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.models.enums.SeatType;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "promotion_details")
@EqualsAndHashCode(callSuper = true)
public class PromotionDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private float discountValue;
    private Float maxDiscountValue;
    @Builder.Default
    @Column(nullable = false)
    private float minOrderValue = 0;
    @Column(nullable = false)
    private int usageLimit;
    @Builder.Default
    @Column(nullable = false)
    private int currentUsageCount = 0;
    private int giftQuantity;
    @ManyToOne
    private Product requiredProduct;
    private int requiredProductQuantity;
    @Enumerated(EnumType.STRING)
    private SeatType requiredSeatType;
    private int requiredSeatQuantity;
    @Enumerated(EnumType.STRING)
    private SeatType giftSeatType;
    private int giftSeatQuantity;
    @ManyToOne
    private Product giftProduct;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private BaseStatus status = BaseStatus.ACTIVE;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(nullable = false)
    private PromotionLine promotionLine;
}
