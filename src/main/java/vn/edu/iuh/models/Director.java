package vn.edu.iuh.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

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
    @Column(nullable = false)
    private String name;
    private Date birthday;
    private String image;
    private String country;
    private String bio;
    @ManyToMany(mappedBy = "directors")
    @JsonBackReference
    private List<Movie> movies;
}
