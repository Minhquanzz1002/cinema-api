package vn.edu.iuh.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import vn.edu.iuh.models.enums.PaymentMethod;
import vn.edu.iuh.models.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payments")
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class Payment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, updatable = false, unique = true)
    private String code;
    private String transactionId;
    @Column(nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod method;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private String detail;
    @Column(nullable = false, updatable = false)
    private float amount;
    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime paymentDate = LocalDateTime.now();
    @ManyToOne
    @JoinColumn(nullable = false)
    private Order order;
}
