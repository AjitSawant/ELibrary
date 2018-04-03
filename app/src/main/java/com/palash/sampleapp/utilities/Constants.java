package com.palash.sampleapp.utilities;

public class Constants {
    public static final String TAG = "E-Library";
    public static final String KEY_REQUEST_DATA = "data";

    public static final int HTTP_OK_200 = 200;
    public static final int HTTP_CREATED_201 = 201;
    public static final int HTTP_NO_RECORD_FOUND_OK_204 = 204;
    public static final int HTTP_NOT_FOUND_401 = 401;
    public static final int HTTP_NOT_OK_404 = 404;
    public static final int HTTP_NOT_OK_500 = 500;
    public static final int HTTP_NOT_OK_501 = 501;
    public static final int HTTP_AMBIGUOUS_300 = 300;
    public static final int SYNC_STOP = 1001;
    public static String CURRENT_ORDER_TIME_STAMP = "";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String CONVERT_DATE_TIME_FORMAT = "dd MMM yyyy HH:mm:aa";

    // Test link
    public static final String BASE_URL = "http://192.168.1.133/InamdarApp/";

    public static final String STATUS_LOG_IN = "login";
    public static final String STATUS_LOG_OUT = "logout";
    public static final String LOGIN_URL = BASE_URL + "Login/DocLogin";
    public static final String ADD_ITEM_URL = BASE_URL + "Order/AddItem";
    public static final String ADD_ORDER_URL = BASE_URL + "Order/AddOrder";
}
