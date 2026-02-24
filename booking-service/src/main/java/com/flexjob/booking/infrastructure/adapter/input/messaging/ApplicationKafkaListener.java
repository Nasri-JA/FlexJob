package com.flexjob.booking.infrastructure.adapter.input.messaging;

import com.flexjob.booking.application.dto.command.CreateBookingCommand;
import com.flexjob.booking.application.port.input.CreateBookingUseCase;
import com.flexjob.common.events.ApplicationStatusChangedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApplicationKafkaListener {

    private final CreateBookingUseCase createBookingUseCase;

    @KafkaListener(topics = "application-status-changed", groupId = "booking-service")
    public void handleApplicationStatusChanged(ApplicationStatusChangedEvent event) {
        if (!"ACCEPTED".equalsIgnoreCase(event.getNewStatus())) {
            return;
        }

        log.info("Creating booking for accepted application: {}", event.getApplicationId());

        try {
            CreateBookingCommand command = CreateBookingCommand.builder()
                    .jobId(event.getJobId())
                    .employeeId(event.getEmployeeId())
                    .employerId(event.getEmployerId())
                    .applicationId(event.getApplicationId())
                    .startDate(LocalDate.now().plusDays(1))
                    .endDate(LocalDate.now().plusDays(7))
                    .build();

            createBookingUseCase.createBooking(command);
            log.info("Booking created for application: {}", event.getApplicationId());
        } catch (Exception e) {
            log.error("Error creating booking: {}", e.getMessage(), e);
        }
    }
}
