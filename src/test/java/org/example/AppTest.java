package org.example;

import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static io.restassured.RestAssured.given;


public class AppTest 
{
    String apiKey="ZLe1z8nwiSov0WiXZdc6bNYkA97W2Ib6";
    String host1="https://api.nytimes.com/svc/movies/v2/reviews/search.json";
    String host2="https://api.nytimes.com/svc/movies/v2/reviews/";

    @Test
    public void verifyThatResultsContainSearchQuery()
    {
        String searchQuery = "Sicario";
        Response response=given().contentType("application/json")
                .when().get(host1+"?query="+searchQuery+"&api-key="+apiKey)
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
    public void verifyThatReviewsAreOrderedByPublicationDateCorrectly(){
        Response response=given().contentType("application/json")
                .pathParam("type","all")
                .queryParam("order","by-publication-date")
                .queryParam("api-key",apiKey).log().uri()
                .when().get(host2+"{type}.json")
                .then().log().body().statusCode(200).extract().response();
        List<String> publicationDates=response.path("results.publication_date");
        ArrayList sortedDates=new ArrayList(publicationDates);
        Collections.sort(sortedDates,Collections.reverseOrder());
        if(!publicationDates.equals(sortedDates)){
            System.out.println("Dates of reviews: --");
            for (String publicationDate : publicationDates) {
                System.out.println(publicationDate);
            }
            System.out.println("--------------------------"+"\n"
                    +"Sorted dates: --"+"\n");
            for (Object sortedDate : sortedDates) {
                System.out.println(sortedDate);
            }
            Assert.fail("Reviews are not sorted correctly by _PublicationDate_");
        }

    }

    @Test
    public void verifyThatReviewsAreSortedCorrectlyByOpeningDate(){
        Response response=given().contentType("application/json")
                .pathParam("type","all")
                .queryParam("order","by-opening-date")
                .queryParam("api-key",apiKey).log().uri()
                .when().get(host2+"{type}.json")
                .then().log().body().statusCode(200).extract().response();
        List<String> openingDates =response.path("results.publication_date");
        ArrayList sortedDates=new ArrayList(openingDates);
        Collections.sort(sortedDates,Collections.reverseOrder());
        if(!openingDates.equals(sortedDates)){
            System.out.println("Dates of reviews: --"+"\n");
            for (String openingDate : openingDates) {
                System.out.println(openingDate);
            }
            System.out.println("--------------------------"+"\n"
                                +"Sorted dates: --"+"\n");
            for (Object sortedDate : sortedDates) {
                System.out.println(sortedDate);
            }
            Assert.fail("Reviews are not sorted correctly by _OpeningDate_");
        }
    }

    @Test
    public void verifyThatReviewsAreSortedCorrectlyByTitle(){
        Response response=given().contentType("application/json")
                .pathParam("type","all")
                .queryParam("order","by-title")
                .queryParam("api-key",apiKey).log().uri()
                .when().get(host2+"{type}.json")
                .then().log().body().statusCode(200).extract().response();
        List <String> reviewTitles=response.path("results.display_title");
        ArrayList sortedTitles=new ArrayList(reviewTitles);
        sortedTitles.sort(Comparator.nullsLast(Comparator.naturalOrder()));
        if(!reviewTitles.equals(sortedTitles)){
            System.out.println("Display titles of reviews --"+"\n");
            for (String reviewTitle : reviewTitles) {
                System.out.println(reviewTitle);
            }
            System.out.println("--------------------------"+"\n"
                    +"Sorted dates: --"+"\n");
            for (Object sortedTitle : sortedTitles) {
                System.out.println(sortedTitle);
            }
            Assert.fail("Reviews are not sorted correctly by _DisplayTitle_");
        }
    }

}
