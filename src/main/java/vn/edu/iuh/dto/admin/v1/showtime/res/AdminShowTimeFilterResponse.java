package vn.edu.iuh.dto.admin.v1.showtime.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.iuh.projections.admin.v1.AdminCinemaFilterProjection;
import vn.edu.iuh.projections.admin.v1.AdminMovieFilterProjection;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminShowTimeFilterResponse {
    List<AdminMovieFilterProjection> movies;
    List<AdminCinemaFilterProjection> cinemas;
}
