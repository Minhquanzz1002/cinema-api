package vn.edu.iuh.projections;

public interface CinemaProjection {
    Integer getId();

    String getName();
    String getAddress();
    String getDistrict();
    String getWard();
    String getSlug();
    CityProjection getCity();
}
