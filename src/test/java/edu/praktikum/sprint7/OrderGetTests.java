package edu.praktikum.sprint7;

import edu.praktikum.sprint7.order.OrderClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OrderGetTests {
    private final static String BASE_URI = "http://qa-scooter.praktikum-services.ru/";
    private OrderClient orderClient;

    @Before
    public void setup() {
        RestAssured.baseURI = BASE_URI;
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Получение всего списка заказов")
    public void getOrderList() {
        Response response = orderClient.getAllOrders();
        assertEquals("Неверный статус код", SC_OK, response.statusCode());
        assertNotNull("Заказы не найдены", response.path("orders"));
    }
}
