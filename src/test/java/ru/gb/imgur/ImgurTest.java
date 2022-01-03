package ru.gb.imgur;

import io.qameta.allure.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import ru.gb.imgur.DTO.*;

import java.io.IOException;
import java.nio.file.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ImgurTest extends AbstractTest {

    @Test
    @Epic("Сервис Imgur")
    @DisplayName("Информация об акаунте")
    void getAccountInfoTest() {

        AccountDTO response = given()
                        .spec(requestSpecification)

                        .when()
                        .get(baseUrl + "/account/" + Username.name)
                        .prettyPeek()

                        .then()
                        .spec(responseSpecification)
                        .extract()
                        .body()
                        .as(AccountDTO.class);

        assertThat(response.getStatus(), equalTo(200));
        assertThat(response.getSuccess(), equalTo(true));
        assertThat(response.getData().getId(), equalTo(Username.ID));
        assertThat(response.getData().getUrl(), equalTo(Username.name));
    }

    @Order(1)
    @Test
    @Epic("Сервис Imgur")
    @Feature("Тестирование альбомов")
    @Story("Получение Album ID")
    @Description("Тест получения хэшей альбомов, хранящихся на сервисе Imgur, и сохранение в файл albumHash.csv для дальнейшего использования")
    @DisplayName("Получение Album ID")
    void getAlbumIDTest() throws IOException {

        HashesDTO response = given()
                        .spec(requestSpecification)

                        .when()
                        .get(baseUrl + "/account/" + Username.name + "/albums/ids/")
                        .prettyPeek()

                        .then()
                        .spec(responseSpecification)
                        .extract()
                        .body()
                        .as(HashesDTO.class);

        assertThat(response.getStatus(), equalTo(200));
        assertThat(response.getSuccess(), equalTo(true));
        assertThat(response.getData().size(), is(not(0)));

        Path file = Paths.get("src/test/resources/albumHash.csv");
        Files.write(file, response.getData());

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

        HashesDTO response = given()
                        .spec(requestSpecification)

                        .when()
                        .get(baseUrl + "/account/" + Username.name + "/images/ids/")
                        .prettyPeek()

                        .then()
                        .spec(responseSpecification)
                        .extract()
                        .body()
                        .as(HashesDTO.class);

        assertThat(response.getStatus(), equalTo(200));
        assertThat(response.getSuccess(), equalTo(true));
        assertThat(response.getData().size(), is(not(0)));

        Path file = Paths.get("src/test/resources/imageHash.csv");
        Files.write(file, response.getData());
    }

    @Test
    @Epic("Сервис Imgur")
    @Feature("Тестирование альбомов")
    @Story("Отсутствие авторизации на сервисе")
    @Description("Тест получения хэшей альбомов на сервисе Imgur с невыполненной авторизацией")
    @DisplayName("Получение Album ID. Отсутствие авторизации")
    void getAlbumIDNotAuthTest() {

        HashesErrorDTO response = given()

                .when()
                .get(baseUrl + "/account/" + Username.name + "/albums/ids/")
                .prettyPeek()

                .then()
                .statusLine("HTTP/1.1 401 Unauthorized")
                .contentType("application/json")
                .extract()
                .body()
                .as(HashesErrorDTO.class);

        assertThat(response.getStatus(), equalTo(401));
        assertThat(response.getSuccess(), equalTo(false));
        assertThat(response.getData().getMethod(), equalTo("GET"));
        assertThat(response.getData().getError(), equalTo("Authentication required"));
    }

    @Test
    @Epic("Сервис Imgur")
    @Feature("Тестирование альбомов")
    @Story("Ошибочная авторизации на сервисе")
    @Description("Тест получения хэшей альбомов на сервисе Imgur с попыткой авторизации неверным токеном")
    @DisplayName("Получение Album ID. Ошибочная авторизация")
    void getAlbumIDErrAuthTest() {

        HashesErrorDTO response = given()
                .headers("Authorization", "Bearer 222333222332-invalid__token-223332223322")

                .when()
                .get(baseUrl + "/account/" + Username.name + "/albums/ids/")
                .prettyPeek()

                .then()
                .statusLine("HTTP/1.1 403 Permission Denied")
                .contentType("application/json")
                .extract()
                .body()
                .as(HashesErrorDTO.class);

        assertThat(response.getStatus(), equalTo(403));
        assertThat(response.getSuccess(), equalTo(false));
        assertThat(response.getData().getMethod(), equalTo("GET"));
        assertThat(response.getData().getError(), equalTo("The access token provided is invalid."));
    }

    @Epic("Сервис Imgur")
    @Feature("Тестирование альбомов")
    @Story("Получение информации об альбомах")
    @Description("Тест получения информации об альбомах, хранящихся на сервисе Imgur, считав их ID из ранее сохраненного файла albumHash.csv")
    @ParameterizedTest (name = "{index} - {0} is a albumHash of album №{index}")
    @CsvFileSource(resources = "/albumHash.csv", numLinesToSkip = 0)
    @DisplayName("Информация об альбомах")
    void getAlbumTest(String albumHash) {

        InfoDTO response = given()
                .spec(requestSpecification)

                .when()
                .get(baseUrl + "/album/{albumHash}", albumHash)
                .prettyPeek()

                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(InfoDTO.class);

        assertThat(response.getStatus(), equalTo(200));
        assertThat(response.getSuccess(), equalTo(true));
        assertThat(response.getData().getAccountId(), equalTo(Username.ID));
        assertThat(response.getData().getAccountUrl(), equalTo(Username.name));
        assertThat(response.getData().getId(), equalTo(albumHash));
    }

    @Epic("Сервис Imgur")
    @Feature("Тестирование альбомов")
    @Story("Любимые альбомы")
    @Description("Тест установления/снятия флага избранности для альбомов, хранящихся на сервисе Imgur")
    @ParameterizedTest (name = "{index} - set/remove favorite for album №{index} with albumHash {0}")
    @CsvFileSource(resources = "/albumHash.csv", numLinesToSkip = 0)
    @DisplayName("Установка/снятие любимого альбома")
    void setFavoriteAlbum(String albumHash) {

        InfoDTO responseInfo = given()
                .spec(requestSpecification)

                .when()
                .get(baseUrl + "/album/{albumHash}", albumHash)

                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(InfoDTO.class);

        FavoriteDTO responseFavorite = given()
                .spec(requestSpecification)

                .when()
                .post(baseUrl + "/album/{albumHash}/favorite", albumHash)

                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(FavoriteDTO.class);

        assertThat(responseFavorite.getStatus(), equalTo(200));
        assertThat(responseFavorite.getSuccess(), equalTo(true));

        if(responseInfo.getData().getFavorite()) {
            assertThat(responseFavorite.getData(), equalTo("unfavorited"));
        } else {
            assertThat(responseFavorite.getData(), equalTo("favorited"));
        }
    }

    @Test
    @Epic("Сервис Imgur")
    @Feature("Тестирование изображений")
    @Story("Отсутствие авторизации на сервисе")
    @Description("Тест получения хэшей изображений на сервисе Imgur с невыполненной авторизацией")
    @DisplayName("Получение Image ID. Отсутствие авторизации")
    void getImageIDNotAuthTest() {

        HashesErrorDTO response = given()

                .when()
                .get(baseUrl + "/account/" + Username.name + "/images/ids/")
                .prettyPeek()

                .then()
                .statusLine("HTTP/1.1 401 Unauthorized")
                .contentType("application/json")
                .extract()
                .body()
                .as(HashesErrorDTO.class);

        assertThat(response.getStatus(), equalTo(401));
        assertThat(response.getSuccess(), equalTo(false));
        assertThat(response.getData().getMethod(), equalTo("GET"));
        assertThat(response.getData().getError(), equalTo("Authentication required"));
    }

    @Test
    @Epic("Сервис Imgur")
    @Feature("Тестирование изображений")
    @Story("Ошибочная авторизации на сервисе")
    @Description("Тест получения хэшей изображений на сервисе Imgur с попыткой авторизации неверным токеном")
    @DisplayName("Получение Image ID. Ошибочная авторизация")
    void getImageIDErrAuthTest() {

        HashesErrorDTO response = given()
                .headers("Authorization", "Bearer 222333222332-invalid__token-223332223322")

                .when()
                .get(baseUrl + "/account/" + Username.name + "/images/ids/")
                .prettyPeek()

                .then()
                .statusLine("HTTP/1.1 403 Permission Denied")
                .contentType("application/json")
                .extract()
                .body()
                .as(HashesErrorDTO.class);

        assertThat(response.getStatus(), equalTo(403));
        assertThat(response.getSuccess(), equalTo(false));
        assertThat(response.getData().getMethod(), equalTo("GET"));
        assertThat(response.getData().getError(), equalTo("The access token provided is invalid."));
    }

    @Epic("Сервис Imgur")
    @Feature("Тестирование изображений")
    @Story("Получение информации об изображениях")
    @Description("Тест получения информации о фотографиях, хранящихся на сервисе Imgur, считав их ID из ранее сохраненного файла imageHash.csv")
    @ParameterizedTest (name = "{index} - {0} is a imageHash of image№{index}")
    @CsvFileSource(resources = "/imageHash.csv", numLinesToSkip = 0)
    @DisplayName("Информация о фотографиях")
    void getImageByIDTest(String imageHash) {

        InfoDTO response = given()
                .spec(requestSpecification)

                .when()
                .get(baseUrl + "/image/{imageHash}", imageHash)
                .prettyPeek()

                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(InfoDTO.class);

        assertThat(response.getStatus(), equalTo(200));
        assertThat(response.getSuccess(), equalTo(true));
        assertThat(response.getData().getAccountId(), equalTo(Username.ID));
        assertThat(response.getData().getAccountUrl(), is(nullValue()));
        assertThat(response.getData().getId(), equalTo(imageHash));

    }

    @Epic("Сервис Imgur")
    @Feature("Тестирование изображений")
    @Story("Любимые фотографии")
    @Description("Тест установления/снятия флага избранности для фотографий, хранящихся на сервисе Imgur")
    @ParameterizedTest (name = "{index} - set/remove favorite for image with imageHash {0}")
    @CsvFileSource(resources = "/imageHash.csv", numLinesToSkip = 0)
    @DisplayName("Установка/снятие любимой фотографии")
    void setFavoriteImage(String imageHash) {

        InfoDTO responseInfo = given()
                .spec(requestSpecification)

                .when()
                .get(baseUrl + "/Image/{imageHash}", imageHash)

                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(InfoDTO.class);

        FavoriteDTO responseFavorite = given()
                .spec(requestSpecification)

                .when()
                .post(baseUrl + "/Image/{imageHash}/favorite", imageHash)

                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(FavoriteDTO.class);

        assertThat(responseFavorite.getStatus(), equalTo(200));
        assertThat(responseFavorite.getSuccess(), equalTo(true));

        if(responseInfo.getData().getFavorite()) {
            assertThat(responseFavorite.getData(), equalTo("unfavorited"));
        } else {
            assertThat(responseFavorite.getData(), equalTo("favorited"));
        }
    }
}
