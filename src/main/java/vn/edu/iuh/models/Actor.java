package vn.edu.iuh.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import vn.edu.iuh.models.enums.BaseStatus;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@SuperBuilder
@ToString
@Table(name = "actors")
@AllArgsConstructor
@NoArgsConstructor
public class Actor extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, unique = true, length = 8)
    private String code;
    @Column(nullable = false)
    private String name;
    private LocalDate birthday;
    private String country;
    private String image;
    private String bio;
    @Builder.Default
    private BaseStatus status = BaseStatus.ACTIVE;
    @ManyToMany(mappedBy = "actors")
    @JsonBackReference
    @ToString.Exclude
    private List<Movie> movies;

}
