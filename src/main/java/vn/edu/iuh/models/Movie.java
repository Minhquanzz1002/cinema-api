package vn.edu.iuh.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.sql.*;
import java.util.Date;
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
    @Column(nullable = false)
    private String slug;
    @Column(nullable = false)
    private int duration;
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

