package com.learn.google.authenticator;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.InitBinder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
class UtilsTest {

    @InitBinder
    public void setup() {
        System.out.println("Setup is running...");
    }


    @Test
    void testGenerateSecretKey() {
        String secretKey = Utils.generateSecretKey();
        assertNotNull(secretKey, "The generated secret key should not be null");
    }

    @Test
    void testGenerateSecretKeyLength() {
        String secretKey = Utils.generateSecretKey();
        assertEquals(32, secretKey.length(), "The generated secret key should have a length of 32");
    }

}