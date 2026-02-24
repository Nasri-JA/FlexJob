package com.flexjob.booking.infrastructure.adapter.output.persistence.mapper;

import com.flexjob.booking.domain.model.TimeTracking;
import com.flexjob.booking.domain.vo.TimeTrackingId;
import com.flexjob.booking.domain.vo.TimeTrackingPeriod;
import com.flexjob.booking.infrastructure.adapter.output.persistence.entity.BookingJpaEntity;
import com.flexjob.booking.infrastructure.adapter.output.persistence.entity.TimeTrackingJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class TimeTrackingPersistenceMapper {

    public TimeTrackingJpaEntity toJpaEntity(TimeTracking timeTracking, BookingJpaEntity bookingJpa) {
        return TimeTrackingJpaEntity.builder()
                .id(timeTracking.getId() != null ? timeTracking.getId().getValue() : null)
                .booking(bookingJpa)
                .startTime(timeTracking.getPeriod().getStartTime())
                .endTime(timeTracking.getPeriod().getEndTime())
                .notes(timeTracking.getNotes())
                .build();
    }

    public TimeTracking toDomain(TimeTrackingJpaEntity jpaEntity) {
        TimeTrackingPeriod period = jpaEntity.getEndTime() != null
                ? TimeTrackingPeriod.completed(jpaEntity.getStartTime(), jpaEntity.getEndTime())
                : TimeTrackingPeriod.started(jpaEntity.getStartTime());

        return TimeTracking.builder()
                .id(TimeTrackingId.of(jpaEntity.getId()))
                .bookingId(jpaEntity.getBooking().getId())
                .period(period)
                .notes(jpaEntity.getNotes())
                .build();
    }
}
