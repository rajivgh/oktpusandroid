package com.app.oktpus.responseModel.ProductDetails;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Gyandeep on 3/7/17.
 */

public class RawValues {

    /*"raw_values":{"product_id":"15101122","domain_id":"1","source_id":"26","make":"Volkswagen",
    "model":"PASSAT 4MOTION","kilometers":"155000","body":"Wagon","accessories":"Sport","engine":"-",
    "cylinders":"-","stock_number":"-","drivetrain":"AWD","safety_rating":"Unknown","transmission":"Automatic",
    "colour":"Brown","interior_colour":"-","passengers":"-","doors":"4 doors","fuel":"Gasoline","city_fuel_economy":"-",
    "hwy_fuel_economy":"-","rating":"4.5","image0":"https:\/\/oktpus.com\/prod_erp\/1\/5\/8\/6\/4\/2\/7\/4\/15864274_image0.jpg","currency":"CAD","city":"Halifax, NS, CA",
    "latitude":"44.642078","longitude":"-63.620571","country":"CA","post_date":"2017-07-02 23:29:00",
    "url":"http:\/\/wwwb.autotrader.ca\/a\/Volkswagen\/Passat\/HALIFAX\/Nova+Scotia\/19_10240572_\/?showcpo=ShowCPO",
    "href_url":"https:\/\/oktpus.com\/redirect.html?page=http%3A%2F%2Fwwwb.autotrader.ca%2Fa%2FVolkswagen%2FPassat%2FHALIFAX%2FNova%2BScotia%2F19_10240572_%2F%3Fshowcpo%3DShowCPO\u0026page_host=wwwb.autotrader.ca\u0026title=click_to_source_pdp\u0026data_source=web\u0026user_id=",
    "market_average":null,"item_mad_value":null,"ma_show":false,"image0_default":"\/\/oktpus.com\/prod_erp\/default_3.jpg","heuristic":"-"},
    "price_list":{"USD":"Please contact",
    "CAD":"Please contact"},"ma_difference_list":{"USD":"-","CAD":"-"},"item_title":" Volkswagen PASSAT 4MOTION",
    "kilometers_list":{"kilometers":"155,000 kilometers","miles":"96,312 miles"},"sent_date":"-"}*/

    @SerializedName("kilometers")
    private int kilometers;
    @SerializedName("href_url")
    private String redirectUrl;

    public int getKilometers() { return kilometers; }
    public String getRedirectUrl() {  return redirectUrl; }
}
