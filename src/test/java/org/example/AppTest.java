package org.example;

import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static io.restassured.RestAssured.given;


public class AppTest 
{
    String apiKey="ZLe1z8nwiSov0WiXZdc6bNYkA97W2Ib6";
    String host="https://api.nytimes.com/svc/movies/v2/reviews/search.json";

    @Test
    public void verifyThatResultsContainSearchQuery()
    {
        String searchQuery = "Sicario";
        Response response=given().contentType("application/json")
                .when().get(host+"?query="+searchQuery+"&api-key="+apiKey)
                .then().log().body().
                        statusCode(200)
                .extract().response();

        List<String> display_titles=response.path("results.display_title");

        List<String> headlines=response.path("results.headline");
        if(display_titles.size()>0) {
            for (int i = 0; i < display_titles.size(); i++) {
                if (!display_titles.get(i).toLowerCase().contains((searchQuery.toLowerCase())) &&
                        !headlines.get(i).toLowerCase().contains(searchQuery.toLowerCase()))
                {
                    System.out.println(display_titles.get(i));
                    System.out.println(headlines.get(i));
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
                .when().get(host)
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
}
