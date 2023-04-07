package org.example;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.example.client.StellarUser;
import org.example.model.User;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class TestChangeUser {
    public User user;
    public Response createdUser;
    public Response changedUser;
    public StellarUser stellarUser;
    String token;

    @Test
    @Description("Авторизованный пользователь может менять информацию о себе (Имя)")
    public void checkChangeNameWithLogin() {
        user = new User("Slava777@ya.ru", "11111", "Slava777");
        stellarUser = new StellarUser();
        createdUser = stellarUser.createUser(user);
        token = stellarUser.loginUser(user).then().extract().path("accessToken");
        user.setName("Vladimir");
        changedUser = stellarUser.changeUser(user, token);
        changedUser.then().statusCode(200).and().body("success", equalTo(true));
    }

    @Test
    @Description("Авторизованный пользователь может менять информацию о себе (Почта)")
    public void checkChangeEmailWithLogin() {
        user = new User("Slava777@ya.ru", "11111", "Slava777");
        stellarUser = new StellarUser();
        createdUser = stellarUser.createUser(user);
        token = stellarUser.loginUser(user).then().extract().path("accessToken");
        user.setEmail("Vyacheslav@ya.ru");
        changedUser = stellarUser.changeUser(user, token);
        changedUser.then().statusCode(200).and().body("success", equalTo(true));
    }

    @Test
    @Description("Авторизованный пользователь может менять информацию о себе (Пароль)")
    public void checkChangePasswordlWithLogin() {
        user = new User("Slava777@ya.ru", "11111", "Slava777");
        stellarUser = new StellarUser();
        createdUser = stellarUser.createUser(user);
        token = stellarUser.loginUser(user).then().extract().path("accessToken");
        user.setPassword("pass1");
        changedUser = stellarUser.changeUser(user, token);
        changedUser.then().statusCode(200).and().body("success", equalTo(true));
    }

    @Test
    @Description("Неавторизованный пользователь не может менять информацию о себе")
    public void checkChangeInformationWithoutLogin() {
        user = new User("Slava777@ya.ru", "11111", "Slava777");
        stellarUser = new StellarUser();
        createdUser = stellarUser.loginUser(user);
        token = stellarUser.loginUser(user).then().extract().path("accessToken");
        user.setName("Vladimir");
        changedUser = stellarUser.changeUser(user);
        changedUser.then().statusCode(401).and().body("message", equalTo("You should be authorised"));
    }

    @After
    public void tearDown() {
        token = stellarUser.loginUser(user).then().extract().path("accessToken");
        if (token != null) {
            stellarUser.deleteUser(token);
        }
    }
}

