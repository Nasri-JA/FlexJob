package com.flexjob.job.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table( name = "job_requirements" )
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobRequirementJpaEntity
{
   @Id
   @GeneratedValue( strategy = GenerationType.IDENTITY )
   private Long id;

   @ManyToOne( fetch = FetchType.LAZY )
   @JoinColumn( name = "job_id", nullable = false )
   private JobJpaEntity job;

   @Column( nullable = false, length = 500 )
   private String requirementText;

   @Column( nullable = false )
   @Builder.Default
   private Boolean isMandatory = true;
}
