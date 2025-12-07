package io.zaplink.notification.dto.request;

import lombok.Data;

@Data
public class EmailRequest {
    private String to;
    private String subject;
    private String body;
    private String cc;
    private String bcc;
    private String[] attachments;
}