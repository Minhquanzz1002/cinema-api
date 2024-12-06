package vn.edu.iuh.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import vn.edu.iuh.models.enums.BaseStatus;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rooms")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Room extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;
    @Column(nullable = false)
    private String name;
    @Builder.Default
    @Column(nullable = false)
    private BaseStatus status = BaseStatus.ACTIVE;
    @JsonIgnore
    @ManyToOne
    private Cinema cinema;
}
