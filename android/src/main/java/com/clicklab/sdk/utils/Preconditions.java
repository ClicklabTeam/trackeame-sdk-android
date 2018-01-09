package com.clicklab.sdk.utils;

public class Preconditions {

    private static final String NULL_DEFAULT_MESSAGE = "Null parameter given";
    private static final String ASSERTION_DEFAULT_MESSAGE = "False assertion";

    /**
     * Checks the given object to ckeck wether is null or not
     * in case is null a IllegalArgumentException is thrown with a
     * generic error message
     *
     * @param object object to check for null
     */
    public static void checkNotNull(Object object) {
        checkNotNull(object, NULL_DEFAULT_MESSAGE);
    }

    /**
     * Checks the given object to ckeck wether is null or not
     * in case it's null it throws the given message
     *
     * @param object  object to check for null
     * @param message message to insert in the exception in case is needed
     */
    public static void checkNotNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }


    /**
     * Checks the given condition is true or not
     * in case is false a IllegalArgumentException is thrown with a
     * generic error message
     *
     * @param assertion assertion to be checked
     */
    public static void checkTrue(boolean assertion) {
        checkTrue(assertion, ASSERTION_DEFAULT_MESSAGE);
    }

    /**
     * Checks the given condition is true or not
     * in case is false a IllegalArgumentException is thrown with a
     * generic error message
     *
     * @param assertion assertion to be checked
     * @param message   message to insert in the exception in case is needed
     */
    public static void checkTrue(boolean assertion, String message) {
        if (!assertion) {
            throw new IllegalArgumentException(message);
        }
    }
}