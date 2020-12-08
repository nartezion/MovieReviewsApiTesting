package helpersClasses;

import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class SearchHelper extends CommonHelper {

    public Response sendRequestWithParams(int expectedStatusCode, Map<String, Object> params) {

        return given(specificationConfiguration("search")).queryParams(params)
                .when().log().uri().get()
                .then().log().body().
                        statusCode(200).extract().response();
    }


}
