package vn.edu.iuh.models;

import jakarta.persistence.*;
import lombok.*;
import vn.edu.iuh.models.enums.RefundMethod;
import vn.edu.iuh.models.enums.RefundStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "refunds")
@EqualsAndHashCode(callSuper = true)
public class Refund extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, unique = true, updatable = false)
    private String code;
    @Column(nullable = false, length = 500)
    private String reason;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RefundMethod refundMethod;
    @OneToOne
    @JoinColumn(nullable = false, unique = true, updatable = false)
    private Order order;
    @Column(nullable = false)
    private float amount;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RefundStatus status;
    private LocalDateTime refundDate;
}
