package org.example;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.example.client.StellarOrder;
import org.example.client.StellarUser;
import org.example.model.Order;
import org.example.model.User;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class TestGetOrders {
    public Order order;
    public User user;
    public Response getOrders;
    public Response createdUser;
    public Response createdOrder;
    public StellarUser stellarUser;
    public StellarOrder stellarOrder;
    public String token;


    @Test
    @Description("Получить заказы с авторизацией")
    public void getUserOrdersWithLogin() {
        user = new User("Slava33@ya.ru", "11111", "Slava3");
        stellarUser = new StellarUser();
        createdUser = stellarUser.createUser(user);
        token = stellarUser.loginUser(user).then().extract().path("accessToken");
        order = new Order("60d3b41abdacab0026a733c6");
        stellarOrder = new StellarOrder();
        createdOrder = stellarOrder.createOrder(order, token);
        getOrders = stellarOrder.getUserOrders(token);
        getOrders.then().statusCode(200).and().body("success", equalTo(true));
    }

    @Test
    @Description("Получить заказы без авторизации")
    public void getUserOrdersWithoutLogin() {
        user = new User("Slava330@ya.ru", "11111", "Slava30");
        stellarUser = new StellarUser();
        createdUser = stellarUser.createUser(user);
        token = stellarUser.loginUser(user).then().extract().path("accessToken");
        order = new Order("60d3b41abdacab0026a733c6");
        stellarOrder = new StellarOrder();
        createdOrder = stellarOrder.createOrder(order, token);
        getOrders = stellarOrder.getUserOrders();
        getOrders.then().statusCode(401).and().body("message", equalTo("You should be authorised"));
    }

    @After
    public void tearDown() {
        if (token != null) {
            stellarUser.deleteUser(token);
        }
    }

}
