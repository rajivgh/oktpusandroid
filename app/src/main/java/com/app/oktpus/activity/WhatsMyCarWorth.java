package com.app.oktpus.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.transition.Explode;
import android.transition.Slide;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.oktpus.R;
import com.app.oktpus.controller.AppController;
import com.app.oktpus.fragment.SuccessFragment;
import com.app.oktpus.fragment.wmcw.WMCWGetStarted;
import com.app.oktpus.fragment.wmcw.WMCWReviewFragment;
import com.app.oktpus.model.wmcw.WMCWWizardModel;

import java.util.List;

import wizardlib.model.AbstractWizardModel;
import wizardlib.model.ModelCallbacks;
import wizardlib.model.Page;
import wizardlib.model.ui.PageFragmentCallbacks;
import wizardlib.model.ui.StepPagerStrip;

/**
 * Created by Gyandeep on 13/3/18.
 */

public class WhatsMyCarWorth extends BaseActivity implements
        PageFragmentCallbacks,
        WMCWReviewFragment.Callbacks,
        ModelCallbacks {

    private ViewPager mPager;
    private WizardPagerAdapter mPagerAdapter;
    private boolean mEditingAfterReview;
    private AbstractWizardModel mWizardModel = new WMCWWizardModel(this);
    private static final String START_PAGE_TAG = "wmcwStart";
    private boolean mConsumePageSelectedEvent;
    private List<Page> mCurrentPageSequence;
    private StepPagerStrip mStepPagerStrip;
    private Bundle mSavedInstanceState;



    /*public WhatsMyCarWorth(){
        super();
    }

    public WhatsMyCarWorth(BaseActivity obj) {
        super(obj);
        baseActivity = obj;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        includeNavigationMenu(this, R.layout.layout_activity_wmcw);
        mSavedInstanceState = savedInstanceState;

        TextView pageTitle = (TextView) findViewById(R.id.page_title);
        ImageView backButton = (ImageView) findViewById(R.id.ss_back_arrow);
        pageTitle.setTypeface(AppController.getFontType(this));
        pageTitle.setText(getResources().getString(R.string.nav_item_wmcw));
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        startPage();

        //getWindow().setExitTransition(new Slide());
    }


    public void startPage() {
        (getSupportFragmentManager())
                .beginTransaction()
                .replace(R.id.frame_container, new WMCWGetStarted())
                .addToBackStack(START_PAGE_TAG).commit();
    }

    public void onStartButtonClicked() {
        try {
            dismissFragment();
            startWizard();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void startWizard() {
        if (mSavedInstanceState != null) {
            mWizardModel.load(mSavedInstanceState.getBundle("wizardlib/model"));
        }

        mWizardModel.registerListener(this);

        mPagerAdapter = new WizardPagerAdapter(getSupportFragmentManager());
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

    @Override
    public void onPageTreeChanged() {
        mCurrentPageSequence = mWizardModel.getCurrentPageSequence();
        recalculateCutOffPage();
        mStepPagerStrip.setPageCount(mCurrentPageSequence.size() + 1); // + 1 = review step
        mPagerAdapter.notifyDataSetChanged();
        updateBottomBar();
    }

    private void updateBottomBar() {
        int position = mPager.getCurrentItem();
        if (position == mCurrentPageSequence.size()) {
            //mNextButton.setText("Finish");
            //mNextButton.setBackgroundResource(R.drawable.finish_background);
            //mNextButton.setTextAppearance(this, R.style.TextAppearanceFinish);
        } else {
            /*mNextButton.setText(mEditingAfterReview
                    ? "Review"
                    : "Next");*/
            //mNextButton.setBackgroundResource(R.drawable.selectable_item_background);
            /*TypedValue v = new TypedValue();
            getTheme().resolveAttribute(android.R.attr.textAppearanceMedium, v, true);
            mNextButton.setTextAppearance(this, v.resourceId);
            mNextButton.setEnabled(position != mPagerAdapter.getCutOffPage());*/
        }

        //mPrevButton.setVisibility(position <= 0 ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWizardModel.unregisterListener(this);
    }

    @Override
    public AbstractWizardModel onGetModel() {
        return mWizardModel;
    }

    @Override
    public void onEditScreenAfterReview(String key) {
        for (int i = mCurrentPageSequence.size() - 1; i >= 0; i--) {
            if (mCurrentPageSequence.get(i).getKey().equals(key)) {
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
    public Page onGetPage(String key) {
        return mWizardModel.findByKey(key);
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

    /*@Override
    public void nextButton() {
        System.out.println("next button pressed");
    }

    @Override
    public void previousButton() {
        System.out.println("previous button pressed");
    }*/

    public class WizardPagerAdapter extends FragmentStatePagerAdapter {
        private int mCutOffPage;
        private Fragment mPrimaryItem;

        public WizardPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (i >= mCurrentPageSequence.size()) {
                return new WMCWReviewFragment();
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

            //mPagerAdapter.notifyDataSetChanged();
        }
    }

    public void onPreviousButtonClick() {
        mPager.setCurrentItem(mPager.getCurrentItem() - 1);
    }

    public void displaySuccess() {
        (getSupportFragmentManager())
                .beginTransaction()
                .replace(R.id.frame_container, new SuccessFragment())
                .addToBackStack(START_PAGE_TAG).commit();
    }

    public void dismissFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        }
    }

    public void dismissActivity() {
        //dismissFragment();
        finish();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
