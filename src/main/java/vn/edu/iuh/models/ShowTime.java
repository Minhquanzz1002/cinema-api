package vn.edu.iuh.models;

import jakarta.persistence.*;
import lombok.*;
import vn.edu.iuh.models.enums.BaseStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "showTimes")
public class ShowTime extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Movie movie;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Cinema cinema;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Room room;
    @Column(nullable = false)
    private LocalTime startTime;
    @Column(nullable = false)
    private LocalTime endTime;
    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private BaseStatus status = BaseStatus.ACTIVE;
    @Builder.Default
    @Column(nullable = false)
    private int totalSeat = 0;
    @Builder.Default
    @Column(nullable = false)
    private int bookedSeat = 0;
    @OneToMany(mappedBy = "showTime", fetch = FetchType.LAZY)
    private List<Order> orders;
}
