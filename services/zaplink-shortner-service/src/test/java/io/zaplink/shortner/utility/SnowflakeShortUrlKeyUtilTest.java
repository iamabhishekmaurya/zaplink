package io.zaplink.shortner.utility;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

class SnowflakeShortUrlKeyUtilTest
{
    @Test
    void testGenerateUniqueShortKeys()
    {
        SnowflakeShortUrlKeyUtil generator = new SnowflakeShortUrlKeyUtil( 1 );
        Set<String> keys = new HashSet<>();
        for ( int i = 0; i < 1000; i++ )
        {
            String key = generator.generateShortKey();
            Assertions.assertFalse( keys.contains( key ), "Duplicate key generated: " + key );
            keys.add( key );
            Assertions.assertEquals( 8, key.length(), "Key length should be 8" );
        }
    }

    @Test
    void testInvalidMachineId()
    {
        Assertions.assertThrows( IllegalArgumentException.class, () -> new SnowflakeShortUrlKeyUtil( -1 ) );
        Assertions.assertThrows( IllegalArgumentException.class, () -> new SnowflakeShortUrlKeyUtil( 1024 ) );
    }
}
