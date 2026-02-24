package com.flexjob.application.domain.model;

import com.flexjob.application.domain.vo.MessageText;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ApplicationMessage {

    private Long id;
    private MessageText messageText;
    private Long senderId;
    private LocalDateTime sentAt;

    public static ApplicationMessage create(MessageText messageText, Long senderId) {
        if (messageText == null) {
            throw new IllegalArgumentException("Message text cannot be null");
        }
        if (senderId == null || senderId <= 0) {
            throw new IllegalArgumentException("Sender ID must be valid");
        }

        return ApplicationMessage.builder()
                .messageText(messageText)
                .senderId(senderId)
                .sentAt(LocalDateTime.now())
                .build();
    }
}
