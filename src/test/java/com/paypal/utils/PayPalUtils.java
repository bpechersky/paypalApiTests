package com.paypal.utils;

import io.restassured.RestAssured;

public class PayPalUtils {
    public static String generateAccessToken(String clientId, String secret) {
        return RestAssured
                .given()
                .baseUri("https://api-m.sandbox.paypal.com")
                .auth().preemptive().basic(clientId, secret)
                .contentType("application/x-www-form-urlencoded")
                .formParam("grant_type", "client_credentials")
                .when()
                .post("/v1/oauth2/token")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getString("access_token");
    }
}
