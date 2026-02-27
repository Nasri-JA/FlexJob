package com.flexjob.engagement.application.infrastructure.adapter.output.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "application_messages")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationMessageJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private ApplicationJpaEntity application;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String messageText;
    @Column(nullable = false)
    private Long senderId;
    @Column(nullable = false)
    private LocalDateTime sentAt;
}
