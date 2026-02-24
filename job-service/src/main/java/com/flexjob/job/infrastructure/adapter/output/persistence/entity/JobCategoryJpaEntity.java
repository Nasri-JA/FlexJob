package com.flexjob.job.infrastructure.adapter.output.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table( name = "job_categories" )
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobCategoryJpaEntity
{
   @Id
   @GeneratedValue( strategy = GenerationType.IDENTITY )
   private Long id;

   @Column( nullable = false, unique = true, length = 100 )
   private String name;

   @Column( columnDefinition = "TEXT" )
   private String description;
}
