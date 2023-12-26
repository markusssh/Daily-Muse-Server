package com.dailymuse.microservicereview.book;

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
@RequestMapping("/api/review/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<?> getUserReviews(
            @RequestHeader("x-user-id") Long userId
    ) {
        BookResponse response = BookResponse
                .builder()
                .books(bookService.getUserReviews(userId))
                .build();
        if (response.getBooks().isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).body("[\n]");
        else return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> addReview(
            @RequestHeader("x-user-id") Long userId,
            @RequestBody BookRequest request
    ) {
        bookService.addReview(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Review has been created");
    }
}
