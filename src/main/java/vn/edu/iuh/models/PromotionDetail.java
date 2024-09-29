package vn.edu.iuh.models;

import jakarta.persistence.*;
import lombok.*;
import vn.edu.iuh.models.enums.BaseStatus;

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
    private float maxDiscountValue;
    private float minOrderValue;
    private int maxUsageCount;
    private int currentUsageCount;
    private int minProductQuantity;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BaseStatus status;
    @ManyToOne
    @JoinColumn(nullable = false)
    private PromotionLine promotionLine;
}
