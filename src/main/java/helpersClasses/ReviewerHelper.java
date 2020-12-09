package helpersClasses;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ReviewerHelper extends CommonHelper {

    public Response sendRequestWithParams(int expectedStatusCode, String pathParam){
        return given(specificationConfiguration("critics"))
                .pathParam("reviewer",pathParam)
                .log().uri()
                .when().get()
                .then().log().body().statusCode(expectedStatusCode).extract().response();
    }

}
