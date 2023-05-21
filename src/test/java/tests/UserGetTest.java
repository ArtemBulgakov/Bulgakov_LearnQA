package tests;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserGetTest extends BaseTestCase {

    @Test
    public void testGetUserDetailsAuthAsSameUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");
        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.Learnqa.ru/api/user/login")
                .andReturn();

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");
        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", header)
                .cookie("auth_sid", cookie)
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();
        String[] expectedFields = {"username", "firstName", "lastName", "email"};
        Assertions.assertJsonHasField(responseUserData, expectedFields);
}

    @Test
    @Description("This test unsuccessful viewing of someone else's user data")
    @DisplayName("Test unsuccessful viewing data")
    public void testCreateUserWithIncorrectMail() {

        Map<String, String> userData = DataGenerator.getRegistrationData();
        Map<String, String> authData = new HashMap<>();
        Response responseCreateUser = ApiCoreRequests.makePostRequestSuccessfulCreate16Ex("https://playground.learnqa.ru/api/user/",userData);
        Assertions.assertResponseCodeEquals(responseCreateUser, 200);

        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        Response responseGetAuth = ApiCoreRequests.makePostRequestAuthorization("https://playground.Learnqa.ru/api/user/login",authData);
        Assertions.assertResponseCodeEquals(responseGetAuth, 200);

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");
        Response responseGetUserData = ApiCoreRequests.makeGetRequestSuccessfulAuth("https://playground.learnqa.ru/api/user/2",header,cookie);
        Assertions.assertResponseCodeEquals(responseGetUserData, 200);
        Assertions.assertJsonHasField(responseGetUserData,"username");

        String[] expectedFields = {"firstName", "lastName", "email"};
        Assertions.assertJsonHasNotField(responseGetUserData,expectedFields);

        ;
    }

}
