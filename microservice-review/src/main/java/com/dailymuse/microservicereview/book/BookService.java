package com.dailymuse.microservicereview.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public List<Book> getUserReviews(Long userId) {
        return bookRepository.findAllByUserId(userId);
    }

    public void addReview(BookRequest request, Long userId) {
        Book book = Book
                .builder()
                .userId(userId)
                .title(request.getTitle())
                .review(request.getReview())
                .rating(request.getRating())
                .author(request.getAuthor())
                .build();
        bookRepository.save(book);
    }
}
