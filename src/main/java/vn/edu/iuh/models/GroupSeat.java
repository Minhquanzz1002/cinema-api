package vn.edu.iuh.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "seat_groups")
public class GroupSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private short columnIndex;
    @Column(nullable = false)
    private short rowIndex;
    @Column(nullable = false)
    private short area;
    @ManyToOne
    private Seat seat;
}
