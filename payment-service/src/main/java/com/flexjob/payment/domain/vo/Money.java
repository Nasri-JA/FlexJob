package com.flexjob.payment.domain.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
@EqualsAndHashCode
public class Money {
    private final BigDecimal amount;
    private final String currency;

    private Money(BigDecimal amount, String currency) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        this.amount = amount;
        this.currency = currency != null ? currency : "EUR";
    }

    public static Money of(BigDecimal amount, String currency) {
        return new Money(amount, currency);
    }

    public static Money euro(BigDecimal amount) {
        return new Money(amount, "EUR");
    }

    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot add different currencies");
        }
        return new Money(this.amount.add(other.amount), this.currency);
    }

    @Override
    public String toString() {
        return String.format("%.2f %s", amount, currency);
    }
}
