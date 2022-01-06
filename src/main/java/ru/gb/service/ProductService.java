package ru.gb.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import ru.gb.DTO.ProductDto;

public interface ProductService {

    // Методы для позитивных тестов
    @POST("products")
    Call<ProductDto> createProduct(@Body ProductDto createProductRequest);
    @PUT("products")
    Call<ProductDto> modifyProduct(@Body ProductDto createProductRequest);
    @GET("products/{id}")
    Call<ProductDto> getProduct(@Path("id") int id);
    @DELETE("products/{id}")
    Call<ResponseBody> deleteProduct(@Path("id") int id);

    // Методы для негативной проверки с отсутствующим body
    @POST("products")
    Call<ResponseBody> createProductAbsentBody();
    @PUT("products")
    Call<ResponseBody> modifyProductAbsentBody();

    // Методы для негативной проверки с отсутствующим ID
    @DELETE("products")
    Call<ResponseBody> deleteProductAbsentID();
    @GET("products")
    Call<ResponseBody> getProductAbsentId();

    // Методы для негативных тестов с неверным путем
    @POST("error_path")
    Call<ResponseBody> createProductErrorPath(@Body ProductDto createProductRequest);
    @PUT("error_path")
    Call<ResponseBody> modifyProductErrorPath(@Body ProductDto createProductRequest);
    @GET("error_path")
    Call<ResponseBody> getProductErrorPath();
    @DELETE("error_path")
    Call<ResponseBody> deleteProductErrorPath();

    // Методы для негативных тестов с некорректной конструкцией запроса
    @POST("products/1")
    Call<ResponseBody> createProductErrorMethod(@Body ProductDto createProductRequest);
    @PUT("products/1")
    Call<ResponseBody> modifyProductErrorMethod(@Body ProductDto createProductRequest);

    // Методы для негативных тестов с невалидным ID
    @GET("products/{id}")
    Call<ResponseBody> getProductNotValidId(@Path("id") String id);
    @DELETE("products/{id}")
    Call<ResponseBody> deleteProductNotValidId(@Path("id") String id);
}
