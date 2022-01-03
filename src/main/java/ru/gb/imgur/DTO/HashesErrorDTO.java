package ru.gb.imgur.DTO;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class HashesErrorDTO {

    private ErrorData data;
    private Boolean success;
    private Integer status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Data
    public class ErrorData {

        private String error;
        private String request;
        private String method;
    }
}
