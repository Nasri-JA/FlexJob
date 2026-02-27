package com.flexjob.engagement.booking.application.dto.command;

import com.flexjob.engagement.booking.domain.vo.BookingId;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EndTimeTrackingCommand {
    private final BookingId bookingId;
    private final String notes;
}
