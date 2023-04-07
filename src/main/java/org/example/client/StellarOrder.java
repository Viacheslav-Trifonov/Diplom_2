package org.example.client;

import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.example.model.Order;

import static io.restassured.RestAssured.given;

public class StellarOrder {

    private final String USER_URI = "https://stellarburgers.nomoreparties.site/api/orders";

    private final RequestSpecification spec = new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .setBaseUri(USER_URI)
            .build();

    @Step("Создание заказа с авторизацией")
    public Response createOrder(Order order, String token) {
        return given().header("authorization", token).spec(spec).body(order).post();
    }

    @Step("Создание заказа без авторизации")
    public Response createOrder(Order order) {
        return given().spec(spec).body(order).post();
    }

    @Step("Получение заказов пользователя без авторизации")
    public Response getUserOrders() {
        return given().spec(spec).get();
    }

    @Step("Получение заказов пользователя с авторизацией")
    public Response getUserOrders(String token) {
        return given().header("authorization", token).spec(spec).get();
    }

}

