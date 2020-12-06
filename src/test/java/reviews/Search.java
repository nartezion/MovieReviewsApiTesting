package reviews;

import helpersClasses.SearchHelper;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;

public class Search {

    public static SearchHelper searchHelper = new SearchHelper();

    @BeforeClass
    public static void specificationConfiguration() {
        searchHelper.specificationConfiguration("search");
    }


    @Test
    public void verifySearchWithExistentMovie() {
        String searchQuery = "28 days later";
        Response response = searchHelper.sendRequestWithOneQueryParam
                (200, "query", searchQuery);

        List<String> display_titles = response.path("results.display_title");

        List<String> headlines = response.path("results.headline");
        if (display_titles.size() > 0) {
            for (int i = 0; i < display_titles.size(); i++) {
                if (!display_titles.get(i).toLowerCase().contains((searchQuery.toLowerCase())) &&
                        !headlines.get(i).toLowerCase().contains(searchQuery.toLowerCase())) {
                    Assert.fail("Result ID = " + i + " does not contain search query in title or headline");
                }
            }
        } else {
            Assert.fail("Zero results");
        }
    }


    @Test
    public void verifyThatOnlyCriticsPickReviewsAreRetrievedWhenOnlyCriticsPickCheckboxIsEnabled() {
        Response response = searchHelper.sendRequestWithOneQueryParam
                (200, "critics-pick", "Y");

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


    @Test
    public void verifySearchWithQueryWithZeroResults() {

        String searchQuery = "testingTitle";

        searchHelper.sendRequestWithOneQueryParam(200, "query", searchQuery)
                .then().assertThat().body("num_results", equalTo(0));
    }


    @Test
    public void verifySearchViaExistentCriticsName() {

        String existentCriticsName = "Ben Kenigsberg";

        Response response = searchHelper.sendRequestWithOneQueryParam
                (200, "reviewer", existentCriticsName);

        List<String> reviewerNames = response.path("results.byline");
        if (reviewerNames.size() > 0) {
            for (int i = 0; i < reviewerNames.size(); i++) {
                if (!reviewerNames.get(i).equals(existentCriticsName)) {
                    Assert.fail("Result ID - " + i + "" + "has not _" + existentCriticsName + "_ reviewer name");
                }
            }
        } else {
            Assert.fail("Zero results");
        }
    }

    @Test
    public void verifySearchViaNonexistentCriticsName() {
        String existentCriticsName = "testing name";

        searchHelper.sendRequestWithOneQueryParam
                (200, "reviewer", existentCriticsName)
                .then().assertThat().body("num_results", equalTo(0));
    }

    @Test
    public void verifySearchViaSinglePublicationDate() {
        String publicationDate = "2020-12-04";

        Response response = searchHelper.sendRequestWithOneQueryParam
                (200, "publication-date", publicationDate);

        List<String> publicationDates = response.path("results.publication_date");
        if (publicationDates.size() > 0) {
            for (int i = 0; i < publicationDates.size(); i++) {
                if (!publicationDates.get(i).equals(publicationDate)) {
                    Assert.fail("Result ID - " + i + "  is not published on _" + publicationDate);
                }
            }
        } else {
            Assert.fail("Zero results");
        }
    }

    @Test
    public void verifySearchViaStartEndPublicationsDates() throws ParseException {
        String startDate = "2010-01-01";
        String endDate = "2012-01-01";
        String startEndDate = startDate + ";" + endDate;

        Response response = searchHelper.sendRequestWithOneQueryParam
                (200, "publication-date", startEndDate);

        List<String> publicationDates = response.path("results.publication_date");
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
                    Assert.fail("Results ID - " + i + ": publication date is not in date range: from "
                            + startDate + " to " + endDate);
                }
            }
        } catch (ParseException error) {
            Assert.fail("ParseException error");
        }

    }

    @Test
    public void verifySearchViaSingleOpeningDate() {
    }

    @Test
    public void verifySearchViaStartEndOpeningDates() {
    }

    @Test
    public void verifySearchWithOrderingByTitle() {
    }

    @Test
    public void verifySearchWithOrderingByPublicationDate() {
    }

    @Test
    public void verifySearchWithOrderByOpeningDate() {
    }


}
