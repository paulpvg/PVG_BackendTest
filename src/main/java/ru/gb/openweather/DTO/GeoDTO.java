package ru.gb.openweather.DTO;

import com.fasterxml.jackson.annotation.*;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GeoDTO {

    private String name;
    private Float lon;
    private Float lat;
    private String country;
    private String state;
}
