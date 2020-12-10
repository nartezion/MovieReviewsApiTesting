package reviews;

import helpersClasses.SearchHelper;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.hamcrest.Matchers.equalTo;

public class SearchTest {

    private static SearchHelper searchHelper = new SearchHelper();

   @BeforeAll
    public static void specificationConfiguration() {
        searchHelper.specificationConfiguration("search");
    }


    @Test
    public void verifySearchWithExistentMovie() {
        String searchQuery = "28 Days Later";

        HashMap<String, Object> params = new HashMap<>();

        params.put("query", searchQuery);

        Response response = searchHelper.sendRequestWithParams
                (200, params);

        List<String> display_titles = response.path("results.display_title");

        List<String> headlines = response.path("results.headline");
        if (display_titles.size() > 0) {
            for (int i = 0; i < display_titles.size(); i++) {
                if (!display_titles.get(i).toLowerCase().contains((searchQuery.toLowerCase())) &&
                        !headlines.get(i).toLowerCase().contains(searchQuery.toLowerCase())) {
                    Assertions.fail("Result ID = " + i + " does not contain search query in title or headline");
                }
            }
        } else {
            Assertions.fail("Zero results");
        }
    }


    @Test
    public void verifyThatOnlyCriticsPickReviewsAreRetrievedWhenOnlyCriticsPickCheckboxIsEnabled() {

        HashMap<String, Object> params = new HashMap<>();

        params.put("critics-pick", "Y");

        Response response = searchHelper.sendRequestWithParams
                (200, params);

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


    @Test
    public void verifySearchWithQueryWithZeroResults() {

        String searchQuery = "testingTitle";

        HashMap<String, Object> params = new HashMap<>();

        params.put("query", searchQuery);

        searchHelper.sendRequestWithParams(200, params)
                .then().assertThat().body("num_results", equalTo(0));
    }


    @Test
    public void verifySearchViaExistentCriticsName() {

        String existentCriticsName = "Ben Kenigsberg";

        HashMap<String, Object> params = new HashMap<>();

        params.put("reviewer", existentCriticsName);

        Response response = searchHelper.sendRequestWithParams
                (200, params);

        List<String> reviewerNames = response.path("results.byline");
        if (reviewerNames.size() > 0) {
            for (int i = 0; i < reviewerNames.size(); i++) {
                if (!reviewerNames.get(i).equals(existentCriticsName)) {
                    Assertions.fail("Result ID - " + i + "" + "has not _" + existentCriticsName + "_ reviewer name");
                }
            }
        } else {
            Assertions.fail("Zero results");
        }
    }

    @Test
    public void verifySearchViaNonexistentCriticsName() {
        String nonExistentCriticsName = "testing name";

        HashMap<String, Object> params = new HashMap<>();

        params.put("reviewer", nonExistentCriticsName);

        searchHelper.sendRequestWithParams
                (200, params)
                .then().assertThat().body("num_results", equalTo(0));
    }

    @Test
    public void verifySearchViaSinglePublicationDate() {
        String publicationDate = "2020-12-04";

        HashMap<String, Object> params = new HashMap<>();

        params.put("publication-date", publicationDate);

        Response response = searchHelper.sendRequestWithParams
                (200, params);

        List<String> publicationDates = response.path("results.publication_date");
        if (publicationDates.size() > 0) {
            for (int i = 0; i < publicationDates.size(); i++) {
                if (!publicationDates.get(i).equals(publicationDate)) {
                    Assertions.fail("Result ID - " + i + "  is not published on _" + publicationDate);
                }
            }
        } else {
            Assertions.fail("Zero results");
        }
    }

    @Test
    public void verifySearchViaStartEndPublicationsDates() throws ParseException {
        String startDate = "2010-01-01";
        String endDate = "2012-01-01";
        String startEndDate = startDate + ";" + endDate;

        HashMap<String, Object> params = new HashMap<>();

        params.put("publication-date", startEndDate);

        Response response = searchHelper.sendRequestWithParams
                (200, params);

        List<String> publicationDates = response.path("results.publication_date");
        if (publicationDates.size() > 0) {
            List<Date> dates = new ArrayList<>();
            for (int i = 0; i < publicationDates.size(); i++) {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(publicationDates.get(i));
                dates.add(date);
            }
            try {
                Date startDateFormatted = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
                Date endDateFormatted = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
                for (int i = 0; i < dates.size(); i++) {
                    if (dates.get(i).after(startDateFormatted) && dates.get(i).before(endDateFormatted)) {
                    } else {
                        Assertions.fail("Results ID - " + i + ": publication date is not in date range: from "
                                + startDate + " to " + endDate);
                    }
                }
            } catch (ParseException error) {
                Assertions.fail("ParseException error");
            }
        } else {
            Assertions.fail("Zero results");
        }

    }

    @Test
    public void verifySearchViaSingleOpeningDate() {
        String openingDate = "2020-12-04";

        HashMap<String, Object> params = new HashMap<>();

        params.put("opening-date", openingDate);

        Response response = searchHelper.sendRequestWithParams
                (200, params);

        List<String> openingDates = response.path("results.opening_date");
        if (openingDates.size() > 0) {
            for (int i = 0; i < openingDates.size(); i++) {
                if (!openingDates.get(i).equals(openingDate)) {
                    Assertions.fail("Result ID - " + i + "  is not opened on _" + openingDate);
                }
            }
        } else {
            Assertions.fail("Zero results");
        }
    }

    @Test
    public void verifySearchViaStartEndOpeningDates() throws ParseException {
        String startDate = "2011-01-01";
        String endDate = "2012-01-01";
        String startEndDate = startDate + ";" + endDate;

        HashMap<String, Object> params = new HashMap<>();

        params.put("opening-date", startEndDate);

        Response response = searchHelper.sendRequestWithParams
                (200, params);

        List<String> openingDates = response.path("results.opening_date");
        if (openingDates.size() > 0) {
            List<Date> dates = new ArrayList<>();
            for (int i = 0; i < openingDates.size(); i++) {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(openingDates.get(i));
                dates.add(date);
            }
            try {
                Date startDateFormatted = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
                Date endDateFormatted = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
                for (int i = 0; i < dates.size(); i++) {
                    if (dates.get(i).after(startDateFormatted) && dates.get(i).before(endDateFormatted)) {
                    } else {
                        Assertions.fail("Results ID - " + i + ": opening date is not in date range: from "
                                + startDate + " to " + endDate);
                    }
                }
            } catch (ParseException error) {
                Assertions.fail("ParseException error");
            }
        } else {
            Assertions.fail("Zero results");
        }
    }

    @Test
    public void verifySearchWithOrderingByTitle() {

        String sortedParam = "by-title";

        HashMap<String, Object> params = new HashMap<>();

        params.put("order", sortedParam);

        Response response = searchHelper.sendRequestWithParams(200, params);

        List<String> reviewTitles = response.path("results.display_title");

        if (reviewTitles.size() > 0) {

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
                Assertions.fail("Reviews are not sorted correctly by _DisplayTitle_");
            }
        } else {
            Assertions.fail("Zero results");
        }
    }

    @Test
    public void verifySearchWithOrderingByPublicationDate() {

        String sortedParam = "by-publication-date";

        HashMap<String, Object> params = new HashMap<>();

        params.put("order", sortedParam);

        Response response = searchHelper.sendRequestWithParams(200, params);

        List<String> publicationDates = response.path("results.publication_date");
        if (publicationDates.size() > 0) {
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
        } else {
            Assertions.fail("Zero results");
        }
    }

    @Test
    public void verifySearchWithOrderByOpeningDate() {
        String sortedParam = "by-opening-date";
        String reviewer = "Special to The New York Times.";

        HashMap<String, Object> params = new HashMap<>();

        params.put("reviewer", reviewer);

        params.put("order", sortedParam);

        Response response = searchHelper.sendRequestWithParams(200, params);

        List<String> openingDates = response.path("results.opening_date");
        if (openingDates.size() > 0) {
            List<String> sortedDates = new ArrayList(openingDates);
            sortedDates.sort(Comparator.nullsLast(Comparator.reverseOrder()));
            if (!openingDates.equals(sortedDates)) {
                System.out.println("Dates of reviews: --");
                for (String publicationDate : openingDates) {
                    System.out.println(publicationDate);
                }
                System.out.println("--------------------------" + "\n"
                        + "Expected sorted dates: --" + "\n");
                for (Object sortedDate : sortedDates) {
                    System.out.println(sortedDate);
                }
                Assertions.fail("Reviews are not sorted correctly by _OpeningDate_");
            }
        } else {
            Assertions.fail("Zero results");
        }
    }

}
