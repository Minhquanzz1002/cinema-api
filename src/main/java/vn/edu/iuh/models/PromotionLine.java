package vn.edu.iuh.models;

import jakarta.persistence.*;
import lombok.*;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.models.enums.PromotionApplyLimitType;
import vn.edu.iuh.models.enums.PromotionLineType;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "promotion_lines")
@EqualsAndHashCode(callSuper = true)
public class PromotionLine extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String code;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PromotionLineType type;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PromotionApplyLimitType applyLimitType = PromotionApplyLimitType.MIN;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BaseStatus status;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Promotion promotion;
    @OneToMany(mappedBy = "promotionLine")
    private List<PromotionDetail> promotionDetails;
}
