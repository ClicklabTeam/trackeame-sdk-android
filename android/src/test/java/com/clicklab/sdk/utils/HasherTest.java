package com.clicklab.sdk.utils;

import junit.framework.Assert;

import org.junit.Test;

import static org.junit.Assert.*;

public class HasherTest {

    private static final String HELLO_SHA1_ENCODED = "c65f99f8c5376adadddc46d5cbcf5762f9e55eb7";
    private static final String HELLO_STRING = "HELLO";

    @Test
    public void whenSha1EncodeThenReturnCorrectHash() throws Exception {
        Assert.assertEquals(new Hasher().SHA1Encode(HELLO_STRING), HELLO_SHA1_ENCODED);
    }

}