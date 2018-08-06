package com.app.oktpus.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.speech.RecognizerIntent;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.oktpus.model.AttrValSpinnerModel;
import com.app.oktpus.model.ItemRequestData;
import com.app.oktpus.model.SearchResultItem;
import com.app.oktpus.constants.Flags;
import com.app.oktpus.controller.AppController;
import com.app.oktpus.fragment.SearchForm;
import com.app.oktpus.fragment.SearchResult;
import com.app.oktpus.listener.OnSearchActivityCalls;
import com.app.oktpus.R;
import com.app.oktpus.responseModel.Config.CommonFields;
import com.app.oktpus.utils.FABBehaviour;
import com.app.oktpus.utils.SessionManagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.app.oktpus.utils.NavigationMenuItems.selectDrawerItem;

/**
 * Created by Gyandeep on 6/12/16.
 */

public class SearchActivity extends BaseActivity implements OnSearchActivityCalls, ViewPager.OnPageChangeListener {
    public static final int SEARCH_FORM_FRAGMENT = 0, SEARCH_RESULT_FRAGMENT = 1;
    public static final boolean KEYWORD_SEARCH_START = true, KEYWORD_SEARCH_FINISH = false;

    private List<SearchResultItem> resultList = new ArrayList<>();
    public List<SearchResultItem> getResultList() {
        return resultList;
    }
    private static final int NUM_PAGES = 2;
    public ViewPager mPager;
    private TabLayout tabLayout;
    private PagerAdapter mPagerAdapter;
    private ScreenSlidePagerAdapter pagerAdapter;
    private SessionManagement mSession;
    public ItemRequestData.Builder builder;

    public FABBehaviour fabBehaviour;
    public FrameLayout holder;
    public static SearchForm searchForm;
    public static SearchResult searchResult;
    public CoordinatorLayout rootView;
    public LinearLayout container;
    public RelativeLayout searchLoadingLayout, containerMsg;
    public ImageView splashImg;
    private int targetPage, mSearchType;
    public RelativeLayout searchButton;
    public TextView tvSearchButton; public ProgressBar pbSearchButton;
    public boolean isNetworkCallOnProgress = false;
    public static final int FLAG_INIT_LOC_REQ = 102;
    public Bundle parcelBundle;
    public boolean isFirstLoad = false;

    FragmentManager fragmentManager;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override                                                                               //prepare request data and bundle it to searchresult if target is SearchResult
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            includeNavigationMenu(this, R.layout.activity_search);
            searchForm = SearchForm.newInstance("SearchForm");
            searchResult = SearchResult.newInstance("SearchResult");
            //Search bar
            rootView = (CoordinatorLayout) findViewById(R.id.coordinator_activity_search);
            containerMsg = (RelativeLayout) findViewById(R.id.layout_nw);
            container = (LinearLayout) findViewById(R.id.main_container);
            searchLoadingLayout = (RelativeLayout) findViewById(R.id.splash_screen);
            splashImg = (ImageView) findViewById(R.id.splash_img);
            tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);

            if(getIntent().hasExtra(Flags.Bundle.Keys.ACTION_LOGIN_POPUP)){
                if(getIntent().getBooleanExtra(Flags.Bundle.Keys.ACTION_LOGIN_POPUP, false))
                    selectDrawerItem(this, Flags.NavMenu.NOT_LOGGED_IN.LOGIN, AppController.getInstance().mSession);
            }

            loadContent();

        }
        catch(Exception e) {
            Log.e(Flags.ActivityTag.SEARCH_ACTIVITY, e.getMessage());
        }
    }

    public void loadContent() {
        if(AppController.getInstance().checkNetworkConn()) {
            mAppBarLayout.setElevation(0f);
            mAppBarLayout.setExpanded(true, true);

            container.setVisibility(View.GONE);
            containerMsg.setVisibility(View.GONE);

            splashImg.setVisibility(View.VISIBLE);
            searchLoadingLayout.setVisibility(View.VISIBLE);
            splashImg.setBackgroundResource(R.drawable.spinner);

            AnimationDrawable frameAnimation = (AnimationDrawable) splashImg.getBackground();
            frameAnimation.start();

            //Call for current location
            if(null != AppController.getInstance().getDefaultCityBundle()) {
                init();
            }
            else {
                Intent intent = new Intent(this, LocationActivity.class);
                intent.putExtra(Flags.IS_LOC_UPDATE_REQUESTED, false);
                startActivityForResult(intent, FLAG_INIT_LOC_REQ);
            }

            fragmentManager = getSupportFragmentManager();
        }
        else {
            container.setVisibility(View.GONE);
            containerMsg.setVisibility(View.VISIBLE);
            splashImg.setVisibility(View.GONE);
            searchLoadingLayout.setVisibility(View.GONE);
            mAppBarLayout.setExpanded(false, true);
        }

    }

    public void setSearchType(int type) {
        mSearchType = type;
    }

    public int getSearchType() {
        return this.mSearchType;
    }

    public void init() {
        try {
            // Operation -> EditSearch, ExpandForm, SearchResult
            mPager = (ViewPager) findViewById(R.id.search_fragment_holder);
            mSession = new SessionManagement(this);
            parcelBundle = getIntent().getBundleExtra(Flags.Bundle.Keys.ATTR_RANGE_PARCEL);
            builder = new ItemRequestData.Builder();

            targetPage = Flags.FragmentTag.SEARCH_FORM;
            if(getIntent().hasExtra(Flags.Bundle.Keys.SOURCE_PAGE)){
                int sourcePage = getIntent().getIntExtra(Flags.Bundle.Keys.SOURCE_PAGE, SEARCH_FORM_FRAGMENT);
                setSearchType(sourcePage);
                boolean searchForm;
                searchForm = getIntent().getBooleanExtra(Flags.Bundle.Keys.TO_SEARCH_FORM, false);
                switch(sourcePage) {
                    case Flags.Bundle.Values.NEW_SEARCH :
                        targetPage = Flags.FragmentTag.SEARCH_RESULT;//getTargetScreen(searchForm);
                        break;
                    case Flags.Bundle.Values.EDIT_SEARCH :
                        targetPage = Flags.FragmentTag.SEARCH_RESULT;
                        break;
                    case Flags.Bundle.Values.SEARCH_BY_KEYWORDS :
                        targetPage = getTargetScreen(searchForm);
                        break;
                    case Flags.Bundle.Values.WALL_OF_DEALS:
                        targetPage = Flags.FragmentTag.SEARCH_RESULT;//getTargetScreen(searchForm);
                        break;
                    case Flags.Bundle.Values.REFINE_SEARCH:
                        targetPage = Flags.FragmentTag.SEARCH_FORM;
                        break;
                    default:
                        targetPage = Flags.FragmentTag.SEARCH_RESULT;//getTargetScreen(searchForm);
                }
                //searchType = targetPage;
                setBuilderInstance(builder);
            }

            //controller(targetPage, null);
            pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
            mPager.addOnPageChangeListener(this);
            mPager.setOffscreenPageLimit(1);
            mPager.setAdapter(pagerAdapter);
            mPager.setCurrentItem(targetPage, true);

            tabLayout.setupWithViewPager(mPager);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    /*@Override
    protected void onShowKeyboard(int keyboardHeight) {
        // do things when keyboard is shown
        mBottomLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onHideKeyboard() {
        // do things when keyboard is hidden
        mBottomLayout.setVisibility(View.VISIBLE);
    }*/

    public int getTargetScreen(boolean searchForm) {
        int targetPage;
        if(searchForm) {
            targetPage = Flags.FragmentTag.SEARCH_FORM;
        }
        else {
            targetPage = Flags.FragmentTag.SEARCH_RESULT;
        }
        return targetPage;
    }

    public void setBuilderInstance(ItemRequestData.Builder builder){
        this.builder = builder;
    }

    /*public ItemRequestData.Builder getBuilderInstance() {
        return this.builder;
    }*/

    /*@Override
    protected void onPause() {
        super.onPause();
        if(null != fabBehaviour) {
            fabBehaviour.enableScrollToTop(false);
        }
    }*/

    @Override
    public void setRequestData(Bundle requestBundle) {
        try {
            SearchResult frag = (SearchResult) pagerAdapter.getRegisteredFragment(SEARCH_RESULT_FRAGMENT);
            frag.setRequestData(requestBundle);
        }
        catch(Exception e) {
            e.printStackTrace();
            Log.e(Flags.ActivityTag.SEARCH_ACTIVITY, e.getMessage());
        }
    }

    public boolean isFromVoiceSearch = false;

    @Override
    public void searchBarEvent(String keywords, boolean isFromResult) {
        try {
            if(isFromVoiceSearch) {
                isFromVoiceSearch = false;
                SearchForm frag = (SearchForm) pagerAdapter.getRegisteredFragment(SEARCH_FORM_FRAGMENT);
                frag.searchBarEvent(keywords, true);
                SearchResult result = (SearchResult) pagerAdapter.getRegisteredFragment(SEARCH_RESULT_FRAGMENT);
                result.searchBarEvent(keywords, false);
            }
            else {
                if(isFromResult) {
                    SearchForm frag = (SearchForm) pagerAdapter.getRegisteredFragment(SEARCH_FORM_FRAGMENT);
                    frag.searchBarEvent(keywords, true);
                }
                else {
                    SearchResult result = (SearchResult) pagerAdapter.getRegisteredFragment(SEARCH_RESULT_FRAGMENT);
                    result.searchBarEvent(keywords, false);
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            Log.e(Flags.ActivityTag.SEARCH_ACTIVITY, e.getMessage());
        }
    }

    @Override
    public void clearAllTags() {
        try {
            SearchForm frag = (SearchForm) pagerAdapter.getRegisteredFragment(SEARCH_FORM_FRAGMENT);
            frag.clearAllTags();
            SearchResult result = (SearchResult) pagerAdapter.getRegisteredFragment(SEARCH_RESULT_FRAGMENT);
            result.clearAllTags();
        }
        catch(Exception e) {
            e.printStackTrace();
            Log.e(Flags.ActivityTag.SEARCH_ACTIVITY, e.getMessage());
        }
    }

    public void clearAllTagsForResult() {
        try {
            SearchResult result = (SearchResult) pagerAdapter.getRegisteredFragment(SEARCH_RESULT_FRAGMENT);
            result.clearAllTags();
        }
        catch(Exception e) {
            e.printStackTrace();
            Log.e(Flags.ActivityTag.SEARCH_ACTIVITY, e.getMessage());
        }
    }

    @Override
    public void locationUpdate() {
        try {
            SearchForm frag = (SearchForm) pagerAdapter.getRegisteredFragment(SEARCH_FORM_FRAGMENT);
            frag.locationUpdate();

            SearchResult result = (SearchResult)pagerAdapter.getRegisteredFragment(SEARCH_RESULT_FRAGMENT);
            result.resetGeoLocIcon();
        }
        catch(Exception e) {
            e.printStackTrace();
            Log.e(Flags.ActivityTag.SEARCH_ACTIVITY, e.getMessage());
        }
    }

    @Override
    public void updateGeoCities(String cities) {
        try {
            SearchResult frag = (SearchResult) pagerAdapter.getRegisteredFragment(SEARCH_RESULT_FRAGMENT);
            frag.updateGeoCities(cities);
        }
        catch(Exception e) {
            Log.e(Flags.ActivityTag.SEARCH_ACTIVITY, "Recreating activity" + e.getMessage());
            Intent i = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(i);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        System.out.println("Search Activity onSaveInstanceState");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.searchResult = null;
        this.searchForm = null;
        System.out.println("Search Activity onDestroy");
    }

    @Override
    public void syncTagLayout(boolean isFromResultScreen, boolean isCreateTagRequest,
                              AttrValSpinnerModel attr, CommonFields rangeField, boolean isMultiSelectView, String keyname) {
        try {
            if(isFromResultScreen) {
                SearchForm frag = (SearchForm) pagerAdapter.getRegisteredFragment(SEARCH_FORM_FRAGMENT);
                frag.syncTagLayout(isFromResultScreen, isCreateTagRequest, attr, rangeField, isMultiSelectView, keyname);
            }
            else {
                SearchResult result = (SearchResult) pagerAdapter.getRegisteredFragment(SEARCH_RESULT_FRAGMENT);
                result.syncTagLayout(isFromResultScreen, isCreateTagRequest, attr, rangeField, isMultiSelectView, keyname);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            Log.e(Flags.ActivityTag.SEARCH_ACTIVITY, e.getMessage());
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        setBackToTopBtn(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        pagerAdapter.notifyDataSetChanged();
    }

    public void setBackToTopBtn(int position) {
        try {
            Fragment fragment = pagerAdapter.getRegisteredFragment(position);
            if (fragment != null) {
                if(fragment instanceof SearchResult) { //&& resultList.size() > 0
                    /*mBackToTopFab.show(new FloatingActionButton.OnVisibilityChangedListener() {
                        @Override
                        public void onShown(FloatingActionButton fab) {
                            super.onShown(fab);
                            fab.setAlpha(0.8f);
                        }
                    });*/
                    checkIfEditSearch();
                    mBottomLayout.setVisibility(View.GONE);
                }
                else {
                    mBackToTopFab.hide();
                    mFabFilter.hide();
                    mBottomLayout.setVisibility(View.VISIBLE);
                }

                /*if(fragment instanceof  SearchForm) {
                    mBottomLayout.setVisibility(View.VISIBLE);
                }
                else {

                }*/
                fragment.onResume();
                pagerAdapter.notifyDataSetChanged();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void checkIfEditSearch() {
        try {
            if(getSearchType() == Flags.Bundle.Values.EDIT_SEARCH && mPager.getCurrentItem() == SEARCH_RESULT_FRAGMENT) {
                showNewSearchFAB();
            }
            else {
                if(mFabFilter.isShown()) {
                    mFabFilter.hide();
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void keywordSearchBarController(boolean isClearText, boolean constraint) {
        try {
            SearchForm searchForm = (SearchForm) pagerAdapter.getRegisteredFragment(SEARCH_FORM_FRAGMENT);
            SearchResult searchResult = (SearchResult) pagerAdapter.getRegisteredFragment(SEARCH_RESULT_FRAGMENT);

            if(!isClearText) {
                searchForm.showKeywordSearchButtonProgress(constraint);
                searchResult.showKeywordSearchButtonProgress(constraint);
            }
            else {
                searchForm.keywordSearchClearText();
                searchResult.keywordSearchClearText();
            }
        }
        catch(Exception e) {
            Log.e("SearchActivity", e.getMessage());
            startNewSearch();
        }
    }

    private static class ScreenSlidePagerAdapter extends FragmentPagerAdapter implements OnSearchActivityCalls{
        Bundle bundle;
        String keywords;
        private String tabTitles[] = new String[] { "Search Filters", "Search Results"};
        //SearchResult searchResult = SearchResult.newInstance("SearchResult");
        SparseArrayCompat<Fragment> registeredFragments = new SparseArrayCompat<>();
        Map<Integer, String> mFragmentTags;
        FragmentManager mFragmentManager;

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
            mFragmentManager = fm;
            mFragmentTags = new HashMap<Integer, String>();
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object object = super.instantiateItem(container, position);
            if (object instanceof Fragment) {
                Fragment fragment = (Fragment) object;
                String tag = fragment.getTag();
                mFragmentTags.put(position, tag);
            }

            return object;
        }

        @Override
        public Fragment getItem(int pos) {
            /*switch(pos) {
                case Flags.FragmentTag.SEARCH_FORM :
                    return SearchForm.newInstance("SearchForm");
                case Flags.FragmentTag.SEARCH_RESULT: return SearchResult.newInstance(bundle);
                default: return SearchForm.newInstance("SearchForm");
            }*/

            Fragment fragment;
            if (pos == SEARCH_FORM_FRAGMENT) {
                fragment = searchForm;
            }
            else {
                fragment = searchResult;
                Bundle args = new Bundle();
                //args.putParcelable(Flags.Bundle.Keys.REQUEST_DATA, getBuilderInstance());
                fragment.setArguments(args);
            }
            registeredFragments.put(pos, fragment);
            return fragment;
        }
        public Fragment getFragment(int position) {
            Fragment fragment = null;
            String tag = mFragmentTags.get(position);
            if (tag != null) {
                fragment = mFragmentManager.findFragmentByTag(tag);
            }
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            //super.destroyItem(container, position, object);
        }

        public Fragment getRegisteredFragment(int position){
            return registeredFragments.get(position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public void setRequestData(Bundle requestBundle) {
            bundle = requestBundle;
        }

        @Override
        public void searchBarEvent(String keyword, boolean isFromResult) {
            keywords = keyword;
        }

        @Override
        public void locationUpdate(){}

        @Override
        public void clearAllTags() {}

        @Override
        public void updateGeoCities(String cities) {}

        @Override
        public void syncTagLayout(boolean isFromResultScreen, boolean isCreateTagRequest,
                                  AttrValSpinnerModel attr, CommonFields rangeField, boolean isMultiSelectView, String keyname) {}

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

    }       //End of PagerFragment


    @Override
    public void onBackPressed() {
        try {

            if(getFragmentManager().getBackStackEntryCount() > 0)
                getFragmentManager().popBackStack();
            else {
                if(null != mPager) {
                    if(mPager.getCurrentItem() == SEARCH_FORM_FRAGMENT) {
                        //Smooth transition needed
                        finish();
                    }
                    else if(mPager.getCurrentItem() == SEARCH_RESULT_FRAGMENT) {
                        if(searchLoadingLayout.getVisibility() != View.VISIBLE)
                            mPager.setCurrentItem(SEARCH_FORM_FRAGMENT);
                    }
                }
                else {
                    super.onBackPressed();
                }
            }
            //finish();
        }
        catch(Exception e) {
            Log.e(Flags.ActivityTag.SEARCH_ACTIVITY, e.getMessage());
        }
    }

    public void onSortParamSetTriggerRequest(int sortParam) {
        try {
            //this.sortParam = sortParam;
            SearchForm frag = (SearchForm) pagerAdapter.getRegisteredFragment(SEARCH_FORM_FRAGMENT);
            frag.sortResultCaller(sortParam);
        }
        catch(Exception e) {
            Log.e(Flags.ActivityTag.SEARCH_ACTIVITY, e.getMessage());
        }
    }

    /*public void onClickEvent(View view) {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Flags.REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    if(result.get(0).length() > 0) {
                        isFromVoiceSearch = true;
                        searchBarEvent(result.get(0), data.getBooleanExtra("ISFROMRESULT", false));
                    }
                }
                break;
            }
            case Flags.REQ_CODE_LOCATION: {
                locationUpdate();
                break;
            }
            case FLAG_INIT_LOC_REQ: {
                init();
                break;
            }
        }
    }

    public void triggerScrolling(int pos) {
        SearchForm frag = (SearchForm) pagerAdapter.getRegisteredFragment(SEARCH_FORM_FRAGMENT);
        frag.scrollToTopListener(pos);
    }

    public interface MultiSelectClickEvent {
        void multiSelectEventTrigger(String keyname);
    }

    public interface EventListener {
        void scrollToTopListener(int pos);
    }

    public void setWallOfDeals(int searchType, Bundle bundle) {
        this.parcelBundle = bundle;
        SearchForm frag = (SearchForm) pagerAdapter.getRegisteredFragment(SEARCH_FORM_FRAGMENT);
        frag.onSearchTypeChange(searchType);
        mFabFilter.hide();
    }

    public void refreshNotifier() {
        //if(!isFirstLoad) {
        SearchResult frag = (SearchResult) pagerAdapter.getRegisteredFragment(SEARCH_RESULT_FRAGMENT);
        frag.showRefreshNotifier();
        //}
    }

    public void startNewSearch() {
        this.parcelBundle = null;
        SearchForm frag = (SearchForm) pagerAdapter.getRegisteredFragment(SEARCH_FORM_FRAGMENT);
        frag.onSearchTypeChange(Flags.Bundle.Values.NEW_SEARCH);
        setSearchType(Flags.Bundle.Values.NEW_SEARCH);
        mFabFilter.hide();
    }

    public void actionTryAgain(View v) {
        loadContent();
    }

    public void showNewSearchFAB() {
        try {
            if(null != mFabFilter) {
                mFabFilter.show(new FloatingActionButton.OnVisibilityChangedListener() {
                    @Override
                    public void onShown(FloatingActionButton fab) {
                        super.onShown(fab);
                        fab.setAlpha(0.8f);
                    }
                });
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}