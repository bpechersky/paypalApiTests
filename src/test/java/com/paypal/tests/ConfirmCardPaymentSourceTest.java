package com.paypal.tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.hamcrest.Matchers.*;

public class ConfirmCardPaymentSourceTest {

    @Test
    public void confirmCardPaymentSource() {
        String orderId = "425040108M824123T";
        String accessToken = "A21AAJART2apWRLgIte5nYQwPHfAeA-BOQekP0juQbj5FsqAU2XDhNaOIGVMX4BxVH1s70Pm_uh6hQYxJf-J3cxTJ4BQD7bag";

        String payload = """
        {
          "payment_source": {
            "card": {
              "number": "4111111111111111",
              "expiry": "2035-12",
              "name": "John Doe",
              "billing_address": {
                "address_line_1": "2211 N First Street",
                "address_line_2": "17.3.160",
                "admin_area_1": "CA",
                "admin_area_2": "San Jose",
                "postal_code": "95131",
                "country_code": "US"
              },
              "attributes": {
                "verification": {
                  "method": "SCA_WHEN_REQUIRED"
                }
              }
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
                .header("Cookie", "LANG=en_US%3BUS; cookie_prefs=T%3D0%2CP%3D0%2CF%3D0%2Ctype%3Dinitial; ts=vreXpYrS%3D1783630234%26vteXpYrS%3D1752096034%26vr%3Df0f4581d1970accc085fc419fad1e62a%26vt%3Df0f4581d1970accc085fc419fad1e629%26vtyp%3Dnew; ts_c=vr%3Df0f4581d1970accc085fc419fad1e62a%26vt%3Df0f4581d1970accc085fc419fad1e629; tsrce=devdiscoverynodeweb")
                .body(payload)
                .log().all()
                .when()
                .post()
                .then()
                .log().all()
                .statusCode(anyOf(equalTo(200), equalTo(201))) // 200 if confirmed, 201 if completed
                .body("id", equalTo(orderId));
    }
}
