package helpersClasses;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.gen5.api.Assertions;

public abstract class CommonHelper {

    private String apiKey = "ZLe1z8nwiSov0WiXZdc6bNYkA97W2Ib6";
    private String host = "https://api.nytimes.com/svc/movies/v2";
    private String searchService = "/reviews/search.json";
    private String typeService = "/reviews/{type}.json";
    private String reviewerService = "/critics/";


    public RequestSpecification specificationConfiguration(String serviceName) {

        if (serviceName.equals("search")) {
            return
                    RestAssured.given()
                            .contentType("application/json")
                            .baseUri(this.host)
                            .queryParam("api-key", this.apiKey)
                            .basePath(this.searchService);
        }
        if (serviceName.equals("type")) {
            return RestAssured.given().
                    contentType("application/json").
                    baseUri(this.host).
                    queryParam("api-key", this.apiKey)
                    .basePath(this.typeService);

        }
        if (serviceName.equals("critics")) {
            return RestAssured.given().
                    contentType("application/json").
                    baseUri(this.host).
                    queryParam("api-key", this.apiKey)
                    .basePath(this.reviewerService);
        } else {
            Assertions.fail("Incorrect service is specified");
            return null;
        }

    }
}
