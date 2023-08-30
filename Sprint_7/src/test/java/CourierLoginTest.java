import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;


import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTest {


    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }


    @Test
    @DisplayName("Login with valid credentials")
    public void loginValidCredentials()
    {
        String name = java.util.UUID.randomUUID().toString();

        given()
                .contentType(ContentType.JSON)
                .body("{\"login\":\"" + name + "\"," +
                        "\"password\":\"1234\"," +
                        "\"firstName\":\"saswke\"}")
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(201)
                .body("ok", CoreMatchers.equalTo(true));

        System.out.println("TEST : loginValidCredentials - started\nCourier with name : '" + name + "' was created");

        Integer id = given()
                .contentType(ContentType.JSON)
                .body("{\"login\":\"" + name + "\"," +
                        "\"password\":\"1234\"," +
                        "\"firstName\":\"saswke\"}")
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .extract()
                .path("id");

        System.out.println("Courier '" + name + "' was logged in. Courier ID = " + id);

        given()
                .contentType(ContentType.JSON)
                .body("{\"id\":\"" + id.toString() + "\"}")
                .when()
                .delete("/api/v1/courier/" + id)
                .then()
                .statusCode(200)
                .body("ok", equalTo(true));

        System.out.println("\nCourier was deleted! \n Courier name: " + name + ";\n Courier ID: " + id + ";\nTEST : loginValidCredentials - ended");

    }

    @Test
    @DisplayName("Login with wrong credentials")
    public void loginWithWrongCredentials()
    {
        String randomLogin = "Test" + new Random().nextInt(1000);
        String randomPass = "" + new Random().nextInt(9999);

            given()
                .contentType(ContentType.JSON)
                .body("{\"login\": \"" + randomLogin + "\", \"password\": \"" + randomPass + "\"}")
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Login with empty 'login' field")
    public void loginWithEmptyLoginField()
    {
        given()
                .contentType(ContentType.JSON)
                .body("{\"login\": \"\", \"password\": \"1234\"}")
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Login with empty 'password' field")
    public void loginWithEmptyPasswordField()
    {
        given()
                .contentType(ContentType.JSON)
                .body("{\"login\": \"ninjwa\", \"password\": \"\"}")
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Login with empty fields")
    public void loginWithEmptyFields()
    {
        given()
                .contentType(ContentType.JSON)
                .body("{\"login\": \"\", \"password\": \"\"}")
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }


}
