package org.example;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.example.client.StellarUser;
import org.example.model.User;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class TestLoginUser {
    public User user;
    public String token;
    public StellarUser stellarUser;
    public Response loginedUser;
    public Response createdUser;


    @Test
    @Description("Существующий пользователь может залогиниться")
    public void checkLoginUserWithValidField() {
        user = new User("Slava777@ya.ru", "11111", "Slava777");
        stellarUser = new StellarUser();
        createdUser = stellarUser.createUser(user);
        loginedUser = stellarUser.loginUser(user);
        loginedUser.then().statusCode(200).and().body("success", equalTo(true));
    }

    @Test
    @Description("Несуществующий пользователь не может залогиниться")
    public void checkLoginUserNonExistent() {
        user = new User("Slava777@ya.ru", "11111", "Slava777");
        stellarUser = new StellarUser();
        loginedUser = stellarUser.loginUser(user);
        loginedUser.then().statusCode(401).and().body("message", equalTo("email or password are incorrect"));
    }

    @After
    public void tearDown() {
        token = stellarUser.loginUser(user).then().extract().path("accessToken");
        if (token != null) {
            stellarUser.deleteUser(token);
        }
    }
}
