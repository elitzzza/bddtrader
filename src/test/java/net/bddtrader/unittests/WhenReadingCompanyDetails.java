package net.bddtrader.unittests;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

public class WhenReadingCompanyDetails {

    @Before
    public void prepare_rest_config() {
        RestAssured.baseURI = "https://bddtrader.herokuapp.com/api";
    }

    @Test
    public void should_return_name_and_sector() {
        RestAssured.get("/stock/aapl/company")
                .then()
                .body("companyName", Matchers.equalTo("Apple, Inc."))
                .body("sector", Matchers.equalTo("Electronic Technology"));
    }

    /**
     * Using path parameter
     */
    @Test
    public void should_return_name_and_sector_with_parameter() {
        RestAssured.get("/stock/{symbol}/company", "aapl")
                .then()
                .body("companyName", Matchers.equalTo("Apple, Inc."))
                .body("sector", Matchers.equalTo("Electronic Technology"));
    }

    @Test
    public void should_return_name_and_sector_with_parameter2() {
        RestAssured.given()
                .pathParam("symbol", "aapl")
                .when()
                .get("/stock/{symbol}/company")
                .then()
                .body("companyName", Matchers.equalTo("Apple, Inc."))
                .body("sector", Matchers.equalTo("Electronic Technology"));
    }

    /**
     * Using query parameter
     */
    @Test
    public void should_return_news_for_a_requested_company(){
        RestAssured.given()
                .queryParam("symbol","aapl")
                .when()
                .get("/news")
                .then()
                .body("related",Matchers.everyItem(Matchers.containsString("AAPL")));
    }
    @Test
    public void find_a_simple_field_value(){
        RestAssured.given()
                .pathParam("symbol","aapl")
                .when()
                .get("/stock/{symbol}/company")
                .then()
                .body("industry",Matchers.equalTo("Telecommunications Equipment"));
    }
    @Test
    public void check_that_a_field_vale_contains_a_given_string(){
        RestAssured.given()
                .pathParam("symbol","aapl")
                .when()
                .get("/stock/{symbol}/company")
                .then()
                .body("description",Matchers.containsString("smartphones"));
    }
    @Test
    public void find_a_nested_field_value(){
        RestAssured.given()
                .pathParam("symbol","aapl")
                .when()
                .get("/stock/{symbol}/book")
                .then()
                .body("quote.symbol",Matchers.equalTo("AAPL"));
    }
    @Test
    public void find_a_list_of_values(){
        RestAssured.when()
                .get("/tops/last")
                .then()
                .body("symbol",Matchers.hasItems("PTN","PINE","TRS"));
    }
    @Test
    public void make_sure_at_least_one_item_matches_a_given_condition(){
        RestAssured.when()
                .get("/tops/last")
                .then()
                .body("price",Matchers.hasItems(Matchers.greaterThan(100.0f)));
    }
    @Test
    public void find_a_field_of_an_element_in_a_list(){
        RestAssured.given()
                .pathParam("symbol","aapl")
                .when()
                .get("/stock/{symbol}/book")
                .then()
                .body("trades[0].price",Matchers.equalTo(319.59f));
    }
    @Test
    public void find_the_field_of_the_last_element_in_a_list(){
        RestAssured.given()
                .pathParam("symbol","aapl")
                .when()
                .get("/stock/{symbol}/book")
                .then()
                .body("trades[-1].price",Matchers.equalTo(319.54f));
    }
    @Test
    public void find_the_number_of_trades(){
        RestAssured.given()
                .pathParam("symbol","aapl")
                .when()
                .get("/stock/{symbol}/book")
                .then()
                .body("trades.size()",Matchers.equalTo(20));
    }
    @Test
    public void find_the_min_trade_price(){
        RestAssured.given()
                .pathParam("symbol","aapl")
                .when()
                .get("/stock/{symbol}/book")
                .then()
                .body("trades.price.min()",Matchers.equalTo(319.38f));
    }
    @Test
    public void find_the_size_of_the_trade_with_the_min_trade_price(){
        RestAssured.given()
                .pathParam("symbol","aapl")
                .when()
                .get("/stock/{symbol}/book")
                .then()
                .body("trades.min{trade->trade.price}.volume",Matchers.equalTo(100.0f));
    }
    @Test
    public void find_the_size_of_the_trade_with_the_min_trade_price2(){
        RestAssured.given()
                .pathParam("symbol","aapl")
                .when()
                .get("/stock/{symbol}/book")
                .then()
                .body("trades.min{it.price}.volume",Matchers.equalTo(100.0f));
    }
    @Test
    public void find_the_number_of_the_trades_with_a_price_greater_than_some_value(){
        RestAssured.given()
                .pathParam("symbol","aapl")
                .when()
                .get("/stock/{symbol}/book")
                .then()
                .body("trades.findAll{trade->trade.price > 319.50}.size()",Matchers.equalTo(13));
    }
}
