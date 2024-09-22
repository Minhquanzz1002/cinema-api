package vn.edu.iuh.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "row_seats")
public class RowSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private short index;
    @Column(nullable = false, columnDefinition = "VARCHAR(3)")
    private String name;
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private RoomLayout layout;
    @OrderBy(value = "name desc")
    @OneToMany(mappedBy = "row")
    private List<Seat> seats = new ArrayList<>();
}
