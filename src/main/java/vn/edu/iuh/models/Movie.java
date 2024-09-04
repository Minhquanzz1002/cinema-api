package vn.edu.iuh.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "movies")
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String imageLandscape;
    @Column(nullable = false)
    private String imagePortrait;
    private String trailer;
    @Column(nullable = false)
    private String slug;
    @Column(nullable = false)
    private int duration;
    @Column(nullable = false)
    private String summary;
    @Column(nullable = false)
    private float rating;
    @Column(nullable = false)
    private String country;
    @Column(nullable = false)
    private String producer;
    @Column(nullable = false)
    private int age;
    private Date releaseDate;
    @ManyToMany
    private List<Genre> genres;
    @ManyToMany
    private List<Director> directors;
    @ManyToMany
    private List<Actor> actors;
}
