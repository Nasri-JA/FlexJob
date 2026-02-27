package com.flexjob.engagement.booking.infrastructure.adapter.input.rest.controller;

import com.flexjob.engagement.booking.application.dto.command.*;
import com.flexjob.engagement.booking.application.dto.response.*;
import com.flexjob.engagement.booking.application.port.input.*;
import com.flexjob.engagement.booking.domain.vo.BookingId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final CreateBookingUseCase createBookingUseCase;
    private final GetBookingUseCase getBookingUseCase;
    private final CompleteBookingUseCase completeBookingUseCase;
    private final StartTimeTrackingUseCase startTimeTrackingUseCase;
    private final EndTimeTrackingUseCase endTimeTrackingUseCase;

    @PostMapping
    public ResponseEntity<BookingResponse> create(@RequestBody CreateBookingCommand command) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createBookingUseCase.createBooking(command));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(getBookingUseCase.getBookingById(BookingId.of(id)));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<BookingResponse>> getByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(getBookingUseCase.getBookingsByEmployeeId(employeeId));
    }

    @GetMapping("/employer/{employerId}")
    public ResponseEntity<List<BookingResponse>> getByEmployer(@PathVariable Long employerId) {
        return ResponseEntity.ok(getBookingUseCase.getBookingsByEmployerId(employerId));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<BookingResponse> complete(@PathVariable Long id) {
        return ResponseEntity.ok(completeBookingUseCase.completeBooking(BookingId.of(id)));
    }

    @PostMapping("/{id}/time-tracking/start")
    public ResponseEntity<TimeTrackingResponse> startTracking(@PathVariable Long id, @RequestBody(required = false) String notes) {
        StartTimeTrackingCommand cmd = StartTimeTrackingCommand.builder().bookingId(BookingId.of(id)).notes(notes).build();
        return ResponseEntity.ok(startTimeTrackingUseCase.startTimeTracking(cmd));
    }

    @PostMapping("/{id}/time-tracking/end")
    public ResponseEntity<TimeTrackingResponse> endTracking(@PathVariable Long id, @RequestBody(required = false) String notes) {
        EndTimeTrackingCommand cmd = EndTimeTrackingCommand.builder().bookingId(BookingId.of(id)).notes(notes).build();
        return ResponseEntity.ok(endTimeTrackingUseCase.endTimeTracking(cmd));
    }
}
