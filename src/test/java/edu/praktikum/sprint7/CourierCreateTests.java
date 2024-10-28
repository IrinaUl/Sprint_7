package edu.praktikum.sprint7;

import edu.praktikum.sprint7.clients.CourierClient;
import edu.praktikum.sprint7.models.Courier;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.junit.Before;
import org.junit.Test;

import static edu.praktikum.sprint7.generators.CourierGenerator.randomCourier;
import static edu.praktikum.sprint7.utils.Utils.randomString;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;

public class CourierCreateTests {

    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";

    private CourierClient courierClient;
    private Courier courier;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        courierClient = new CourierClient();
    }

    @Test
    @DisplayName("Создание курьера")
    public void createCourier() {
        courier = randomCourier();
        Response response = courierClient.create(courier);

        assertEquals("Неверный статус код", SC_CREATED, response.statusCode());
        assertEquals("Описание ответа не соответствует ожидаемому", true, response.path("ok"));
    }

    @Test
    @DisplayName("Создание курьера без логина")
    public void createCouriersWithoutLogin() {
        courier = new Courier()
                .withPassword(randomString(12))
                .withFirstName(randomString(20));
        Response response = courierClient.create(courier);

        assertEquals("Неверный статус код", SC_BAD_REQUEST, response.statusCode());
        assertEquals("Описание ответа не соответствует ожидаемому", "Недостаточно данных для создания учетной записи", response.path("message"));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    public void createCouriersWithoutPassword() {
        courier = new Courier()
                .withLogin(randomString(8))
                .withFirstName(randomString(20));
        Response response = courierClient.create(courier);

        assertEquals("Неверный статус код", SC_BAD_REQUEST, response.statusCode());
        assertEquals("Описание ответа не соответствует ожидаемому", "Недостаточно данных для создания учетной записи", response.path("message"));
    }

    @Test
    @DisplayName("Создание двух одинаковых курьеров")
    public void createCouriersWithDuplicateLogin() { //todo Найден баг "Описание ответа не соответствует ожидаемому"
        courier = randomCourier();
        courierClient.create(courier);
        Response response = courierClient.create(courier);

        assertEquals("Неверный статус код", SC_CONFLICT, response.statusCode());
        assertEquals("Описание ответа не соответствует ожидаемому", "Этот логин уже используется", response.path("message"));
    }
}
