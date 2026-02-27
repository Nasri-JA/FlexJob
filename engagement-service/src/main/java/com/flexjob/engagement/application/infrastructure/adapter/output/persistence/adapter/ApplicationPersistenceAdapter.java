package com.flexjob.engagement.application.infrastructure.adapter.output.persistence.adapter;

import com.flexjob.engagement.application.application.port.output.LoadApplicationPort;
import com.flexjob.engagement.application.application.port.output.SaveApplicationPort;
import com.flexjob.engagement.application.domain.model.Application;
import com.flexjob.engagement.application.domain.vo.ApplicationId;
import com.flexjob.engagement.application.enums.ApplicationStatus;
import com.flexjob.engagement.application.infrastructure.adapter.output.persistence.mapper.ApplicationPersistenceMapper;
import com.flexjob.engagement.application.infrastructure.adapter.output.persistence.repository.ApplicationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ApplicationPersistenceAdapter implements LoadApplicationPort, SaveApplicationPort {
    private final ApplicationJpaRepository repository;
    private final ApplicationPersistenceMapper mapper;

    @Override public Optional<Application> findById(ApplicationId applicationId) { return repository.findById(applicationId.getValue()).map(mapper::toDomain); }
    @Override public List<Application> findByJobId(Long jobId) { return mapper.toDomainList(repository.findByJobId(jobId)); }
    @Override public List<Application> findByEmployeeId(Long employeeId) { return mapper.toDomainList(repository.findByEmployeeId(employeeId)); }
    @Override public List<Application> findByEmployerId(Long employerId) { return mapper.toDomainList(repository.findByEmployerId(employerId)); }
    @Override public List<Application> findByStatus(ApplicationStatus status) { return mapper.toDomainList(repository.findByStatus(status)); }
    @Override public boolean existsById(ApplicationId applicationId) { return repository.existsById(applicationId.getValue()); }
    @Override public boolean existsByJobIdAndEmployeeId(Long jobId, Long employeeId) { return repository.existsByJobIdAndEmployeeId(jobId, employeeId); }
    @Override public Application save(Application application) { return mapper.toDomain(repository.save(mapper.toJpaEntity(application))); }
}
