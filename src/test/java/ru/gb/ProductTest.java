package ru.gb;

import com.github.javafaker.Faker;
import io.qameta.allure.*;

import lombok.SneakyThrows;
import okhttp3.ResponseBody;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;

import retrofit2.Response;
import ru.gb.DTO.ProductDto;
import ru.gb.db.model.Products;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.gb.util.ColouredSystemOutPrintln.*;

public class ProductTest extends AbstractProductTest {

    @SneakyThrows
    @Epic("Тестирование товаров")
    @Feature("Позитивное тестирование")
    @Description("Тестирование добавления товара с последующим удалением в существующую категорию")
    @Test
    @DisplayName("Добавление нового товара")
    void createProductPositiveTest() {

        Response<ProductDto> response = productService
                .createProduct(product)
                .execute();

        id = response
                .body()
                .getId();

        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.code(), equalTo(201));

        // Запросим в БД товар по полученному выше id
        Products productFromDb = productsMapper.selectByPrimaryKey((long) id);

        // Сравним имя товара, сгенерированного Faker, с именем товара, запрошенного в БД
        assertThat(product.getTitle(), equalTo(productFromDb.getTitle()));

        System.out.println(ANSI_BRIGHT_WHITE + "from faker: "
                + ANSI_BRIGHT_CYAN + product.getTitle()
                + ANSI_BRIGHT_WHITE + " from DB: "
                + ANSI_BRIGHT_GREEN + productFromDb.getTitle() + ANSI_RESET);

        // Удалим сгенерированный товар в БД и проверим на единичность удаленной записи
        assertThat(productsMapper.deleteByPrimaryKey((long) id), equalTo(1));

        // Проверим отсутствие удаленного товара в БД
        assertThat(productsMapper.selectByPrimaryKey((long) id), equalTo(null));
    }

    @SneakyThrows
    @Epic("Тестирование товаров")
    @Feature("Позитивное тестирование")
    @Description("Тестирование просмотра добавленого товара с последующим его удалением в существующей категории")
    @Test
    @DisplayName("Просмотр добавленного товара")
    void getProductPositiveTest() {

        Response<ProductDto> responsePost = productService
                .createProduct(product)
                .execute();

        id = responsePost
                .body()
                .getId();

        assertThat(responsePost.isSuccessful(), CoreMatchers.is(true));
        assertThat(responsePost.code(), equalTo(201));

        Response<ProductDto> responseGet = productService
                .getProduct(id)
                .execute();

        assertThat(responseGet.isSuccessful(), CoreMatchers.is(true));
        assertThat(responseGet.code(), equalTo(200));
        assertThat(responseGet.headers().toString(), containsString("Content-Type: application/json"));
        assertThat(responseGet.headers().toString(), containsString("Transfer-Encoding: chunked"));

        // Запросим в БД товар по полученному выше id
        Products productFromDb = productsMapper.selectByPrimaryKey((long) id);

        //Особого смысла проверять GET параллельным запросом из базы нет, только ради баловства
        assertThat(responseGet.body().getTitle(), equalTo(productFromDb.getTitle()));
        assertThat(responseGet.body().getPrice(), equalTo(productFromDb.getPrice()));

        System.out.println(ANSI_BRIGHT_WHITE + "from response: "
                + ANSI_BRIGHT_CYAN + responseGet.body().getTitle() + ", " + responseGet.body().getPrice() + " rub"
                + ANSI_BRIGHT_WHITE + " from DB: "
                + ANSI_BRIGHT_GREEN + productFromDb.getTitle() + ", " + productFromDb.getPrice() + " rub"
                + ANSI_RESET);

        // Удалим сгенерированный товар в БД и проверим на единичность удаленной записи
        assertThat(productsMapper.deleteByPrimaryKey((long) id), equalTo(1));

        // Проверим отсутствие удаленного товара в БД
        assertThat(productsMapper.selectByPrimaryKey((long) id), equalTo(null));
    }

    @SneakyThrows
    @Epic("Тестирование товаров")
    @Feature("Позитивное тестирование")
    @Description("Тестирование изменения добавленого товара с последующим его удалением в существующей категории")
    @Test
    @DisplayName("Изменение добавленного товара")
    void modifyProductPositiveTest() {

        // Создадим предварительно в БД сгенерированный товар
        Response<ProductDto> responsePost = productService
                .createProduct(product)
                .execute();

        id = responsePost
                .body()
                .getId();

        assertThat(responsePost.isSuccessful(), CoreMatchers.is(true));
        assertThat(responsePost.code(), equalTo(201));

        // Сгенерируем новый товар для последующих изменений в БД и подготовим данные для апдейта
        Faker faker = new Faker();
        String newTitle = faker.food().ingredient();
        Integer newPrice = (int) (Math.random() * 1000);

        // Проведем изменения в БД созданного товара значениями вновь сгенерированнного
        Response<ProductDto> responsePut = productService
                .modifyProduct(product
                        .withId(id)
                        .withTitle(newTitle)
                        .withPrice(newPrice))
                .execute();

        assertThat(responsePut.isSuccessful(), CoreMatchers.is(true));
        assertThat(responsePut.code(), equalTo(200));

        // Запросим в БД товар по полученному выше id
        Products productFromDb = productsMapper.selectByPrimaryKey((long) id);

        // Сравним имя товара, сгенерированного Faker, с именем товара, запрошенного в БД
        assertThat(newTitle, equalTo(productFromDb.getTitle()));
        assertThat(newPrice, equalTo(productFromDb.getPrice()));

        System.out.println(ANSI_BRIGHT_WHITE + "from Faker: "
                + ANSI_BRIGHT_CYAN + newTitle + ", " + newPrice + " rub"
                + ANSI_BRIGHT_WHITE + " from DB: "
                + ANSI_BRIGHT_GREEN + productFromDb.getTitle() + ", " + productFromDb.getPrice() + " rub"
                + ANSI_RESET);

        // Удалим сгенерированный товар в БД и проверим на единичность удаленной записи
        assertThat(productsMapper.deleteByPrimaryKey((long) id), equalTo(1));

        // Проверим отсутствие удаленного товара в БД
        assertThat(productsMapper.selectByPrimaryKey((long) id), equalTo(null));
    }

    @SneakyThrows
    @Epic("Тестирование товаров")
    @Feature("Позитивное тестирование")
    @Description("Тестирование удаления ранее созданного в существующей категории товара")
    @Test
    @DisplayName("Удаление добавленного товара")
    void deleteProductPositiveTest() {

        // Создадим предварительно в БД сгенерированный товар
        Response<ProductDto> responsePost = productService
                .createProduct(product)
                .execute();

        id = responsePost
                .body()
                .getId();

        assertThat(responsePost.isSuccessful(), CoreMatchers.is(true));
        assertThat(responsePost.code(), equalTo(201));

        // Не для смысла, а для баловства только
        // Сгенерируем новый товар для последующих изменений в БД и подготовим данные для апдейта
        Faker faker = new Faker();
        String newTitle = faker.food().ingredient();
        Integer newPrice = (int) (Math.random() * 1000);

        Products newProduct = new Products();
                newProduct.setId((long)id);
                newProduct.setTitle(newTitle);
                newProduct.setPrice(newPrice);
                newProduct.setCategory_id(1L);

        // Обновим товар в БД
        productsMapper.updateByPrimaryKey(newProduct);

        // Запросим в БД товар по полученному выше id
        Products productFromDb = productsMapper.selectByPrimaryKey((long) id);

        // Сравним имя товара, сгенерированного Faker, с именем товара, запрошенного в БД
        assertThat(newTitle, equalTo(productFromDb.getTitle()));
        assertThat(newPrice, equalTo(productFromDb.getPrice()));

        System.out.println(ANSI_BRIGHT_WHITE + "from Faker: "
                + ANSI_BRIGHT_CYAN + newTitle + ", " + newPrice + " rub"
                + ANSI_BRIGHT_WHITE + " from DB: "
                + ANSI_BRIGHT_GREEN + productFromDb.getTitle() + ", " + productFromDb.getPrice() + " rub"
                + ANSI_RESET);

        Response<ResponseBody> responseDel = productService
                .deleteProduct(id)
                .execute();

        assertThat(responseDel.isSuccessful(), CoreMatchers.is(true));
        assertThat(responseDel.code(), equalTo(200));

        // Проверим отсутствие удаленного товара в БД
        assertThat(productsMapper.deleteByPrimaryKey((long) id), equalTo(0));
    }

    @SneakyThrows
    @Epic("Тестирование товаров")
    @Feature("Негативное тестирование")
    @Description("Тестирование изменения несуществующего в маркете товара")
    @Test
    @DisplayName("Изменение товара - несуществующий ID")
    void modifyProductInAbsentCategoryNegativeTest() {
        Response<ProductDto> response = productService
                .modifyProduct(product.withId(-666))
                .execute();

        assertThat(response.code(), equalTo(400)); // Здесь логичнее 404 код получать, так что это скорее всего баг
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
}
