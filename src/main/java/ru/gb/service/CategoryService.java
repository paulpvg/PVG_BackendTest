package ru.gb.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;
import ru.gb.DTO.GetCategoryResponseDto;

public interface CategoryService {

    @GET("categories/{id}")
    Call<GetCategoryResponseDto> getCategory(@Path("id") int id);

    @GET("categories/notfound/{id}")
    Call<ResponseBody> getCategoryErrorPath(@Path("id") int id);
    @GET("categories/{id}")
    Call<ResponseBody> getCategoryNotValidId(@Path("id") String id);
}
