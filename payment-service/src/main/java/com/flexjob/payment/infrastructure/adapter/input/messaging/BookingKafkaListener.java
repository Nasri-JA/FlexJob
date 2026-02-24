package com.flexjob.payment.infrastructure.adapter.input.messaging;

import com.flexjob.common.events.BookingCompletedEvent;
import com.flexjob.payment.application.dto.command.ProcessPaymentCommand;
import com.flexjob.payment.application.port.input.ProcessPaymentUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingKafkaListener {

    private final ProcessPaymentUseCase processPaymentUseCase;

    @KafkaListener(topics = "booking-completed", groupId = "payment-service")
    public void handleBookingCompleted(BookingCompletedEvent event) {
        log.info("Processing payment for booking: {}", event.getBookingId());

        try {
            BigDecimal amount = BigDecimal.valueOf(event.getTotalHours() * 50.0);

            ProcessPaymentCommand command = ProcessPaymentCommand.builder()
                    .bookingId(event.getBookingId())
                    .employeeId(event.getEmployeeId())
                    .employerId(event.getEmployerId())
                    .amount(amount)
                    .build();

            processPaymentUseCase.processPayment(command);
            log.info("Payment processed for booking: {}", event.getBookingId());
        } catch (Exception e) {
            log.error("Error processing payment: {}", e.getMessage(), e);
        }
    }
}
