package vn.edu.iuh.models;

import jakarta.persistence.*;
import lombok.*;
import vn.edu.iuh.models.enums.BaseStatus;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "promotions")
@EqualsAndHashCode(callSuper = true)
public class Promotion extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String imagePortrait;
    @Column(unique = true, updatable = false)
    private String code;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BaseStatus status;
    @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PromotionLine> promotionLines;
}
