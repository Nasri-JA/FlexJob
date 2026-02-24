package com.flexjob.booking.application.dto.response;

import com.flexjob.booking.domain.enums.BookingStatus;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class BookingResponse {
    private final Long id;
    private final Long jobId;
    private final Long employeeId;
    private final Long employerId;
    private final Long applicationId;
    private final BookingStatus status;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Double totalHours;
    private final LocalDateTime createdAt;
}
