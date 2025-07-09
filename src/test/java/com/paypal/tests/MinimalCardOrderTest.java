package com.paypal.tests;

import com.paypal.utils.PayPalUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.hamcrest.Matchers.*;

public class MinimalCardOrderTest {

    @Test
    public void createMinimalCardOrder() {
        String clientId = System.getenv("PAYPAL_CLIENT_ID");
        String secret = System.getenv("PAYPAL_CLIENT_SECRET");
        String accessToken = PayPalUtils.generateAccessToken(clientId, secret);

        String payload = """
        {
          "intent": "CAPTURE",
          "purchase_units": [
            {
              "amount": {
                "currency_code": "USD",
                "value": "10.00"
              }
            }
          ],
          "payment_source": {
            "card": {
              "number": "4111111111111111",
              "expiry": "2035-12",
              "name": "John Doe",
              "billing_address": {
                "address_line_1": "2211 N First Street",
                "admin_area_2": "San Jose",
                "admin_area_1": "CA",
                "postal_code": "95131",
                "country_code": "US"
              }
            }
          }
        }
        """;

        RestAssured.given()
                .baseUri("https://api-m.sandbox.paypal.com")
                .basePath("/v2/checkout/orders")
                .auth().oauth2(accessToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Prefer", "return=representation")
                .header("PayPal-Request-Id", UUID.randomUUID().toString())
                .body(payload)
                .log().all()
                .when()
                .post()
                .then()
                .log().all()
                .statusCode(201)
                .body("id", notNullValue())
                .body("status", anyOf(equalTo("CREATED"), equalTo("COMPLETED")))
                .body("payment_source.card.last_digits", equalTo("1111"));
    }
}
