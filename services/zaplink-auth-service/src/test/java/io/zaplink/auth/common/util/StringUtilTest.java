package io.zaplink.auth.common.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive test suite for StringUtil.
 * Tests all utility methods for string operations and formatting.
 */
@DisplayName("StringUtil Tests")
class StringUtilTest
{
    @Test @DisplayName("Constructor should throw UnsupportedOperationException")
    void constructor_ShouldThrowUnsupportedOperationException()
    {
        // When & Then
        assertThrows( UnsupportedOperationException.class, () -> {
            // Use reflection to access private constructor
            try
            {
                java.lang.reflect.Constructor<StringUtil> constructor = StringUtil.class.getDeclaredConstructor();
                constructor.setAccessible( true );
                constructor.newInstance();
            }
            catch ( Exception e )
            {
                if ( e.getCause() instanceof UnsupportedOperationException )
                {
                    throw (UnsupportedOperationException) e.getCause();
                }
                throw new RuntimeException( e );
            }
        } );
    }

    @Test @DisplayName("concat with multiple strings should concatenate all")
    void concat_WithMultipleStrings_ShouldConcatenateAll()
    {
        // Given
        String str1 = "Hello";
        String str2 = " ";
        String str3 = "World";
        String str4 = "!";
        // When
        String result = StringUtil.concat( str1, str2, str3, str4 );
        // Then
        assertEquals( "Hello World!", result );
    }

    @Test @DisplayName("concat with null array should return empty string")
    void concat_WithNullArray_ShouldReturnEmptyString()
    {
        // When
        String result = StringUtil.concat( (String[]) null );
        // Then
        assertEquals( "", result );
    }

    @Test @DisplayName("concat with empty array should return empty string")
    void concat_WithEmptyArray_ShouldReturnEmptyString()
    {
        // When
        String result = StringUtil.concat();
        // Then
        assertEquals( "", result );
    }

    @Test @DisplayName("concat with null values should skip nulls")
    void concat_WithNullValues_ShouldSkipNulls()
    {
        // Given
        String str1 = "Hello";
        String str2 = null;
        String str3 = "World";
        // When
        String result = StringUtil.concat( str1, str2, str3 );
        // Then
        assertEquals( "HelloWorld", result );
    }

    @Test @DisplayName("concat with all null values should return empty string")
    void concat_WithAllNullValues_ShouldReturnEmptyString()
    {
        // When
        String result = StringUtil.concat( null, null, null );
        // Then
        assertEquals( "", result );
    }

    @Test @DisplayName("appendValue with valid strings should concatenate")
    void appendValue_WithValidStrings_ShouldConcatenate()
    {
        // Given
        String base = "User ID: ";
        Object value = 123;
        // When
        String result = StringUtil.appendValue( base, value );
        // Then
        assertEquals( "User ID: 123", result );
    }

    @Test @DisplayName("appendValue with null base should handle gracefully")
    void appendValue_WithNullBase_ShouldHandleGracefully()
    {
        // Given
        String base = null;
        Object value = "test";
        // When
        String result = StringUtil.appendValue( base, value );
        // Then
        assertEquals( "test", result );
    }

    @Test @DisplayName("appendValue with null value should handle gracefully")
    void appendValue_WithNullValue_ShouldHandleGracefully()
    {
        // Given
        String base = "User: ";
        Object value = null;
        // When
        String result = StringUtil.appendValue( base, value );
        // Then
        assertEquals( "User: ", result );
    }

    @Test @DisplayName("appendValue with both null should return empty string")
    void appendValue_WithBothNull_ShouldReturnEmptyString()
    {
        // Given
        String base = null;
        Object value = null;
        // When
        String result = StringUtil.appendValue( base, value );
        // Then
        assertEquals( "", result );
    }

    @Test @DisplayName("createMessage should delegate to appendValue")
    void createMessage_ShouldDelegateToAppendValue()
    {
        // Given
        String prefix = "Error: ";
        Object value = "Invalid input";
        // When
        String result = StringUtil.createMessage( prefix, value );
        // Then
        assertEquals( "Error: Invalid input", result );
    }
}
