package ru.gb.openweather.DTO;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import ru.gb.openweather.Coordinates;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ForecastWeatherDTO {

    private Integer cod;
    private City city;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public class City {
        private Integer id;
        private String name;
        private Coordinates coord;
        private String country;
    }
}
