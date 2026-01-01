package io.zaplink.core.utility;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.zaplink.core.utility.SnowflakeShortKeyGenerator;

import java.util.HashSet;
import java.util.Set;

class SnowflakeShortKeyGeneratorTest
{
    @Test
    void testGenerateUniqueShortKeys()
    {
        SnowflakeShortKeyGenerator generator = new SnowflakeShortKeyGenerator( 1 );
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
        Assertions.assertThrows( IllegalArgumentException.class, () -> new SnowflakeShortKeyGenerator( -1 ) );
        Assertions.assertThrows( IllegalArgumentException.class, () -> new SnowflakeShortKeyGenerator( 1024 ) );
    }
}
