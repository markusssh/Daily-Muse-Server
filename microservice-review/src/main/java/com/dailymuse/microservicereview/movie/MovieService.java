package com.dailymuse.microservicereview.movie;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;

    public List<Movie> getUserReviews(Long userId) {
        return movieRepository.findAllByUserId(userId);
    }

    public void addReview(MovieRequest request, Long userId) {
        Movie movie = Movie
                .builder()
                .userId(userId)
                .title(request.getTitle())
                .review(request.getReview())
                .rating(request.getRating())
                .year(request.getYear())
                .build();
        movieRepository.save(movie);
    }
}
