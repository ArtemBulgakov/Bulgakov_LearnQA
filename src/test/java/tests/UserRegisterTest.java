package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

@Epic("Register cases")
@Feature("Registration user")
public class UserRegisterTest extends BaseTestCase {

    @Test
    @Description("This test unsuccessful create user with existing email")
    @DisplayName("Test unsuccessful create user")
    public void testCreateUserWithExistingEmail() {
        {
            String email = "vinkotov@example.com";
            Map<String, String> userData = DataGenerator.getRegistrationData();
            userData
                    .put("email",email);
            Response responseCreateUser = ApiCoreRequests
                    .makePostRequest("https://playground.learnqa.ru/api/user/",userData);
            Assertions.assertResponseCodeEquals(responseCreateUser, 400);
            Assertions.assertResponseTextEquals(responseCreateUser, "Users with email '" + email + "' already exists");
        }

    }

    @Test
    @Description("This test successful create user")
    @DisplayName("Test create user")
    public void testCreateUserSuccessfully() {
        Map<String, String> userData = DataGenerator.getRegistrationData();
        Response responseCreateUser = ApiCoreRequests
                .makePostRequestSuccessfulCreate("https://playground.learnqa.ru/api/user/",userData);

        Assertions.assertResponseCodeEquals(responseCreateUser, 200);
        Assertions.assertJsonHasField(responseCreateUser, "id");

    }


    @Test
    @Description("This test unsuccessful create user without @ in the email")
    @DisplayName("Test unsuccessful create user")
    public void testCreateUserWithIncorrectMail() {

        Map<String, String> userData = DataGenerator.getRegistrationData();
        String emailIncorrect = "testmail.example.ru";
        userData
                .put("email",emailIncorrect);

        Response responseCheckCreate = ApiCoreRequests
                .makePostRequestIncorrectMail("https://playground.learnqa.ru/api/user/",userData);
        Assertions.assertResponseCodeEquals(responseCheckCreate, 400);
        Assertions.assertResponseTextEquals(responseCheckCreate, "Invalid email format");
    }

    @ParameterizedTest(name = "{index} - without {0} ")
    @ValueSource(strings = {"email","password","username","firstName","lastName"})
    @Description("This test unsuccessful create user without one of the fields")
    @DisplayName("Test unsuccessful create user")
    public void makePostRequestWithoutOneFields(String oneField) {

        Map<String, String> userData = DataGenerator.getRegistrationData();
        userData
                .remove(oneField);

        Response responseCheckCreate = ApiCoreRequests
                .makePostRequestWithoutOneFields("https://playground.learnqa.ru/api/user/",userData);
        Assertions.assertResponseCodeEquals(responseCheckCreate, 400);
        Assertions.assertResponseTextEquals(responseCheckCreate, "The following required params are missed: "+oneField);
    }

    @Test
    @Description("This test unsuccessful create user without very short name")
    @DisplayName("Test unsuccessful create user")
    public void testCreateUserWithShortName() {

        Map<String, String> userData = DataGenerator.getRegistrationData();
        String shortName = "1";
        userData
                .put("username",shortName);

        Response responseCheckCreate = ApiCoreRequests
                .makePostRequestWithoutVeryShortName("https://playground.learnqa.ru/api/user/",userData);

        Assertions.assertResponseCodeEquals(responseCheckCreate, 400);
        Assertions.assertResponseTextEquals(responseCheckCreate, "The value of 'username' field is too short");
    }

    @Test
    @Description("This test unsuccessful create user without very long name")
    @DisplayName("Test unsuccessful create user")
    public void testCreateUserWithLongName() {

        Map<String, String> userData = DataGenerator.getRegistrationData();
        String longName = "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012" +
                "34567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901";
        userData
                .put("username",longName);

        Response responseCheckCreate = ApiCoreRequests
                .makePostRequestWithoutVeryLongName("https://playground.learnqa.ru/api/user/",userData);

        Assertions.assertResponseCodeEquals(responseCheckCreate, 400);
        Assertions.assertResponseTextEquals(responseCheckCreate, "The value of 'username' field is too long");
    }

}