package net.bddtrader.unittests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.bddtrader.clients.Client;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class WhenDeletingAndUpdatingAClient {
    @Before
    public void setupBaseUrl(){
        RestAssured.baseURI = "http://localhost:9000/api";
    }
    @Test
    public void should_be_able_to_delete_a_client(){
        //Given a client exist
        Client existingClient = Client.withFirstName("Nicole").andLastName("Sr").andEmail("nicole@sr.com");
        String id = aClientExist(existingClient);

        //When I delete the client
        //DELETE http://localhost:9000/api/{id}
        RestAssured.given()
                .pathParam("id", id)
                .delete("/client/{id}");

        //Then the client should no longer exist
        RestAssured.given()
                .pathParam("id",id)
                .get("/client/{id}")
                .then()
                .statusCode(404);
    }

    /**
     * Same without pathParam in delete and check step
     */
    @Test
    public void should_be_able_to_delete_a_client2(){
        //Given a client exist
        String id = aClientExist(Client.withFirstName("Nicole").andLastName("Sr").andEmail("nicole@sr.com"));

        //When I delete the client
        //DELETE http://localhost:9000/api/{id}
        RestAssured.given()
                .delete("/client/{id}",id);

        //Then the client should no longer exist
        RestAssured.given()
                .get("/client/{id}",id)
                .then()
                .statusCode(404);
    }

    @Test
    public void should_be_able_to_update_a_client(){
        Client pam = Client.withFirstName("pam").andLastName("james").andEmail("pam@james.com");
        //Given a client exist
        String id = aClientExist(pam);

        //When I update the email of a client
        Client pamWithUpdates = Client.withFirstName("pam").andLastName("james").andEmail("pam@gmail.com");
        RestAssured.given()
                .contentType(ContentType.JSON)
                .and()
                .body(pamWithUpdates)
                .when()
                .put("/client/{id}",id)
                .then()
                .statusCode(200);

        RestAssured.when()
                .get("/client/{id}",id)
                .then()
                .body("email", Matchers.equalTo("pam@gmail.com"));


    }

    /**
     * Same with Map
     */
    @Test
    public void should_be_able_to_update_a_client2(){
        Client pam = Client.withFirstName("pam").andLastName("james").andEmail("pam@james.com");
        //Given a client exist
        String id = aClientExist(pam);

        //When I update the email of a client

        Map<String,Object> updates = new HashMap<>();
        updates.put("email","pam@gmail.com");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .and()
                .body(updates)
                .when()
                .put("/client/{id}",id)
                .then()
                .statusCode(200);

        RestAssured.when()
                .get("/client/{id}",id)
                .then()
                .body("email", Matchers.equalTo("pam@gmail.com"));


    }
    private String aClientExist(Client existingClient) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(existingClient)
                .when()
                .post("/client")
                .jsonPath().getString("id");
    }
}
