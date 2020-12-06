package helpersClasses;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class SearchHelper extends CommonHelper {

    public Response sendRequestWithOneQueryParam(int expectedStatusCode, String queryParamName, Object paramValue) {

        return given(specificationConfiguration("search")).queryParam(queryParamName, paramValue)
                .when().log().uri().get()
                .then().log().body().
                        statusCode(200).extract().response();
    }


}
