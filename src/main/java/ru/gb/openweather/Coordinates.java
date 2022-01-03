package ru.gb.openweather;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Coordinates {

    private Float lon;
    private Float lat;
}
