package ru.gb.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class GetCategoryResponseDto {
    private Integer id;
    private String title;
    private List<ProductDto> products = new ArrayList<>();
}
