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
@Table(name = "room_layouts")
public class RoomLayout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int maxColumn;
    private int maxRow;
    @ManyToOne
    @ToString.Exclude
    private Room room;
    @OrderBy(value = "name desc")
    @OneToMany(mappedBy = "layout", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RowSeat> rows = new ArrayList<>();
}
