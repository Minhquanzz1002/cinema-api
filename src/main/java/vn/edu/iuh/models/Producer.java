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
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "producers")
public class Producer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @Builder.Default
    private BaseStatus status = BaseStatus.ACTIVE;
    @ManyToMany(mappedBy = "producers")
    @JsonBackReference
    private List<Movie> movies;
}
