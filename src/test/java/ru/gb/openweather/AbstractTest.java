package ru.gb.openweather;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.*;
import io.restassured.http.ContentType;
import io.restassured.specification.*;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;

import java.io.*;

import java.util.*;

public abstract class AbstractTest {

    static Properties prop = new Properties();

    final static String baseUrl = "http://api.openweathermap.org/data/2.5/";
    final static String geoUrl = "http://api.openweathermap.org/geo/1.0/";

    static RequestSpecification requestSpecification;
    static ResponseSpecification responseSpecification;

    @BeforeAll
    static void setUp() throws IOException {

        RestAssured.filters(new AllureRestAssured());
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        FileInputStream fis = new FileInputStream("src/test/resources/openweather.properties");
        prop.load(fis);

        requestSpecification = new RequestSpecBuilder()
                .addParam("units", "metric")
                .addParam("appid", prop.get("appid"))
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.ANY)
                .build();

        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectStatusLine("HTTP/1.1 200 OK")
                .expectContentType(ContentType.JSON)
                .expectResponseTime(Matchers.lessThan(5000L))
                .expectHeader("Server", "openresty")
                .expectHeader("Access-Control-Allow-Credentials", "true")
                .build();
    }
}

