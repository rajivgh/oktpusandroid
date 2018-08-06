package com.app.oktpus.model;

import com.app.oktpus.constants.SearchResultViewType;

/**
 * Created by Gyandeep on 8/11/16.
 */

public class SearchResultItem extends SearchResultViewType {

    private int productID;
    private String item_title, img0, post_date, city, price, currency_usd,currency_cad,
            kilometers_kilometers,kilometers_miles,belowmarketby_cad,belowmarketby_usd,
            url, maKeyword, sentDate, href_url;
    private boolean maShow, isFavActive, isFeedbackCard;
    private int viewType = SearchResultViewType.VIEW_ITEM;

    public SearchResultItem(String item_title, String img0, String post_date, String city, String currency_usd,String currency_cad,
                            String kilometers_kilometers,String kilometers_miles, String price, String belowmarketby_usd,String belowmarketby_cad,
                            String url, String maKeyword, boolean maShow, String sentDate, boolean isFavActive, int productID, String href_url) {

        this.item_title = item_title;
        this.img0 = img0;
        this.post_date = post_date;
        this.city = city;
        this.currency_usd = currency_usd;
        this.currency_cad = currency_cad;
        this.kilometers_kilometers = kilometers_kilometers;
        this.kilometers_miles = kilometers_miles;
        this.price = price;
        this.belowmarketby_usd = belowmarketby_usd;
        this.belowmarketby_cad = belowmarketby_cad;
        this.url = url;
        this.maKeyword = maKeyword;
        this.maShow = maShow;
        this.sentDate = sentDate;
        this.isFavActive = isFavActive;
        this.productID = productID;
        this.href_url = href_url;
    }

    public SearchResultItem(int viewType, String imageUrl, String linkUrl, boolean isFeedbackCard) {
        this.viewType = viewType;
        this.img0 = imageUrl;
        this.href_url = linkUrl;
        this.isFeedbackCard = isFeedbackCard;
    }

    public int getProductID() { return productID; }

    public String getHref_url() { return href_url; }

    public boolean isFavActive() {return isFavActive;}
    public void setFavActive(boolean isFavActive) { this.isFavActive = isFavActive; }

    public String getSentDate() { return sentDate; }
    public void setSentDate(String sentDate) { this.sentDate = sentDate; }

    public String getItemTitle() {return this.item_title;}
    public void setItemTitle(String item_title) { this.item_title = item_title;}

    public String getImage() {return this.img0;}
    public void setImg(String img0) { this.img0 = img0;}

    public String getPostDate() {return this.post_date;}
    public void setPostDate(String post_date) { this.post_date = post_date;}

    public String getCity() {return this.city;}
    public void setCity(String city) { this.city = city;}

    public String getCurrency_usd() {return this.currency_usd;}
    public void setCurrency_usd(String currency) { this.currency_usd = currency;}

    public String getCurrency_cad() {return this.currency_cad;}
    public void setCurrency_cad(String currency) { this.currency_cad = currency;}

    public String getKilometers_kilometers() {return this.kilometers_kilometers;}
    public void setKilometers_kilometers(String kilometers) { this.kilometers_kilometers = kilometers;}

    public String getKilometers_miles() {return this.kilometers_miles;}
    public void setKilometers_miles(String kilometers) { this.kilometers_miles = kilometers;}

    public String getPrice() {return this.price;}
    public void setPrice(String price) { this.price = price;}

    public String getbelowMarketBy_usd() {return this.belowmarketby_usd;}
    public void setbelowMarketBy_usd(String belowmarketby) { this.belowmarketby_usd = belowmarketby;}

    public String getbelowMarketBy_cad() {return this.belowmarketby_cad;}
    public void setbelowMarketBy_cad(String belowmarketby) { this.belowmarketby_cad = belowmarketby;}

    public String getUrl() {
        return url;
    }

    public String getMaKeyword() { return maKeyword; }
    public boolean isMaShow() { return maShow; }

    @Override
    public int getType() {
        return viewType;
    }
}