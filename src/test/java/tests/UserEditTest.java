package tests;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserEditTest extends BaseTestCase {

    @Test
    @Description("This test unsuccessful edit user data without authorization")
    @DisplayName("Test unsuccessful edit user data without authorization")
    public void testEditJustCreatedTestWithoutAuth() {
        //создание пользователя
        Map<String, String> userData = DataGenerator.getRegistrationData();
        Map<String, String> authData = new HashMap<>();
        Response responseCreateUser = ApiCoreRequests.makePostRequestSuccessfulCreate16Ex("https://playground.learnqa.ru/api/user/", userData);
        Assertions.assertResponseCodeEquals(responseCreateUser, 200);
        String userId = responseCreateUser.jsonPath().getString("id");

        //редактируем пользователя не авторизовавшись
        Map<String, String> userEditData = new HashMap<>();
        userEditData.put("username", "EditName");

        Response responsePutUser = ApiCoreRequests.makePutRequestUnauthorized("https://playground.Learnqa.ru/api/user/" + userId, userEditData);
        Assertions.assertResponseCodeEquals(responsePutUser, 400);
        Assertions.assertResponseTextEquals(responsePutUser, "Auth token not supplied");

    }

    @Test
    @Description("This test unsuccessful edit user data with auth for one user and without authorization for two user")
    @DisplayName("Test unsuccessful edit user data for two user")
    public void testEditJustCreatedTestAuthAnotherUser() {
        //создание первого пользователя
        Map<String, String> userData = DataGenerator.getRegistrationData();
        Map<String, String> authData = new HashMap<>();
        Response responseCreateUser = ApiCoreRequests.makePostRequestSuccessfulCreate16Ex("https://playground.learnqa.ru/api/user/", userData);
        Assertions.assertResponseCodeEquals(responseCreateUser, 200);
        String userId = responseCreateUser.jsonPath().getString("id");

        //создание второго пользователя
        Map<String, String> userData2 = DataGenerator.getRegistrationData();
        Response responseCreateUser2 = ApiCoreRequests.makePostRequestSuccessfulCreate16Ex("https://playground.learnqa.ru/api/user/", userData2);
        Assertions.assertResponseCodeEquals(responseCreateUser2, 200);
        String userId2 = responseCreateUser2.jsonPath().getString("id");

        //авторизовываемся под первым пользователем
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        Response responseGetAuth = ApiCoreRequests.makePostRequestAuthorization("https://playground.Learnqa.ru/api/user/login", authData);
        Assertions.assertResponseCodeEquals(responseGetAuth, 200);


        //редактируем второго пользователя под которым не авторизовывались, используя данные авторизации первого пользователя
        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");
        String EditName = "EditName";
        Map<String, String> userEditData = new HashMap<>();
        userEditData.put("username", EditName);

        Response responsePutUser = ApiCoreRequests.makePutRequestAuthorized("https://playground.Learnqa.ru/api/user/" + userId2, userEditData, header, cookie);
        Assertions.assertResponseCodeEquals(responsePutUser, 200);

        //получаем данные второго пользователя после попытки изменения для проверки изменения данных, используя данные авторизации второго пользователя
        String header2 = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie2 = this.getCookie(responseGetAuth, "auth_sid");
        Response responseGetUserData = ApiCoreRequests.makeGetRequestSuccessfulAuth("https://playground.learnqa.ru/api/user/" + userId2, header2, cookie2);
        Assertions.assertResponseCodeEquals(responseGetUserData, 200);
        Assertions.assertValueEquals(responseGetUserData.jsonPath().getString("username"), userData2.get("username"));

    }

    @Test
    @Description("This test unsuccessful edit user data with auth for user and with incorrect email")
    @DisplayName("Test unsuccessful edit user data with incorrect email")
    public void testEditJustCreatedTestWithIncorrectMail() {
        //создание первого пользователя
        Map<String, String> userData = DataGenerator.getRegistrationData();
        Map<String, String> authData = new HashMap<>();
        Response responseCreateUser = ApiCoreRequests.makePostRequestSuccessfulCreate16Ex("https://playground.learnqa.ru/api/user/", userData);
        Assertions.assertResponseCodeEquals(responseCreateUser, 200);
        String userId = responseCreateUser.jsonPath().getString("id");


        //авторизовываемся под первым пользователем
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        Response responseGetAuth = ApiCoreRequests.makePostRequestAuthorization("https://playground.Learnqa.ru/api/user/login", authData);
        Assertions.assertResponseCodeEquals(responseGetAuth, 200);


        //редактируем пользователя
        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");
        String EditEmail = "EditEmailexample.ru";
        Map<String, String> userEditData = new HashMap<>();
        userEditData.put("email", EditEmail);

        Response responsePutUser = ApiCoreRequests.makePutRequestAuthorized("https://playground.Learnqa.ru/api/user/" + userId, userEditData, header, cookie);
        Assertions.assertResponseCodeEquals(responsePutUser, 400);
        Assertions.assertResponseTextEquals(responsePutUser, "Invalid email format");

        //получаем данные второго пользователя после попытки изменения для проверки изменения данных, используя данные авторизации второго пользователя
        Response responseGetUserData = ApiCoreRequests.makeGetRequestSuccessfulAuth("https://playground.learnqa.ru/api/user/" + userId, header, cookie);
        Assertions.assertResponseCodeEquals(responseGetUserData, 200);
        Assertions.assertValueEquals(responseGetUserData.jsonPath().getString("email"), userData.get("email"));

    }

    @Test
    @Description("This test unsuccessful edit user data with auth for user and with incorrect firstName")
    @DisplayName("Test unsuccessful edit user data with incorrect firstName")
    public void testEditJustCreatedTestWithIncorrectFirstName() {
        //создание первого пользователя
        Map<String, String> userData = DataGenerator.getRegistrationData();
        Map<String, String> authData = new HashMap<>();
        Response responseCreateUser = ApiCoreRequests.makePostRequestSuccessfulCreate16Ex("https://playground.learnqa.ru/api/user/", userData);
        Assertions.assertResponseCodeEquals(responseCreateUser, 200);
        String userId = responseCreateUser.jsonPath().getString("id");


        //авторизовываемся под первым пользователем
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        Response responseGetAuth = ApiCoreRequests.makePostRequestAuthorization("https://playground.Learnqa.ru/api/user/login", authData);
        Assertions.assertResponseCodeEquals(responseGetAuth, 200);


        //редактируем пользователя
        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");
        String EditFirstName  = "1";
        Map<String, String> userEditData = new HashMap<>();
        userEditData.put("firstName", EditFirstName);

        Response responsePutUser = ApiCoreRequests.makePutRequestAuthorized("https://playground.Learnqa.ru/api/user/" + userId, userEditData, header, cookie);
        Assertions.assertResponseCodeEquals(responsePutUser, 400);
        Assertions.assertValueEquals(responsePutUser.jsonPath().getString("error"), "Too short value for field firstName");

        //получаем данные второго пользователя после попытки изменения для проверки изменения данных, используя данные авторизации второго пользователя
        Response responseGetUserData = ApiCoreRequests.makeGetRequestSuccessfulAuth("https://playground.learnqa.ru/api/user/" + userId, header, cookie);
        Assertions.assertResponseCodeEquals(responseGetUserData, 200);
        Assertions.assertValueEquals(responseGetUserData.jsonPath().getString("firstName"), userData.get("firstName"));

    }
}
