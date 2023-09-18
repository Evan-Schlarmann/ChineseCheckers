package com.chinesecheckers.serverside;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

@RunWith(SpringRunner.class)
public class LobbyTest {

    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void privateLobbyTest() {

        String userC = "520227";
        String userD = "520230";

        // create a new  private lobby to test
        Response response = RestAssured.given().
                header("Content-Type", "application/json").
                header("charset","utf-8").
                body("{\"userId\": "+userC+"}").
                when().
                post("/lobbys/newprivate");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();
        try {
            //System.out.println(returnString);
            //JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = new JSONObject(returnString); //returnArr.getJSONObject(returnArr.length()-1);
            //System.out.println(returnObj);

            String lobbyId = returnObj.get("lobbyId").toString(); //lobby to use
            String joincode = returnObj.get("joinCode").toString();

            //System.out.println(joincode);

            //second user joins lobby
            response = RestAssured.given().
                    header("Content-Type", "application/json").
                    header("charset","utf-8").
                    body("{\"userId\": "+userD+"}").
                    when().
                    post("/lobbys/join/"+joincode);
            // Check status code
            statusCode = response.getStatusCode();
            assertEquals(200, statusCode);

            // Check response body for correct response
            returnString = response.getBody().asString();
            JSONObject returnObj2 = new JSONObject(returnString);

            //System.out.println(returnObj2);

            int joinedLobby = (int) returnObj2.get("lobbyId");
            int originalLobby = (int) Integer.valueOf(lobbyId);
            assertEquals(originalLobby, joinedLobby);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
