package ru.gb.openweather;

import io.qameta.allure.*;

import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import ru.gb.openweather.DTO.*;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class OpenWeatherTest extends AbstractTest {

    @Epic("Сервис OpenWeather")
    @Feature("Current Weather API")
    @Story("По городу")
    @Description("Тестирование текущей погоды, запросив ее по имени города")
    @ParameterizedTest(name = "{index}: {0} is a city name")
    @CsvFileSource(resources = "/openweather.csv", numLinesToSkip = 0)
    @DisplayName("Текущая погода по наименованию города")
    void getCurrentWeatherByCityName(String cityName, int cityID, float longitude, float latitude) {

        CurrentWeatherDTO response = given()
                .spec(requestSpecification)
                .param("q", cityName)

                .when()
                .get(baseUrl + "/weather")
                .prettyPeek()

                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(CurrentWeatherDTO.class);

        assertThat(response.getCod(), equalTo(200));
        assertThat(response.getName(), equalTo(cityName));
        assertThat(response.getId(), equalTo(cityID));
        assertThat(response.getCoord().getLon(), equalTo(longitude));
        assertThat(response.getCoord().getLat(), equalTo(latitude));
    }

    @Epic("Сервис OpenWeather")
    @Feature("Current Weather API")
    @Story("По идентификатору города")
    @Description("Тестирование текущей погоды, запросив ее по идентификатору города")
    @ParameterizedTest(name = "{index}: {1} is a city ID of {0}")
    @CsvFileSource(resources = "/openweather.csv", numLinesToSkip = 0)
    @DisplayName("Текущая погода по идентификатору города")
    void getCurrentWeatherByCityID(String cityName, int cityID, float longitude, float latitude) {

        CurrentWeatherDTO response = given()
                .spec(requestSpecification)
                .param("id", cityID)

                .when()
                .get(baseUrl + "/weather")
                .prettyPeek()

                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(CurrentWeatherDTO.class);

        assertThat(response.getCod(), equalTo(200));
        assertThat(response.getName(), equalTo(cityName));
        assertThat(response.getId(), equalTo(cityID));
        assertThat(response.getCoord().getLon(), equalTo(longitude));
        assertThat(response.getCoord().getLat(), equalTo(latitude));
    }

    @Epic("Сервис OpenWeather")
    @Feature("Current Weather API")
    @Story("По координатам города")
    @Description("Тестирование текущей погоды, запросив ее по координатам города")
    @ParameterizedTest(name = "{index}: {3} is a latitude, {2} is a longitude of {0}")
    @CsvFileSource(resources = "/openweather.csv", numLinesToSkip = 0)
    @DisplayName("Текущая погода по географическим координатам")
    void getCurrentWeatherByCoordinates(String cityName, int cityID, float longitude, float latitude) {

        CurrentWeatherDTO response = given()
                .spec(requestSpecification)
                .param("lon", longitude)
                .param("lat", latitude)

                .when()
                .get(baseUrl + "/weather")
                .prettyPeek()

                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(CurrentWeatherDTO.class);

        assertThat(response.getCod(), equalTo(200));
        assertThat(response.getName(), equalTo(cityName));
        assertThat(response.getId(), equalTo(cityID));
        assertThat(Math.ceil(response.getCoord().getLon()), equalTo(Math.ceil(longitude))); // В response у некоторых городов немного отличаются координаты
        assertThat(Math.ceil(response.getCoord().getLat()), equalTo(Math.ceil(latitude)));  // в тысячных, поэтому при сравнении округляем
    }

    @Epic("Сервис OpenWeather")
    @Feature("Forecast Weather API")
    @Story("По городу")
    @Description("Тестирование прогноза погоды, запросив его по имени города")
    @ParameterizedTest(name = "{index}: {0} is a city name")
    @CsvFileSource(resources = "/openweather.csv", numLinesToSkip = 0)
    @DisplayName("Прогноз погоды по наименованию города")
    void getForecastWeatherByCityName(String cityName, int cityID, float longitude, float latitude) {

        ForecastWeatherDTO response = given()
                .spec(requestSpecification)
                .param("q", cityName)

                .when()
                .get(baseUrl + "/forecast")
                .prettyPeek()

                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(ForecastWeatherDTO.class);

        assertThat(response.getCod(), equalTo(200));
        assertThat(response.getCity().getName(), equalTo(cityName));
        assertThat(response.getCity().getId(), equalTo(cityID));
        assertThat(response.getCity().getCoord().getLon(), equalTo(longitude));
        assertThat(response.getCity().getCoord().getLat(), equalTo(latitude));
    }

    @Epic("Сервис OpenWeather")
    @Feature("Forecast Weather API")
    @Story("По идентификатору города")
    @Description("Тестирование прогноза погоды, запросив его по идентификатору города")
    @ParameterizedTest(name = "{index}: {1} is a city ID of {0}")
    @CsvFileSource(resources = "/openweather.csv", numLinesToSkip = 0)
    @DisplayName("Прогноз погоды по идентификатору города")
    void getForecastWeatherByCityID(String cityName, int cityID, float longitude, float latitude) {

        ForecastWeatherDTO response = given()
                .spec(requestSpecification)
                .param("id", cityID)

                .when()
                .get(baseUrl + "/forecast")
                .prettyPeek()

                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(ForecastWeatherDTO.class);

        assertThat(response.getCod(), equalTo(200));
        assertThat(response.getCity().getName(), equalTo(cityName));
        assertThat(response.getCity().getId(), equalTo(cityID));
        assertThat(response.getCity().getCoord().getLon(), equalTo(longitude));
        assertThat(response.getCity().getCoord().getLat(), equalTo(latitude));
    }

    @Epic("Сервис OpenWeather")
    @Feature("Forecast Weather API")
    @Story("По координатам города")
    @Description("Тестирование прогноза погоды, запросив его по координатам города")
    @ParameterizedTest(name = "{index}: {3} is a latitude, {2} is a longitude of {0}")
    @CsvFileSource(resources = "/openweather.csv", numLinesToSkip = 0)
    @DisplayName("Прогноз погоды по географическим координатам")
    void getForecastWeatherByCoordinates(String cityName, int cityID, float longitude, float latitude) {

        ForecastWeatherDTO response = given()
                .spec(requestSpecification)
                .param("lon", longitude)
                .param("lat", latitude)

                .when()
                .get(baseUrl + "/forecast")
                .prettyPeek()

                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(ForecastWeatherDTO.class);

        assertThat(response.getCod(), equalTo(200));
        assertThat(response.getCity().getName(), equalTo(cityName));
        assertThat(response.getCity().getId(), equalTo(cityID));
        assertThat(Math.ceil(response.getCity().getCoord().getLon()), equalTo(Math.ceil(longitude))); // В response у некоторых городов немного отличаются координаты
        assertThat(Math.ceil(response.getCity().getCoord().getLat()), equalTo(Math.ceil(latitude)));  // в тысячных, поэтому при сравнении округляем
    }

    @Epic("Сервис OpenWeather")
    @Feature("One Call API")
    @Story("Прогноз на час")
    @Description("Тестирование поминутного прогноза погоды на ближайший час, запросив его по координатам города")
    @ParameterizedTest(name = "{index}: {3} is a latitude, {2} is a longitude of {0}")
    @CsvFileSource(resources = "/openweather.csv", numLinesToSkip = 0)
    @DisplayName("Поминутный прогноз погоды на ближайший час")
    void getMinuteForecastFor1Hour(String cityName, int cityID, float longitude, float latitude, String timezone) {

        OneCallDTO response = given()
                .spec(requestSpecification)
                .param("lon", longitude)
                .param("lat", latitude)
                .param("exclude", "current,hourly,daily,alerts")

                .when()
                .get(baseUrl + "/onecall")
                .prettyPeek()

                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(OneCallDTO.class);

        assertThat(response.getTimezone(), equalTo(timezone));
        assertThat(response.getLon(), equalTo(longitude));
        assertThat(response.getLat(), equalTo(latitude));
    }

    @Epic("Сервис OpenWeather")
    @Feature("One Call API")
    @Story("Прогноз на двое суток")
    @Description("Тестирование почасового прогноза погоды на ближайшие двое суток, запросив его по координатам города")
    @ParameterizedTest(name = "{index}: {3} is a latitude, {2} is a longitude of {0}")
    @CsvFileSource(resources = "/openweather.csv", numLinesToSkip = 0)
    @DisplayName("Почасовой прогноз погоды на двое суток")
    void getHourlyForecastFor48Hours(String cityName, int cityID, float longitude, float latitude, String timezone) {

        OneCallDTO response = given()
                .spec(requestSpecification)
                .param("lon", longitude)
                .param("lat", latitude)
                .param("exclude", "current,minutely,daily,alert")

                .when()
                .get(baseUrl + "/onecall")
                .prettyPeek()

                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(OneCallDTO.class);

        assertThat(response.getTimezone(), equalTo(timezone));
        assertThat(response.getLon(), equalTo(longitude));
        assertThat(response.getLat(), equalTo(latitude));
    }

    @Epic("Сервис OpenWeather")
    @Feature("One Call API")
    @Story("Прогноз на неделю")
    @Description("Тестирование ежедневного прогноза погоды на ближайшую неделю, запросив его по координатам города")
    @ParameterizedTest(name = "{index}: {3} is a latitude, {2} is a longitude of {0}")
    @CsvFileSource(resources = "/openweather.csv", numLinesToSkip = 0)
    @DisplayName("Ежедневный прогноз погоды на неделю")
    void getDailyForecastFor7Days(String cityName, int cityID, float longitude, float latitude, String timezone) {

        OneCallDTO response = given()
                .spec(requestSpecification)
                .param("lon", longitude)
                .param("lat", latitude)
                .param("exclude", "current,minutely,hourly,alerts")

                .when()
                .get(baseUrl + "/onecall")
                .prettyPeek()

                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(OneCallDTO.class);

        assertThat(response.getTimezone(), equalTo(timezone));
        assertThat(response.getLon(), equalTo(longitude));
        assertThat(response.getLat(), equalTo(latitude));
    }

    @Epic("Сервис OpenWeather")
    @Feature("One Call API")
    @Story("Метеоусловия")
    @Description("Тестирование прогноза сложных метеоусловий, запросив его по координатам города")
    @ParameterizedTest(name = "{index}: {3} is a latitude, {2} is a longitude of {0}")
    @CsvFileSource(resources = "/openweather.csv", numLinesToSkip = 0)
    @DisplayName("Прогноз наличия сложных метеоусловий")
    void getNationalWeatherAlerts(String cityName, int cityID, float longitude, float latitude, String timezone) {

        OneCallDTO response = given()
                .spec(requestSpecification)
                .param("lon", longitude)
                .param("lat", latitude)
                .param("exclude", "current,minutely,hourly,daily")

                .when()
                .get(baseUrl + "/onecall")
                .prettyPeek()

                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(OneCallDTO.class);

        assertThat(response.getTimezone(), equalTo(timezone));
        assertThat(response.getLon(), equalTo(longitude));
        assertThat(response.getLat(), equalTo(latitude));
    }

    @Epic("Сервис OpenWeather")
    @Feature("Air Pollution API")
    @Description("Тестирование наличия вредных примесей в атмосфере, запросив их по координатам города")
    @ParameterizedTest(name = "{index}: {3} is a latitude, {2} is a longitude of {0}")
    @CsvFileSource(resources = "/openweather.csv", numLinesToSkip = 0)
    @DisplayName("Прогноз чистоты воздуха в атмосфере")
    void getAirPollutionData(String cityName, int cityID, float longitude, float latitude) {

        AirPollutionDTO response = given()
                .spec(requestSpecification)
                .param("lon", longitude)
                .param("lat", latitude)

                .when()
                .get(baseUrl + "/air_pollution")
                .prettyPeek()

                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(AirPollutionDTO.class);

        assertThat(response.getCoord().getLon(), equalTo(longitude));
        assertThat(response.getCoord().getLat(), equalTo(latitude));
        assertThat(response.getList().get(0).getComponents().getCo(), is(notNullValue()));
    }

    @Epic("Сервис OpenWeather")
    @Feature("Geocoding API")
    @Story("Прямой метод")
    @Description("Тестирование получения географических координат по наименованию города")
    @ParameterizedTest(name = "{index}: request coordinate for {0}")
    @CsvFileSource(resources = "/openweather.csv", numLinesToSkip = 0)
    @DisplayName("Географическим координаты по наименованию города")
    void getCoordinatesByLocationName(String cityName, int cityID, float longitude, float latitude, String timezone, String country, String state) {

        List<GeoDTO> response = given()
                .spec(requestSpecification)
                .param("q", cityName)

                .when()
                .get(geoUrl + "/direct")
                .prettyPeek()

                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(new TypeRef<List<GeoDTO>> () {});

        assertThat(response.get(0).getName(),equalTo(cityName));
        assertThat(Math.ceil(response.get(0).getLon()), equalTo(Math.ceil(longitude))); // В response у некоторых городов немного отличаются координаты
        assertThat(Math.ceil(response.get(0).getLat()), equalTo(Math.ceil(latitude)));  // в тысячных, поэтому при сравнении округляем
        assertThat(response.get(0).getCountry(), equalTo(country));
        assertThat(response.get(0).getState(), equalTo(state));
    }

    @Epic("Сервис OpenWeather")
    @Feature("Geocoding API")
    @Story("Обратный метод")
    @Description("Тестирование получения наименования города по географическим координатам")
    @ParameterizedTest(name = "{index}: {3} is a latitude, {2} is a longitude of {0}")
    @CsvFileSource(resources = "/openweather.csv", numLinesToSkip = 0)
    @DisplayName("Наименование города по географическим координатам")
    void getLocationNameByCoordinates(String cityName, int cityID, float longitude, float latitude, String timezone, String country, String state) {

        List<GeoDTO> response = given()
                .spec(requestSpecification)
                .param("lon", longitude)
                .param("lat", latitude)

                .when()
                .get(geoUrl + "/reverse")
                .prettyPeek()

                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(new TypeRef<List<GeoDTO>> () {});

        assertThat(response.get(0).getName(),equalTo(cityName));
        assertThat(Math.ceil(response.get(0).getLon()), equalTo(Math.ceil(longitude))); // В response у некоторых городов немного отличаются координаты
        assertThat(Math.ceil(response.get(0).getLat()), equalTo(Math.ceil(latitude)));  // в тысячных, поэтому при сравнении округляем
        assertThat(response.get(0).getCountry(), equalTo(country));
        assertThat(response.get(0).getState(), equalTo(state));
    }
}
