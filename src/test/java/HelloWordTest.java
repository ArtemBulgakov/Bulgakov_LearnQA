import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class HelloWordTest {


    @Test
    public void testRestAssured(){

        Response response = RestAssured
                .given()
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .andReturn();

        System.out.println("THE TEXT OF THE SECOND MESSAGE - " + response.jsonPath().getList("messages.message").get(1).toString());

    }
}
