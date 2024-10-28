package edu.praktikum.sprint7;

import edu.praktikum.sprint7.clients.CourierClient;
import edu.praktikum.sprint7.models.Courier;
import edu.praktikum.sprint7.models.CourierCreds;
import edu.praktikum.sprint7.models.CourierId;
import edu.praktikum.sprint7.utils.Utils;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static edu.praktikum.sprint7.generators.CourierGenerator.randomCourier;
import static edu.praktikum.sprint7.utils.Utils.randomString;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CourierLoginTests {
    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";
    private final String BAD_REQUEST = "Недостаточно данных для входа";
    private final String NOT_FOUND = "Учетная запись не найдена";

    private CourierClient courierClient;
    private Courier courier;
    private int id;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        courier = randomCourier();
        courierClient = new CourierClient();
        id = courierClient.create(courier).as(CourierId.class).getId();
    }

    @Test
    @DisplayName("Авторизация курьера")
    public void checkSuccessfulLogin() {
        CourierCreds creds = new CourierCreds(courier.getLogin(), courier.getPassword());
        Response response = courierClient.login(creds);
        assertEquals("Неверный статус код", SC_OK, response.statusCode());
        assertNotNull("Id не найден", response.path("id"));
    }

    @Test
    @DisplayName("Авторизация курьера без пароля")
    public void checkLoginWithoutPassword() {
        CourierCreds creds = new CourierCreds(courier.getLogin(), "");
        Response response = courierClient.login(creds);
        assertEquals("Неверный статус код", SC_BAD_REQUEST, response.statusCode());
        assertEquals("Описание ответа не соответствует ожидаемому", BAD_REQUEST, response.path("message"));
    }

    @Test
    @DisplayName("Авторизация курьера без логина")
    public void checkLoginWithoutLogin() {
        CourierCreds creds = new CourierCreds("", courier.getPassword());
        Response response = courierClient.login(creds);
        assertEquals("Неверный статус код", SC_BAD_REQUEST, response.statusCode());
        assertEquals("Описание ответа не соответствует ожидаемому", BAD_REQUEST, response.path("message"));
    }

    @Test
    @DisplayName("Авторизация курьера с несуществующим логином")
    public void checkLoginWithIncorrectLogin() {
        CourierCreds creds = new CourierCreds(randomString(7), courier.getPassword());
        Response response = courierClient.login(creds);
        assertEquals("Неверный статус код", SC_NOT_FOUND, response.statusCode());
        assertEquals("Описание ответа не соответствует ожидаемому", NOT_FOUND, response.path("message"));
    }

    @After
    public void tearDown() {
        courierClient.delete(id);
    }
}
