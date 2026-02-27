package com.flexjob.engagement.review.domain.vo;

import java.util.Objects;

public final class Rating {
    private static final int MIN_RATING = 1;
    private static final int MAX_RATING = 5;

    private final Integer value;

    private Rating(Integer value) {
        if (value == null) {
            throw new IllegalArgumentException("Rating cannot be null");
        }
        if (value < MIN_RATING || value > MAX_RATING) {
            throw new IllegalArgumentException(
                    String.format("Rating must be between %d and %d", MIN_RATING, MAX_RATING)
            );
        }
        this.value = value;
    }

    public static Rating of(Integer value) {
        return new Rating(value);
    }

    public Integer getValue() {
        return value;
    }

    public boolean isExcellent() {
        return value == 5;
    }

    public boolean isGood() {
        return value >= 4;
    }

    public boolean isPoor() {
        return value <= 2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rating rating = (Rating) o;
        return Objects.equals(value, rating.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Rating{" + value + "/5}";
    }
}
