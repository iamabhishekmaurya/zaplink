package io.zaplink.core.common.exception;

/**
 * Exception thrown when a requested resource is not found.
 * Used for business logic where an entity doesn't exist.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
