package helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Field;


import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class FunctionsHelper {

    public String getValueFromResponseBody(Response response,String fieldName){
        responseCode200Assertion(response);
        return response.jsonPath().getString(fieldName);
    }

    public void responseCode200Assertion(Response response){
        assertEquals(response.getStatusCode(), HttpStatus.SC_OK, "Response status code should be 200 instead of "+response.getStatusCode());//message includes the expected result
    }
    public void responseCode400Assertion(Response response){
        assertEquals(response.getStatusCode(), HttpStatus.SC_BAD_REQUEST, "Response status code should be 400 instead of "+response.getStatusCode());//message includes the expected result
    }
    public void responseCode404Assertion(Response response){
        assertEquals(response.getStatusCode(), HttpStatus.SC_NOT_FOUND, "Response status code should be 404 instead of "+response.getStatusCode());//message includes the expected result
    }
    public void responseCode405Assertion(Response response){
        assertEquals(response.getStatusCode(), HttpStatus.SC_METHOD_NOT_ALLOWED, "Response status code should be 405 instead of "+response.getStatusCode());//message includes the expected result
    }
    public void assertResponseBodyContainsSingleField(Response response,SoftAssert softAssert,String fieldName){
        softAssert.assertTrue(response.toString().contains(fieldName),"Response should contain field " + fieldName);
    }
    public <T> void validateResponseContainFields(Response response,Class<T> responseModelClass) {
        //mapping model fields name and storing it in a list of strings
        List<String> fields = Arrays.stream(responseModelClass.getDeclaredFields()).map(Field::getName).toList();
        String responseString = response.body().asString();
        // Check if any field is not exist
        for (String field : fields) {
            assertTrue(responseString.contains(field), "Response shall  contain " + field);

        }
    }

    public <T> void validateResponseUsingTreeObjectMapper(Response response, Class<T> responseModelClass) throws JsonProcessingException {
        // Deserialize the JSON response into your POJO class using ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode actualResponse = objectMapper.readTree(response.getBody().asString());
        List<String> fields = Arrays.stream(responseModelClass.getDeclaredFields())
                .map(Field::getName)
                .toList();
        for (int i = 0; i < actualResponse.size(); i++) {
            JsonNode innerNode = actualResponse.get(i);
            for (String field : fields) {
                Assert.assertTrue(innerNode.has(field), "Field '" + field + "' is missing in the API response");

            }
        }
    }

    public <T> void validateResponseUsingTreeAndAssertFieldsNotNull(Response response, Class<T> responseModelClass) throws JsonProcessingException {
        // Deserialize the JSON response into your POJO class using ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode actualResponse = objectMapper.readTree(response.getBody().asString());
        List<String> fields = Arrays.stream(responseModelClass.getDeclaredFields())
                .map(Field::getName)
                .toList();
        for (int i = 0; i < actualResponse.size(); i++) {
            JsonNode innerNode = actualResponse.get(i);
            for (String field : fields) {
                Assert.assertTrue(innerNode.has(field), "Field '" + field + "' is missing in the API response");

                if (innerNode.has(field)) {
                    JsonNode fieldNode = innerNode.get(field);
                    Assert.assertNotNull(fieldNode, "Field '" + field + "' contains a null value");
                    Assert.assertFalse(fieldNode.isNull(), "Field '" + field + "' contains a null value");
                }
            }
        }
    }


}


