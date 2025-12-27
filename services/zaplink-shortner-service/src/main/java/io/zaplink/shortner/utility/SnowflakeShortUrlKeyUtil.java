package io.zaplink.shortner.utility;

import java.util.HashSet;
import java.util.Set;

public class SnowflakeShortUrlKeyUtil
{
    // Custom epoch (e.g., Jan 1, 2021)
    private static final long   EPOCH            = 1609459200000L;
    // Bit allocations
    private static final long   MACHINE_ID_BITS  = 10L;
    private static final long   SEQUENCE_BITS    = 12L;
    private static final long   MAX_MACHINE_ID   = ~ ( -1L << MACHINE_ID_BITS );                                    // 1023
    private static final long   MAX_SEQUENCE     = ~ ( -1L << SEQUENCE_BITS );                                      // 4095
    private static final long   MACHINE_ID_SHIFT = SEQUENCE_BITS;
    private static final long   TIMESTAMP_SHIFT  = SEQUENCE_BITS + MACHINE_ID_BITS;
    private final long          machineId;
    private long                lastTimestamp    = -1L;
    private long                sequence         = 0L;
    // Base62 character set
    private static final String BASE62           = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    public SnowflakeShortUrlKeyUtil( long machineId )
    {
        if ( machineId < 0 || machineId > MAX_MACHINE_ID )
        {
            throw new IllegalArgumentException( "Machine ID must be between 0 and " + MAX_MACHINE_ID );
        }
        this.machineId = machineId;
    }

    // Generate unique Snowflake ID
    public synchronized long nextId()
    {
        long timestamp = System.currentTimeMillis();
        if ( timestamp < lastTimestamp )
        {
            throw new RuntimeException( "Clock moved backwards. Refusing to generate id." );
        }
        if ( timestamp == lastTimestamp )
        {
            sequence = ( sequence + 1 ) & MAX_SEQUENCE;
            if ( sequence == 0 )
            {
                // Sequence overflow, wait for next millisecond
                while ( ( timestamp = System.currentTimeMillis() ) <= lastTimestamp )
                {
                }
            }
        }
        else
        {
            sequence = 0L;
        }
        lastTimestamp = timestamp;
        return ( ( timestamp - EPOCH ) << TIMESTAMP_SHIFT ) | ( machineId << MACHINE_ID_SHIFT ) | sequence;
    }

    // Encode Snowflake ID into Base62 and trim/pad to 8 chars
    public String generateShortKey()
    {
        long id = nextId();
        long space = (long) Math.pow( 62, 8 ); // total possible 8-char combos
        long reduced = id % space; // fold into 8-char space
        StringBuilder sb = new StringBuilder();
        while ( reduced > 0 )
        {
            sb.append( BASE62.charAt( (int) ( reduced % 62 ) ) );
            reduced /= 62;
        }
        String encoded = sb.reverse().toString();
        return String.format( "%8s", encoded ).replace( ' ', '0' ); // pad to 8 chars
    }
    // Example usage
    /*  public static void main( String[] args )
    {
        SnowflakeShortUrlKeyUtil generator = new SnowflakeShortUrlKeyUtil( 1 ); // machineId = 1
        Set<String> keySet = new HashSet<>();
        int counter = 0;
        for ( int i = 0; i < 10000000; i++ )
        {
            String key = generator.generateShortKey();
            if ( keySet.contains( key ) )
            {
                System.out.println( key );
                counter++;
            }
            else
            {
                keySet.add( key );
            }
        }
        System.out.println( counter );
    } */
}
