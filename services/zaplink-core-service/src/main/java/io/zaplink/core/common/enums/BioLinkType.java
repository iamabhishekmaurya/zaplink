package io.zaplink.core.common.enums;

/**
     * Enum defining the types of bio links available.
     * Each type has specific validation rules and behavior.
     * 
     * <p><strong>Java 21 Features Used:</strong></p>
     * <ul>
     *   <li>Enhanced switch expressions with pattern matching</li>
     *   <li>Record patterns for type-safe validation</li>
     *   <li>Sealed interface approach (simplified for compatibility)</li>
     * </ul>
     */
public enum BioLinkType {
    /**
     * Regular website link - requires URL, no pricing.
     */
    LINK("LINK", true, false),
    /**
     * Social media link - requires URL, no pricing.
     */
    SOCIAL("SOCIAL", true, false),
    /**
     * Product link - optional URL, requires pricing.
     */
    PRODUCT("PRODUCT", false, true),
    /**
     * Email link - requires email format, no pricing.
     */
    EMAIL("EMAIL", false, false),
    /**
     * Phone link - requires phone format, no pricing.
     */
    PHONE("PHONE", false, false),
    /**
     * Embed link - requires URL for oEmbed content (YouTube, Spotify, Twitter).
     */
    EMBED("EMBED", true, false),
    /**
     * Scheduled link - has visibility time window, requires URL.
     */
    SCHEDULED("SCHEDULED", true, false),
    /**
     * Gated link - password or email capture required, requires URL.
     */
    GATED("GATED", true, false),
    /**
     * Payment link - for Razorpay checkout integration, supports pricing.
     */
    PAYMENT("PAYMENT", false, true);
    private final String  typeName;
    private final boolean requiresUrl;
    private final boolean supportsPricing;
    /**
     * Constructor for BioLinkType enum.
     * 
     * @param typeName the display name of the type
     * @param requiresUrl whether this type requires a URL
     * @param supportsPricing whether this type supports pricing
     */
    BioLinkType( String typeName, boolean requiresUrl, boolean supportsPricing )
    {
        this.typeName = typeName;
        this.requiresUrl = requiresUrl;
        this.supportsPricing = supportsPricing;
    }

    /**
     * Gets the string representation of the link type.
     * 
     * @return the type name as a string
     */
    public String getTypeName()
    {
        return typeName;
    }

    /**
     * Checks if this link type requires a URL.
     * 
     * @return true if a URL is required, false otherwise
     */
    public boolean requiresUrl()
    {
        return requiresUrl;
    }

    /**
     * Checks if this link type supports pricing.
     * 
     * @return true if pricing is supported, false otherwise
     */
    public boolean supportsPricing()
    {
        return supportsPricing;
    }

    /**
     * Creates a BioLinkType from a string value.
     * Uses Java 21 switch expression for type safety.
     * 
     * @param typeName the type name string
     * @return the corresponding BioLinkType
     * @throws IllegalArgumentException if the type is unknown
     */
    public static BioLinkType fromString( String typeName )
    {
        return switch ( typeName.toUpperCase() )
        {
            case "LINK" -> LINK;
            case "SOCIAL" -> SOCIAL;
            case "PRODUCT" -> PRODUCT;
            case "EMAIL" -> EMAIL;
            case "PHONE" -> PHONE;
            case "EMBED" -> EMBED;
            case "SCHEDULED" -> SCHEDULED;
            case "GATED" -> GATED;
            case "PAYMENT" -> PAYMENT;
            default -> throw new IllegalArgumentException( "Unknown link type: " + typeName );
        };
    }
}
