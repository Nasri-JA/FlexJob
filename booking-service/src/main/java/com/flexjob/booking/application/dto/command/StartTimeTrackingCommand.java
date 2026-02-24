package com.flexjob.booking.application.dto.command;

import com.flexjob.booking.domain.vo.BookingId;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StartTimeTrackingCommand {
    private final BookingId bookingId;
    private final String notes;
}
