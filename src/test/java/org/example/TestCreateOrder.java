package org.example;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.example.client.StellarOrder;
import org.example.client.StellarUser;
import org.example.model.Order;
import org.example.model.User;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class TestCreateOrder {

    public Order order;
    public User user;
    public Response createdOrder;
    public Response createdUser;
    public StellarUser stellarUser;
    public StellarOrder stellarOrder;
    public String token;


    @Test
    @Description("Создание заказа с ингредиентами и авторизацией")
    public void checkCreateOrderWithIngredientsAndWithLogin() {
        user = new User("Slava777@ya.ru", "11111", "Slava777");
        stellarUser = new StellarUser();
        createdUser = stellarUser.createUser(user);
        token = stellarUser.loginUser(user).then().extract().path("accessToken");
        order = new Order("61c0c5a71d1f82001bdaaa6d");
        stellarOrder = new StellarOrder();
        createdOrder = stellarOrder.createOrder(order, token);
        createdOrder.then().statusCode(200).and().body("success", equalTo(true));
    }

    @Test
    @Description("Создание заказа с пустым списком ингредиентов и авторизацией")
    public void checkCreateOrderWithBlankIngredientsAndWithLogin() {
        user = new User("Slava777@ya.ru", "11111", "Slava777");
        stellarUser = new StellarUser();
        createdUser = stellarUser.createUser(user);
        createdUser.then().statusCode(200).and().body("success", Matchers.equalTo(true));
        token = stellarUser.loginUser(user).then().extract().path("accessToken");
        order = new Order("");
        stellarOrder = new StellarOrder();
        createdOrder = stellarOrder.createOrder(order, token);
        createdOrder.then().statusCode(400);
    }

    @Test
    @Description("Создание заказа с ингредиентами и без авторизации (найден баг, заказ создаётся без авторизации)")
    public void checkCreateOrderWithIngredientsAndWithoutLogin() {
        order = new Order("60d3b41abdacab0026a733c6");
        stellarOrder = new StellarOrder();
        createdOrder = stellarOrder.createOrder(order);
        createdOrder.then().statusCode(200).and().body("success", equalTo(false));
    }

    @Test
    @Description("Создание заказа с неверным ингридиентом и с авторизацией")
    public void checkCreateOrderWithWrongIngredientsAndWithLogin() {
        user = new User("Slava777@ya.ru", "11111", "Slava777");
        stellarUser = new StellarUser();
        stellarUser.createUser(user);
        token = stellarUser.loginUser(user).then().extract().path("accessToken");
        order = new Order("мывмывмыв");
        stellarOrder = new StellarOrder();
        createdOrder = stellarOrder.createOrder(order, token);
        createdOrder.then().statusCode(500);
    }

    @After
    public void tearDown() {
        if (token != null) {
            stellarUser.deleteUser(token);
        }
    }
}
