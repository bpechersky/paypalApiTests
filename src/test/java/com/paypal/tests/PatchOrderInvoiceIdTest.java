package com.paypal.tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

public class PatchOrderInvoiceIdTest {

    @Test
    public void patchOrderInvoiceId() {
        String accessToken = "A21AAJUE7NWjJs165tuDBqo_bSgCLjFE4A6eh3u1R6eC1rpSDqp7OI-uu3CdtrsFdrb3CPBeoqRuNbJWcaFiwTzve5Y81okkQ";
        String orderId = "1HS830096G338960N";

        String patchPayload = """
        [
          {
            "op": "add",
            "path": "/purchase_units/@reference_id=='default'/invoice_id",
            "value": "03012022-3303-01"
          }
        ]
        """;

        RestAssured.given()
                .baseUri("https://api-m.sandbox.paypal.com")
                .basePath("/v2/checkout/orders/" + orderId)
                .auth().oauth2(accessToken)
                .contentType(ContentType.JSON)
                .header("PayPal-Request-Id", "15dabc3d-8001-4251-b4a3-7472dd6c8b71")
                .body(patchPayload)
                .log().all()
                .when()
                .patch()
                .then()
                .log().all()
                .statusCode(204); // PATCH usually returns 204 No Content if successful
    }
}
