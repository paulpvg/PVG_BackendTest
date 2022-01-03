package ru.gb.openweather.DTO;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import ru.gb.openweather.Coordinates;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class AirPollutionDTO {

    private Coordinates coord;
    private List <DataList> list;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class DataList {

        private Components components;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        @Data
        public class Components {

            private Float co;
        }
    }
}
