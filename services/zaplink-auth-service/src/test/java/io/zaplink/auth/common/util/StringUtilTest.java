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
class StringUtilTest {

    @Test @DisplayName("Constructor should throw UnsupportedOperationException")
    void constructor_ShouldThrowUnsupportedOperationException() {
        // When & Then
        assertThrows(UnsupportedOperationException.class, () -> {
            // Use reflection to access private constructor
            try {
                java.lang.reflect.Constructor<StringUtil> constructor = StringUtil.class.getDeclaredConstructor();
                constructor.setAccessible(true);
                constructor.newInstance();
            } catch (Exception e) {
                if (e.getCause() instanceof UnsupportedOperationException) {
                    throw (UnsupportedOperationException) e.getCause();
                }
                throw new RuntimeException(e);
            }
        });
    }

    @Test @DisplayName("concat with multiple strings should concatenate all")
    void concat_WithMultipleStrings_ShouldConcatenateAll() {
        // Given
        String str1 = "Hello";
        String str2 = " ";
        String str3 = "World";
        String str4 = "!";
        
        // When
        String result = StringUtil.concat(str1, str2, str3, str4);
        
        // Then
        assertEquals("Hello World!", result);
    }

    @Test @DisplayName("concat with null array should return empty string")
    void concat_WithNullArray_ShouldReturnEmptyString() {
        // When
        String result = StringUtil.concat((String[]) null);
        
        // Then
        assertEquals("", result);
    }

    @Test @DisplayName("concat with empty array should return empty string")
    void concat_WithEmptyArray_ShouldReturnEmptyString() {
        // When
        String result = StringUtil.concat();
        
        // Then
        assertEquals("", result);
    }

    @Test @DisplayName("concat with null values should skip nulls")
    void concat_WithNullValues_ShouldSkipNulls() {
        // Given
        String str1 = "Hello";
        String str2 = null;
        String str3 = "World";
        
        // When
        String result = StringUtil.concat(str1, str2, str3);
        
        // Then
        assertEquals("HelloWorld", result);
    }

    @Test @DisplayName("concat with all null values should return empty string")
    void concat_WithAllNullValues_ShouldReturnEmptyString() {
        // When
        String result = StringUtil.concat(null, null, null);
        
        // Then
        assertEquals("", result);
    }

    @Test @DisplayName("appendValue with valid strings should concatenate")
    void appendValue_WithValidStrings_ShouldConcatenate() {
        // Given
        String base = "User ID: ";
        Object value = 123;
        
        // When
        String result = StringUtil.appendValue(base, value);
        
        // Then
        assertEquals("User ID: 123", result);
    }

    @Test @DisplayName("appendValue with null base should handle gracefully")
    void appendValue_WithNullBase_ShouldHandleGracefully() {
        // Given
        String base = null;
        Object value = "test";
        
        // When
        String result = StringUtil.appendValue(base, value);
        
        // Then
        assertEquals("test", result);
    }

    @Test @DisplayName("appendValue with null value should handle gracefully")
    void appendValue_WithNullValue_ShouldHandleGracefully() {
        // Given
        String base = "User: ";
        Object value = null;
        
        // When
        String result = StringUtil.appendValue(base, value);
        
        // Then
        assertEquals("User: ", result);
    }

    @Test @DisplayName("appendValue with both null should return empty string")
    void appendValue_WithBothNull_ShouldReturnEmptyString() {
        // Given
        String base = null;
        Object value = null;
        
        // When
        String result = StringUtil.appendValue(base, value);
        
        // Then
        assertEquals("", result);
    }

    @Test @DisplayName("createMessage should delegate to appendValue")
    void createMessage_ShouldDelegateToAppendValue() {
        // Given
        String prefix = "Error: ";
        Object value = "Invalid input";
        
        // When
        String result = StringUtil.createMessage(prefix, value);
        
        // Then
        assertEquals("Error: Invalid input", result);
    }

    @Test @DisplayName("createRangeMessage should create proper range message")
    void createRangeMessage_ShouldCreateProperRangeMessage() {
        // Given
        int minValue = 3;
        int maxValue = 50;
        String unit = "characters";
        
        // When
        String result = StringUtil.createRangeMessage(minValue, maxValue, unit);
        
        // Then
        assertEquals("between 3 and 50 characters", result);
    }

    @Test @DisplayName("createRangeMessage with null unit should handle gracefully")
    void createRangeMessage_WithNullUnit_ShouldHandleGracefully() {
        // Given
        int minValue = 1;
        int maxValue = 10;
        String unit = null;
        
        // When
        String result = StringUtil.createRangeMessage(minValue, maxValue, unit);
        
        // Then
        assertEquals("between 1 and 10 ", result);
    }

    @Test @DisplayName("createRangeMessage with empty unit should handle gracefully")
    void createRangeMessage_WithEmptyUnit_ShouldHandleGracefully() {
        // Given
        int minValue = 5;
        int maxValue = 25;
        String unit = "   ";
        
        // When
        String result = StringUtil.createRangeMessage(minValue, maxValue, unit);
        
        // Then
        assertEquals("between 5 and 25 ", result);
    }

    @Test @DisplayName("createSizeValidationMessage should create proper validation message")
    void createSizeValidationMessage_ShouldCreateProperValidationMessage() {
        // Given
        String fieldName = "Username";
        int minValue = 3;
        int maxValue = 20;
        
        // When
        String result = StringUtil.createSizeValidationMessage(fieldName, minValue, maxValue);
        
        // Then
        assertEquals("Username must be between 3 and 20 characters", result);
    }

    @Test @DisplayName("createMaxSizeValidationMessage should create proper max validation message")
    void createMaxSizeValidationMessage_ShouldCreateProperMaxValidationMessage() {
        // Given
        String fieldName = "Password";
        int maxValue = 128;
        String unit = "characters";
        
        // When
        String result = StringUtil.createMaxSizeValidationMessage(fieldName, maxValue, unit);
        
        // Then
        assertEquals("Password must not exceed 128 characters", result);
    }

    @Test @DisplayName("createMaxSizeValidationMessage with null unit should handle gracefully")
    void createMaxSizeValidationMessage_WithNullUnit_ShouldHandleGracefully() {
        // Given
        String fieldName = "Field";
        int maxValue = 100;
        String unit = null;
        
        // When
        String result = StringUtil.createMaxSizeValidationMessage(fieldName, maxValue, unit);
        
        // Then
        assertEquals("Field must not exceed 100", result);
    }

    @Test @DisplayName("createRoleName should add ROLE_ prefix")
    void createRoleName_ShouldAddROLE_Prefix() {
        // Given
        String role = "USER";
        
        // When
        String result = StringUtil.createRoleName(role);
        
        // Then
        assertEquals("ROLE_USER", result);
    }

    @Test @DisplayName("createRoleName with null role should handle gracefully")
    void createRoleName_WithNullRole_ShouldHandleGracefully() {
        // Given
        String role = null;
        
        // When
        String result = StringUtil.createRoleName(role);
        
        // Then
        assertEquals("ROLE_", result);
    }

    @Test @DisplayName("createMaskedToken should mask token properly")
    void createMaskedToken_ShouldMaskTokenProperly() {
        // Given
        String token = "12345678-1234-1234-1234-123456789012";
        int maskLength = 8;
        String suffix = "***";
        
        // When
        String result = StringUtil.createMaskedToken(token, maskLength, suffix);
        
        // Then
        assertEquals("12345678***", result);
    }

    @Test @DisplayName("createMaskedToken with null token should return suffix")
    void createMaskedToken_WithNullToken_ShouldReturnSuffix() {
        // Given
        String token = null;
        int maskLength = 8;
        
        // When
        String result = StringUtil.createMaskedToken(token, maskLength, "***");
        
        // Then
        assertEquals("***", result);
    }

    @Test @DisplayName("createMaskedToken with short token should return suffix")
    void createMaskedToken_WithShortToken_ShouldReturnSuffix() {
        // Given
        String token = "123";
        int maskLength = 8;
        String suffix = "***";
        
        // When
        String result = StringUtil.createMaskedToken(token, maskLength, suffix);
        
        // Then
        assertEquals("***", result);
    }

    @Test @DisplayName("createMaskedToken with exact length should return suffix")
    void createMaskedToken_WithExactLength_ShouldReturnSuffix() {
        // Given
        String token = "12345678";
        int maskLength = 8;
        String suffix = "***";
        
        // When
        String result = StringUtil.createMaskedToken(token, maskLength, suffix);
        
        // Then
        assertEquals("***", result);
    }
}
