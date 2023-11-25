package helpers;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.authRequestModel.AuthRequestModel;
import utils.ConfigManager;
import static constants.EndPoints.AUTH_END_POINT;


public class AuthHelper {
    static String token;
    public AuthRequestModel authCredentials(){
        AuthRequestModel authRequestModel = new AuthRequestModel();
        authRequestModel.setUsername(USERNAME);
        authRequestModel.setPassword(PASSWORD);
        return authRequestModel;
    }
    /**
     * Used in authentication request
     */
    TokenEncryptionUtility tokenEncryptionUtility=new TokenEncryptionUtility();
    /**
     * Used in authentication request
     */
    private final String USERNAME = ConfigManager.getInstance().getString("integrationUserName");

    private String PASSWORD = ConfigManager.getInstance().getString("integrationPassword");
    /**
    {
        try {
            PASSWORD = tokenEncryptionUtility.decrypt(ConfigManager.getInstance().getString("integrationPassword"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Set base URL
     */
    public AuthHelper() {
        RestAssured.baseURI = ConfigManager.getInstance().getString("baseUrl");
        RestAssured.useRelaxedHTTPSValidation();
    }

    /**
     * Send auth request using username and password
     *
     * @return the auth token to be used in other requests headers
     */
    public Response authenticate(AuthRequestModel authRequestModel) {
        Response response = RestAssured
                .given().log().method().log().uri()
                .contentType(ContentType.JSON)
                .when()
                .body(authRequestModel)
                .get(AUTH_END_POINT);
        return response;
    }

    /**
     * @return expired token to be used to reset password
     * using Change Authentication Password API
     */
    public String resetPasswordToken() {
        Response response = RestAssured
                .given().log().method().log().uri().log().body()
                .auth()
                .preemptive()
                .basic(USERNAME, PASSWORD)
                .get(AUTH_END_POINT);
        return response.jsonPath().getString("token");
    }
}
