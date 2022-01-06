package ru.gb;

import com.github.javafaker.Faker;

import org.junit.jupiter.api.*;

import ru.gb.DTO.ProductDto;
import ru.gb.service.ProductService;
import ru.gb.util.RetrofitUtils;

public class AbstractProductTest {

    static ProductService productService;
    ProductDto product;
    Faker faker = new Faker();
    int id;

    @BeforeAll
    static void beforeAll() {
        productService = RetrofitUtils
                .getRetrofit()
                .create(ProductService.class);
    }

    @BeforeEach
    void setUp() {
        product = new ProductDto()
                .withTitle(faker.food().ingredient())
                .withCategoryTitle("Food")
                .withPrice((int) (Math.random() * 10000));
    }
}
