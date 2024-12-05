package vn.edu.iuh.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import vn.edu.iuh.models.enums.BaseStatus;

import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cinemas")
public class Cinema extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, unique = true, updatable = false)
    private String code;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String ward;
    @Column(nullable = false)
    private String wardCode;
    @Column(nullable = false)
    private String district;
    @Column(nullable = false)
    private String districtCode;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String cityCode;
    @Column(columnDefinition = "TEXT[]")
    @JdbcTypeCode(SqlTypes.ARRAY)
    private List<String> images;
    private String hotline;
    @Column(nullable = false, unique = true)
    private String slug;
    @OneToMany
    private List<Room> rooms;
    @Builder.Default
    @Column(nullable = false)
    private BaseStatus status = BaseStatus.ACTIVE;
}
