package ru.gb.imgur.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class FavoriteDTO {

    private String data;
    private Boolean success;
    private Integer status;
}
