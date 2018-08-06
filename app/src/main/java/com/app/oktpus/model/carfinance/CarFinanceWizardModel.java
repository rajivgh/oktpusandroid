package com.app.oktpus.model.carfinance;

import android.content.Context;

import wizardlib.model.AbstractWizardModel;
import wizardlib.model.PageList;

import static com.app.oktpus.constants.carfinance.FinanceConstants.PageTitle.CO_APPLICANT_EMPLOYMENT_INFO;
import static com.app.oktpus.constants.carfinance.FinanceConstants.PageTitle.CO_APPLICANT_PERSONAL_INFO;
import static com.app.oktpus.constants.carfinance.FinanceConstants.PageTitle.EMPLOYMENT_INFO_PAGE;
import static com.app.oktpus.constants.carfinance.FinanceConstants.PageTitle.LOAN_INFO_PAGE;
import static com.app.oktpus.constants.carfinance.FinanceConstants.PageTitle.PERSONAL_AND_RESIDENTIAL_PAGE;
import static com.app.oktpus.constants.carfinance.FinanceConstants.PageTitle.START_PAGE;
import static com.app.oktpus.constants.carfinance.FinanceConstants.PageTitle.VEHICLE_INFO_PAGE;

/**
 * Created by Gyandeep on 9/1/18.
 */

public class CarFinanceWizardModel extends AbstractWizardModel {

    private PageList pageList, additionalPages;
    private PageList getPageList() {
        pageList = new PageList(new CFStartModel(this, START_PAGE),
                new CFPersonalInfoModel(this, PERSONAL_AND_RESIDENTIAL_PAGE),
                new CFEmploymentInfoModel(this, EMPLOYMENT_INFO_PAGE),
                new CFLoanInfoModel(this, LOAN_INFO_PAGE),
                new CFVehicleInfoModel(this, VEHICLE_INFO_PAGE));
        return pageList;
    }

    public CarFinanceWizardModel(Context context) {
        super(context);
        additionalPages = new PageList(new CFPersonalInfoModel(this, CO_APPLICANT_PERSONAL_INFO),
                new CFEmploymentInfoModel(this, CO_APPLICANT_EMPLOYMENT_INFO));
    }

    @Override
    protected PageList onNewRootPageList() {
        return getPageList();
    }

    public void removeAdditionalPages() {
        if(additionalPages != null) {
            pageList.removeAll(additionalPages);
        }
    }

    public void addPagesForJointApplication() {
        pageList.addAll(additionalPages);
    }


}
