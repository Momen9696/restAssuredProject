package constants;

import utils.ConfigManager;

public interface EndPoints {
String BOOKING_ENDPOINT = "/booking";

String AUTH_END_POINT = "/auth";
    String BASE_EXCEL_PATH = ConfigManager.getInstance().getString("baseExcelPath");
}
