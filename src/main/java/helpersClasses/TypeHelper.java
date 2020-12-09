package helpersClasses;

import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class TypeHelper extends CommonHelper {


    public Response sendRequestWithParams(int expectedStatusCode, Map<String, Object> queryParams,String pathParam) {
        return given(specificationConfiguration("type")).queryParams(queryParams)
                .pathParam("type",pathParam)
                .when().log().uri().get()
                .then().log().body().
                        statusCode(expectedStatusCode).extract().response();
    }


}
