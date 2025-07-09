package com.paypal.tests;

import com.paypal.utils.PayPalUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CreateOrderDetailedTest {

    @Test
    public void createOrderWithDetailedRequest() {
        // Replace with your actual sandbox credentials (or load from env variables)
        String clientId = System.getenv("PAYPAL_CLIENT_ID");
        String clientSecret = System.getenv("PAYPAL_CLIENT_SECRET");


        // 1. Get access token
        String accessToken = given()
                .auth().preemptive().basic(clientId, clientSecret)
                .contentType("application/x-www-form-urlencoded")
                .formParam("grant_type", "client_credentials")
                .when()
                .post("https://api-m.sandbox.paypal.com/v1/oauth2/token")
                .then()
                .statusCode(200)
                .extract()
                .path("access_token");

        // 2. Create order with detailed payload
        String orderPayload = """
            {
              "intent": "CAPTURE",
              "purchase_units": [
                {
                  "amount": {
                    "currency_code": "USD",
                    "value": "100.00",
                    "breakdown": {
                      "item_total": {
                        "currency_code": "USD",
                        "value": "90.00"
                      },
                      "tax_total": {
                        "currency_code": "USD",
                        "value": "10.00"
                      }
                    }
                  },
                  "items": [
                    {
                      "name": "T-Shirt",
                      "description": "Green XL",
                      "sku": "sku01",
                      "unit_amount": {
                        "currency_code": "USD",
                        "value": "45.00"
                      },
                      "tax": {
                        "currency_code": "USD",
                        "value": "5.00"
                      },
                      "quantity": "1",
                      "category": "PHYSICAL_GOODS"
                    },
                    {
                      "name": "Shoes",
                      "description": "Running, Size 10",
                      "sku": "sku02",
                      "unit_amount": {
                        "currency_code": "USD",
                        "value": "45.00"
                      },
                      "tax": {
                        "currency_code": "USD",
                        "value": "5.00"
                      },
                      "quantity": "1",
                      "category": "PHYSICAL_GOODS"
                    }
                  ]
                }
              ]
            }
            """;

        given()
                .baseUri("https://api-m.sandbox.paypal.com")
                .basePath("/v2/checkout/orders")
                .auth().oauth2(accessToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(orderPayload)
                .log().all()
                .when()
                .post()
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("status", equalTo("CREATED"))
                .body("links", notNullValue())
                .log().all();

    }
}
