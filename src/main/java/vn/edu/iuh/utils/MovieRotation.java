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

    private Movie lastSelectedMovie;

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
        // Get available movies excluding the last selected movie
        List<Movie> availableMovies = remainingShowTimes.entrySet().stream()
                                                        .filter(e -> e.getValue() > 0)
                                                        .map(Map.Entry::getKey)
                                                        .filter(movie -> !movie.equals(lastSelectedMovie))
                                                        .collect(Collectors.toList());

        // If no other movies available, reset the list
        if (availableMovies.isEmpty()) {
            availableMovies = remainingShowTimes.entrySet().stream()
                                                .filter(e -> e.getValue() > 0)
                                                .map(Map.Entry::getKey)
                                                .toList();
        }

        if (availableMovies.isEmpty()) {
            return null;
        }

        // Calculate total weight based on remaining shows
        double totalWeight = availableMovies.stream()
                                            .mapToDouble(remainingShowTimes::get)
                                            .sum();

        // Weighted random selection algorithm
        double randomValue = Math.random() * totalWeight;
        double currentSum = 0;

        for (Movie movie : availableMovies) {
            currentSum += remainingShowTimes.get(movie);
            if (randomValue <= currentSum) {
                lastSelectedMovie = movie;
                return movie;
            }
        }

        // Fallback selection if no movie is selected
        lastSelectedMovie = availableMovies.get(availableMovies.size() - 1);
        return lastSelectedMovie;
    }
}
