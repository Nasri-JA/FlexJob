package com.flexjob.booking.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class TimeTrackingResponse {
    private final Long id;
    private final Long bookingId;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final String notes;
    private final Double hours;
}
