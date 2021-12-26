package ru.gb;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

import java.io.*;

import java.util.*;

public abstract class AbstractOpenWeatherTest {
    static Map<String, String> headers = new HashMap<>();
    static Properties prop = new Properties();

    @BeforeAll
    static void setUp() throws IOException {

        RestAssured.filters(new AllureRestAssured());
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        FileInputStream fis = new FileInputStream("src/test/resources/openweather.properties");
        prop.load(fis);

    }
}

