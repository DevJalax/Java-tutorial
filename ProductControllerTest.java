import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;

public class ProductControllerTest {

    private static final String BASE_URL = "http://localhost:8080/api/product"; // Adjust the port as necessary

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test(priority = 1)
    public void testCreateProduct() {
        String productJson = "{\"name\":\"Test Product\",\"price\":99.99}";

        Response response = given()
                .contentType(ContentType.JSON)
                .body(productJson)
                .when()
                .post()
                .then()
                .statusCode(201) // Expecting 201 Created
                .extract().response();

        assertEquals(response.jsonPath().getString("name"), "Test Product");
        assertEquals(response.jsonPath().getDouble("price"), 99.99);
    }

    @Test(priority = 2)
    public void testGetAllProducts() {
        given()
                .when()
                .get()
                .then()
                .statusCode(200) // Expecting 200 OK
                .body("$", is(not(empty())));
    }

    @Test(priority = 3)
    public void testGetProductById() {
        // Assuming the product with ID 1 exists
        given()
                .when()
                .get("/1")
                .then()
                .statusCode(200) // Expecting 200 OK
                .body("name", equalTo("Test Product"));
    }

    @Test(priority = 4)
    public void testGetNonExistentProduct() {
        given()
                .when()
                .get("/999") // Assuming 999 does not exist
                .then()
                .statusCode(404); // Expecting 404 Not Found
    }

    @Test(priority = 5)
    public void testUpdateProductPrice() {
        // Assuming the product with ID 1 exists
        given()
                .param("newPrice", 79.99)
                .when()
                .put("/1/price")
                .then()
                .statusCode(200); // Expecting 200 OK
    }

    @Test(priority = 6)
    public void testUpdateNonExistentProductPrice() {
        given()
                .param("newPrice", 79.99)
                .when()
                .put("/999/price") // Assuming 999 does not exist
                .then()
                .statusCode(404); // Expecting 404 Not Found
    }

    @Test(priority = 7)
    public void testDeleteProduct() {
        // Assuming the product with ID 1 exists
        given()
                .when()
                .delete("/1")
                .then()
                .statusCode(204); // Expecting 204 No Content
    }

    @Test(priority = 8)
    public void testDeleteNonExistentProduct() {
        given()
                .when()
                .delete("/999") // Assuming 999 does not exist
                .then()
                .statusCode(404); // Expecting 404 Not Found
    }
}
