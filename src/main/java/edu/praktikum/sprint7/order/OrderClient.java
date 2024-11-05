package edu.praktikum.sprint7.order;

import edu.praktikum.sprint7.models.Order;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class OrderClient {
    private final static String ORDERS_URL = "/api/v1/orders";

    @Step("Создание заказа")
    public Response create(Order order){
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post(ORDERS_URL);
    }

    @Step("Получение списка заказов")
    public Response getAllOrders(){
        return given()
                .header("Content-type", "application/json")
                .when()
                .get(ORDERS_URL);
    }
}
