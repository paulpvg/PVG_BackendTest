package ru.gb.service;

import retrofit2.Call;
import retrofit2.http.*;
import ru.gb.DTO.GetCategoryResponseDto;

public interface CategoryService {

    @GET("categories/{id}")
    Call<GetCategoryResponseDto> getCategory(@Path("id") int id);
}
