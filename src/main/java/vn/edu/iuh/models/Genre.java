package vn.edu.iuh.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import vn.edu.iuh.models.enums.BaseStatus;

import java.util.List;

@Getter
@Setter
@Entity
@Builder
@Table(name = "genres")
@AllArgsConstructor
@NoArgsConstructor
public class Genre extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String name;
    @ManyToMany(mappedBy = "genres")
    @JsonBackReference
    private List<Movie> movies;
    @Enumerated(EnumType.STRING)
    private BaseStatus status;
}
