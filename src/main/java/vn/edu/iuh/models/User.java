package vn.edu.iuh.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import vn.edu.iuh.models.enums.UserStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@ToString
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, unique = true, updatable = false)
    private String code;
    @Column(nullable = false)
    private String name;
    private boolean gender;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, unique = true)
    private String phone;
    @JsonIgnore
    @Column(nullable = false)
    private String password;
    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime invalidateBefore = LocalDateTime.now();
    private LocalDate birthday;
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    @Column(columnDefinition = "TEXT")
    private String avatar;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, updatable = false)
    private Role role;
    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Order> orders;
}
