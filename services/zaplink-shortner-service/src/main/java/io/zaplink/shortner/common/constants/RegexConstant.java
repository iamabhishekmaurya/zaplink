package io.zaplink.shortner.common.constants;

public class RegexConstant
{
    public static final String URL_REGEX      = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$";
    public static final String TRACE_ID_REGEX = "^[A-Za-z0-9-]{0,32}$";
}
