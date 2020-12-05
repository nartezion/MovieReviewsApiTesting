package reviews;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

public class Search {

    private String apiKey="ZLe1z8nwiSov0WiXZdc6bNYkA97W2Ib6";
    private String host1="https://api.nytimes.com/svc/movies/v2/reviews/search.json";
    private static RequestSpecification requestSpecification;

    @BeforeClass
    public static void specificationConfiguration(){
        requestSpecification= RestAssured.given()
        .contentType("application/json")
        .baseUri("https://api.nytimes.com/svc/movies/v2")
        .basePath("/reviews/search.json")
        .queryParam("api-key","ZLe1z8nwiSov0WiXZdc6bNYkA97W2Ib6");
    }


    @Test
    public void verifyThatResultsContainSearchQuery()
    {
        String searchQuery = "28 days later";

        Response response=given(requestSpecification).queryParam("query",searchQuery)
                .when().get()
                .then().log().body().
                        statusCode(200).extract().response();

        List<String> display_titles=response.path("results.display_title");

        List<String> headlines=response.path("results.headline");
        if(display_titles.size()>0) {
            for (int i = 0; i < display_titles.size(); i++) {
                if (!display_titles.get(i).toLowerCase().contains((searchQuery.toLowerCase())) &&
                        !headlines.get(i).toLowerCase().contains(searchQuery.toLowerCase()))
                {
                    Assert.fail("Result ID = " + i + " does not contain search query in title or headline");
                }
            }
        }
        else {
            Assert.fail("Zero results");
        }
    }


    @Test
    public void verifyThatOnlyCriticsPickReviewsAreRetrievedWhenOnlyCriticsPickCheckboxIsEnabled(){
        Response response = given().contentType("application/json").queryParam("critics-pick","Y")
                .queryParam("api-key",apiKey).log().uri()
                .when().get(host1)
                .then().log().body().statusCode(200).extract().response();
        List<Integer> criticsPickValues=response.path("results.critics_pick");

        if(criticsPickValues.size()>0){
            for(int i=0;i<criticsPickValues.size();i++){
                if(criticsPickValues.get(i).equals(0)){
                    Assert.fail("Result ID = "+i+" has not Critics Pick Mark");
                }
            }
        }
        else {
            Assert.fail("Zero results");
        }
    }

    @Test
    public void verifyThatReviewsThatAreNotMarketWithCriticsPicksMarkAreRetrievedWhenOnlyCriticsPickCheckboxIsDisabled(){
        Response response = given().contentType("application/json").queryParam("critics-pick","N")
                .queryParam("api-key",apiKey).log().uri()
                .when().get(host1)
                .then().log().body().statusCode(200).extract().response();
        List<Integer> criticsPickValues=response.path("results.critics_pick");

        if(criticsPickValues.size()>0){
            for(int i=0;i<criticsPickValues.size();i++){
                if(criticsPickValues.get(i).equals(1)){
                    Assert.fail("Result ID = "+i+" has Critics Pick Mark");
                }
            }
        }
        else {
            Assert.fail("Zero results");
        }
    }

//    @Test
//    public void verifySearchWithQueryWithZeroResults(){}
//
//
//    @Test
//    public void verifySearchViaExistentCriticsName(){}
//
//    @Test
//    public void verifySearchViaNonexistentCriticsName(){}
//
//    @Test
//    public void verifySearchViaSinglePublicationDate(){}
//
//    @Test
//    public void verifySearchViaStartEndPublicationsDates(){}
//
//    @Test
//    public void verifySearchViaSingleOpeningDate(){}
//
//    @Test
//    public void verifySearchViaStartEndOpeningDates(){}
//
//    @Test
//    public void verifySearchWithOrderingByTitle(){}
//
//    @Test
//    public void verifySearchWithOrderingByPublicationDate(){}
//
//    @Test
//    public void verifySearchWithOrderByOpeningDate(){}










}
