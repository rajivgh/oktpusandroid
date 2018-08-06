package com.app.oktpus.constants;

import android.util.Log;

import com.app.oktpus.utils.SessionManagement;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.app.oktpus.constants.Flags.CarInsurance.Keys.DRIVER_ACCIDENTS_TICKET_CLAIM;
import static com.app.oktpus.constants.Flags.CarInsurance.Keys.DRIVER_ANY_OTHER;
import static com.app.oktpus.constants.Flags.CarInsurance.Keys.DRIVER_CREDIT_SCORE;
import static com.app.oktpus.constants.Flags.CarInsurance.Keys.DRIVER_CURRENT_CAR_COMPANY;
import static com.app.oktpus.constants.Flags.CarInsurance.Keys.DRIVER_GENDER;
import static com.app.oktpus.constants.Flags.CarInsurance.Keys.DRIVER_HIGHEST_EDUCATION_LEVEL;
import static com.app.oktpus.constants.Flags.CarInsurance.Keys.DRIVER_HOUSE_OWNERSHIP;
import static com.app.oktpus.constants.Flags.CarInsurance.Keys.DRIVER_INSURANCE_PERIOD;
import static com.app.oktpus.constants.Flags.CarInsurance.Keys.DRIVER_MARITAL_STATUS;
import static com.app.oktpus.constants.Flags.CarInsurance.Keys.HAVE_INSURANCE;
import static com.app.oktpus.constants.Flags.CarInsurance.Keys.VEHICLE_MAKE;
import static com.app.oktpus.constants.Flags.CarInsurance.Keys.VEHICLE_MILEAGE_COVERED_PER_YEAR;
import static com.app.oktpus.constants.Flags.CarInsurance.Keys.VEHICLE_MODEL;
import static com.app.oktpus.constants.Flags.CarInsurance.Keys.VEHICLE_OWNERSHIP;
import static com.app.oktpus.constants.Flags.CarInsurance.Keys.VEHICLE_PRIMARY_PARKING;
import static com.app.oktpus.constants.Flags.CarInsurance.Keys.VEHICLE_PRIMARY_USAGE;
import static com.app.oktpus.constants.Flags.CarInsurance.Keys.VEHICLE_YEAR;
import static com.app.oktpus.constants.Flags.URL.GET_MODEL_LIST;
import static com.app.oktpus.constants.Flags.URL.MODEL_URL_GROUP_CONSTRAINT;

/**
 * Created by Gyandeep on 27/9/16.
 */

public final class Flags {

    /*
    *Check-list
    * 1) Change Release-mode
    * 2) Change HOST
    * 3) Build variant to release
    *
    * */

    public static final float bgOpacity = 0.15f;
    public static final String IS_LOC_UPDATE_REQUESTED = "isLocUpdateNeeded";
    public static final int REQ_CODE_SPEECH_INPUT = 100;
    public static final int REQ_CODE_LOCATION = 101;
    public static final int releaseMode = ReleaseMode.PRODUCTION;
    public static final String WMCW_AMOUNT = "2.99";

    public static final class ActivityTag {
        public static final String
                MAIN_ACTIVITY           = "MainActivity",
                LOCATION_ACTIVITY       = "LocationActivity",
                LOGIN_ACTIVITY          = "LoginActivity",
                SEARCH_ACTIVITY         = "SearchActivity",
                SEARCH_RESULT           = "SearchResult",
                SEARCH_FORM_ACTIVITY    = "SearchFormActivity",
                SAVED_SEARCH_ACTIVITY   = "SavedSearchActivity",
                WMCW                    = "wmcwActivity",
                USER_SETTINGS           = "UserSettings",
                NOTIFICATIONS_HISTORY   = "NotificationHistory",
                GARAGE                  = "GarageActivity",
                CAR_FINANCE             = "CarFinanceActivity",
                CAR_INSURANCE           = "CarInsuranceActivity";
    }

    public static final class FragmentTag {
        public static final int
                SEARCH_FORM             = 0,
                SEARCH_RESULT           = 1;
    }

    public static final class ReleaseMode {
        public static final int
                PRODUCTION          = 1,
                STAGING             = 2;
    }

    public static final class URL {

        public static final String
                //HOST                    = "http://142.4.215.210",
                HOST                    = "https://oktpus.com",//"",////:11080

        //User APIs
        LOGIN                   = HOST + "/api/user/login",
                API_USER                = HOST + "/api/user",
                FORGOT_PWD              = HOST + "/api/user/forgot_password",
                GET_USER                = HOST + "/api/user/",                  //GET
                UPDATE_USER             = HOST + "/api/user/",               //POST
                LOGOUT                  = HOST + "/api/user/logout",

        //Search APIs
        SEARCH_CREATE           = HOST + "/api/search?domain_id=1",
                UPDATE_SAVED_SEARCH     = HOST + "/api/search/",
                GET_SEARCH              = HOST + "/api/search/",
                POST_DELETE_SEARCH_ITEM = HOST + "/search/update/status",
                GET_SEARCH_LIST         = HOST + "/api/search?domain_id=1",
                GET_NOTIFICATION_LIST   = HOST + "/api/notification?domain_id=1",
                GET_GARAGE              = HOST + "/api/garage",

        POST_FINANCE            = HOST + "/api/financing_request?domain_id=1" ,
                POST_INSURANCE          = HOST + "/api/insurance_request?domain_id=1" ,

        GARAGE_ACTION_PREFIX    = HOST + "/api/user/",
                GARAGE_ACTION_SUFFIX    = "/product/flag",

        //Items
        ITEM_GETCOUNT           = HOST + "/api/item/count",
                GET_ITEM_LIST           = HOST + "/api/item?domain_id=1",
                ITEM_GET_ATTRIBUTE_VALUE= HOST + "/api/attribute?domain_id=1",
                GET_MODEL_LIST          = HOST + "/get/1/attribute/4/by/attribute_values/",
                MODEL_URL_GROUP_CONSTRAINT = "/groups/",
                CONFIG                  = "/config?domain_id=1",

        WMCW                    = HOST + "/api/market_average_request?domain_id=1",

        CITY_BY_LOCATION        = HOST + "/api/attribute?domain_id=1",

        TERMS                   = HOST + "/terms",

        API_ITEM_DETAILS        = HOST + "/api/item/",
                GET_CAR_PARTS           = HOST + "/api/external_link?type_id=1&product_id=",
                GET_CAR_REVIEW          = HOST + "/api/external_link?type_id=2&product_id=",
                GET_CAR_RECALL          = HOST + "/api/external_link?type_id=3&product_id="
                        ;

        public static final String
                PAGE                    = "&page=",
                PER_PAGE                = "&per_page=",
                DOMAIN_ID               = "?domain_id=1",
                ATTRIBUTE_CITY          = "?domain_id=1&attributes=city",
                ATTRIBUTE_MAKE          = "?domain_id=1&attributes=make",
                ATTRIBUTE_MODEL         = "?domain_id=1&attributes=model",
                ATTRIBUTE_BODY_TRANS_COLOUR = "?domain_id=1&attributes=body,transmission,colour,doors,drivetrain,user_type";
    }

    public static final class WMCWRequestParams {
        public static final String
                CAR_MAKE        = "car[make]",
                CAR_MODEL       = "car[model]",
                CAR_YEAR        = "car[year]",
                CAR_TRANSMISSION= "car[transmission]",
                CAR_KILOMETERS  = "car[kilometers]",
                CAR_KMS_TYPE    = "car[kilometers][type]",
                PERSON_EMAIL    = "person[email]",
                PERSON_CITY     = "person[city]",
                PERSON_STATE    = "person[state]",
                PERSON_COUNTRY  = "person[country]",
                PERSON_FNAME    = "person[first_name]",
                PERSON_LNAME    = "person[last_name]",
                PERSON_TXN_ID   = "person[transaction_id]";
    }

    public static final class NavMenu {

        public static final class NOT_LOGGED_IN {
            public static final int
                    //Not logged in
                    //PROFILE                 = 0,
                    SIGNUP                  = 1,
                    LOGIN                   = 2,
                    SEARCH                  = 3,
                    WALL_OF_DEALS           = 4,
                    COMPARE_CARS            = 5,
                    CAR_INSURANCE           = 6,
                    CAR_FINANCE             = 7,
                    WMCW                    = 8,
                    CAR_PARTS               = 9,
                    COUPON                  = 10,
                    ABOUT                   = 11;

        }

        public static final class LOGGED_IN {
            public static final int
                    //Logged In
                    SEARCH                  = 1,
                    WALL_OF_DEALS           = 2,
                    SAVED_SEARCHES          = 3,
                    NOTIFICATION_HISTORY    = 4,
                    GARAGE                  = 5,
                    COMPARE_CARS            = 6,
                    CAR_INSURANCE           = 7,
                    CAR_FINANCE             = 8,
                    WMCW                    = 9,
                    CAR_PARTS               = 10,
                    COUPON                  = 11,
                    SETTINGS                = 12,
                    LOGOUT                  = 13,
                    ABOUT                   = 14
                    ;
        }

        public static final class LOGGED_IN_SOCIAL {
            public static final int
                    //Logged In Social
                    SEARCH                  = 1,
                    WALL_OF_DEALS           = 2,
                    SAVED_SEARCHES          = 3,
                    NOTIFICATION_HISTORY    = 4,
                    GARAGE                  = 5,
                    COMPARE_CARS            = 6,
                    CAR_INSURANCE           = 7,
                    CAR_FINANCE             = 8,
                    WMCW                    = 9,
                    CAR_PARTS               = 10,
                    COUPON                  = 11,
                    LOGOUT                  = 12,
                    ABOUT                   = 13;
        }

    }

    public static final class Keys {
        public static final String
                STATUS                  = "status",
                PRODUCT_ID              = "product_id",
                PRODUCTS                = "products",
                SOURCE_ID               = "source_id",
                YEAR                    = "year",
                PRICE                   = "price",
                MAKE                    = "make",
                MODEL                   = "model",
                IMAGE_URL               = "image0",
                KILOMETERS              = "kilometers",
                BELOW_MARKET_AVG        = "below_market_average_percent",
                MILES                   = "miles",
                USER_TYPE               = "user_type",
                BODY                    = "body",
                TRANSMISSION            = "transmission",
                COLOUR                  = "colour",
                DOORS                   = "doors",
                DRIVETRAIN              = "drivetrain",
                POST_DATE               = "post_date",
                SENT_DATE               = "sent_date",
                CURRENCY                = "currency",
                RAW_VALUES              = "raw_values",
                PRICE_LIST              = "price_list",
                KILOMETERS_LIST         = "kilometers_list",
                ITEM_TITLE              = "item_title",
                PRICE_LIST_USD          = "USD",
                PRICE_LIST_CAD          = "CAD",
                PRICE_LIST_EUR          = "EUR",
                CITY                    = "city",
                COUNTRY                 = "country",
                URL                     = "url",
                HREF_URL                = "href_url",

        COUNT                   = "count",
                RESULT                  = "result",
                DATA                    = "data",
                SEARCH_RESULT           = "search_result",
                SEARCH_ID               = "search_id",
                ATTRIBUTE_VALUE         = "attribute_value",
                USER_HAS_PRODUCT_FLAG   = "user_has_product_flag",

        SERIALIZED_VALUES       = "serialized_values",
                STATUS_ID               = "status_id",
                SEARCH_VALUES           = "search_values",
                VALUES                  = "values",
                ATTRIBUTE               = "attribute",
                IS_GROUP                = "is_group",
                MUST_HAVE               = "must_have",

        DOMAIN_ID               = "domain_id",
                ID                      = "id",

        //User Params
        USER                    = "user",
                USR_EMAIL               = "email",
                USR_PASSWORD            = "password",
                USR_PASSWORD2           = "password2",
                USR_REMEMBER_ME         = "rememberme",
                LOGIN_MODE              = "mode",
                SOCIAL_LOGIN_TOKEN      = "token",

        NEW_EMAIL               = "new-email",
                NEW_PASSWORD            = "new-password",
                NEW_PASSWORD2           = "new-password2",

        MIN                     = "min",
                MAX                     = "max",
                DISPLAY_FORMAT          = "display_format",

        EXTERNAL_URL            = "external_url",

        MA_DIFFERENCE           = "ma_difference",
                MA_KEYWORD              = "ma_keyword",
                MA_SHOW                 = "ma_show",

        MA_ABOVE                = "Above",
                MA_BELOW                = "Below"
                        ;
    }

    public static final class NetworkRequestTags {
        public static final String
                LOGIN                   = "login",
                FORGOT_PASSWORD         = "forgot_password",
                SIGNUP                  = "signup",
                SAVED_SEARCH_LIST       = "saved_search_list",
                GARAGE_LIST             = "garage_list",
                SEARCH_RESULT           = "search_result",
                SEARCH_RESULT_COUNT     = "count";

    }

    public static final class DefaultValues {
        public static final String
                PRICE_MIN_DEFAULT       = "CDN$ 0",
                PRICE_MAX_DEFAULT       = "above CDN$ 100,000",
                YEAR_MIN_DEFAULT        = "1980 and lower",
                YEAR_MAX_DEFAULT        = "2018",
                KMS_MIN_DEFAULT         = "0 km",
                KMS_MAX_DEFAULT         = "300,000 km",
                BMA_MIN_DEFAULT         = "0%",
                BMA_MAX_DEFAULT         = "100%",

        PRICE_MIN_DEFAULT_USD   = "CDN$ 0",
                PRICE_MAX_DEFAULT_USD   = "above CDN$ 100,000",
                KMS_MIN_DEFAULT_MILES   = "0 miles",
                KMS_MAX_DEFAULT_MILES   = "300,000 miles",

        KMS_FORMAT_DEFAULT      = "kilometers",
                PRICE_FORMAT_DEFAULT    = "CAD";

    }

    public static final class Bundle {

        public static final class Keys {
            public static final String
                    ATTRIBUTE_PARCEL    = "attrParcel",
                    INCLUDE_NO_IMAGE_PARCEL    = "imgage0",
                    ATTR_RANGE_PARCEL   = "attrRangeParcel",
                    ATTR_PARCEL_BY_KEYWORDS = "attrParcelKeywords",
                    SOURCE_PAGE         = "SourcePage",
                    TO_SEARCH_FORM         = "SearchForm",
                    ATTR_PARCEL_BY_LOC  = "attrLoc",
                    DEFAULT_CITY        = "defCity",

            REQUEST_DATA        = "requestData",
                    SEARCH_ID           = "searchID",
                    STATUS_ID           = "statusID",
                    NO_RESULT_STATUS    = "noResult",
                    KEYWORD             = "keyword",
                    CLR_KEYWORD         = "clr",

            ACTION_LOGIN_POPUP  = "ActionLoginPopup",

            WALL_OF_DEALS       = "wallOfDeals"
                    ;
        }


        public static final class Values{
            public static final int
                    NEW_SEARCH          = 0,
                    EDIT_SEARCH         = 1,
                    SEARCH_BY_KEYWORDS  = 2,
                    WALL_OF_DEALS       = 3,
                    REFINE_SEARCH       = 4;
        }

    }

    public static final class Sort {

        public static final class Values {
            public static final String
                    NEWEST_ARRIVALS     = "post_date desc",
                    MOST_RELEVANT       = "heuristic desc",
                    PRICE_LOW_TO_HIGH   = "price asc",
                    PRICE_HIGH_TO_LOW   = "price desc",
                    YEAR_OLD_TO_NEW     = "year asc",
                    YEAR_NEW_TO_OLD     = "year desc";
        }

        public static final class Keys {
            public static final int
                    NEWEST_ARRIVALS     = 0,
                    MOST_RELEVANT       = 1,
                    PRICE_LOW_TO_HIGH   = 2,
                    PRICE_HIGH_TO_LOW   = 3,
                    YEAR_OLD_TO_NEW     = 4,
                    YEAR_NEW_TO_OLD     = 5;
        }
    }

    public static final class Garage{
        public static final String FLAG_ACTION = "flag_action";
        public static final String FLAG_NAME = "flag_name";

        public static final class FlagAction{
            public static final String
                    FLAG    = "flag",
                    UNFLAG  = "unflag";
        }

        public static final class FlagName{
            public static final String
                    FAVORITE    = "favorite",
                    HIDDEN      = "hidden";
        }
    }

    public static final class CarInsurance {
        public static final class Keys {
            public static final String
                    HAVE_INSURANCE      = "have_insurance",
                    VEHICLE_YEAR        = "vehicle[year]",
                    VEHICLE_MAKE        = "vehicle[make]",
                    VEHICLE_MODEL       = "vehicle[model]",
                    VEHICLE_OWNERSHIP   = "vehicle[ownership]",
                    VEHICLE_PRIMARY_USAGE= "vehicle[primary_usage]",
                    VEHICLE_MILEAGE_COVERED_PER_YEAR = "vehicle[mileage_covered_per_year]",
                    VEHICLE_PRIMARY_PARKING = "vehicle[primary_parking]",
                    DRIVER_FIRST_NAME   = "driver[first_name]",
                    DRIVER_LAST_NAME    = "driver[last_name]",
                    DRIVER_BIRTH_DATE   = "driver[birth_date]",
                    DRIVER_EMAIL        = "driver[email]",
                    DRIVER_PHONE        = "driver[phone]",
                    DRIVER_GENDER       = "driver[gender]",
                    DRIVER_MARITAL_STATUS   = "driver[marital_status]",
                    DRIVER_CREDIT_SCORE     = "driver[credit_score]",
                    DRIVER_HIGHEST_EDUCATION_LEVEL = "driver[highest_education_level]",
                    DRIVER_HOUSE_OWNERSHIP  = "driver[house_ownership]",
                    DRIVER_ACCIDENTS_TICKET_CLAIM = "driver[accidents_tickets_claims]",
                    DRIVER_ANY_OTHER        = "driver[any_other]",
                    DRIVER_INSURANCE_PERIOD = "driver[insurance_period]",
                    DRIVER_CURRENT_CAR_COMPANY = "driver[current_car_company]",
                    DISCOUNT_EMPLOYED_FULL_TIME = "discount[current_employed_full_time]",
                    DISCOUNT_ACTIVE_MILITARY_VETERAN = "discount[active_duty_military_veteran]",
                    DISCOUNT_PAY_IN_FULL_AT_START = "discount[plan_pay_in_full_at_start]";
        }

        public static Map<String, String> insuranceKeyTitlePairs() {
            Map<String, String> insuranceKeyTitlePairs = new HashMap<>();
            insuranceKeyTitlePairs.put(HAVE_INSURANCE, "Do you already have an Insurance?");
            insuranceKeyTitlePairs.put(VEHICLE_YEAR, "Year");
            insuranceKeyTitlePairs.put(VEHICLE_MAKE, "Make");
            insuranceKeyTitlePairs.put(VEHICLE_MODEL, "Model");
            insuranceKeyTitlePairs.put(VEHICLE_OWNERSHIP, "Do you own or lease your vehicle?");
            insuranceKeyTitlePairs.put(VEHICLE_PRIMARY_USAGE, "What's your primary use for the vehicle?");
            insuranceKeyTitlePairs.put(VEHICLE_MILEAGE_COVERED_PER_YEAR, "How many miles do you drive each year?");
            insuranceKeyTitlePairs.put(VEHICLE_PRIMARY_PARKING, "Where do you primarily park this vehicle?");

            insuranceKeyTitlePairs.put(DRIVER_GENDER, "What's your gender?");
            insuranceKeyTitlePairs.put(DRIVER_MARITAL_STATUS, "What's your marital status?");
            insuranceKeyTitlePairs.put(DRIVER_CREDIT_SCORE, "What's your credit score?");
            insuranceKeyTitlePairs.put(DRIVER_HIGHEST_EDUCATION_LEVEL, "What's your highest level of education?");
            insuranceKeyTitlePairs.put(DRIVER_HOUSE_OWNERSHIP, "Do you own or rent your home?");
            insuranceKeyTitlePairs.put(DRIVER_INSURANCE_PERIOD, "How long have you been insured?");
            insuranceKeyTitlePairs.put(DRIVER_ACCIDENTS_TICKET_CLAIM, "Any accidents, or tickets, or claims in the past 3 years?");
            insuranceKeyTitlePairs.put(DRIVER_CURRENT_CAR_COMPANY, "What's your current car insurance company?");
            insuranceKeyTitlePairs.put(DRIVER_ANY_OTHER, "Do any of these apply to you?");

            return insuranceKeyTitlePairs;
        }
    }

    public static String getConfigUrl(SessionManagement session, int searchType, String searchId) {
        String url;
        if(session != null && session.isLoggedIn()) {
            if(searchType == Bundle.Values.EDIT_SEARCH)
                url = URL.API_USER + "/"+session.getUserID() + URL.CONFIG+ "&search_id="+searchId;
            else
                url = URL.API_USER + "/"+session.getUserID() + URL.CONFIG;
        }
        else
            url = URL.API_USER + "/null"+ URL.CONFIG;
        return url;
    }

    public static String getGarageActionUrl(SessionManagement session) {
        String url = "";
        if(null != session && session.isLoggedIn()) {
            url = URL.GARAGE_ACTION_PREFIX + session.getUserID() + URL.GARAGE_ACTION_SUFFIX;
        }
        return url;
    }

    public static byte[] buildGarageActionRequest(int productID, String flagName, String flagAction){
        String req = Keys.PRODUCT_ID +"="+ productID + "&" + Garage.FLAG_NAME + "=" + flagName + "&" + Garage.FLAG_ACTION + "=" + flagAction;
        Log.d("Garage action req: ", req);
        return req.getBytes(StandardCharsets.UTF_8);
    }

    public static String getModelUrl(int id, int group) {
        return GET_MODEL_LIST+id+MODEL_URL_GROUP_CONSTRAINT+group;
    }

}