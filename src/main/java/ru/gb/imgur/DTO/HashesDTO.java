package ru.gb.imgur.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class HashesDTO {

    private List<String> data;
    private Boolean success;
    private Integer status;
}
