package com.flexjob.application.domain.vo;

import java.util.Objects;

public final class ApplicationId {

    private final Long value;

    private ApplicationId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("ApplicationId must be a positive number");
        }
        this.value = value;
    }

    public static ApplicationId of(Long value) {
        return new ApplicationId(value);
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplicationId that = (ApplicationId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "ApplicationId{" + value + "}";
    }
}
