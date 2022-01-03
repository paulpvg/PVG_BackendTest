package ru.gb.imgur;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.*;
import io.restassured.http.ContentType;
import io.restassured.specification.*;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;

public abstract class AbstractTest {

    final static String baseUrl = "https://api.imgur.com/3/";

    static RequestSpecification requestSpecification;
    static ResponseSpecification responseSpecification;

    @BeforeAll
    static void setUp() {

        RestAssured.filters(new AllureRestAssured());
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        String token = "Bearer 1b3557bee0d8e8b9dbe8deacf4d93f36a8def045";
        requestSpecification = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.ANY)
                .build();

        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectStatusLine("HTTP/1.1 200 OK")
                .expectContentType(ContentType.JSON)
                .expectResponseTime(Matchers.lessThan(5000L))
                .expectHeader("Server", "cat factory 1.0")
                .expectHeader("Access-Control-Allow-Credentials", "true")
                .build();
    }
}
