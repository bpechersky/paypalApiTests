package com.paypal.tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.hamcrest.Matchers.*;

public class ConfirmPaymentSourceTest {

    @Test
    public void confirmCardPaymentSource() {
        String accessToken = "A21AAJART2apWRLgIte5nYQwPHfAeA-BOQekP0juQbj5FsqAU2XDhNaOIGVMX4BxVH1s70Pm_uh6hQYxJf-J3cxTJ4BQD7bag";
        String orderId = "425040108M824123T";

        String payload = """
        {
          "payment_source": {
            "card": {
              "number": "4111111111111111",
              "expiry": "2035-12"
            }
          }
        }
        """;

        RestAssured.given()
                .baseUri("https://api-m.sandbox.paypal.com")
                .basePath("/v2/checkout/orders/" + orderId + "/confirm-payment-source")
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
                .statusCode(200) // or 200 or 422 based on state; can be adjusted
                .body("id", equalTo(orderId));
    }
}
