import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class HelloWordTest {


    @Test
    public void testRestAssured() throws InterruptedException {

        Response response1 = RestAssured
                .given()
                .when()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .andReturn();
        response1.print();
        System.out.println();


        Response response2 = RestAssured
                .given()
                .queryParam("token", response1.jsonPath().getString("token"))
                .when()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .andReturn();
        response2.print();
        System.out.println();


        if (response2.jsonPath().getString("status") != "Job is ready") {
            Thread.sleep(response1.jsonPath().getInt("seconds") * 1000);
        }
        Response response3 = RestAssured
                .given()
                .queryParam("token", response1.jsonPath().getString("token"))
                .when()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .andReturn();
        response3.print();


        if ((response3.jsonPath().getString("result") != null) &&
                (response3.jsonPath().getString("status").equals("Job is ready"))) {
            System.out.println();
            System.out.println("It's good");
        }
    }

}
