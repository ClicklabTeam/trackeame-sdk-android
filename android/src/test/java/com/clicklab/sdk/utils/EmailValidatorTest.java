package com.clicklab.sdk.utils;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EmailValidatorTest {

    private EmailValidator emailValidator;

    @Before
    public void initData() {
        emailValidator = new EmailValidator();
    }

    public String[] validEmailList() {
        return new String[]{"cliklab@yahoo.com",
                "cliklab-100@yahoo.com", "cliklab.100@yahoo.com",
                "cliklab111@cliklab.com", "cliklab-100@cliklab.net",
                "cliklab.100@cliklab.com.au", "cliklab@1.com",
                "cliklab@gmail.com.com", "cliklab+100@gmail.com",
                "cliklab-100@yahoo-test.com"};
    }

    public String[] invalidEmailList() {
        return new String[]{"cliklab", "cliklab@.com.ma",
                "cliklab123@gmail.b", "cliklab123@.com", "cliklab123@.com.com",
                ".cliklab@cliklab.com", "cliklab()*@gmail.com", "cliklab@%*.com",
                "cliklab..2002@gmail.com", "cliklab.@gmail.com",
                "cliklab@cliklab@gmail.com", "cliklab@gmail.com.1b"};
    }

    @Test
    public void validEmailTest() {
        checkEmailList(true, validEmailList());
    }

    @Test
    public void inValidEmailTest() {
        checkEmailList(false, invalidEmailList());
    }

    private void checkEmailList(boolean condition, String[] emails) {
        for (String temp : emails) {
            assertEquals(condition, emailValidator.validate(temp));
        }
    }
}