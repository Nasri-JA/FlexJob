package com.flexjob.payment.infrastructure.adapter.output.persistence.repository;

import com.flexjob.payment.infrastructure.adapter.output.persistence.entity.PaymentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PaymentJpaRepository extends JpaRepository<PaymentJpaEntity, Long> {
    List<PaymentJpaEntity> findByBookingId(Long bookingId);
    boolean existsByBookingId(Long bookingId);
}
