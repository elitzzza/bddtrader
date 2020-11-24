package net.bddtrader;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.everyItem;

public class WhenGettingCompanyNews {

    @Before
    public void prepare_rest_config() {
        RestAssured.baseURI = "https://bddtrader.herokuapp.com/api";
    }

    @Test
    public void should_return_name_and_sector_from_local() {
        RestAssured.get("http://localhost:9000/api/stock/aapl/company")
                .then()
                .body("companyName", Matchers.equalTo("Apple, Inc."))
                .body("sector",Matchers.equalTo("Electronic Technology"));
    }

    @Test
    public void should_return_news_for_a_requested_company() {
        RestAssured.given().queryParam("symbols","AAPL")
                .when()
                .get("/news")
                .then()
                .body("related", everyItem(containsString("AAPL")));
    }
}
