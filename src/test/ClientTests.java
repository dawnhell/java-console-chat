package test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

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