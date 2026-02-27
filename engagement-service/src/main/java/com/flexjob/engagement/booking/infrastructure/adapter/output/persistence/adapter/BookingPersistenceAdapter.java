package com.flexjob.engagement.booking.infrastructure.adapter.output.persistence.adapter;

import com.flexjob.engagement.booking.application.port.output.*;
import com.flexjob.engagement.booking.domain.model.Booking;
import com.flexjob.engagement.booking.domain.model.TimeTracking;
import com.flexjob.engagement.booking.domain.vo.BookingId;
import com.flexjob.engagement.booking.domain.vo.TimeTrackingId;
import com.flexjob.engagement.booking.infrastructure.adapter.output.persistence.entity.BookingJpaEntity;
import com.flexjob.engagement.booking.infrastructure.adapter.output.persistence.entity.TimeTrackingJpaEntity;
import com.flexjob.engagement.booking.infrastructure.adapter.output.persistence.mapper.*;
import com.flexjob.engagement.booking.infrastructure.adapter.output.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookingPersistenceAdapter implements LoadBookingPort, SaveBookingPort, LoadTimeTrackingPort, SaveTimeTrackingPort {

    private final BookingJpaRepository bookingRepository;
    private final TimeTrackingJpaRepository timeTrackingRepository;
    private final BookingPersistenceMapper bookingMapper;
    private final TimeTrackingPersistenceMapper timeTrackingMapper;

    @Override
    public Optional<Booking> findById(BookingId id) {
        return bookingRepository.findById(id.getValue()).map(bookingMapper::toDomain);
    }
    @Override
    public List<Booking> findByEmployeeId(Long employeeId) {
        return bookingRepository.findByEmployeeId(employeeId).stream().map(bookingMapper::toDomain).collect(Collectors.toList());
    }
    @Override
    public List<Booking> findByEmployerId(Long employerId) {
        return bookingRepository.findByEmployerId(employerId).stream().map(bookingMapper::toDomain).collect(Collectors.toList());
    }
    @Override
    public List<Booking> findByJobId(Long jobId) {
        return bookingRepository.findByJobId(jobId).stream().map(bookingMapper::toDomain).collect(Collectors.toList());
    }
    @Override
    public boolean existsById(BookingId id) { return bookingRepository.existsById(id.getValue()); }
    @Override
    public boolean existsByApplicationId(Long applicationId) { return bookingRepository.existsByApplicationId(applicationId); }
    @Override
    public Booking save(Booking booking) {
        BookingJpaEntity jpa = bookingMapper.toJpaEntity(booking);
        BookingJpaEntity saved = bookingRepository.save(jpa);
        return bookingMapper.toDomain(saved);
    }
    @Override
    public Optional<TimeTracking> findById(TimeTrackingId id) {
        return timeTrackingRepository.findById(id.getValue()).map(timeTrackingMapper::toDomain);
    }
    @Override
    public List<TimeTracking> findByBookingId(Long bookingId) {
        return timeTrackingRepository.findByBooking_Id(bookingId).stream().map(timeTrackingMapper::toDomain).collect(Collectors.toList());
    }
    @Override
    public Optional<TimeTracking> findActiveByBookingId(Long bookingId) {
        return timeTrackingRepository.findByBooking_IdAndEndTimeIsNull(bookingId).map(timeTrackingMapper::toDomain);
    }
    @Override
    public TimeTracking save(TimeTracking timeTracking) {
        BookingJpaEntity bookingJpa = bookingRepository.findById(timeTracking.getBookingId()).orElseThrow();
        TimeTrackingJpaEntity jpa = timeTrackingMapper.toJpaEntity(timeTracking, bookingJpa);
        TimeTrackingJpaEntity saved = timeTrackingRepository.save(jpa);
        return timeTrackingMapper.toDomain(saved);
    }
}
