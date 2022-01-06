package ru.gb;

import org.junit.jupiter.api.BeforeAll;
import ru.gb.service.CategoryService;
import ru.gb.util.RetrofitUtils;

public class AbstractCategoryTest {

    static CategoryService categoryService;

    @BeforeAll
    static void beforeAll() {
        categoryService = RetrofitUtils
                .getRetrofit()
                .create(CategoryService.class);
    }
}
