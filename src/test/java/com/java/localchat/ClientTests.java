package com.java.localchat;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class ClientTests {
    @BeforeAll
    static void beforeAll() {
        System.out.println("Started Client tests.");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("Finished Client tests.");
    }
}