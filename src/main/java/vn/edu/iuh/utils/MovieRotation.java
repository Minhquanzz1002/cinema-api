package vn.edu.iuh.utils;

import vn.edu.iuh.models.Movie;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class MovieRotation {
    private final Map<Movie, Integer> totalShowTimes;
    private final Map<Movie, Integer> remainingShowTimes;
    private final Map<Movie, Double> weights;
    private double currentRotation = 0.0;

    public MovieRotation(Map<Movie, Integer> totalShowTimes, Map<Movie, Integer> remainingShowTimes) {
        this.totalShowTimes = totalShowTimes;
        this.remainingShowTimes = remainingShowTimes;
        this.weights = calculateWeights();
    }

    public Map<Movie, Double> calculateWeights() {
        return totalShowTimes.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> 1.0 / e.getValue()
                ));
    }

    public Movie getNextMovie() {
        List<Movie> availableMovies = remainingShowTimes.entrySet().stream()
                .filter(e -> e.getValue() > 0)
                .map(Map.Entry::getKey)
                .toList();

        if (availableMovies.isEmpty()) {
            return null;
        }

        Movie selectedMovie = availableMovies.stream()
                .min((m1, m2) -> {
                    double angle1 = weights.get(m1) * (totalShowTimes.get(m1) - remainingShowTimes.get(m1));
                    double angle2 = weights.get(m2) * (totalShowTimes.get(m2) - remainingShowTimes.get(m2));
                    return Double.compare(
                            Math.abs(angle1 - currentRotation),
                            Math.abs(angle2 - currentRotation)
                    );
                })
                .orElse(availableMovies.get(0));

        currentRotation += weights.get(selectedMovie);
        return selectedMovie;
    }
}
