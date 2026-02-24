package com.flexjob.review.infrastructure.adapter.input.rest.controller;

import com.flexjob.review.application.dto.command.CreateReviewCommand;
import com.flexjob.review.application.dto.response.ReviewResponse;
import com.flexjob.review.application.port.input.CreateReviewUseCase;
import com.flexjob.review.application.port.input.GetReviewUseCase;
import com.flexjob.review.infrastructure.adapter.input.rest.dto.CreateReviewRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final CreateReviewUseCase createReviewUseCase;
    private final GetReviewUseCase getReviewUseCase;

    @PostMapping
    public ResponseEntity<ReviewResponse> create(@RequestBody CreateReviewRequest request) {
        CreateReviewCommand cmd = CreateReviewCommand.builder()
                .bookingId(request.getBookingId())
                .reviewerId(request.getReviewerId())
                .revieweeId(request.getRevieweeId())
                .rating(request.getRating())
                .comment(request.getComment())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createReviewUseCase.createReview(cmd));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(getReviewUseCase.getReviewById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewResponse>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(getReviewUseCase.getReviewsForUser(userId));
    }

    @GetMapping("/user/{userId}/average")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long userId) {
        return ResponseEntity.ok(getReviewUseCase.getAverageRating(userId));
    }
}
