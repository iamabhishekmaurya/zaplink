package io.zaplink.auth.dto.request;

import lombok.Builder;

@Builder
public record EmailRequest( String to, String subject, String body, String cc, String bcc, String[] attachments )
{
}
