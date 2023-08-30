import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class CourierCreationTest {


    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void createCourierWithValidData()
    {
        String name = java.util.UUID.randomUUID().toString();
        System.out.println(name);

        given()
                .contentType(ContentType.JSON)
                .body("{\"login\":\"" + name + "\"," +
                        "\"password\":\"1234\"," +
                        "\"firstName\":\"saswke\"}")
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Test
    public void createCourierWithNonOriginalLogin()
    {

        String name = java.util.UUID.randomUUID().toString();
        System.out.println(name);

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

        given()
                .contentType(ContentType.JSON)
                .body("{\"login\":\"" + name + "\"," +
                        "\"password\":\"1234\"," +
                        "\"firstName\":\"saswke\"}")
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется"));



    }

    @Test
    public void createCourierWithEmptyLogin()
    {
        given()
                .contentType(ContentType.JSON)
                .body("{\"login\":\"\"," +
                        "\"password\":\"1234\"," +
                        "\"firstName\":\"saswke\"}")
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    public void createCourierWithEmptyPassword()
    {
        given()
                .contentType(ContentType.JSON)
                .body("{\"login\":\"TEST\"," +
                        "\"password\":\"\"," +
                        "\"firstName\":\"saswke\"}")
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    public void createCourierWithEmptyFirstName()
    {
        given()
                .contentType(ContentType.JSON)
                .body("{\"login\":\"TEST\"," +
                        "\"password\":\"1234\"," +
                        "\"firstName\":\"\"}")
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

}
