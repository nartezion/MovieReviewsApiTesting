package reviews;

import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static io.restassured.RestAssured.given;

public class Type {

    String apiKey = "ZLe1z8nwiSov0WiXZdc6bNYkA97W2Ib6";
    String host2 = "https://api.nytimes.com/svc/movies/v2/reviews/{type}.json";


    @Test
    public void verifyThatReviewsAreSortedCorrectlyByPublicationDate() {
        Response response = given().contentType("application/json")
                .pathParam("type", "all")
                .queryParam("order", "by-publication-date")
                .queryParam("api-key", apiKey).log().uri()
                .when().get(host2 + "{type}.json")
                .then().log().body().statusCode(200).extract().response();
        List<String> publicationDates = response.path("results.publication_date");
        List<String> sortedDates = new ArrayList(publicationDates);
        sortedDates.sort(Collections.reverseOrder());
        if (!publicationDates.equals(sortedDates)) {
            System.out.println("Dates of reviews: --");
            for (String publicationDate : publicationDates) {
                System.out.println(publicationDate);
            }
            System.out.println("--------------------------" + "\n"
                    + "Sorted dates: --" + "\n");
            for (Object sortedDate : sortedDates) {
                System.out.println(sortedDate);
            }
            Assert.fail("Reviews are not sorted correctly by _PublicationDate_");
        }

    }

    @Test
    public void verifyThatReviewsAreSortedCorrectlyByOpeningDate() {
        Response response = given().contentType("application/json")
                .pathParam("type", "all")
                .queryParam("order", "by-opening-date")
                .queryParam("api-key", apiKey).log().uri()
                .when().get(host2 + "{type}.json")
                .then().log().body().statusCode(200).extract().response();
        List<String> openingDates = response.path("results.publication_date");
        List<String> sortedDates = new ArrayList(openingDates);
        sortedDates.sort(Collections.reverseOrder());
        if (!openingDates.equals(sortedDates)) {
            System.out.println("Dates of reviews: --" + "\n");
            for (String openingDate : openingDates) {
                System.out.println(openingDate);
            }
            System.out.println("--------------------------" + "\n"
                    + "Sorted dates: --" + "\n");
            for (Object sortedDate : sortedDates) {
                System.out.println(sortedDate);
            }
            Assert.fail("Reviews are not sorted correctly by _OpeningDate_");
        }
    }

    @Test
    public void verifyThatReviewsAreSortedCorrectlyByTitle() {
        Response response = given().contentType("application/json")
                .pathParam("type", "all")
                .queryParam("order", "by-title")
                .queryParam("api-key", apiKey).log().uri()
                .when().get(host2 + "{type}.json")
                .then().log().body().statusCode(200).extract().response();
        List<String> reviewTitles = response.path("results.display_title");
        List<String> sortedTitles = new ArrayList(reviewTitles);
        sortedTitles.sort(Comparator.nullsLast(Comparator.naturalOrder()));
        if (!reviewTitles.equals(sortedTitles)) {
            System.out.println("Display titles of reviews --" + "\n");
            for (String reviewTitle : reviewTitles) {
                System.out.println(reviewTitle);
            }
            System.out.println("--------------------------" + "\n"
                    + "Sorted titles: --" + "\n");
            for (Object sortedTitle : sortedTitles) {
                System.out.println(sortedTitle);
            }
            Assert.fail("Reviews are not sorted correctly by _DisplayTitle_");
        }
    }

    @Test
    public void verifyThatReviewsAreSortedByPublicationDateWhenNoSortedOrderIsSpecified() {
        Response response = given().contentType("application/json")
                .pathParam("type", "all")
                .queryParam("api-key", apiKey).log().uri()
                .when().get(host2 + "{type}.json")
                .then().log().body().statusCode(200).extract().response();
        List<String> publicationDates = response.path("results.publication_date");
        List<String> sortedDates = new ArrayList(publicationDates);
        sortedDates.sort(Collections.reverseOrder());
        if (!publicationDates.equals(sortedDates)) {
            System.out.println("Dates of reviews: --");
            for (String publicationDate : publicationDates) {
                System.out.println(publicationDate);
            }
            System.out.println("--------------------------" + "\n"
                    + "Sorted dates: --" + "\n");
            for (Object sortedDate : sortedDates) {
                System.out.println(sortedDate);
            }
            Assert.fail("Reviews are not sorted correctly by _PublicationDate_");
        }
    }

    @Test
    public void verifyThatOnlyCriticsPickReviewsAreRetrievedWhenPickTypeIsEnabled() {
        Response response = given().contentType("application/json")
                .pathParam("type", "picks")
                .queryParam("api-key", apiKey).log().uri()
                .when().get(host2 + "{type}.json")
                .then().log().body().statusCode(200).extract().response();
        List<Integer> criticsPickValues = response.path("results.critics_pick");
        if (criticsPickValues.size() > 0) {
            for (int i = 0; i < criticsPickValues.size(); i++) {
                if (criticsPickValues.get(i).equals(0)) {
                    Assert.fail("Result ID = " + i + " has not Critics Pick Mark");
                }
            }
        } else {
            Assert.fail("Zero results");
        }
    }


}
