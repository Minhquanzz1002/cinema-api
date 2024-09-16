package vn.edu.iuh.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import vn.edu.iuh.models.enums.MovieStatus;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@Table(name = "movies")
@AllArgsConstructor
@NoArgsConstructor
public class Movie extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true, nullable = false)
    private String code;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String imageLandscape;
    @Column(nullable = false)
    private String imagePortrait;
    @Column(nullable = false)
    private String trailer;
    @Column(nullable = false, unique = true)
    private String slug;
    @Column(nullable = false)
    private int duration;
    @Column(columnDefinition = "TEXT")
    private String summary;
    @Column(nullable = false)
    private float rating;
    @Column(nullable = false)
    private String country;
    @Column(nullable = false)
    private int age;
    private LocalDate releaseDate;
    @Enumerated(EnumType.STRING)
    private MovieStatus status;
    @ManyToMany
    @JsonManagedReference
    private List<Producer> producers;
    @ManyToMany
    @JsonManagedReference
    private List<Genre> genres;
    @ManyToMany
    @JsonManagedReference
    private List<Director> directors;
    @ManyToMany
    @JsonManagedReference
    private List<Actor> actors;
}

