package vn.edu.iuh.dto.admin.v1.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.iuh.projections.admin.v1.ActorProjection;
import vn.edu.iuh.projections.admin.v1.DirectorProjection;
import vn.edu.iuh.projections.admin.v1.GenreProjection;
import vn.edu.iuh.projections.admin.v1.ProducerProjection;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieFiltersResponseDTO {
    private List<GenreProjection> genres;
    private List<ProducerProjection> producers;
    private List<ActorProjection> actors;
    private List<DirectorProjection> directors;
}
