package com.flexjob.booking.infrastructure.adapter.output.messaging;

import com.flexjob.booking.application.port.output.PublishEventPort;
import com.flexjob.booking.domain.model.Booking;
import com.flexjob.booking.domain.vo.WorkingHours;
import com.flexjob.common.events.BookingCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingEventPublisher implements PublishEventPort {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publishBookingCompleted(Booking booking, WorkingHours totalHours) {
        BookingCompletedEvent event = BookingCompletedEvent.builder()
                .bookingId(booking.getId().getValue())
                .jobId(booking.getJobId())
                .employeeId(booking.getEmployeeId())
                .employerId(booking.getEmployerId())
                .totalHours(totalHours.getHours())
                .completedAt(LocalDateTime.now())
                .build();

        kafkaTemplate.send("booking-completed", event);
        log.info("Published BookingCompletedEvent for booking ID: {}", booking.getId().getValue());
    }
}
