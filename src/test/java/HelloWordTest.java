import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class HelloWordTest {


    @Test
    public void testRestAssured() {

        int i = 1;
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();

        String HedLocation = response.getHeader("Location");
        System.out.println(i + " " + "redirect to - " + response.getHeader("Location"));
        System.out.println();

        while (response.getStatusCode() > 200) {
            response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(HedLocation)
                    .andReturn();
            if (response.getStatusCode() > 200) {
                i++;
                System.out.println(i + " " + "redirect to - " + response.getHeader("Location"));
                System.out.println();
            }
            HedLocation = response.getHeader("Location");
        }
    }
}
