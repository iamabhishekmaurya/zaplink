package io.zaplink.auth.common.util;

import java.security.SecureRandom;

/**
 * Provides reusable methods for common operations.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2025-12-07
 */
public class Utility
{
    private static final SecureRandom secureRandom = new SecureRandom();
    public static Integer grnerateVerificationCode()
    {
        return secureRandom.nextInt( 900000 ) + 100000;
    }
}
