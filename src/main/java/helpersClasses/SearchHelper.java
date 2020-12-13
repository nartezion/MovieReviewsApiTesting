package helpersClasses;

import io.restassured.response.Response;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.Assertions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class SearchHelper extends CommonHelper {

    public Response sendRequestWithParams(int expectedStatusCode, Map<String, Object> params) {

        return given(specificationConfiguration("search")).queryParams(params)
                .when().log().uri().get()
                .then().log().body().
                        statusCode(expectedStatusCode).extract().response();
    }

    public void validateJSON(String response) throws FileNotFoundException {
        try {
            JSONTokener schemaData = new JSONTokener(new FileInputStream("src/main/java/resources/searchSchema"));
            JSONObject jsonSchema = new JSONObject(schemaData);

            JSONObject jsonResponse = new JSONObject(response);

            Schema schemaValidator = SchemaLoader.load(jsonSchema);
            schemaValidator.validate(jsonResponse);

        } catch (FileNotFoundException schemaFileNotFound){
            Assertions.fail("JSON schema validation file cannot be found!");
        }


    }


}
