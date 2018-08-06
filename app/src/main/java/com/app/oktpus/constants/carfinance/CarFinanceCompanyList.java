package com.app.oktpus.constants.carfinance;

import com.app.oktpus.model.SimpleListItemModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Gyandeep on 17/1/18.
 */

public class CarFinanceCompanyList {

    public List<SimpleListItemModel> getList() {
        List<SimpleListItemModel> list = new ArrayList<>();
        for(String item: CarFinanceCompanyList.list) {
            list.add(new SimpleListItemModel(item));
        }
        return list;
    }

    public static final List<String> list =
            Collections.unmodifiableList(Arrays
                    .asList(
                            "Ally Financial, Inc.",
                            "American Honda Finance",
                            "Auto Approve",
                            "AutoPay",
                            "BankSource Solutions LLC",
                            "BMW Financial Services",
                            "Capital One Auto Finance",
                            "CarFinance.com",
                            "CBC Federal Credit Union",
                            "Chase Bank",
                            "Chrysler Financial Services",
                            "Compass Bank",
                            "CPS Financial Services",
                            "DriveTime",
                            "Exeter Finance Corporation",
                            "First Investors Financial Services",
                            "Fleet Financial, Inc.",
                            "Ford Motor Credit",
                            "GM Financial",
                            "Honda Auto Finance",
                            "LendingClub",
                            "LightStream",
                            "Nissan Motors Acceptance Corporation",
                            "OpenRoad Lending",
                            "Prestige Financial Services",
                            "Regional Acceptance Corporation",
                            "Resource One Credit Union",
                            "RoadLoans",
                            "Santander Consumer",
                            "Security National Acceptance Corporation",
                            "SpringboardAuto.com, Inc.",
                            "SunTrust Bank",
                            "TD Auto Finance",
                            "Toyota Motor Credit",
                            "up2drive",
                            "Wells Fargo Bank",
                            "Western Preferred Financial",
                            "Westlake Financial Service, Inc.",
                            "Wilshire Consumer Credit",
                            "Yield Solutions Group LLC dba RefiJet",
                            "Other" ));
}
