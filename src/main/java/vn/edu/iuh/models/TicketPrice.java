package vn.edu.iuh.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ticket_prices")
public class TicketPrice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;
    @Column(nullable = false)
    private float priceSeatNormal;
    @Column(nullable = false)
    private float priceSeatCouple;
}
