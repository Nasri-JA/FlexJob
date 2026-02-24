package com.flexjob.booking.infrastructure.adapter.output.persistence.mapper;

import com.flexjob.booking.domain.enums.BookingStatus;
import com.flexjob.booking.domain.model.Booking;
import com.flexjob.booking.domain.vo.BookingId;
import com.flexjob.booking.domain.vo.DateRange;
import com.flexjob.booking.infrastructure.adapter.output.persistence.entity.BookingJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class BookingPersistenceMapper {

    public BookingJpaEntity toJpaEntity(Booking booking) {
        return BookingJpaEntity.builder()
                .id(booking.getId() != null ? booking.getId().getValue() : null)
                .jobId(booking.getJobId())
                .employeeId(booking.getEmployeeId())
                .employerId(booking.getEmployerId())
                .applicationId(booking.getApplicationId())
                .status(booking.getStatus())
                .startDate(booking.getDateRange().getStartDate())
                .endDate(booking.getDateRange().getEndDate())
                .createdAt(booking.getCreatedAt())
                .build();
    }

    public Booking toDomain(BookingJpaEntity jpaEntity) {
        return Booking.builder()
                .id(BookingId.of(jpaEntity.getId()))
                .jobId(jpaEntity.getJobId())
                .employeeId(jpaEntity.getEmployeeId())
                .employerId(jpaEntity.getEmployerId())
                .applicationId(jpaEntity.getApplicationId())
                .status(jpaEntity.getStatus())
                .dateRange(DateRange.of(jpaEntity.getStartDate(), jpaEntity.getEndDate()))
                .createdAt(jpaEntity.getCreatedAt())
                .build();
    }
}
