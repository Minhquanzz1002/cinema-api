package vn.edu.iuh.models;

import jakarta.persistence.*;
import lombok.*;

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
    private Movie movie;
    @ManyToOne
    private Cinema cinema;
    @ManyToOne
    private Room room;
    @Column(nullable = false)
    private LocalTime startTime;
    @Column(nullable = false)
    private LocalTime endTime;
    @Column(nullable = false)
    private LocalDate startDate;
    @Builder.Default
    @Column(nullable = false)
    private int totalSeat = 0;
    @Builder.Default
    @Column(nullable = false)
    private int bookedSeat = 0;
    @OneToMany(mappedBy = "showTime")
    private List<Order> orders;
}
