package io.zaplink.auth.common.util;

/**
 * Utility class for efficient string operations using StringBuilder.
 * Provides reusable methods for string concatenation and formatting.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2025-11-30
 */
public final class StringUtil
{
    // Prevent instantiation
    private StringUtil()
    {
        throw new UnsupportedOperationException( "Utility class cannot be instantiated" );
    }

    /**
     * Concatenates multiple strings efficiently using StringBuilder.
     * 
     * @param strings Variable number of strings to concatenate
     * @return Concatenated string
     */
    public static String concat( String... strings )
    {
        if ( strings == null || strings.length == 0 )
        {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for ( String str : strings )
        {
            if ( str != null )
            {
                builder.append( str );
            }
        }
        return builder.toString();
    }

    /**
     * Concatenates a base string with a value (supports any Object type).
     * Commonly used for error messages and logging.
     * 
     * @param base The base string
     * @param value The value to append (any Object)
     * @return Concatenated string
     */
    public static String appendValue( String base, Object value )
    {
        if ( base == null )
        {
            base = "";
        }
        if ( value == null )
        {
            value = "";
        }
        return new StringBuilder( base ).append( value ).toString();
    }

    /**
     * Creates a message with prefix and value (supports any Object type).
     * Commonly used for "User not found with ID: 123" type messages.
     * 
     * @param prefix The message prefix
     * @param value The value to append (any Object)
     * @return Formatted message
     */
    public static String createMessage( String prefix, Object value )
    {
        return appendValue( prefix, value );
    }
}
