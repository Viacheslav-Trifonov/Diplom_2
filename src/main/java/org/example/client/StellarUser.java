package org.example.client;

import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.example.model.User;

import static io.restassured.RestAssured.given;

public class StellarUser {
    private final String USER_URI = "https://stellarburgers.nomoreparties.site/api/auth";

    private final RequestSpecification spec = new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .setBaseUri(USER_URI)
            .build();

    @Step("Создать нового пользователя")
    public Response createUser(User user) {
        return given().spec(spec).body(user).post("/register");
    }

    @Step("Удалить пользователя")
    public Response deleteUser(String token) {
        return given().header("authorization", token).spec(spec).when().delete("/user");
    }

    @Step("Авторизация пользователя")
    public Response loginUser(User user) {
        return given().spec(spec).body(user).post("/login");
    }


    @Step("Изменить информацию пользователя с авторизацией")
    public Response changeUser(User user, String token) {
        return given().header("authorization", token).spec(spec).body(user).patch("/user");
    }

    @Step("Изменить информацию пользователя без авторизации")
    public Response changeUser(User user) {
        return given().spec(spec).body(user).patch("/user");
    }
}
