import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Lesson3 {

    @Test
    public void task1_Ex10() {

        String data = "task1_Ex10123457";
        int length = 15; // минимальное количество символов, которое должно быть в строке, не включая это число
        Assertions.assertTrue(data.length() > length, "the length of the string is longer - " + length);
        System.out.println("the length of the string is longer - " + length);

    }

}
