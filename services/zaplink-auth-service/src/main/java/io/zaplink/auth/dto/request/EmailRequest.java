package io.zaplink.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class EmailRequest {
    private String to;
    private String subject;
    private String body;
    private String cc;
    private String bcc;
    private String[] attachments;
}
