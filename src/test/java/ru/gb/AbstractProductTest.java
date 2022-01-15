package ru.gb;

import com.github.javafaker.Faker;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.*;

import org.junit.jupiter.api.*;

import ru.gb.DTO.ProductDto;
import ru.gb.db.dao.ProductsMapper;
import ru.gb.service.ProductService;
import ru.gb.util.RetrofitUtils;

import java.io.IOException;
import java.io.InputStream;

public class AbstractProductTest {

    static ProductService productService;
    ProductDto product;
    Faker faker = new Faker();
    int id;

    String resource = "mybatis-config.xml";

    InputStream is;
    {
        try {is = Resources.getResourceAsStream(resource);}
        catch (IOException e) {e.printStackTrace();}
    }

    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
    SqlSession sqlSession = sqlSessionFactory.openSession(true);

    ProductsMapper productsMapper = sqlSession.getMapper(ProductsMapper.class);

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
