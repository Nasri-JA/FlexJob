package com.flexjob.job.infrastructure.persistence.entity;

import com.flexjob.job.domain.model.JobStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table( name = "jobs" )
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobJpaEntity
{
   @Id
   @GeneratedValue( strategy = GenerationType.IDENTITY )
   private Long id;

   @Column( nullable = false )
   private Long employerId;

   @ManyToOne( fetch = FetchType.EAGER )
   @JoinColumn( name = "category_id", nullable = false )
   private JobCategoryJpaEntity category;

   @Column( nullable = false, length = 200 )
   private String title;

   @Column( columnDefinition = "TEXT" )
   private String description;

   @Column( nullable = false )
   private String location;

   @Column( nullable = false )
   private Double hourlyRate;

   @Column( nullable = false )
   private Integer durationHours;

   @Enumerated( EnumType.STRING )
   @Column( nullable = false, length = 20 )
   @Builder.Default
   private JobStatus status = JobStatus.OPEN;

   @OneToMany( mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true )
   @Builder.Default
   private List<JobRequirementJpaEntity> requirements = new ArrayList<>();

   @CreationTimestamp
   @Column( nullable = false, updatable = false )
   private LocalDateTime createdAt;

   @UpdateTimestamp
   @Column( nullable = false )
   private LocalDateTime updatedAt;

   public void addRequirement( JobRequirementJpaEntity requirement )
   {
      requirements.add( requirement );
      requirement.setJob( this );
   }

   public void removeRequirement( JobRequirementJpaEntity requirement )
   {
      requirements.remove( requirement );
      requirement.setJob( null );
   }

   public void clearRequirements()
   {
      requirements.clear();
   }
}
