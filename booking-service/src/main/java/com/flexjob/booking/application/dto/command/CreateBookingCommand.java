package com.flexjob.booking.application.dto.command;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDate;

@Getter
@Builder
public class CreateBookingCommand {
    private final Long jobId;
    private final Long employeeId;
    private final Long employerId;
    private final Long applicationId;
    private final LocalDate startDate;
    private final LocalDate endDate;
}
