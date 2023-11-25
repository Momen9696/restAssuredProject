package testPackage;

import helpers.AuthHelper;
import helpers.BookingHelper;
import helpers.FunctionsHelper;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import jdk.jfr.Description;
import models.authRequestModel.AuthRequestModel;
import models.bookingRequestModel.BookingRequestModel;
import models.bookingResponseModel.BookingResponseModel;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.ExcelUtils;

public class BookingTest {
    BookingHelper bookingHelper;
    Response response;
    ExcelUtils testDataSheet;
    BookingRequestModel bookingRequestModel;
    SoftAssert softAssert;
    AuthRequestModel authRequestModel;
    AuthHelper authHelper;
    Response authResponse;
    FunctionsHelper functions;
    BookingResponseModel responseModel;

    @BeforeClass
    public void init() {
        responseModel = new BookingResponseModel();
        functions = new FunctionsHelper();
        authHelper = new AuthHelper();
        authRequestModel = authHelper.authCredentials();
        authResponse = authHelper.authenticate(authRequestModel);
        bookingHelper = new BookingHelper();
        testDataSheet = bookingHelper.getDataSheet();
        bookingRequestModel = bookingHelper.requestModel();
        response = bookingHelper.bookingSendRequest(bookingRequestModel);
        softAssert = new SoftAssert();
    }


    @Test(groups = {"smoke_tests", "valid_tests", "full_regression_tests", "happy_scenarios"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate that response is 200 when using valid data")
    public void validAuthTest() {
        functions.responseCode200Assertion(authResponse);
    }
    @Test(groups = {"smoke_tests", "valid_tests", "full_regression_tests", "happy_scenarios"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate that response is 200 when using valid data")
    public void validRequestUsingValidBodyDataWithAdditionalNeeds() {
        functions.responseCode200Assertion(response);
        System.out.println(response.jsonPath().getString("bookingid"));
    }


    @Test(groups = {"smoke_tests", "valid_tests", "full_regression_tests", "happy_scenarios"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate that response is 200 when using valid data")
    public void validRequestUsingValidBodyDataWithoutAdditionalNeeds() {
        bookingRequestModel.setAdditionalneeds(testDataSheet.getCellData(testDataSheet.getRowNum("validTestDataNoAdditionalNeeds"), testDataSheet.getColNumber("additionalNeeds") ));
        functions.getValueFromResponseBody(response,"bookingid");
    }

    @Test(groups = {"smoke_tests", "invalid_tests", "unknown_issues", "validations_scenarios"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate that response is 400 when using valid data")
    public void emptyFirstName() {
        bookingRequestModel.setFirstname(testDataSheet.getCellData(testDataSheet.getRowNum("emptyFirstName"), testDataSheet.getColNumber("firstName")));
        response = bookingHelper.bookingSendRequest(bookingRequestModel);
        functions.responseCode400Assertion(response);
    }
    @Test(groups = { "invalid_tests", "unknown_issues", "Negative_scenarios"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate that response is 400 when using valid data")
    public void emptySecondName() {
        bookingRequestModel.setLastname(testDataSheet.getCellData(testDataSheet.getRowNum("emptySecondName"), testDataSheet.getColNumber("lastName")));
        response = bookingHelper.bookingSendRequest(bookingRequestModel);
        functions.responseCode400Assertion(response);
    }
    @Test(groups = { "invalid_tests", "unknown_issues", "Negative_scenarios"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate that response is 400 when using valid data")
    public void invalidTotalPrice() {
        bookingRequestModel.setTotalprice((testDataSheet.getCellData(testDataSheet.getRowNum("invalidTotalPrice"), testDataSheet.getColNumber("totalPrice"))));
        response = bookingHelper.bookingSendRequest(bookingRequestModel);
        functions.responseCode400Assertion(response);
    }

    //invalid test case
    @Test(groups = { "invalid_tests", "unknown_issues", "Negative_scenarios"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate that response is 400 when using valid data")
    public void emptyDepositPaid() {
        bookingRequestModel.setDepositpaid((testDataSheet.getCellData(testDataSheet.getRowNum("emptyDepositPaid"), testDataSheet.getColNumber("depositPaid"))));
        response = bookingHelper.bookingSendRequest(bookingRequestModel);
        functions.responseCode400Assertion(response);
    }
    @Test(groups = { "invalid_tests","Negative_scenarios"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate that response is 400 when using booking date in past")
    public void pastCheckInDate() {
        // bookingRequestModel.setCheckin(testDataSheet.getCellData(testDataSheet.getRowNum("pastCheckIn"), testDataSheet.getColNumber("checkIn")));;
        //        bookings.setCheckout(bookingHelper.getBookingDate().getCheckout());
        bookingRequestModel.getBookingdates().setCheckin(testDataSheet.getCellData(testDataSheet.getRowNum("pastCheckIn"), testDataSheet.getColNumber("checkIn")));
        response = bookingHelper.bookingSendRequest(bookingRequestModel);
        functions.responseCode400Assertion(response);
    }
    @Test(groups = { "invalid_tests","Negative_scenarios"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate that response is 400 when using booking date in past")
    public void pastCheckOutDate() {
        bookingRequestModel.getBookingdates().setCheckout(testDataSheet.getCellData(testDataSheet.getRowNum("pastCheckOut"), testDataSheet.getColNumber("checkOut")));
        response = bookingHelper.bookingSendRequest(bookingRequestModel);
        functions.responseCode400Assertion(response);
    }
    @Test(groups = { "invalid_tests","Negative_scenarios"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate that response is 400 when using booking date in past")
    public void emptyRequestBody() {
        bookingRequestModel = bookingHelper.emptyRequestModel();
        response = bookingHelper.bookingSendRequest(bookingRequestModel);
        functions.responseCode400Assertion(response);
    }

}

