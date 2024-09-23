package vn.edu.iuh.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@ToString
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, updatable = false)
    private String name;

    public Role(String name) {
        this.name = name;
    }
}
