package edu.praktikum.sprint7;

import edu.praktikum.sprint7.generators.OrderGenerator;
import edu.praktikum.sprint7.models.Order;
import edu.praktikum.sprint7.order.OrderClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Parameterized.class)
public class OrderCreateTests {
    private final static String BASE_URI = "http://qa-scooter.praktikum-services.ru/";
    OrderClient orderClient = new OrderClient();
    Order order = OrderGenerator.ranndomOrder();

    private final String[] color;

    public OrderCreateTests(String[] color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "Предпочитаемые цвета")
    public static Object[][] getColor() {
        return new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"BLACK", "GREY"}},
                {new String[]{"GREY"}},
                {new String[]{""}},
        };
    }

    @Before
    public void setup() {
        RestAssured.baseURI = BASE_URI;
    }

    @Test
    @DisplayName("Создание заказов с разными предпочитаемыми цветами")
    public void createOrder() {
        order.setColor(color);
        Response response = orderClient.create(order);

        assertEquals("Неверный статус код", SC_CREATED, response.statusCode());
        assertNotNull("Номер заказа не найден", response.path("track"));
    }
}
