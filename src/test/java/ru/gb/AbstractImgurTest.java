package ru.gb;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

import java.util.*;

public abstract class AbstractImgurTest {
    static Map<String, String> headers = new HashMap<>();

    @BeforeAll
    static void setUp() {

        RestAssured.filters(new AllureRestAssured());
        headers.put("Authorization", "Bearer 1b3557bee0d8e8b9dbe8deacf4d93f36a8def045");
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}
