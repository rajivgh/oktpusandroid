package com.app.oktpus.constants;

/**
 * Created by remees on 28/6/18.
 */

public class Helper {

    public static String IS_LOGGED = "isLogged";
    public static String USER_ID = "349";
//    public static  String USER_ID = "userId";


    public static String CURRENT_DOMAIN  = "http://mall140.com";
    public static String PROD_DOMAIN  = "https://oktpus.com";

    public static String COUPON_URL = CURRENT_DOMAIN + "/oktpusApi/public/couponcodes";
    public static String CAR_PARTS_URL = CURRENT_DOMAIN + "/oktpusApi/public/getProducts";
    public static String CAR_PARTS_DETAILS_URL = CURRENT_DOMAIN + "/oktpusApi/public/getProductDetails?product_id=";
    public static String CAR_PARTS_ADD_CART_URL = CURRENT_DOMAIN + "/oktpusApi/public/shoppingCart";
    public static String CAR_PARTS_GET_CART_URL = CURRENT_DOMAIN + "/oktpusApi/public/getCartDetails?user_id=";

    public static String SIGNUP_URL = PROD_DOMAIN + "/api/user";
    public static String SIGNIN_URL = PROD_DOMAIN + "/api/user/login";
    public static String paypal_client_id = "AQnQTCwskbXrVBt5kJK9IwOlqwkpH-6Rrj8A7-f_dmAqazUT6zs_j9PToNXsoPmbCTthv02dC4tuzGCL";
    public static String paypal_config_env =  "sandbox";



}
