package net.bddtrader.unittests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.bddtrader.clients.Client;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class WhenCreatingANewClient {
    @Before
    public void setBaseURL(){
        RestAssured.baseURI = "http://localhost:9000/api";
    }

    /**
     * Using string for the request body
     */
    @Test
    public void each_client_should_get_an_unique_id(){
        String newClient = "{\n" +
                "  \"email\": \"mail@gmail.com\",\n" +
                "  \"firstName\": \"Eli\",\n" +
                "  \"lastName\": \"Sr\"\n" +
                "}";
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(newClient)
                .when()
                .post("/client")
                .then()
                .statusCode(200)
                .and()
                .body("id", Matchers.not(Matchers.equalTo(0)))
                .and()
                .body("email",Matchers.equalTo("mail@gmail.com"))
                .and()
                .body("firstName",Matchers.equalTo("Eli"));

    }

    /**
     * Using Object in the request body
     */
    @Test
    public void each_client_should_get_an_unique_id2(){
        Client aNewClient = Client.withFirstName("Elitza").andLastName("Angelova").andEmail("mymail@mail.com");
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(aNewClient)
                .when()
                .post("/client")
                .then()
                .statusCode(200)
                .and()
                .body("id", Matchers.not(Matchers.equalTo(0)))
                .and()
                .body("email",Matchers.equalTo("mymail@mail.com"))
                .and()
                .body("firstName",Matchers.equalTo("Elitza"));

    }
    @Test
    public void each_client_should_get_an_unique_id3(){
        Map<String, Object> clientData = new HashMap<>();
        clientData.put("firstName","Elin");
        clientData.put("lastName","Niko");
        clientData.put("email","one@mail.com");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(clientData)
                .when()
                .post("/client")
                .then()
                .statusCode(200)
                .and()
                .body("id", Matchers.not(Matchers.equalTo(0)))
                .and()
                .body("email",Matchers.equalTo("one@mail.com"))
                .and()
                .body("firstName",Matchers.equalTo("Elin"));

    }
}
