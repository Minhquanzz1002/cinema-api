package vn.edu.iuh.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import vn.edu.iuh.models.enums.AgeRating;
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
    @Column(unique = true, nullable = false, updatable = false, length = 8)
    private String code;
    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String imageLandscape;
    @Column(columnDefinition = "TEXT")
    private String imagePortrait;
    private String trailer;
    @Column(nullable = false, unique = true)
    private String slug;
    @Column(nullable = false)
    private int duration;
    @Column(columnDefinition = "TEXT")
    private String summary;
    @Column(nullable = false)
    @Builder.Default
    private float rating = 0;
    @Column(nullable = false)
    private String country;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private AgeRating ageRating = AgeRating.T18;
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

