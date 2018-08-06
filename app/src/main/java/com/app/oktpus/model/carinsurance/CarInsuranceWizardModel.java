package com.app.oktpus.model.carinsurance;

import android.content.Context;

import wizardlib.model.AbstractWizardModel;
import wizardlib.model.PageList;

/**
 * Created by Gyandeep on 6/11/17.
 */

public class CarInsuranceWizardModel extends AbstractWizardModel {
    public CarInsuranceWizardModel(Context context) {
        super(context);
    }

    @Override
    protected PageList onNewRootPageList() {
        return new PageList(
                new CIStartModel(this, "Start"),
                new CIVehicleModel(this, "Vehicle"),
                new CIDriverModel(this, "Driver"),
                new CIDiscountModel(this, "Discounts")
            /*new BranchPage(this, "Do you already have an insurance?")
                    .addBranch("Yes",
                            new CarInsuranceVehicleModel(this, "Vehicle"),
                            new CarInsuranceDriverModel(this, "Driver"))
                    .addBranch("No",
                            new SingleFixedChoicePage(this, "Question 2")
                                    .setChoices("Choice A", "Choice B"),

                            new SingleFixedChoicePage(this, "Question 3")
                                    .setChoices("Choice A", "Choice B", "Choice C",
                                            "Choice D", "Choice E")
                                    .setValue("Choice A")
                    )*/

                    //.setRequired(true)
        );
    }
}
