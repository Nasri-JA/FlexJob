package com.flexjob.payment.infrastructure.adapter.output.messaging;

import com.flexjob.common.events.PaymentCompletedEvent;
import com.flexjob.payment.application.port.output.PublishEventPort;
import com.flexjob.payment.domain.model.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventPublisher implements PublishEventPort {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publishPaymentCompleted(Payment payment) {
        PaymentCompletedEvent event = PaymentCompletedEvent.builder()
                .paymentId(payment.getId().getValue())
                .bookingId(payment.getBookingId())
                .employeeId(payment.getEmployeeId())
                .employerId(payment.getEmployerId())
                .amount(payment.getAmount().getAmount().doubleValue())
                .completedAt(LocalDateTime.now())
                .build();

        kafkaTemplate.send("payment-completed", event);
        log.info("Published PaymentCompletedEvent for payment ID: {}", payment.getId().getValue());
    }
}
