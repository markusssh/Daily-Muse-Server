package com.dailymuse.microservicereview.movie;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/review/movie")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping
    public ResponseEntity<?> getUserReviews(
            @RequestHeader("x-user-id") Long userId
    ) {
        MovieResponse response = MovieResponse
                .builder()
                .movies(movieService.getUserReviews(userId))
                .build();
        if (response.getMovies().isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).body("[\n]");
        else return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> addReview(
            @RequestHeader("x-user-id") Long userId,
            @RequestBody MovieRequest request
    ) {
        movieService.addReview(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Review has been created");
    }
}
