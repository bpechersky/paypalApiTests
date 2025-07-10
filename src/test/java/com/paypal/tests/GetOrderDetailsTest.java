package com.paypal.tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class GetOrderDetailsTest {

    @Test
    public void getOrderById() {
        String accessToken = "A21AAJUE7NWjJs165tuDBqo_bSgCLjFE4A6eh3u1R6eC1rpSDqp7OI-uu3CdtrsFdrb3CPBeoqRuNbJWcaFiwTzve5Y81okkQ";
        String orderId = "61Y721497H8838359";

        RestAssured.given()
                .baseUri("https://api-m.sandbox.paypal.com")
                .basePath("/v2/checkout/orders/" + orderId)
                .auth().oauth2(accessToken)
                .accept(ContentType.JSON)
                .log().all()
                .when()
                .get()
                .then()
                .log().all()
                .statusCode(200)
                .body("id", equalTo(orderId))
                .body("intent", notNullValue())
                .body("status", notNullValue());
    }
}
