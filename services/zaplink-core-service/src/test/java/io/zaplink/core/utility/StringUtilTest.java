package io.zaplink.core.utility;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.zaplink.core.utility.StringUtil;

class StringUtilTest
{
    @Test
    void testConcatStrings()
    {
        String result = StringUtil.concatStrings( "Hello", " ", "World" );
        Assertions.assertEquals( "Hello World", result );
    }

    @Test
    void testConcatStringsWithNulls()
    {
        String result = StringUtil.concatStrings( "Hello", null, "World" );
        Assertions.assertEquals( "HelloWorld", result );
    }

    @Test
    void testConcatStringsEmpty()
    {
        String result = StringUtil.concatStrings();
        Assertions.assertEquals( "", result );
    }
}
