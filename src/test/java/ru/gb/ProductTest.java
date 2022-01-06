package ru.gb;

import com.github.javafaker.Faker;
import io.qameta.allure.*;

import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;

import retrofit2.Response;
import ru.gb.DTO.ProductDto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class ProductTest extends AbstractProductTest {

    @SneakyThrows
    @Epic("Тестирование товаров")
    @Feature("Позитивное тестирование")
    @Description("Тестирование добавления товара с последующим удалением в существующую категорию")
    @Test
    @DisplayName("Добавление нового товара в существующую категорию")
    void createProductInFoodCategoryPositiveTest() {
        Response<ProductDto> responsePost = productService
                .createProduct(product)
                .execute();

        id = responsePost
                .body()
                .getId();

        assertThat(responsePost.code(), equalTo(201));
        assertThat(responsePost.isSuccessful(), CoreMatchers.is(true));
        assertThat(responsePost.body().getCategoryTitle(), equalTo("Food"));
        assertThat(responsePost.headers().toString(), containsString("Content-Type: application/json"));
        assertThat(responsePost.headers().toString(), containsString("Transfer-Encoding: chunked"));

        Response<ResponseBody> responseDel = productService
                .deleteProduct(id)
                .execute();

        assertThat(responseDel.code(), equalTo(200));
        assertThat(responseDel.isSuccessful(), CoreMatchers.is(true));
        assertThat(responseDel.headers().toString(), containsString("Content-Length: 0"));
    }

    @SneakyThrows
    @Epic("Тестирование товаров")
    @Feature("Позитивное тестирование")
    @Description("Тестирование изиенения добавленого товара с последующим его удалением в существующей категории")
    @Test
    @DisplayName("Изменение добавленного товара")
    void modifyProductInFoodCategoryPositiveTest() {
        Response<ProductDto> responsePost = productService
                .createProduct(product)
                .execute();

        id = responsePost
                .body()
                .getId();

        assertThat(responsePost.code(), equalTo(201));
        assertThat(responsePost.isSuccessful(), CoreMatchers.is(true));

        Faker faker = new Faker();
        Response<ProductDto> responsePut = productService
                .modifyProduct(product
                        .withId(id)
                        .withTitle(faker.food().ingredient())
                        .withPrice((int) (Math.random() * 10000)))
                .execute();

        assertThat(responsePut.code(), equalTo(200));
        assertThat(responsePut.isSuccessful(), CoreMatchers.is(true));
        assertThat(responsePut.body().getCategoryTitle(), equalTo("Food"));
        assertThat(responsePut.headers().toString(), containsString("Content-Type: application/json"));
        assertThat(responsePut.headers().toString(), containsString("Transfer-Encoding: chunked"));

        Response<ResponseBody> responseDel = productService
                .deleteProduct(id)
                .execute();

        assertThat(responseDel.code(), equalTo(200));
        assertThat(responseDel.isSuccessful(), CoreMatchers.is(true));
        assertThat(responseDel.headers().toString(), containsString("Content-Length: 0"));
    }

    @SneakyThrows
    @Epic("Тестирование товаров")
    @Feature("Позитивное тестирование")
    @Description("Тестирование просмотра добавленого товара с последующим его удалением в существующей категории")
    @Test
    @DisplayName("Просмотр добавленного товара")
    void getProductInFoodCategoryPositiveTest() {
        Response<ProductDto> responsePost = productService
                .createProduct(product)
                .execute();

        id = responsePost
                .body()
                .getId();

        assertThat(responsePost.code(), equalTo(201));
        assertThat(responsePost.isSuccessful(), CoreMatchers.is(true));

        Response<ProductDto> responseGet = productService
                .getProduct(id)
                .execute();

        assertThat(responseGet.code(), equalTo(200));
        assertThat(responseGet.isSuccessful(), CoreMatchers.is(true));
        assertThat(responseGet.body().getCategoryTitle(), equalTo("Food"));
        assertThat(responseGet.headers().toString(), containsString("Content-Type: application/json"));
        assertThat(responseGet.headers().toString(), containsString("Transfer-Encoding: chunked"));

        Response<ResponseBody> responseDel = productService
                .deleteProduct(id)
                .execute();

        assertThat(responseDel.code(), equalTo(200));
        assertThat(responseDel.isSuccessful(), CoreMatchers.is(true));
        assertThat(responseDel.headers().toString(), containsString("Content-Length: 0"));
    }

    @SneakyThrows
    @Epic("Тестирование товаров")
    @Feature("Негативное тестирование")
    @Description("Тестирование добавления товара в несуществующую категорию")
    @Test
    @DisplayName("Добавление нового товара - несуществующая категория")
    void createProductInAbsentCategoryNegativeTest() {
        Response<ProductDto> response = productService
                .createProduct(product.withCategoryTitle("absent"))
                .execute();

        assertThat(response.code(), equalTo(500));
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.headers().toString(), containsString("Content-Type: application/json"));
        assertThat(response.headers().toString(), containsString("Transfer-Encoding: chunked"));
        assertThat(response.headers().toString(), containsString("Connection: close"));
    }

    @SneakyThrows
    @Epic("Тестирование товаров")
    @Feature("Негативное тестирование")
    @Description("Тестирование добавления товара с указанием его ID")
    @Test
    @DisplayName("Добавление нового товара - с вводом ID")
    void createProductWithIdNegativeTest() {
        Response<ProductDto> response = productService
                .createProduct(product.withId(1904))
                .execute();

        assertThat(response.code(), equalTo(400));
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.headers().toString(), containsString("Content-Type: application/json"));
        assertThat(response.headers().toString(), containsString("Transfer-Encoding: chunked"));
        assertThat(response.headers().toString(), containsString("Connection: close"));
    }

    @SneakyThrows
    @Epic("Тестирование товаров")
    @Feature("Негативное тестирование")
    @Description("Тестирование добавления товара, используя неверный путь")
    @Test
    @DisplayName("Добавление нового товара - ошибочный путь")
    void createProductErrorPathNegativeTest() {
        Response<ResponseBody> response = productService
                .createProductErrorPath(product)
                .execute();

        assertThat(response.code(), equalTo(404));
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.headers().toString(), containsString("Content-Type: application/json"));
        assertThat(response.headers().toString(), containsString("Transfer-Encoding: chunked"));
    }

    @SneakyThrows
    @Epic("Тестирование товаров")
    @Feature("Негативное тестирование")
    @Description("Тестирование добавления товара, используя путь от методов GET, DELETE")
    @Test
    @DisplayName("Добавление нового товара - ошибочный метод")
    void createProductErrorMethodNegativeTest() {
        Response<ResponseBody> response = productService
                .createProductErrorMethod(product)
                .execute();

        assertThat(response.code(), equalTo(405));
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.headers().toString(), containsString("Content-Type: application/json"));
        assertThat(response.headers().toString(), containsString("Transfer-Encoding: chunked"));
        assertThat(response.headers().toString(), containsString("Allow: DELETE, GET"));
    }

    @SneakyThrows
    @Epic("Тестирование товаров")
    @Feature("Негативное тестирование")
    @Description("Тестирование добавления товара с пустым телом запроса")
    @Test
    @DisplayName("Добавление нового товара - пустой body")
    void createProductAbsentBodyNegativeTest() {
        Response<ResponseBody> response = productService
                .createProductAbsentBody()
                .execute();

        assertThat(response.code(), equalTo(400));
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.headers().toString(), containsString("Content-Type: application/json"));
        assertThat(response.headers().toString(), containsString("Transfer-Encoding: chunked"));
        assertThat(response.headers().toString(), containsString("Connection: close"));
    }

    @SneakyThrows
    @Epic("Тестирование товаров")
    @Feature("Негативное тестирование")
    @Description("Тестирование изменения несуществующего в маркете товара")
    @Test
    @DisplayName("Изменение несуществующего товара")
    void modifyProductInAbsentCategoryNegativeTest() {
        Response<ProductDto> response = productService
                .modifyProduct(product.withId(-666))
                .execute();

        assertThat(response.code(), equalTo(400));
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.headers().toString(), containsString("Content-Type: application/json"));
        assertThat(response.headers().toString(), containsString("Transfer-Encoding: chunked"));
        assertThat(response.headers().toString(), containsString("Connection: close"));
    }

    @SneakyThrows
    @Epic("Тестирование товаров")
    @Feature("Негативное тестирование")
    @Description("Тестирование изменения товара, используя неверный путь")
    @Test
    @DisplayName("Изменение товара - ошибочный путь")
    void modifyProductErrorPathNegativeTest() {
        Response<ResponseBody> response = productService
                .modifyProductErrorPath(product)
                .execute();

        assertThat(response.code(), equalTo(404));
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.headers().toString(), containsString("Content-Type: application/json"));
        assertThat(response.headers().toString(), containsString("Transfer-Encoding: chunked"));
    }

    @SneakyThrows
    @Epic("Тестирование товаров")
    @Feature("Негативное тестирование")
    @Description("Тестирование изменения товара, используя путь от методов GET, DELETE")
    @Test
    @DisplayName("Изменение товара - ошибочный метод")
    void modifyProductErrorMethodNegativeTest() {
        Response<ResponseBody> response = productService
                .modifyProductErrorMethod(product)
                .execute();

        assertThat(response.code(), equalTo(405));
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.headers().toString(), containsString("Content-Type: application/json"));
        assertThat(response.headers().toString(), containsString("Transfer-Encoding: chunked"));
        assertThat(response.headers().toString(), containsString("Allow: DELETE, GET"));
    }

    @SneakyThrows
    @Epic("Тестирование товаров")
    @Feature("Негативное тестирование")
    @Description("Тестирование изменения товара с пустым телом запроса")
    @Test
    @DisplayName("Изменение товара - пустой body")
    void modifyProductAbsentBodyNegativeTest() {
        Response<ResponseBody> response = productService
                .modifyProductAbsentBody()
                .execute();

        assertThat(response.code(), equalTo(400));
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.headers().toString(), containsString("Content-Type: application/json"));
        assertThat(response.headers().toString(), containsString("Transfer-Encoding: chunked"));
        assertThat(response.headers().toString(), containsString("Connection: close"));
    }

    @SneakyThrows
    @Epic("Тестирование товаров")
    @Feature("Негативное тестирование")
    @Description("Тестирование просмотра товара, используя несуществующий ID")
    @Test
    @DisplayName("Просмотр товара - несуществующий ID")
    void getProductErrorIdNegativeTest() {

         Response<ProductDto> response = productService
                .getProduct(-666)
                .execute();

        assertThat(response.code(), equalTo(404));
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.headers().toString(), containsString("Content-Type: application/json"));
        assertThat(response.headers().toString(), containsString("Transfer-Encoding: chunked"));
    }

    @SneakyThrows
    @Epic("Тестирование товаров")
    @Feature("Негативное тестирование")
    @Description("Тестирование просмотра товара, используя невалидный ID неподходящего типа данных")
    @Test
    @DisplayName("Просмотр товара - невалидный ID")
    void getProductNotValidIdNegativeTest() {

        Response<ResponseBody> response = productService
                .getProductNotValidId("ID")
                .execute();

        assertThat(response.code(), equalTo(400));
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.headers().toString(), containsString("Content-Type: application/json"));
        assertThat(response.headers().toString(), containsString("Transfer-Encoding: chunked"));
        assertThat(response.headers().toString(), containsString("Connection: close"));
    }

    @SneakyThrows
    @Epic("Тестирование товаров")
    @Feature("Негативное тестирование")
    @Description("Тестирование просмотра товара, не передавая ID")
    @Test
    @DisplayName("Просмотр товара - отсутствие ID")
    void getProductAbsentIdNegativeTest() {

        Response<ResponseBody> response = productService
                .getProductAbsentId()
                .execute();

        assertThat(response.code(), equalTo(500));
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.headers().toString(), containsString("Content-Type: application/json"));
        assertThat(response.headers().toString(), containsString("Transfer-Encoding: chunked"));
        assertThat(response.headers().toString(), containsString("Connection: close"));
    }

    @SneakyThrows
    @Epic("Тестирование товаров")
    @Feature("Негативное тестирование")
    @Description("Тестирование просмотра товара, используя неверный путь")
    @Test
    @DisplayName("Просмотр товара - ошибочный путь")
    void getProductErrorPathNegativeTest() {

        Response<ResponseBody> response = productService
                .getProductErrorPath()
                .execute();

        assertThat(response.code(), equalTo(404));
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.headers().toString(), containsString("Content-Type: application/json"));
        assertThat(response.headers().toString(), containsString("Transfer-Encoding: chunked"));
    }

    @SneakyThrows
    @Epic("Тестирование товаров")
    @Feature("Негативное тестирование")
    @Description("Тестирование удаления товара, используя несуществующий ID")
    @Test
    @DisplayName("Удаление товара - несуществующий ID")
    void deleteProductErrorIdNegativeTest() {

        Response<ResponseBody> response = productService
                .deleteProduct(-666)
                .execute();

        assertThat(response.code(), equalTo(500));
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.headers().toString(), containsString("Content-Type: application/json"));
        assertThat(response.headers().toString(), containsString("Transfer-Encoding: chunked"));
        assertThat(response.headers().toString(), containsString("Connection: close"));
    }

    @SneakyThrows
    @Epic("Тестирование товаров")
    @Feature("Негативное тестирование")
    @Description("Тестирование удаления товара, используя невалидный ID неподходящего типа данных")
    @Test
    @DisplayName("Удаление товара - невалидный ID")
    void deleteProductNotValidIdNegativeTest() {

        Response<ResponseBody> response = productService
                .deleteProductNotValidId("ID")
                .execute();

        assertThat(response.code(), equalTo(400));
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.headers().toString(), containsString("Content-Type: application/json"));
        assertThat(response.headers().toString(), containsString("Transfer-Encoding: chunked"));
        assertThat(response.headers().toString(), containsString("Connection: close"));
    }

    @SneakyThrows
    @Epic("Тестирование товаров")
    @Feature("Негативное тестирование")
    @Description("Тестирование удаления товара, не передавая ID")
    @Test
    @DisplayName("Удаление товара - отсутствие ID")
    void deleteProductAbsentIdNegativeTest() {

        Response<ResponseBody> response = productService
                .deleteProductAbsentID()
                .execute();

        assertThat(response.code(), equalTo(405));
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.headers().toString(), containsString("Content-Type: application/json"));
        assertThat(response.headers().toString(), containsString("Transfer-Encoding: chunked"));
        assertThat(response.headers().toString(), containsString("Allow: POST, PUT, GET"));
    }

    @SneakyThrows
    @Epic("Тестирование товаров")
    @Feature("Негативное тестирование")
    @Description("Тестирование удаления товара, используя неверный путь")
    @Test
    @DisplayName("Удаление товара - ошибочный путь")
    void deleteProductErrorPathNegativeTest() {

        Response<ResponseBody> response = productService
                .getProductErrorPath()
                .execute();

        assertThat(response.code(), equalTo(404));
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.headers().toString(), containsString("Content-Type: application/json"));
        assertThat(response.headers().toString(), containsString("Transfer-Encoding: chunked"));
    }
}
