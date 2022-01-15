package ru.gb;

import io.qameta.allure.*;
import lombok.SneakyThrows;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import retrofit2.Response;
import ru.gb.DTO.GetCategoryResponseDto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GetCategoryTest extends AbstractCategoryTest{

    @SneakyThrows
    @Epic("Тестирование категорий товаров")
    @Feature("Позитивное тестирование")
    @Description("Тестирование категории товаров, возвращаемой по ID")
    @Test
    @DisplayName("Категория товаров по ID")
    void getCategoryByIdPositiveTest() {
        Response<GetCategoryResponseDto> response = categoryService
                .getCategory(1)
                .execute();

        assertThat(response.code(), equalTo(200));
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getId(), equalTo(1));
        assertThat(response.body().getTitle(), equalTo("Food"));

        response
                .body()
                .getProducts()
                .forEach(product -> assertThat(product.getCategoryTitle(), equalTo("Food")));

        assertThat(response.headers().toString(), containsString("Content-Type: application/json"));
        assertThat(response.headers().toString(), containsString("Transfer-Encoding: chunked"));
    }

    @SneakyThrows
    @Epic("Тестирование категорий товаров")
    @Feature("Негативное тестирование")
    @Description("Негативное тестирование категории товаров, используя несуществующий ID")
    @Test
    @DisplayName("Категория товаров - несуществующий ID")
    void getCategoryByIdNegativeTest() {
        Response<GetCategoryResponseDto> response = categoryService
                .getCategory(-13)
                .execute();

        assertThat(response.code(), equalTo(404));
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.headers().toString(), containsString("Content-Type: application/json"));
        assertThat(response.headers().toString(), containsString("Transfer-Encoding: chunked"));
    }
}
