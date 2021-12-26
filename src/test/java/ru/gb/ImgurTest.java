package ru.gb;

import io.qameta.allure.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;


import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ImgurTest extends AbstractImgurTest
{
    @Test
    @Epic("Сервис Imgur")
    @DisplayName("Информация об акаунте")
    void getAccountInfoTest() {
        String url = given()
                .headers(headers)
                .expect()
                .body("success", is(true))
                .body("data.id", is(Username.ID))
                .when()
                .get("https://api.imgur.com/3/account/{username}", Username.name)
                .prettyPeek()
                .then()
                .statusCode(200)
                .contentType("application/json")
                .extract()
                .response()
                .jsonPath()
                .getString("data.url");
        assertThat(url, equalTo(Username.name));
    }

    @Order(1)
    @Test
    @Epic("Сервис Imgur")
    @Feature("Тестирование альбомов")
    @Story("Получение Album ID")
    @Description("Тест получения хэшей альбомов, хранящихся на сервисе Imgur, и сохранение в файл albumHash.csv для дальнейшего использования")
    @DisplayName("Получение Album ID")
    void getAlbumIDTest() throws IOException {
        List<String> data = given()
                .headers(headers)
                .expect()
                .body("success", is(true))
                .when()
                .get("https://api.imgur.com/3/account/{username}/albums/ids/", Username.name)
                .prettyPeek()
                .then()
                .statusCode(200)
                .contentType("application/json")
                .extract()
                .response()
                .jsonPath()
                .getList("data");

        Path file = Paths.get("src/test/resources/albumHash.csv");
        Files.write(file, data);

        // Как задумано - не работает, файл сохраняется и становится доступен только после прохождения всех тестов
        // В результате при отсутствии файла тесты проходят только со второй попытки
    }

    @Order(2)
    @Test
    @Epic("Сервис Imgur")
    @Feature("Тестирование изображений")
    @Story("Получение Image ID")
    @Description("Тест получения хэшей изображений, хранящихся на сервисе Imgur, и сохранение в файл imageHash.csv для дальнейшего использования")
    @DisplayName("Получение Image ID")
    void getImageIDTest() throws IOException {
        List<String> data = given()
                .headers(headers)
                .expect()
                .body("success", is(true))
                .when()
                .get("https://api.imgur.com/3/account/{username}/images/ids/", Username.name)
                .prettyPeek()
                .then()
                .statusCode(200)
                .contentType("application/json")
                .extract()
                .response()
                .jsonPath()
                .getList("data");

        Path file = Paths.get("src/test/resources/imageHash.csv");
        Files.write(file, data);
    }

    @Test
    @Epic("Сервис Imgur")
    @Feature("Тестирование альбомов")
    @Story("Отсутствие авторизации на сервисе")
    @Description("Тест получения хэшей альбомов на сервисе Imgur с невыполненной авторизацией")
    @DisplayName("Получение Album ID. Отсутствие авторизации")
    void getAlbumIDNotAuthTest() {
        String error = given()
                .expect()
                .body("success", is(false))
                .when()
                .get("https://api.imgur.com/3/account/{username}/albums/ids/", Username.name)
                .prettyPeek()
                .then()
                .statusCode(401)
                .contentType("application/json")
                .extract()
                .response()
                .jsonPath()
                .getString("data.error");
        assertThat(error, equalTo("Authentication required"));
    }

    @Test
    @Epic("Сервис Imgur")
    @Feature("Тестирование альбомов")
    @Story("Ошибочная авторизации на сервисе")
    @Description("Тест получения хэшей альбомов на сервисе Imgur с попыткой авторизации неверным токеном")
    @DisplayName("Получение Album ID. Ошибочная авторизация")
    void getAlbumIDErrAuthTest() {
        String error = given()
                .headers("Authorization", "Bearer 222333222332-invalid__token-223332223322")
                .expect()
                .body("success", is(false))
                .when()
                .get("https://api.imgur.com/3/account/{username}/albums/ids/", Username.name)
                .prettyPeek()
                .then()
                .statusCode(403)
                .contentType("application/json")
                .extract()
                .response()
                .jsonPath()
                .getString("data.error");
        assertThat(error, equalTo("The access token provided is invalid."));
    }

    @Epic("Сервис Imgur")
    @Feature("Тестирование альбомов")
    @Story("Получение информации об альбомах")
    @Description("Тест получения информации об альбомах, хранящихся на сервисе Imgur, считав их ID из ранее сохраненного файла albumHash.csv")
    @ParameterizedTest (name = "{index} - {0} is a albumHash of album №{index}")
    @CsvFileSource(resources = "/albumHash.csv", numLinesToSkip = 0)
    @DisplayName("Информация об альбомах")
    void getAlbumTest(String albumHash) {

        String url = given()
                .headers(headers)
                .expect()
                .body("success", is(true))
                .body("data.id", is(albumHash))
                .when()
                .get("https://api.imgur.com/3/album/{albumHash}", albumHash)
                .prettyPeek()
                .then()
                .statusCode(200)
                .contentType("application/json")
                .extract()
                .response()
                .jsonPath()
                .getString("data.account_url");
        assertThat(url, equalTo(Username.name));
    }

    @Epic("Сервис Imgur")
    @Feature("Тестирование альбомов")
    @Story("Любимые альбомы")
    @Description("Тест установления/снятия флага избранности для альбомов, хранящихся на сервисе Imgur")
    @ParameterizedTest (name = "{index} - set/remove favorite for album №{index} with albumHash {0}")
    @CsvFileSource(resources = "/albumHash.csv", numLinesToSkip = 0)
    @DisplayName("Установка/снятие любимого альбома")
    void setFavoriteAlbum(String albumHash) {
        Boolean favorite = given()
                .headers(headers)
                .when()
                .get("https://api.imgur.com/3/album/{albumHash}", albumHash)
                .then()
                .contentType("application/json")
                .extract()
                .response()
                .jsonPath()
                .getBoolean("data.favorite");

        String data = given().log().all()
                .headers(headers)
                .expect()
                .body("success", is(true))
                .when()
                .post("https://api.imgur.com/3/album/{albumHash}/favorite", albumHash)
                .prettyPeek()
                .then()
                .statusCode(200)
                .contentType("application/json")
                .extract()
                .response()
                .jsonPath()
                .getString("data");
        if (favorite) {
            assertThat(data, equalTo("unfavorited"));
        } else {
            assertThat(data, equalTo("favorited"));
        }
    }

    @Test
    @Epic("Сервис Imgur")
    @Feature("Тестирование изображений")
    @Story("Отсутствие авторизации на сервисе")
    @Description("Тест получения хэшей изображений на сервисе Imgur с невыполненной авторизацией")
    @DisplayName("Получение Image ID. Отсутствие авторизации")
    void getImageIDNotAuthTest() {
        String error = given()
                .expect()
                .body("success", is(false))
                .when()
                .get("https://api.imgur.com/3/account/{username}/images/ids/", Username.name)
                .prettyPeek()
                .then()
                .statusCode(401)
                .contentType("application/json")
                .extract()
                .response()
                .jsonPath()
                .getString("data.error");
        assertThat(error, equalTo("Authentication required"));
    }

    @Test
    @Epic("Сервис Imgur")
    @Feature("Тестирование изображений")
    @Story("Ошибочная авторизации на сервисе")
    @Description("Тест получения хэшей изображений на сервисе Imgur с попыткой авторизации неверным токеном")
    @DisplayName("Получение Image ID. Ошибочная авторизация")
    void getImageIDErrAuthTest() {
        String error = given()
                .headers("Authorization", "Bearer 222333222332-invalid__token-223332223322")
                .expect()
                .body("success", is(false))
                .when()
                .get("https://api.imgur.com/3/account/{username}/images/ids/", Username.name)
                .prettyPeek()
                .then()
                .statusCode(403)
                .contentType("application/json")
                .extract()
                .response()
                .jsonPath()
                .getString("data.error");
        assertThat(error, equalTo("The access token provided is invalid."));
    }

    @Epic("Сервис Imgur")
    @Feature("Тестирование изображений")
    @Story("Получение информации об изображениях")
    @Description("Тест получения информации о фотографиях, хранящихся на сервисе Imgur, считав их ID из ранее сохраненного файла imageHash.csv")
    @ParameterizedTest (name = "{index} - {0} is a imageHash of image№{index}")
    @CsvFileSource(resources = "/imageHash.csv", numLinesToSkip = 0)
    @DisplayName("Информация о фотографиях")
    void getImageByIDTest(String imageHash) {
        String ID = given()
                .headers(headers)
                .expect()
                .body("success", is(true))
                .when()
                .get("https://api.imgur.com/3/image/{imageHash}", imageHash)
                .prettyPeek()
                .then()
                .statusCode(200)
                .contentType("application/json")
                .extract()
                .response()
                .jsonPath()
                .getString("data.id");
        assertThat(ID, equalTo(imageHash));
    }

    @Epic("Сервис Imgur")
    @Feature("Тестирование изображений")
    @Story("Любимые фотографии")
    @Description("Тест установления/снятия флага избранности для фотографий, хранящихся на сервисе Imgur")
    @ParameterizedTest (name = "{index} - set/remove favorite for image with imageHash {0}")
    @CsvFileSource(resources = "/imageHash.csv", numLinesToSkip = 0)
    @DisplayName("Установка/снятие любимой фотографии")
    void setFavoriteImage(String imageHash) {
        Boolean favorite = given()
                .headers(headers)
                .when()
                .get("https://api.imgur.com/3/Image/{albumHash}", imageHash)
                .then()
                .contentType("application/json")
                .extract()
                .response()
                .jsonPath()
                .getBoolean("data.favorite");

        String data = given().log().all()
                .headers(headers)
                .expect()
                .body("success", is(true))
                .when()
                .post("https://api.imgur.com/3/Image/{albumHash}/favorite", imageHash)
                .prettyPeek()
                .then()
                .statusCode(200)
                .contentType("application/json")
                .extract()
                .response()
                .jsonPath()
                .getString("data");
        if (favorite) {
            assertThat(data, equalTo("unfavorited"));
        } else {
            assertThat(data, equalTo("favorited"));
        }
    }
}
