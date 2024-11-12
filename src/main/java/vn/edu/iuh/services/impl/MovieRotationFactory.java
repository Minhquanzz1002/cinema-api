package vn.edu.iuh.services.impl;

import org.springframework.stereotype.Component;
import vn.edu.iuh.models.Movie;
import vn.edu.iuh.utils.MovieRotation;

import java.util.Map;

@Component
public class MovieRotationFactory {
    public MovieRotation createRotation(Map<Movie, Integer> totalShowTimes, Map<Movie, Integer> remainingShowTimes) {
        return new MovieRotation(totalShowTimes, remainingShowTimes);
    }
}
