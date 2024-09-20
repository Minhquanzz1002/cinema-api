package vn.edu.iuh.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import vn.edu.iuh.models.enums.ProductStatus;
import vn.edu.iuh.models.enums.ProductType;

import java.util.List;

@Getter
@Setter
@Entity
@SuperBuilder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, unique = true, columnDefinition = "CHAR(8)")
    private String code;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String image;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductType type;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductStatus status = ProductStatus.INACTIVE;
    @OneToMany(mappedBy = "product")
    private List<ProductPrice> productPrices;
}
