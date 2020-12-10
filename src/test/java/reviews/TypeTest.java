package reviews;

import helpersClasses.TypeHelper;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TypeTest {

    private static TypeHelper typeHelper = new TypeHelper();


@BeforeAll
    public static void specificationConfiguration() {
        typeHelper.specificationConfiguration("type");
    }

    @Test
    public void verifyThatReviewsAreSortedCorrectlyByPublicationDate() {

        HashMap<String, Object> queryParams = new HashMap<>();

        queryParams.put("order", "by-publication-date");

        String typeParam = "all";

        Response response = typeHelper.sendRequestWithParams(200, queryParams, typeParam);

        List<String> publicationDates = response.path("results.publication_date");
        List<String> sortedDates = new ArrayList(publicationDates);
        sortedDates.sort(Collections.reverseOrder());
        if (!publicationDates.equals(sortedDates)) {
            System.out.println("Dates of reviews: --");
            for (String publicationDate : publicationDates) {
                System.out.println(publicationDate);
            }
            System.out.println("--------------------------" + "\n"
                    + "Expected sorted dates: --" + "\n");
            for (Object sortedDate : sortedDates) {
                System.out.println(sortedDate);
            }
            Assertions.fail("Reviews are not sorted correctly by _PublicationDate_");
        }

    }

    @Test
    public void verifyThatReviewsAreSortedCorrectlyByOpeningDate() {

        HashMap<String, Object> queryParams = new HashMap<>();

        queryParams.put("order", "by-opening-date");

        String typeParam = "all";

        Response response = typeHelper.sendRequestWithParams(200, queryParams, typeParam);

        List<String> openingDates = response.path("results.opening_date");

        boolean doesListContainOnlyNullValues = true;
        while (doesListContainOnlyNullValues)
            for (int i = 0; i < openingDates.size(); i++) {
                if (openingDates.get(i) != null) {
                    doesListContainOnlyNullValues = false;
                    break;
                } else {
                    queryParams.put("offset", 5000 * (i + 1));
                    openingDates = typeHelper.sendRequestWithParams(200, queryParams, typeParam)
                            .path("results.opening_date");
                }
            }

        List<String> sortedDates = new ArrayList(openingDates);
        sortedDates.sort(Comparator.nullsLast(Comparator.reverseOrder()));
        if (!openingDates.equals(sortedDates)) {
            System.out.println("Dates of reviews: --" + "\n");
            for (String openingDate : openingDates) {
                System.out.println(openingDate);
            }
            System.out.println("--------------------------" + "\n"
                    + "Expected sorted dates: --" + "\n");
            for (Object sortedDate : sortedDates) {
                System.out.println(sortedDate);
            }
            Assertions.fail("Reviews are not sorted correctly by _OpeningDate_");
        }
    }

    @Test
    public void verifyThatReviewsAreSortedCorrectlyByTitle() {

        HashMap<String, Object> queryParams = new HashMap<>();

        queryParams.put("order", "by-title");

        String typeParam = "all";

        Response response = typeHelper.sendRequestWithParams(200, queryParams, typeParam);

        List<String> reviewTitles = response.path("results.display_title");
        List<String> sortedTitles = new ArrayList(reviewTitles);
        sortedTitles.sort(Comparator.nullsLast(Comparator.naturalOrder()));
        if (!reviewTitles.equals(sortedTitles)) {
            System.out.println("Display titles of reviews --" + "\n");
            for (String reviewTitle : reviewTitles) {
                System.out.println(reviewTitle);
            }
            System.out.println("--------------------------" + "\n"
                    + "Expected sorted titles: --" + "\n");
            for (Object sortedTitle : sortedTitles) {
                System.out.println(sortedTitle);
            }
            Assertions.fail("Reviews are not sorted correctly by _DisplayTitle_");
        }
    }

    @Test
    public void verifyThatReviewsAreSortedByPublicationDateWhenNoSortedOrderIsSpecified() {

        String typeParam = "all";

        Response response = typeHelper.sendRequestWithParams(200, new HashMap<>(), typeParam);

        List<String> publicationDates = response.path("results.publication_date");
        List<String> sortedDates = new ArrayList(publicationDates);
        sortedDates.sort(Collections.reverseOrder());
        if (!publicationDates.equals(sortedDates)) {
            System.out.println("Dates of reviews: --");
            for (String publicationDate : publicationDates) {
                System.out.println(publicationDate);
            }
            System.out.println("--------------------------" + "\n"
                    + "Expected sorted dates: --" + "\n");
            for (Object sortedDate : sortedDates) {
                System.out.println(sortedDate);
            }
            Assertions.fail("Reviews are not sorted correctly by _PublicationDate_");
        }
    }

    @Test
    public void verifyThatOnlyCriticsPickReviewsAreRetrievedWhenPickTypeIsEnabled() {

        String typeParam = "picks";

        Response response = typeHelper.sendRequestWithParams(200, new HashMap<>(), typeParam);

        List<Integer> criticsPickValues = response.path("results.critics_pick");
        if (criticsPickValues.size() > 0) {
            for (int i = 0; i < criticsPickValues.size(); i++) {
                if (criticsPickValues.get(i).equals(0)) {
                    Assertions.fail("Result ID = " + i + " has not Critics Pick Mark");
                }
            }
        } else {
            Assertions.fail("Zero results");
        }
    }


}
