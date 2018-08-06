package com.app.oktpus.model.wmcw;

import android.content.Context;

import wizardlib.model.AbstractWizardModel;
import wizardlib.model.PageList;

/**
 * Created by Gyandeep on 13/3/18.
 */

public class WMCWWizardModel extends AbstractWizardModel {

    public WMCWWizardModel(Context context) {
        super(context);
    }

    @Override
    protected PageList onNewRootPageList() {
        return new PageList(
                new WMCWVehicleModel(this, "Vehicle"),
                new WMCWContactModel(this, "Contact"),
                new WMCWLocationModel(this, "Location")
        );
    }
}
