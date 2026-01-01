package io.zaplink.core.utility;

/**
 * SnowflakeShortKeyGenerator
 *
 * This class implements a URL shortener key generator using the Snowflake algorithm.
 * It produces globally unique, time-ordered 64-bit IDs and then maps them into the
 * 62^8 space (≈ 218 trillion possibilities) to generate exactly 8-character Base62 keys.
 *
 * Key Features:
 * - Uses a custom epoch (Jan 1, 2021) to reduce timestamp size.
 * - Combines timestamp, machine ID, and sequence number to ensure uniqueness.
 * - Encodes the generated ID into Base62 and folds it into the 8-character space.
 * - Thread-safe (synchronized) ID generation.
 *
 * Usage:
 *   SnowflakeShortKeyGenerator generator = new SnowflakeShortKeyGenerator(1);
 *   String shortKey = generator.generateShortKey();
 *
 * Notes:
 * - Ensure each instance of this generator has a unique machineId (0–1023).
 * - This avoids collisions when running multiple instances in distributed systems.
 * - The generated short keys are always 8 characters long, padded with '0' if needed.
 */
public class SnowflakeShortKeyGenerator
{
    // ---------------- Base62 Encoding ----------------
    /** Character set for Base62 encoding (0-9, A-Z, a-z). */
    private static final String BASE62           = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    /** Total possible unique 8-character Base62 combinations = 62^8. */
    private static final long   SPACE            = (long) Math.pow( 62, 8 );
    // ---------------- Snowflake Configuration ----------------
    /** Custom epoch (Jan 1, 2021 UTC) to reduce timestamp size. */
    private static final long   EPOCH            = 1609459200000L;
    /** Number of bits allocated for machine ID (10 bits → 0–1023). */
    private static final long   MACHINE_ID_BITS  = 10L;
    /** Number of bits allocated for sequence number (12 bits → 0–4095). */
    private static final long   SEQUENCE_BITS    = 12L;
    /** Maximum allowed machine ID (1023). */
    private static final long   MAX_MACHINE_ID   = ~ ( -1L << MACHINE_ID_BITS );
    /** Maximum allowed sequence number per millisecond (4095). */
    private static final long   MAX_SEQUENCE     = ~ ( -1L << SEQUENCE_BITS );
    /** Bit shift for machine ID (12 bits for sequence). */
    private static final long   MACHINE_ID_SHIFT = SEQUENCE_BITS;
    /** Bit shift for timestamp (12 + 10 bits). */
    private static final long   TIMESTAMP_SHIFT  = SEQUENCE_BITS + MACHINE_ID_BITS;
    // ---------------- Instance Variables ----------------
    /** Unique machine ID for this generator instance (0–1023). */
    private final long          machineId;
    /** Last timestamp when an ID was generated. */
    private long                lastTimestamp    = -1L;
    /** Sequence number within the same millisecond. */
    private long                sequence         = 0L;
    /**
     * Constructor for SnowflakeShortKeyGenerator.
     *
     * @param machineId Unique machine ID (0–1023). Must be different for each instance.
     */
    public SnowflakeShortKeyGenerator( long machineId )
    {
        if ( machineId < 0 || machineId > MAX_MACHINE_ID )
        {
            throw new IllegalArgumentException( "Machine ID must be between 0 and " + MAX_MACHINE_ID );
        }
        this.machineId = machineId;
    }

    /**
     * Generates the next unique Snowflake ID (64-bit).
     *
     * Structure of the ID:
     * - 41 bits: timestamp (milliseconds since custom epoch)
     * - 10 bits: machine ID
     * - 12 bits: sequence number (per millisecond)
     *
     * @return A unique 64-bit Snowflake ID.
     */
    public synchronized long nextId()
    {
        long timestamp = System.currentTimeMillis();
        // Handle clock rollback (system clock moved backwards)
        if ( timestamp < lastTimestamp )
        {
            throw new RuntimeException( "Clock moved backwards. Refusing to generate id." );
        }
        if ( timestamp == lastTimestamp )
        {
            // Same millisecond → increment sequence
            sequence = ( sequence + 1 ) & MAX_SEQUENCE;
            // Sequence overflow → wait for next millisecond
            if ( sequence == 0 )
            {
                while ( ( timestamp = System.currentTimeMillis() ) <= lastTimestamp )
                {
                }
            }
        }
        else
        {
            // New millisecond → reset sequence
            sequence = 0L;
        }
        lastTimestamp = timestamp;
        // Compose final 64-bit ID
        return ( ( timestamp - EPOCH ) << TIMESTAMP_SHIFT ) | ( machineId << MACHINE_ID_SHIFT ) | sequence;
    }

    /**
     * Generates a unique 8-character short key.
     *
     * Steps:
     * 1. Generate a unique Snowflake ID.
     * 2. Fold it into the 62^8 space using modulo operation.
     * 3. Encode the result into Base62.
     * 4. Pad with '0' if shorter than 8 characters.
     *
     * @return An 8-character Base62 short key.
     */
    public String generateShortKey()
    {
        long id = nextId();
        long reduced = id % SPACE; // fold into 62^8 space
        StringBuilder sb = new StringBuilder();
        while ( reduced > 0 )
        {
            sb.append( BASE62.charAt( (int) ( reduced % 62 ) ) );
            reduced /= 62;
        }
        String encoded = sb.reverse().toString();
        // Ensure exactly 8 characters (pad with '0' if needed)
        return String.format( "%8s", encoded ).replace( ' ', '0' );
    }
    /**
     * Example usage and test.
     * Generates 10 unique short keys and prints them.
     */
    /* public static void main( String[] args )
    {
        SnowflakeShortKeyGenerator generator = new SnowflakeShortKeyGenerator( 1 ); // machineId = 1
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