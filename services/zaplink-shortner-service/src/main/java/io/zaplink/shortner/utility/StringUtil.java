package io.zaplink.shortner.utility;

public class StringUtil
{
    /**
    * Concatenates any number of strings using StringBuilder.
    *
    * @param parts variable number of string arguments
    * @return a single concatenated string
    */
    public static String concatStrings( String... parts )
    {
        StringBuilder sb = new StringBuilder();
        for ( String part : parts )
        {
            if ( part != null )
            { // avoid NullPointerException
                sb.append( part );
            }
        }
        return sb.toString();
    }
}
