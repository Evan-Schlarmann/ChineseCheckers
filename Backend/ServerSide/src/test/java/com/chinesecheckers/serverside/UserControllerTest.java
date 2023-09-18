package com.chinesecheckers.serverside;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

@RunWith(SpringRunner.class)
public class UserControllerTest {

    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void usernameFailedTest() {

        // Send request and receive response
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("").
                when().
                get("/users/id/58");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString); //returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("failed", returnObj.get("username"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void username68Test() {

        // Send request and receive response
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("").
                when().
                get("/users/id/68");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString); //returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("Demo2", returnObj.get("username"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void statsNullTest() {

        // Send request and receive response
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("").
                when().
                get("/users/stats/58");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString); //returnArr.getJSONObject(returnArr.length()-1);
            assertEquals(Integer.valueOf(-1), returnObj.get("matches"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void stats68Test() {

        // Send request and receive response
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("").
                when().
                get("/users/stats/68");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString); //returnArr.getJSONObject(returnArr.length()-1);
            assertEquals(Integer.valueOf(0), returnObj.get("matches"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void eloNullTest() {

        // Send request and receive response
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("").
                when().
                get("/users/elo/58");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();
        try {
            assertEquals(Long.valueOf(-1), Long.valueOf(returnString));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void elo68Test() {

        // Send request and receive response
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("").
                when().
                get("/users/elo/68");

        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();
        try {
            assertEquals(Long.valueOf(5), Long.valueOf(returnString));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
