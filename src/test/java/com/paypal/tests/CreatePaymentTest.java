package com.paypal.tests;

import com.paypal.utils.PayPalUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class CreatePaymentTest {



    @Test
    public void createPayment() {
        String clientId = System.getenv("PAYPAL_CLIENT_ID");
        String secret = System.getenv("PAYPAL_CLIENT_SECRET");
        String accessToken = PayPalUtils.generateAccessToken(clientId, secret);

        String body = """
    {
        "intent": "sale",
        "payer": {
            "payment_method": "paypal"
        },
        "transactions": [
            {
                "amount": {
                    "total": "10.00",
                    "currency": "USD"
                },
                "description": "Test payment"
            }
        ],
        "redirect_urls": {
            "return_url": "https://example.com/success",
            "cancel_url": "https://example.com/cancel"
        }
    }
    """;

        RestAssured
                .given()
                .baseUri("https://api-m.sandbox.paypal.com")
                .auth().oauth2(accessToken)
                .contentType(ContentType.JSON)
                .body(body)
                .log().all()
                .when()
                .post("/v1/payments/payment")
                .then()
                .log().all()
                .statusCode(201)
                .body("state", equalTo("created"));
    }

}