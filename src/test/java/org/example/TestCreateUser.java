package org.example;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.example.client.StellarUser;
import org.example.model.User;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;


public class TestCreateUser {

    public User user;
    public String token;
    public StellarUser stellarUser;
    public Response createdUser;


    @Test
    @Description("Пользователя можно создать со всеми валидными полями")
    public void checkCreateUserWithValidField() {
        user = new User("Slava777@ya.ru", "11111", "Slava777");
        stellarUser = new StellarUser();
        createdUser = stellarUser.createUser(user);
        createdUser.then().statusCode(200).and().body("success", equalTo(true));
    }

    @Test
    @Description("Нельзя создать уже существующего пользователя")
    public void checkCreateUserAlredyExist() {
        user = new User("Slava777@ya.ru", "11111", "Slava777");
        stellarUser = new StellarUser();
        stellarUser.createUser(user);
        createdUser = stellarUser.createUser(user);
        createdUser.then().statusCode(403).and().body("message", equalTo("User already exists"));
    }

    @Test
    @Description("Нельзя создать пользователя без всех обязательных полей (без пароля)")
    public void checkCreateUserWithoutPassword() {
        user = new User("Slava777@ya.ru", "", "Slava777");
        stellarUser = new StellarUser();
        createdUser = stellarUser.createUser(user);
        createdUser.then().assertThat().statusCode(403).and().body("message", equalTo("Email, password and name are required fields"));
    }

    @After
    public void tearDown() {
        token = stellarUser.loginUser(user).then().extract().path("accessToken");
        if (token != null) {
            stellarUser.deleteUser(token);
        }
    }

}

