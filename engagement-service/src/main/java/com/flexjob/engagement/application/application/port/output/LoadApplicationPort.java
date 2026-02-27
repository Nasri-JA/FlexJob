package com.flexjob.engagement.application.application.port.output;
import com.flexjob.engagement.application.domain.model.Application;
import com.flexjob.engagement.application.domain.vo.ApplicationId;
import com.flexjob.engagement.application.enums.ApplicationStatus;
import java.util.List;
import java.util.Optional;
public interface LoadApplicationPort {
    Optional<Application> findById(ApplicationId applicationId);
    List<Application> findByJobId(Long jobId);
    List<Application> findByEmployeeId(Long employeeId);
    List<Application> findByEmployerId(Long employerId);
    List<Application> findByStatus(ApplicationStatus status);
    boolean existsById(ApplicationId applicationId);
    boolean existsByJobIdAndEmployeeId(Long jobId, Long employeeId);
}
