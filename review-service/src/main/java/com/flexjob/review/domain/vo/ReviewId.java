package com.flexjob.review.domain.vo;

import java.util.Objects;

public final class ReviewId {
    private final Long value;

    private ReviewId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("ReviewId must be positive");
        }
        this.value = value;
    }

    public static ReviewId of(Long value) {
        return new ReviewId(value);
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewId reviewId = (ReviewId) o;
        return Objects.equals(value, reviewId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "ReviewId{" + value + "}";
    }
}
