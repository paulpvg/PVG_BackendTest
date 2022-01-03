package ru.gb.imgur.DTO;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class AccountDTO {

    private AccountData data;
    private Boolean success;
    private Integer status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public class AccountData {
        private Integer id;
        private String url;
    }
}
