package vn.edu.iuh.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import vn.edu.iuh.models.enums.BaseStatus;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "directors")
public class Director extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, unique = true)
    private String code;
    @Column(nullable = false)
    private String name;
    private LocalDate birthday;
    private String image;
    private String country;
    private String bio;
    @Builder.Default
    private BaseStatus status = BaseStatus.ACTIVE;
    @ManyToMany(mappedBy = "directors")
    @JsonBackReference
    private List<Movie> movies;
}
