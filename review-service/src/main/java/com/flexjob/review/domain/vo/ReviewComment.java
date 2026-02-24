package com.flexjob.review.domain.vo;

import java.util.Objects;

public final class ReviewComment {
    private static final int MAX_LENGTH = 1000;

    private final String value;

    private ReviewComment(String value) {
        if (value != null && value.trim().length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Comment cannot exceed " + MAX_LENGTH + " characters");
        }
        this.value = value != null ? value.trim() : null;
    }

    public static ReviewComment of(String value) {
        return new ReviewComment(value);
    }

    public String getValue() {
        return value;
    }

    public boolean isEmpty() {
        return value == null || value.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewComment that = (ReviewComment) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "ReviewComment{" + (isEmpty() ? "empty" : value.substring(0, Math.min(50, value.length()))) + "}";
    }
}
