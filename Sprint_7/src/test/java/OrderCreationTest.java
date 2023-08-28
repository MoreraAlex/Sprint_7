import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import io.restassured.http.ContentType;

import java.util.Arrays;
import java.util.Collection;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@RunWith(Parameterized.class)
public class OrderCreationTest {

    private final String firstName;
    private final String lastName;
    private final String address;
    private final String apt;
    private final int metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final String[] colors;

    public OrderCreationTest(String firstName, String lastName, String address, String apt, int metroStation,
                              String phone, int rentTime, String deliveryDate, String comment, String[] colors) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.apt = apt;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.colors = colors;
    }
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][]{
                {"Jack", "Black", "A", "203 Koroleva", 8, "+7 800 123 45 67", 3, "2023-08-31", "Looking forward to my order", new String[]{"BLACK"}},
                {"Jane", "Grey", "B", "301 Sadovnikov", 6, "+7 800 987 65 43", 7, "2023-09-05", "I need this order for my mission", new String[]{"GREY"}},
                {"James", "Johns", "C", "501 Sennaya", 2, "+7 800 555 55 55", 4, "2023-09-10", "Make sure it's high quality", new String[]{"BLACK", "GREY"}},
                {"John", "Doe", "D", "702 Pushkinskaya", 9, "+7 800 789 12 34", 2, "2023-09-01", "Please handle with care", new String[]{}}
        });
    }

    @Test
    public void createOrderTest() {
        String colorsJson = Arrays.toString(colors)
                .replace("[", "[\"")
                .replace("]", "\"]")
                .replace(", ", "\", \"");

        given()
                .contentType(ContentType.JSON)
                .body(
                        "{\"firstName\": \"" + firstName + "\"," +
                                "\"lastName\": \"" + lastName + "\"," +
                                "\"address\": \"" + address + ", " + apt + "\"," +
                                "\"metroStation\": " + metroStation + "," +
                                "\"phone\": \"" + phone + "\"," +
                                "\"rentTime\": " + rentTime + "," +
                                "\"deliveryDate\": \"" + deliveryDate + "\"," +
                                "\"comment\": \"" + comment + "\"," +
                                "\"color\": " + colorsJson + "}"
                )
                .when()
                .post("/api/v1/orders")
                .then()
                .statusCode(201)
                .body("track", notNullValue());
    }
}

