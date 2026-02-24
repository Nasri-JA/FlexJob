package com.flexjob.application.domain.vo;

import java.util.Objects;

public final class MessageText {

    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 5000;

    private final String value;

    private MessageText(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Message text cannot be null or empty");
        }

        String trimmed = value.trim();

        if (trimmed.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("Message text cannot be empty");
        }

        if (trimmed.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("Message text cannot exceed %d characters (got %d)",
                    MAX_LENGTH, trimmed.length())
            );
        }

        this.value = trimmed;
    }

    public static MessageText of(String value) {
        return new MessageText(value);
    }

    public String getValue() {
        return value;
    }

    public int getLength() {
        return value.length();
    }

    public String truncate(int maxLength) {
        if (value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength - 3) + "...";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageText that = (MessageText) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "MessageText{" + truncate(50) + "}";
    }
}
