import groovyjarjarpicocli.CommandLine;
import io.restassured.RestAssured;
import io.restassured.internal.common.assertion.Assertion;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertAll;

public class Lesson3 {

    @Test
    public void task1_Ex10() {

        String data = "task1_Ex10123457";
        int length = 15; // минимальное количество символов, которое должно быть в строке, не включая это число
        Assertions.assertTrue(data.length() > length, "the length of the string is longer - " + length);
        System.out.println("the length of the string is longer - " + length);
    }

    @Test
    public void task2_Ex11() {

        Response response = RestAssured
                .given()
                .when()
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();

        Assertions.assertNotNull(response.getCookie("HomeWork"));
        Assertions.assertEquals("hw_value", response.getCookie("HomeWork"));
    }

    @Test
    public void task3_Ex12() {

        Response response = RestAssured
                .given()
                .when()
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();
        Assertions.assertNotNull(response.getHeader("x-secret-homework-header"));
        Assertions.assertEquals("Some secret value", response.getHeader("x-secret-homework-header"));
    }


    @ParameterizedTest(name = "{index} - numberTest")
    @CsvSource({"1 , Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30",
            "2, Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1",
            "3, Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)",
            "4, Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0",
            "5, Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1"})

    public void task4_Ex13(int NumberUserAgent, String UserAgent) {

        if (UserAgent.length() > 0) {
            Response response = RestAssured
                    .given()
                    .header("User-Agent", UserAgent)
                    .when()
                    .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                    .andReturn();

            response.print();
            System.out.println("number - " + NumberUserAgent);

            switch (NumberUserAgent) {
                case 1:
                    assertAll(
                            () -> Assertions.assertEquals("Mobile", response.jsonPath().getString("platform")),
                            () -> Assertions.assertEquals("No", response.jsonPath().getString("browser")),
                            () -> Assertions.assertEquals("Android", response.jsonPath().getString("device"))
                    );

                    break;
                case 2:
                    assertAll(
                            () -> Assertions.assertEquals("Mobile", response.jsonPath().getString("platform")),
                            () -> Assertions.assertEquals("Chrome", response.jsonPath().getString("browser")),
                            () -> Assertions.assertEquals("iOS", response.jsonPath().getString("device"))
                    );
                    break;

                case 3:
                    assertAll(
                            () -> Assertions.assertEquals("Googlebot", response.jsonPath().getString("platform")),
                            () -> Assertions.assertEquals("Unknown", response.jsonPath().getString("browser")),
                            () -> Assertions.assertEquals("Unknown", response.jsonPath().getString("device"))
                    );
                    break;

                case 4:
                    assertAll(
                            () -> Assertions.assertEquals("Web", response.jsonPath().getString("platform")),
                            () -> Assertions.assertEquals("Chrome", response.jsonPath().getString("browser")),
                            () -> Assertions.assertEquals("No", response.jsonPath().getString("device"))
                    );
                    break;

                case 5:
                    assertAll(
                            () -> Assertions.assertEquals("Mobile", response.jsonPath().getString("platform")),
                            () -> Assertions.assertEquals("No", response.jsonPath().getString("browser")),
                            () -> Assertions.assertEquals("iPhone", response.jsonPath().getString("device"))
                    );
                    break;

            }


        }

    }

}
