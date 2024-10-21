package vn.edu.iuh.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import vn.edu.iuh.models.enums.BaseStatus;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_prices")
public class ProductPrice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;
    @Column(nullable = false)
    private float price;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private BaseStatus status = BaseStatus.INACTIVE;
    @ManyToOne
    @JoinColumn(nullable = false)
    @JsonIgnore
    private Product product;
}
