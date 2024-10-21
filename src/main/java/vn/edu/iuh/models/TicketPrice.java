package vn.edu.iuh.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import vn.edu.iuh.models.enums.BaseStatus;

import java.time.LocalDate;
import java.util.List;

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
    private String name;
    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;
    @Builder.Default
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BaseStatus status = BaseStatus.ACTIVE;
    @OneToMany(mappedBy = "ticketPrice")
    @OrderBy("startTime ASC")
    List<TicketPriceLine> ticketPriceLines;
}
