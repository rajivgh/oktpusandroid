package com.app.oktpus.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.oktpus.R;
import com.app.oktpus.constants.carfinance.FinanceConstants;
import com.app.oktpus.controller.AppController;
import com.app.oktpus.fragment.SuccessFragment;
import com.app.oktpus.fragment.carfinance.CFReviewFragment;
import com.app.oktpus.fragment.carfinance.GettingStartedPage;
import com.app.oktpus.model.carfinance.CFLoanInfoModel;
import com.app.oktpus.model.carfinance.CFPersonalInfoModel;
import com.app.oktpus.model.carfinance.CarFinanceWizardModel;
import com.app.oktpus.model.carinsurance.QuestionItems;

import java.util.ArrayList;
import java.util.List;

import wizardlib.model.AbstractWizardModel;
import wizardlib.model.ModelCallbacks;
import wizardlib.model.Page;
import wizardlib.model.ui.PageFragmentCallbacks;
import wizardlib.model.ui.StepPagerStrip;


/**
 * Created by Gyandeep on 9/1/18.
 */

public class CarFinance extends BaseActivity implements
        PageFragmentCallbacks,
        CFReviewFragment.Callbacks,
        ModelCallbacks {

    private ViewPager mPager;
    private CustomPagerAdapter mPagerAdapter;

    private boolean mEditingAfterReview;

    public AbstractWizardModel mWizardModel = new CarFinanceWizardModel(this);

    private boolean mConsumePageSelectedEvent;

    private List<Page> mCurrentPageSequence;
    private StepPagerStrip mStepPagerStrip;
    private Bundle mSavedInstanceState;
    private static final String START_PAGE_TAG = "cfStart";
    public List<QuestionItems> countryPageItem;
    public String mApplicationType = FinanceConstants.ApplicationType.INDIVIDUAL;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        includeNavigationMenu(this, R.layout.activity_car_finance);
        mSavedInstanceState = savedInstanceState;
        TextView pageTitle = (TextView) findViewById(R.id.page_title);
        ImageView backButton = (ImageView) findViewById(R.id.ss_back_arrow);
        pageTitle.setTypeface(AppController.getFontType(this));
        pageTitle.setText(getResources().getString(R.string.nav_item_car_finance));
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        countryPageItem = new ArrayList<>();
        countryPageItem.add(new QuestionItems("country", "Country", "Canada"));
        startPage();

    }

    public void startPage() {
        (getSupportFragmentManager())
                .beginTransaction()
                .replace(R.id.frame_container, new GettingStartedPage())
                .addToBackStack(START_PAGE_TAG).commit();
    }

    public void onStartButtonClicked() {
        try {
            dismissFragment();
            updateCountry();
            startWizard();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void onPreviousButtonClick() {
        mPager.setCurrentItem(mPager.getCurrentItem() - 1);
    }

    public void dismissFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public AbstractWizardModel onGetModel() {
        return mWizardModel;
    }

    @Override
    public void onEditScreenAfterReview(String pageKey) {
        for (int i = mCurrentPageSequence.size() - 1; i >= 0; i--) {
            if (mCurrentPageSequence.get(i).getKey().equals(pageKey)) {
                mConsumePageSelectedEvent = true;
                mEditingAfterReview = true;
                mPager.setCurrentItem(i);
                updateBottomBar();
                break;
            }
        }
    }

    @Override
    public void onPageDataChanged(Page page) {
        if (page.isRequired()) {
            if (recalculateCutOffPage()) {
                mPagerAdapter.notifyDataSetChanged();
                updateBottomBar();
            }
        }
    }

    @Override
    public void onPageTreeChanged() {
        mCurrentPageSequence = mWizardModel.getCurrentPageSequence();
        recalculateCutOffPage();
        mStepPagerStrip.setPageCount(mCurrentPageSequence.size() + 1); // + 1 = review step
        mPagerAdapter.notifyDataSetChanged();
        updateBottomBar();
        updateApplicationType();
    }

    @Override
    public Page onGetPage(String key) {
        return mWizardModel.findByKey(key);
    }

    private void updateBottomBar() {
        int position = mPager.getCurrentItem();
        /*if (position == mCurrentPageSequence.size()) {

        } else {

        }*/
    }

    public void startWizard() {
        if (mSavedInstanceState != null) {
            mWizardModel.load(mSavedInstanceState.getBundle("wizardlib/model"));
        }

        mWizardModel.registerListener(this);

        mPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);
        mStepPagerStrip = (StepPagerStrip) findViewById(R.id.strip);
        mStepPagerStrip.setOnPageSelectedListener(new StepPagerStrip.OnPageSelectedListener() {
            @Override
            public void onPageStripSelected(int position) {
                position = Math.min(mPagerAdapter.getCount() - 1, position);
                if (mPager.getCurrentItem() != position) {
                    mPager.setCurrentItem(position);
                }
            }
        });

        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mStepPagerStrip.setCurrentPage(position);

                if (mConsumePageSelectedEvent) {
                    mConsumePageSelectedEvent = false;
                    return;
                }

                mEditingAfterReview = false;
                updateBottomBar();
            }
        });
        onPageTreeChanged();
        updateBottomBar();

    }



    public void onNextButtonClick() {
        if (mPager.getCurrentItem() == mCurrentPageSequence.size()) {
            /*DialogFragment dg = new DialogFragment() {
                @Override
                public Dialog onCreateDialog(Bundle savedInstanceState) {
                    return new AlertDialog.Builder(getActivity())
                            .setMessage("Ready to submit?")
                            .setPositiveButton("Yes", null)
                            .setNegativeButton(android.R.string.cancel, null)
                            .create();
                }
            };
            dg.show(getSupportFragmentManager(), "place_order_dialog");*/
        } else {
            if (mEditingAfterReview) {
                mPager.setCurrentItem(mPagerAdapter.getCount() - 1);
            } else {
                mPager.setCurrentItem(mPager.getCurrentItem() + 1);
            }
        }
    }

    private boolean recalculateCutOffPage() {
        // Cut off the pager adapter at first required page that isn't completed
        int cutOffPage = mCurrentPageSequence.size() + 1;
        for (int i = 0; i < mCurrentPageSequence.size(); i++) {
            Page page = mCurrentPageSequence.get(i);
            if (page.isRequired() && !page.isCompleted()) {
                cutOffPage = i;
                break;
            }
        }

        if (mPagerAdapter.getCutOffPage() != cutOffPage) {
            mPagerAdapter.setCutOffPage(cutOffPage);
            return true;
        }

        return false;
    }

    public class CustomPagerAdapter extends FragmentStatePagerAdapter {
        private int mCutOffPage;
        private Fragment mPrimaryItem;

        public CustomPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (i >= mCurrentPageSequence.size()) {
                return new CFReviewFragment();
            }

            return mCurrentPageSequence.get(i).createFragment();
        }

        @Override
        public int getItemPosition(Object object) {
            // TODO: be smarter about this
            if (object == mPrimaryItem) {
                // Re-use the current fragment (its position never changes)
                return POSITION_UNCHANGED;
            }

            return POSITION_NONE;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            mPrimaryItem = (Fragment) object;
        }

        @Override
        public int getCount() {
            if (mCurrentPageSequence == null) {
                return 0;
            }
            return Math.min(mCutOffPage + 1, mCurrentPageSequence.size() + 1);
        }

        public void setCutOffPage(int cutOffPage) {
            if (cutOffPage < 0) {
                cutOffPage = Integer.MAX_VALUE;
            }
            mCutOffPage = cutOffPage;
        }

        public int getCutOffPage() {
            return mCutOffPage;
        }
    }

    public void displaySuccess() {
        (getSupportFragmentManager())
                .beginTransaction()
                .replace(R.id.frame_container, new SuccessFragment())
                .addToBackStack(START_PAGE_TAG).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWizardModel.unregisterListener(this);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    public void dismissActivity() {
        //dismissFragment();
        finish();
    }

    public void removeAdditionalPages() {
        ((CarFinanceWizardModel)mWizardModel).removeAdditionalPages();
        onPageTreeChanged();
    }

    public void addPages() {
        ((CarFinanceWizardModel)mWizardModel).addPagesForJointApplication();
        onPageTreeChanged();
    }

    public void setApplicationType(String type) {
        mApplicationType = type;
    }

    public void updateApplicationType() {
        if ((CFPersonalInfoModel)onGetPage(FinanceConstants.PageTitle.CO_APPLICANT_PERSONAL_INFO) != null) {
            ((CFPersonalInfoModel)onGetPage(FinanceConstants.PageTitle.CO_APPLICANT_PERSONAL_INFO)).setApplicationType(mApplicationType);
        }

        if ((CFPersonalInfoModel)onGetPage(FinanceConstants.PageTitle.PERSONAL_AND_RESIDENTIAL_PAGE) != null) {
            ((CFPersonalInfoModel)onGetPage(FinanceConstants.PageTitle.PERSONAL_AND_RESIDENTIAL_PAGE)).setApplicationType(mApplicationType);
        }

        if ((CFPersonalInfoModel)onGetPage(FinanceConstants.PageTitle.CO_APPLICANT_PERSONAL_INFO) != null) {
            ((CFPersonalInfoModel)onGetPage(FinanceConstants.PageTitle.CO_APPLICANT_PERSONAL_INFO)).setCountry(countryPageItem.get(0).getValue());
        }
    }

    public void updateCountry() {
        if ((CFLoanInfoModel)onGetPage(FinanceConstants.PageTitle.LOAN_INFO_PAGE) != null) {
            ((CFLoanInfoModel)onGetPage(FinanceConstants.PageTitle.LOAN_INFO_PAGE)).setCountry(countryPageItem.get(0).getValue());
        }

        if ((CFPersonalInfoModel)onGetPage(FinanceConstants.PageTitle.PERSONAL_AND_RESIDENTIAL_PAGE) != null) {
            ((CFPersonalInfoModel)onGetPage(FinanceConstants.PageTitle.PERSONAL_AND_RESIDENTIAL_PAGE)).setCountry(countryPageItem.get(0).getValue());
        }
    }
}
