package vn.edu.iuh.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@SuperBuilder
@Table(name = "actors")
@AllArgsConstructor
@NoArgsConstructor
public class Actor extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String name;
    private Date birthday;
    private String country;
    private String image;
    private String bio;
    @ManyToMany(mappedBy = "actors")
    @JsonBackReference
    private List<Movie> movies;
}
