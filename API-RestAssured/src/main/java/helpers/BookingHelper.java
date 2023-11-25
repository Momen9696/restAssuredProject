package helpers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import models.authRequestModel.AuthRequestModel;
import models.bookingRequestModel.BookingRequestModel;
import models.bookingRequestModel.Bookingdates;
import utils.ConfigManager;
import utils.ExcelUtils;

import static constants.EndPoints.*;

import io.restassured.response.Response;


public class BookingHelper {
    String token;
    //child Excel sheet path
    String bookingDataExcel = ConfigManager.getInstance().getString("bookingExcelDataSheet");
    //excel sheet full path (parent and child)
    public ExcelUtils dataSheet = new ExcelUtils(BASE_EXCEL_PATH + bookingDataExcel, "TestData");

    //excel return function (will be used in test class)
    public ExcelUtils getDataSheet() {
        return dataSheet;
    }

    public BookingHelper() {
        RestAssured.baseURI = ConfigManager.getInstance().getString("baseUrl");
        RestAssured.useRelaxedHTTPSValidation();
        AuthHelper authHelper = new AuthHelper();
        AuthRequestModel authRequestModel = authHelper.authCredentials();
         token = String.valueOf(authHelper.authenticate(authRequestModel));
    }

    int testCaseName = dataSheet.getRowNum("validTestData");

    public Bookingdates getBookingDate(String rowName) {
        testCaseName = dataSheet.getRowNum(rowName);
        Bookingdates bookingdates = new Bookingdates();
        bookingdates.setCheckin(dataSheet.getCellData(testCaseName, dataSheet.getColNumber("checkIn")));
        bookingdates.setCheckout(dataSheet.getCellData(testCaseName, dataSheet.getColNumber("checkOut")));
        return bookingdates;
    }


    public BookingRequestModel requestModel() {
        BookingRequestModel requestModel = new BookingRequestModel();
        requestModel.setFirstname(dataSheet.getCellData(testCaseName, dataSheet.getColNumber("firstName")));
        requestModel.setLastname(dataSheet.getCellData(testCaseName, dataSheet.getColNumber("lastName")));
        //  requestModel.setTotalprice(Integer.valueOf(dataSheet.getCellData(testCaseName,dataSheet.getColNumber("totalPrice"))));
        requestModel.setTotalprice((dataSheet.getCellData(testCaseName, dataSheet.getColNumber("totalPrice"))));
        requestModel.setDepositpaid((dataSheet.getCellData(testCaseName, dataSheet.getColNumber("depositPaid"))));
        requestModel.setBookingdates(getBookingDate("validTestData"));
        requestModel.setAdditionalneeds(dataSheet.getCellData(testCaseName, dataSheet.getColNumber("additionalNeeds")));
        return requestModel;
    }

    public BookingRequestModel emptyRequestModel() {
        BookingRequestModel emptyRequestModel = new BookingRequestModel();
        String emptyRow = "emptyRequestBody";
        int rowName = dataSheet.getRowNum(emptyRow);
        emptyRequestModel.setFirstname(dataSheet.getCellData(rowName, dataSheet.getColNumber("firstName")));
        emptyRequestModel.setLastname(dataSheet.getCellData(rowName, dataSheet.getColNumber("lastName")));
        emptyRequestModel.setTotalprice((dataSheet.getCellData(rowName, dataSheet.getColNumber("totalPrice"))));
        emptyRequestModel.setDepositpaid((dataSheet.getCellData(rowName, dataSheet.getColNumber("depositPaid"))));
        emptyRequestModel.setBookingdates(getBookingDate(emptyRow));
        emptyRequestModel.setAdditionalneeds((dataSheet.getCellData(rowName, dataSheet.getColNumber("additionalNeeds"))));
        return emptyRequestModel;


    }

    public Response bookingSendRequest(BookingRequestModel requestModel) {
        return RestAssured
                .given()
                .log().method().log().uri().log().body()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .when()
                .body(requestModel)
                .post(BOOKING_ENDPOINT)
                .andReturn()
                .then()
                .log().status().log().body()
                .extract()
                .response();
    }

}







