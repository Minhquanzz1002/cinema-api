package vn.edu.iuh.models;

import jakarta.persistence.*;
import lombok.*;
import vn.edu.iuh.models.enums.SeatStatus;
import vn.edu.iuh.models.enums.SeatType;

import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "seats")
public class Seat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private short name;
    @Column(nullable = false)
    private String fullName;
    @Column(nullable = false)
    private short area;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SeatType type;
    @Column(nullable = false)
    private short rowIndex;
    @Column(nullable = false)
    private short columnIndex;
    @Builder.Default
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SeatStatus status = SeatStatus.ACTIVE;
    @ManyToOne
    private RowSeat row;
    @OneToMany(mappedBy = "seat")
    private List<GroupSeat> groupSeats;
    @OneToMany(mappedBy = "seat")
    private List<OrderDetail> orderDetails;
}
