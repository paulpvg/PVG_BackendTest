package ru.gb.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import ru.gb.DTO.ProductDto;

public interface ProductService {

    @GET("products/{id}")
    Call<ProductDto> getProduct(@Path("id") int id);
    @POST("products")
    Call<ProductDto> createProduct(@Body ProductDto createProductRequest);
    @PUT("products")
    Call<ProductDto> modifyProduct(@Body ProductDto createProductRequest);
    @DELETE("products/{id}")
    Call<ResponseBody> deleteProduct(@Path("id") int id);
}
