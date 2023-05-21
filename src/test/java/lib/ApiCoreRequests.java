package lib;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiCoreRequests {
    @Step("Make a GET-request with token and auth cookie")
    public Response makeGetRequest(String url, String token, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }


    @Step("Make a GET-request with auth cookie only")
        public Response makeGetRequestWithCookie (String url, String cookie){
            return given()
                    .filter(new AllureRestAssured())
                    .cookie("auth_sid", cookie)
                    .get(url)
                    .andReturn();
        }

    @Step("Make a GET-request with auth token only")
    public Response makeGetRequestWithToken (String url, String token){
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .get(url)
                .andReturn();
    }

    @Step("Make a Post-request")
    public static Response makePostRequest(String url, Map<String, String> authData){
        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .post(url)
                .andReturn();
    }

    @Step("Make a Post-request")
    public static Response makePostRequestSuccessfulCreate(String url, Map<String, String> userData){
        return given()
                .filter(new AllureRestAssured())
                .body(userData)
                .post(url)
                .andReturn();
    }

    @Step("Make a Post-request create user without @ in the mail")
    public static Response makePostRequestIncorrectMail(String url, Map<String, String> userData){
        return given()
                .filter(new AllureRestAssured())
                .body(userData)
                .post(url)
                .andReturn();
    }

    @Step("Make a Post-request create user without one of the fields ")
    public static Response makePostRequestWithoutOneFields(String url, Map<String, String> userData){
        return given()
                .filter(new AllureRestAssured())
                .body(userData)
                .post(url)
                .andReturn();
    }

    @Step("Make a Post-request create user without very short name")
    public static Response makePostRequestWithoutVeryShortName(String url, Map<String, String> userData){
        return given()
                .filter(new AllureRestAssured())
                .body(userData)
                .post(url)
                .andReturn();
    }

    @Step("Make a Post-request create user without very short name")
    public static Response makePostRequestWithoutVeryLongName(String url, Map<String, String> userData){
        return given()
                .filter(new AllureRestAssured())
                .body(userData)
                .post(url)
                .andReturn();
    }

    @Step("Make a Post-request")
    public static Response makePostRequestSuccessfulCreate16Ex(String url, Map<String, String> userData){
        return given()
                .filter(new AllureRestAssured())
                .body(userData)
                .post(url)
                .andReturn();
    }

    @Step("Make a Post-request successful authorization")
    public static Response makePostRequestAuthorization(String url, Map<String, String> authData){
        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .post(url)
                .andReturn();
    }

    @Step("Make a GET-request with token and auth cookie random user")
    public static Response makeGetRequestSuccessfulAuth(String url, String token, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

}
