package critics;

import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Reviewer {

    String apiKey="ZLe1z8nwiSov0WiXZdc6bNYkA97W2Ib6";
    String host3="https://api.nytimes.com/svc/movies/v2/critics/";

    @Test
    public void verifyThatOnlyPartTimeCriticsAreRetrievedWhenPartTimeStatusIsEnabled(){

        String reviewerParam = "part-time";

        Response response=given().contentType("application/json")
                .pathParam("reviewer",reviewerParam)
                .queryParam("api-key",this.apiKey).log().uri()
                .when().get(host3+"{reviewer}.json")
                .then().log().body().statusCode(200).extract().response();

        List<String> criticsStatuses = response.path("results.status");

        if(criticsStatuses.size()>0) {
            for (int i = 0; i < criticsStatuses.size(); i++) {
                if (!criticsStatuses.get(i).equals(reviewerParam))
                {
                    Assert.fail("Result ID = " + i + " has not PART_TIME status");
                }
            }
        }
        else {
            Assert.fail("Zero results");
        }
    }

    @Test
    public void verifySearchWithExistedCriticsName(){

        String reviewerParam = "Stephen Holden";

        Response response=given().contentType("application/json")
                .pathParam("reviewer",reviewerParam)
                .queryParam("api-key",this.apiKey).log().uri()
                .when().get(host3+"{reviewer}.json")
                .then().log().body().statusCode(200).extract().response();

        List<String> criticsNames = response.path("results.display_name");
        if(criticsNames.size()>0) {
            for (int i = 0; i < criticsNames.size(); i++) {
                if (!criticsNames.get(i).equals(reviewerParam))
                {
                    Assert.fail("Result ID = " + i + " has not PART_TIME status");
                }
            }
        }
        else {
            Assert.fail("Zero results");
        }
    }

    @Test
    public void verifySearchWithNonexistentCriticsName(){
        String reviewerParam = "Test Name";

        given().contentType("application/json")
                .pathParam("reviewer",reviewerParam)
                .queryParam("api-key",apiKey).log().uri()
                .when().get(host3+"{reviewer}.json")
                .then().statusCode(404).assertThat().body("num_results",equalTo ("0"));

    }

    @Test
    public void verifySendingARequestWithIncorrectApiKey() {
        String  apiKey = "112233";

        String reviewerParam = "full-time";

        given().contentType("application/json")
                .pathParam("reviewer",reviewerParam)
                .queryParam("api-key",apiKey).log().uri()
                .when().get(host3+"{reviewer}.json")
                .then().log().body().statusCode(401).assertThat().body("fault.faultstring"
                ,equalTo("Invalid ApiKey"));

    }


    @Test
    public void verifySendingARequestWithEmptyReviewerParam(){

        String reviewerParam = "";

        given().contentType("application/json")
                .pathParam("reviewer",reviewerParam)
                .queryParam("api-key",this.apiKey).log().uri()
                .when().get(host3+"{reviewer}.json")
                .then().log().body().statusCode(404).assertThat().body("errors[0]"
                ,equalTo("The URL doesn't match any of the supported URLS. " +
                        "Please review the api docs or call the help.json api."));

    }

//    @Test
//    public void verifyServerRestrictionWhenSendingTooManyRequestsPerMinute(){
//        String reviewerParam = "Test Name";
//        for (int i=0; i<10;i++){
//            given().contentType("application/json")
//                    .pathParam("reviewer",reviewerParam)
//                    .queryParam("api-key",apiKey).log().uri()
//                    .when().get(host3+"{reviewer}.json")
//                    .then().statusCode(404);
//        }
//        given().contentType("application/json")
//                .pathParam("reviewer",reviewerParam)
//                .queryParam("api-key",apiKey).log().uri()
//                .when().get(host3+"{reviewer}.json")
//                .then()
//                .statusCode(429);
//    }

}

