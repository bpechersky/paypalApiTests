package com.paypal.tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

public class CreateOrderTest {

    private String getAccessToken() {
        String clientId = System.getenv("PAYPAL_CLIENT_ID");
        String secret = System.getenv("PAYPAL_CLIENT_SECRET");

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

    @Test
    public void createOrder() {
        String token = getAccessToken();
        String body = """
    {
      "intent": "CAPTURE",
      "purchase_units": [
        {
          "amount": {
            "currency_code": "USD",
            "value": "100.00"
          }
        }
      ]
    }
    """;


        RestAssured
                .given()
                .baseUri("https://api-m.sandbox.paypal.com")
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(body)
                .when()
                .post("/v2/checkout/orders")
                .then()
                .statusCode(201)
                .body("status", equalTo("CREATED"));
    }
}
