package edu.praktikum.sprint7.clients;

import edu.praktikum.sprint7.models.Courier;
import edu.praktikum.sprint7.models.CourierCreds;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class CourierClient {
    public static final String CREATE_PATH = "/api/v1/courier";
    public static final String LOGIN_PATH = "/api/v1/courier/login";
    private static final String DELETE_PATH = "/api/v1/courier/";

    @Step("Создание курьера")
    public Response create(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(CREATE_PATH);
    }

    @Step("Логин курьера")
    public Response login(CourierCreds courierCreds) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courierCreds)
                .when()
                .post(LOGIN_PATH);
    }

    @Step("Удаление курьера")
    public Response delete(int id) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .when()
                .delete(DELETE_PATH + id);
    }
}
