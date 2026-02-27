package com.flexjob.engagement.application.application.dto.response;
import lombok.Builder;
import lombok.Value;
import java.time.LocalDateTime;
@Value
@Builder
public class MessageResponse {
    Long id;
    String messageText;
    Long senderId;
    LocalDateTime sentAt;
}
