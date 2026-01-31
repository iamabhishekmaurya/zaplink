package io.zaplink.core.common.exception;

/**
 * Exception thrown when business rules are violated.
 * Used for business logic validations that don't fit into validation annotations.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
public class BusinessException extends RuntimeException {
    
    public BusinessException(String message) {
        super(message);
    }
    
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
