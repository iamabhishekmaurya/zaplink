package io.zaplink.core.dto.response;

import java.util.List;

public record ShortnerResponse( String url, String traceId, List<String> tags )
{
}
