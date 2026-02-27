package com.flexjob.engagement.booking.application.port.output;

import com.flexjob.engagement.booking.domain.model.Booking;
import com.flexjob.engagement.booking.domain.vo.BookingId;
import java.util.List;
import java.util.Optional;

public interface LoadBookingPort {
    Optional<Booking> findById(BookingId id);
    List<Booking> findByEmployeeId(Long employeeId);
    List<Booking> findByEmployerId(Long employerId);
    List<Booking> findByJobId(Long jobId);
    boolean existsById(BookingId id);
    boolean existsByApplicationId(Long applicationId);
}
