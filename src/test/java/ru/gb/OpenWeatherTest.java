package ru.gb;

import io.qameta.allure.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class OpenWeatherTest extends AbstractOpenWeatherTest{

    @Epic("Сервис OpenWeather")
    @Feature("Current Weather API")
    @Story("По городу")
    @Description("Тестирование текущей погоды, запросив ее по имени города")
    @ParameterizedTest(name = "{index}: {0} is a city name")
    @CsvFileSource(resources = "/openweather.csv", numLinesToSkip = 0)
    @DisplayName("Текущая погода по наименованию города")
    void getCurrentWeatherByCityName(String cityName, int cityID, float longitude, float latitude) {
        Integer id = given()
                .expect()
                .body("coord.lon", is(longitude))
                .body("coord.lat", is(latitude))
                .when()
                .get("http://api.openweathermap.org/data/2.5/weather?q={cityName}&units=metric&appid={apiKey}", cityName, prop.get("appid"))
                .prettyPeek()
                .then()
                .statusCode(200)
                .contentType("application/json")
                .extract()
                .response()
                .jsonPath()
                .getInt("id");
        assertThat(id, equalTo(cityID));
    }

    @Epic("Сервис OpenWeather")
    @Feature("Current Weather API")
    @Story("По идентификатору города")
    @Description("Тестирование текущей погоды, запросив ее по идентификатору города")
    @ParameterizedTest(name = "{index}: {1} is a city ID of {0}")
    @CsvFileSource(resources = "/openweather.csv", numLinesToSkip = 0)
    @DisplayName("Текущая погода по идентификатору города")
    void getCurrentWeatherByCityID(String cityName, int cityID, float longitude, float latitude) {
        String name = given()
                .expect()
                .body("coord.lon", is(longitude))
                .body("coord.lat", is(latitude))
                .when()
                .get("http://api.openweathermap.org/data/2.5/weather?id={cityID}&units=metric&appid={apiKey}", cityID, prop.get("appid"))
                .prettyPeek()
                .then()
                .statusCode(200)
                .contentType("application/json")
                .extract()
                .response()
                .jsonPath()
                .getString("name");
        assertThat(name, equalTo(cityName));
    }

    @Epic("Сервис OpenWeather")
    @Feature("Current Weather API")
    @Story("По координатам города")
    @Description("Тестирование текущей погоды, запросив ее по координатам города")
    @ParameterizedTest(name = "{index}: {3} is a latitude, {2} is a longitude of {0}")
    @CsvFileSource(resources = "/openweather.csv", numLinesToSkip = 0)
    @DisplayName("Текущая погода по географическим координатам")
    void getCurrentWeatherByCoordinates(String cityName, int cityID, float longitude, float latitude) {
        String name = given()
                .expect()
                .body("id", is(cityID))
                .when()
                .get("http://api.openweathermap.org/data/2.5/weather?lat={latitude}&lon={longitude}&units=metric&appid={apiKey}", latitude, longitude, prop.get("appid"))
                .prettyPeek()
                .then()
                .statusCode(200)
                .contentType("application/json")
                .extract()
                .response()
                .jsonPath()
                .getString("name");
        assertThat(name, equalTo(cityName));
    }

    @Epic("Сервис OpenWeather")
    @Feature("Forecast Weather API")
    @Story("По городу")
    @Description("Тестирование прогноза погоды, запросив его по имени города")
    @ParameterizedTest(name = "{index}: {0} is a city name")
    @CsvFileSource(resources = "/openweather.csv", numLinesToSkip = 0)
    @DisplayName("Прогноз погоды по наименованию города")
    void getForecastWeatherByCityName(String cityName, int cityID, float longitude, float latitude) {
        Integer id = given()
                .expect()
                .body("city.coord.lon", is(longitude))
                .body("city.coord.lat", is(latitude))
                .when()
                .get("http://api.openweathermap.org/data/2.5/forecast?q={cityName}&units=metric&appid={apiKey}", cityName, prop.get("appid"))
                .prettyPeek()
                .then()
                .statusCode(200)
                .contentType("application/json")
                .extract()
                .response()
                .jsonPath()
                .getInt("city.id");
        assertThat(id, equalTo(cityID));
    }

    @Epic("Сервис OpenWeather")
    @Feature("Forecast Weather API")
    @Story("По идентификатору города")
    @Description("Тестирование прогноза погоды, запросив его по идентификатору города")
    @ParameterizedTest(name = "{index}: {1} is a city ID of {0}")
    @CsvFileSource(resources = "/openweather.csv", numLinesToSkip = 0)
    @DisplayName("Прогноз погоды по идентификатору города")
    void getForecastWeatherByCityID(String cityName, int cityID, float longitude, float latitude) {
        String name = given()
                .expect()
                .body("city.coord.lon", is(longitude))
                .body("city.coord.lat", is(latitude))
                .when()
                .get("http://api.openweathermap.org/data/2.5/forecast?id={cityID}&units=metric&appid={apiKey}", cityID, prop.get("appid"))
                .prettyPeek()
                .then()
                .statusCode(200)
                .contentType("application/json")
                .extract()
                .response()
                .jsonPath()
                .getString("city.name");
        assertThat(name, equalTo(cityName));
    }

    @Epic("Сервис OpenWeather")
    @Feature("Forecast Weather API")
    @Story("По координатам города")
    @Description("Тестирование прогноза погоды, запросив его по координатам города")
    @ParameterizedTest(name = "{index}: {3} is a latitude, {2} is a longitude of {0}")
    @CsvFileSource(resources = "/openweather.csv", numLinesToSkip = 0)
    @DisplayName("Прогноз погоды по географическим координатам")
    void getForecastWeatherByCoordinates(String cityName, int cityID, float longitude, float latitude) {
        String name = given()
                .expect()
                .body("city.id", is(cityID))
                .when()
                .get("http://api.openweathermap.org/data/2.5/forecast?lat={latitude}&lon={longitude}&units=metric&appid={apiKey}", latitude, longitude, prop.get("appid"))
                .prettyPeek()
                .then()
                .statusCode(200)
                .contentType("application/json")
                .extract()
                .response()
                .jsonPath()
                .getString("city.name");
        assertThat(name, equalTo(cityName));
    }

    @Epic("Сервис OpenWeather")
    @Feature("One Call API")
    @Story("Прогноз на час")
    @Description("Тестирование поминутного прогноза погоды на ближайший час, запросив его по координатам города")
    @ParameterizedTest(name = "{index}: {3} is a latitude, {2} is a longitude of {0}")
    @CsvFileSource(resources = "/openweather.csv", numLinesToSkip = 0)
    @DisplayName("Поминутный прогноз погоды на ближайший час")
    void getMinuteForecastFor1Hour(String cityName, int cityID, float longitude, float latitude, String timezone) {
        String tz = given()
                .expect()
                .body("lon", is(longitude))
                .body("lat", is(latitude))
                .when()
                .get("http://api.openweathermap.org/data/2.5/onecall?lat={latitude}&lon={longitude}&units=metric&exclude=current,hourly,daily,alerts&appid={apiKey}", latitude, longitude, prop.get("appid"))
                .prettyPeek()
                .then()
                .statusCode(200)
                .contentType("application/json")
                .extract()
                .response()
                .jsonPath()
                .getString("timezone");
        assertThat(tz, equalTo(timezone));
    }

    @Epic("Сервис OpenWeather")
    @Feature("One Call API")
    @Story("Прогноз на двое суток")
    @Description("Тестирование почасового прогноза погоды на ближайшие двое суток, запросив его по координатам города")
    @ParameterizedTest(name = "{index}: {3} is a latitude, {2} is a longitude of {0}")
    @CsvFileSource(resources = "/openweather.csv", numLinesToSkip = 0)
    @DisplayName("Почасовой прогноз погоды на двое суток")
    void getHourlyForecastFor48Hours(String cityName, int cityID, float longitude, float latitude, String timezone) {
        String tz = given()
                .expect()
                .body("lon", is(longitude))
                .body("lat", is(latitude))
                .when()
                .get("http://api.openweathermap.org/data/2.5/onecall?lat={latitude}&lon={longitude}&units=metric&exclude=current,minutely,daily,alerts&appid={apiKey}", latitude, longitude, prop.get("appid"))
                .prettyPeek()
                .then()
                .statusCode(200)
                .contentType("application/json")
                .extract()
                .response()
                .jsonPath()
                .getString("timezone");
        assertThat(tz, equalTo(timezone));
    }

    @Epic("Сервис OpenWeather")
    @Feature("One Call API")
    @Story("Прогноз на неделю")
    @Description("Тестирование ежедневного прогноза погоды на ближайшую неделю, запросив его по координатам города")
    @ParameterizedTest(name = "{index}: {3} is a latitude, {2} is a longitude of {0}")
    @CsvFileSource(resources = "/openweather.csv", numLinesToSkip = 0)
    @DisplayName("Ежедневный прогноз погоды на неделю")
    void getDailyForecastFor7Days(String cityName, int cityID, float longitude, float latitude, String timezone) {
        String tz = given()
                .expect()
                .body("lon", is(longitude))
                .body("lat", is(latitude))
                .when()
                .get("http://api.openweathermap.org/data/2.5/onecall?lat={latitude}&lon={longitude}&units=metric&exclude=current,minutely,hourly,alerts&appid={apiKey}", latitude, longitude, prop.get("appid"))
                .prettyPeek()
                .then()
                .statusCode(200)
                .contentType("application/json")
                .extract()
                .response()
                .jsonPath()
                .getString("timezone");
        assertThat(tz, equalTo(timezone));
    }

    @Epic("Сервис OpenWeather")
    @Feature("One Call API")
    @Story("Метеоусловия")
    @Description("Тестирование прогноза сложных метеоусловий, запросив его по координатам города")
    @ParameterizedTest(name = "{index}: {3} is a latitude, {2} is a longitude of {0}")
    @CsvFileSource(resources = "/openweather.csv", numLinesToSkip = 0)
    @DisplayName("Прогноз наличия сложных метеоусловий")
    void getNationalWeatherAlerts(String cityName, int cityID, float longitude, float latitude, String timezone) {
        String tz = given()
                .expect()
                .body("lon", is(longitude))
                .body("lat", is(latitude))
                .when()
                .get("http://api.openweathermap.org/data/2.5/onecall?lat={latitude}&lon={longitude}&units=metric&exclude=current,minutely,hourly,daily&appid={apiKey}", latitude, longitude, prop.get("appid"))
                .prettyPeek()
                .then()
                .statusCode(200)
                .contentType("application/json")
                .extract()
                .response()
                .jsonPath()
                .getString("timezone");
        assertThat(tz, equalTo(timezone));
    }

    @Epic("Сервис OpenWeather")
    @Feature("Air Pollution API")
    @Description("Тестирование наличия вредных примесей в атмосфере, запросив их по координатам города")
    @ParameterizedTest(name = "{index}: {3} is a latitude, {2} is a longitude of {0}")
    @CsvFileSource(resources = "/openweather.csv", numLinesToSkip = 0)
    @DisplayName("Прогноз чистоты воздуха в атмосфере")
    void getAirPollutionData(String cityName, int cityID, float longitude, float latitude) {
        String co = given()
                .expect()
                .body("coord.lon", is(longitude))
                .body("coord.lat", is(latitude))
                .when()
                .get("http://api.openweathermap.org/data/2.5/air_pollution?lat={latitude}&lon={longitude}&appid={apiKey}", latitude, longitude, prop.get("appid"))
                .prettyPeek()
                .then()
                .statusCode(200)
                .contentType("application/json")
                .extract()
                .response()
                .jsonPath()
                .getString("list.components.co");
        assertThat(co, not(emptyOrNullString()));
    }

    @Epic("Сервис OpenWeather")
    @Feature("Geocoding API")
    @Story("Прямой метод")
    @Description("Тестирование получения географических координат по наименованию города")
    @ParameterizedTest(name = "{index}: request coordinate for {0}")
    @CsvFileSource(resources = "/openweather.csv", numLinesToSkip = 0)
    @DisplayName("Географическим координаты по наименованию города")
    void getCoordinatesByLocationName(String cityName) {
        String name = given()
                .expect()
                .when()
                .get("http://api.openweathermap.org/geo/1.0/direct?q={cityName}&appid={apiKey}", cityName, prop.get("appid"))
                .prettyPeek()
                .then()
                .statusCode(200)
                .contentType("application/json")
                .extract()
                .response()
                .jsonPath()
                .getString("name[0]");
        assertThat(name, equalTo(cityName));
    }

    @Epic("Сервис OpenWeather")
    @Feature("Geocoding API")
    @Story("Обратный метод")
    @Description("Тестирование получения наименования города по географическим координатам")
    @ParameterizedTest(name = "{index}: {3} is a latitude, {2} is a longitude of {0}")
    @CsvFileSource(resources = "/openweather.csv", numLinesToSkip = 0)
    @DisplayName("Наименование города по географическим координатам")
    void getLocationNameByCoordinates(String cityName, int cityID, float longitude, float latitude) {
        String name = given()
                .expect()
                .when()
                .get("http://api.openweathermap.org/geo/1.0/reverse?lat={latitude}&lon={longitude}&appid={apiKey}", latitude, longitude, prop.get("appid"))
                .prettyPeek()
                .then()
                .statusCode(200)
                .contentType("application/json")
                .extract()
                .response()
                .jsonPath()
                .getString("name[0]");
        assertThat(name, equalTo(cityName));
    }
}
