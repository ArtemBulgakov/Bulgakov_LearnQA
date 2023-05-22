package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("Delete cases")
@Feature("Delete user")
public class UserDeleteTest extends BaseTestCase {

    @Test
    @Description("This test unsuccessful delete user blocked for deletion")
    @DisplayName("Test unsuccessful delete user blocked for deletion")
    public void testDeleteBlockedUser() {

        //авторизовываемся под пользователем
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");
        Response responseGetAuth = ApiCoreRequests.makePostRequestAuthorization("https://playground.Learnqa.ru/api/user/login", authData);
        Assertions.assertResponseCodeEquals(responseGetAuth, 200);
        int userId = responseGetAuth.jsonPath().getInt("user_id");

        //удаляем пользователя
        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        Response responseDeleteUser = ApiCoreRequests.makeDeleteRequestAuthorized("https://playground.Learnqa.ru/api/user/" + userId, header, cookie);
        Assertions.assertResponseCodeEquals(responseDeleteUser, 400);
        Assertions.assertResponseTextEquals(responseDeleteUser, "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");


    }


    @Test
    @Description("This test successful delete user just create")
    @DisplayName("Test successful delete user")
    public void testSuccessfulDelete() {

        //создание пользователя
        Map<String, String> userData = DataGenerator.getRegistrationData();
        Map<String, String> authData = new HashMap<>();
        Response responseCreateUser = ApiCoreRequests.makePostRequestSuccessfulCreate16Ex("https://playground.learnqa.ru/api/user/", userData);
        Assertions.assertResponseCodeEquals(responseCreateUser, 200);
        String userId = responseCreateUser.jsonPath().getString("id");


        //авторизовываемся под пользователем
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        Response responseGetAuth = ApiCoreRequests.makePostRequestAuthorization("https://playground.Learnqa.ru/api/user/login", authData);
        Assertions.assertResponseCodeEquals(responseGetAuth, 200);


        //удаляем пользователя
        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        Response responseDeleteUser = ApiCoreRequests.makeDeleteRequestAuthorized("https://playground.Learnqa.ru/api/user/" + userId, header, cookie);
        Assertions.assertResponseCodeEquals(responseDeleteUser, 200);

        //получаем данные пользователя после попытки удаления
        Response responseGetUserData = ApiCoreRequests.makeGetRequestSuccessfulAuth("https://playground.learnqa.ru/api/user/" + userId, header, cookie);
        Assertions.assertResponseCodeEquals(responseGetUserData, 404);
        Assertions.assertResponseTextEquals(responseGetUserData, "User not found");
    }

    @Test
    @Description("This test unsuccessful delete two user with auto only one user")
    @DisplayName("Test unsuccessful delete two user with auto only one user")
    public void testUnsuccessfulDeleteTwoUser() {

        //создание первого пользователя
        Map<String, String> userData = DataGenerator.getRegistrationData();
        Map<String, String> authData = new HashMap<>();
        Response responseCreateUser = ApiCoreRequests.makePostRequestSuccessfulCreate16Ex("https://playground.learnqa.ru/api/user/", userData);
        Assertions.assertResponseCodeEquals(responseCreateUser, 200);

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


        //удаляем второго пользователя, авторизовавшись только под первым
        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        Response responseDeleteUser = ApiCoreRequests.makeDeleteRequestAuthorized("https://playground.Learnqa.ru/api/user/" + userId2, header, cookie);
        Assertions.assertResponseCodeEquals(responseDeleteUser, 200);

        //получаем данные пользователя после попытки удаления
        Response responseGetUserData = ApiCoreRequests.makeGetRequestSuccessfulAuth("https://playground.learnqa.ru/api/user/" + userId2, header, cookie);
        Assertions.assertResponseCodeEquals(responseGetUserData, 200);
        Assertions.assertJsonHasField(responseGetUserData, "username");


    }
}
