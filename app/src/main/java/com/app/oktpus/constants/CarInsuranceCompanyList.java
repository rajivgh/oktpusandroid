package com.app.oktpus.constants;

import com.app.oktpus.model.SimpleListItemModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Gyandeep on 19/11/17.
 */

/*For US and Canada only*/
public final class CarInsuranceCompanyList {

    public List<SimpleListItemModel> getList() {
        List<SimpleListItemModel> list = new ArrayList<>();
        for(String item: CarInsuranceCompanyList.list) {
            list.add(new SimpleListItemModel(item));
        }
        return list;
    }

    public static final List<String> list =
            Collections.unmodifiableList(Arrays
                    .asList(
                            "21st Century",
                            "AAA",
                            "AARP",
                            "Acceptance",
                            "Affirmative",
                            "AIG",
                            "Allianz Canada",
                            "Allstate",
                            "Allstate Insurance Company of Canada",
                            "AmFam",
                            "Amica",
                            "AmWINS",
                            "ASI",
                            "Auto Club",
                            "Aviva",
                            "Belair Direct",
                            "CAA",
                            "CAARP",
                            "Certas Direct Insurance Company",
                            "Chieftain Insurance Company",
                            "Chubb Insurance",
                            "Coachman Insurance Company",
                            "Co-operators",
                            "COUNTRY",
                            "CSE",
                            "Dairyland",
                            "Direct General",
                            "Dominion of Canada",
                            "Echelon",
                            "Economical",
                            "Electric",
                            "Elephant",
                            "Elite Insurance Company",
                            "Empower",
                            "Encompass",
                            "Erie",
                            "Esurance",
                            "Facility Association",
                            "Farmers",
                            "Farmer’s Mutual",
                            "Fidelity National",
                            "Firemans Fund",
                            "Foremost",
                            "Gainsco",
                            "GEICO",
                            "GeoVera",
                            "GMAC",
                            "Gore Mutual",
                            "Hartford",
                            "High Point",
                            "iMingle",
                            "Infinity",
                            "Intact Insurance",
                            "Jevco Insurance Company",
                            "Kemper Specialty",
                            "Kingsway General Insurance",
                            "Liberty Mutual",
                            "Lloyd’s",
                            "Lombard Insurance",
                            "Mapfre",
                            "Mendota",
                            "Mercury",
                            "MetLife",
                            "Missisquoi Insurance",
                            "Motors Insurance Ontario",
                            "Motors Insurance Quebec",
                            "Nationwide",
                            "North Waterloo Farmers Mutual",
                            "Optimum Frontier",
                            "Optimum West Insurance",
                            "Pafco Insurance",
                            "Peace Hills Insurance",
                            "Peel Mutual Insurance Company",
                            "Pembridge Insurance",
                            "Perth Insurance",
                            "Pilot",
                            "Portage Mutual Insurance",
                            "Progressive",
                            "RBC Insurance",
                            "Red River Insurance",
                            "RSA",
                            "SafeAuto",
                            "Safeco",
                            "Saskatchewan Mutual",
                            "Scottish & York Insurance Co Limited",
                            "Selective Flood",
                            "SGI Canada",
                            "Shelter Insurance",
                            "State Auto",
                            "State Farm",
                            "State Farm Fire and Casualty Company",
                            "TD General Insurance Company",
                            "The General",
                            "Titan",
                            "Travelers",
                            "Traders General Insurance Company",
                            "The Guarantee Company",
                            "Unica Insurance",
                            "Unitrin",
                            "USAA",
                            "Viking",
                            "Wawanesa Insurance Company",
                            "West Wawanosh Mutual",
                            "Western Assurance Company",
                            "Zenith Insurance Company",
                            "Other"
                    ));
}
