
package vn.edu.iuh.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import vn.edu.iuh.models.enums.AudienceType;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.models.enums.DayType;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ticket_price_lines")
public class TicketPriceLine extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, columnDefinition = "TEXT[]")
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.ARRAY)
    private List<DayType> applyForDays;
    @Column(nullable = false)
    private LocalTime startTime;
    @Column(nullable = false)
    private LocalTime endTime;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AudienceType audienceType = AudienceType.ADULT;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(nullable = false)
    private TicketPrice ticketPrice;
    @OrderBy("seatType ASC")
    @Builder.Default
    @OneToMany(mappedBy = "ticketPriceLine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TicketPriceDetail> ticketPriceDetails = new ArrayList<>();
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BaseStatus status = BaseStatus.ACTIVE;

    public void addTicketPriceDetail(TicketPriceDetail ticketPriceDetail) {
        ticketPriceDetails.add(ticketPriceDetail);
        ticketPriceDetail.setTicketPriceLine(this);
    }
}
