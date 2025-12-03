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

    /**
     * Creates a range message (e.g., "between 3 and 50 characters").
     * 
     * @param minValue Minimum value
     * @param maxValue Maximum value
     * @param unit Unit description (e.g., "characters")
     * @return Range message
     */
    public static String createRangeMessage( int minValue, int maxValue, String unit )
    {
        StringBuilder builder = new StringBuilder( "between " );
        builder.append( minValue );
        builder.append( " and " );
        builder.append( maxValue );
        builder.append( " " );
        if ( unit != null && !unit.trim().isEmpty() )
        {
            builder.append( unit );
        }
        return builder.toString();
    }

    /**
     * Creates a size validation message.
     * 
     * @param fieldName Field name
     * @param minValue Minimum value
     * @param maxValue Maximum value
     * @return Validation message
     */
    public static String createSizeValidationMessage( String fieldName, int minValue, int maxValue )
    {
        StringBuilder builder = new StringBuilder();
        builder.append( fieldName );
        builder.append( " must be " );
        builder.append( createRangeMessage( minValue, maxValue, "characters" ) );
        return builder.toString();
    }

    /**
     * Creates a max size validation message.
     * 
     * @param fieldName Field name
     * @param maxValue Maximum value
     * @param unit Unit description
     * @return Validation message
     */
    public static String createMaxSizeValidationMessage( String fieldName, int maxValue, String unit )
    {
        StringBuilder builder = new StringBuilder();
        builder.append( fieldName );
        builder.append( " must not exceed " );
        builder.append( maxValue );
        if ( unit != null && !unit.trim().isEmpty() )
        {
            builder.append( " " );
            builder.append( unit );
        }
        return builder.toString();
    }

    /**
     * Creates a role name with "ROLE_" prefix.
     * 
     * @param role The role name
     * @return Role name with prefix
     */
    public static String createRoleName( String role )
    {
        return appendValue( "ROLE_", role );
    }

    /**
     * Creates a token with masked suffix.
     * 
     * @param token The original token
     * @param maskLength Length of visible part
     * @param suffix The suffix to append
     * @return Masked token
     */
    public static String createMaskedToken( String token, int maskLength, String suffix )
    {
        if ( token == null || token.length() <= maskLength )
        {
            return suffix;
        }
        StringBuilder builder = new StringBuilder();
        builder.append( token.substring( 0, maskLength ) );
        builder.append( suffix );
        return builder.toString();
    }
}
