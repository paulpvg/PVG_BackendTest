package ru.gb.openweather.DTO;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import ru.gb.openweather.Coordinates;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CurrentWeatherDTO {

    private Coordinates coord;
    private Integer id;
    private String name;
    private Integer cod;
}
