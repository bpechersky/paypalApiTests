package com.paypal.tests;

import io.restassured.RestAssured;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;

public class PayPalTokenTest {

    @Test
    public void generateAccessToken() {
        String clientId = System.getenv("PAYPAL_CLIENT_ID");
        String secret = System.getenv("PAYPAL_CLIENT_SECRET");

        String token = RestAssured
                .given()
                    .baseUri("https://api-m.sandbox.paypal.com")
                    .auth().preemptive().basic(clientId, secret)
                    .contentType("application/x-www-form-urlencoded")
                    .formParam("grant_type", "client_credentials")
                    .log().all()
                .when()
                    .post("/v1/oauth2/token")
                .then()
                    .log().all()
                    .statusCode(200)
                    .extract()
                    .jsonPath().getString("access_token");

        System.out.println("Token: " + token);
    }
}