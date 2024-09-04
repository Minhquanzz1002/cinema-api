package vn.edu.iuh.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.iuh.models.enums.SeatType;

@Getter
@Setter
@Entity
@Table(name = "seats")
@AllArgsConstructor
@NoArgsConstructor
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private SeatType type;
    private String rowName;
    private int seatNumber;
    private int colIndex;
    private int rowIndex;
}
