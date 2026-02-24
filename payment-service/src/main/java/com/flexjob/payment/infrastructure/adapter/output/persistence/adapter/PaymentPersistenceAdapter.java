package com.flexjob.payment.infrastructure.adapter.output.persistence.adapter;

import com.flexjob.payment.application.port.output.*;
import com.flexjob.payment.domain.model.Payment;
import com.flexjob.payment.domain.vo.PaymentId;
import com.flexjob.payment.infrastructure.adapter.output.persistence.entity.PaymentJpaEntity;
import com.flexjob.payment.infrastructure.adapter.output.persistence.mapper.PaymentPersistenceMapper;
import com.flexjob.payment.infrastructure.adapter.output.persistence.repository.PaymentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PaymentPersistenceAdapter implements LoadPaymentPort, SavePaymentPort {

    private final PaymentJpaRepository repository;
    private final PaymentPersistenceMapper mapper;

    @Override
    public Optional<Payment> findById(PaymentId id) {
        return repository.findById(id.getValue()).map(mapper::toDomain);
    }

    @Override
    public List<Payment> findByBookingId(Long bookingId) {
        return repository.findByBookingId(bookingId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByBookingId(Long bookingId) {
        return repository.existsByBookingId(bookingId);
    }

    @Override
    public Payment save(Payment payment) {
        PaymentJpaEntity jpa = mapper.toJpaEntity(payment);
        PaymentJpaEntity saved = repository.save(jpa);
        return mapper.toDomain(saved);
    }
}
