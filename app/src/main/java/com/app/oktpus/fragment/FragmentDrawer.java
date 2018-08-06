package com.app.oktpus.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.app.oktpus.activity.MainActivity;
import com.app.oktpus.adapter.NavigationDrawerAdapter;
import com.app.oktpus.model.NavDrawerItem;
import com.app.oktpus.constants.Flags;
import com.app.oktpus.R;
import com.app.oktpus.utils.SessionManagement;

/**
 * Created by Gyandeep on 22/9/16.
 */

public class FragmentDrawer extends Fragment {
    private RecyclerView recyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationDrawerAdapter adapter;
    private View containerView;
    private static String[] titles = null;
    private FragmentDrawerListener drawerListener;
    private SessionManagement mSession;
    public RelativeLayout drawerLayout;
    public LinearLayout mainContent;
    public int menuTxtColor;
    public List<NavDrawerItem> navDrawerList;
    public Map<Integer, NavDrawerItem> menuListData;
    public ImageView closeButton;
    public FragmentDrawer() {}

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    /*public static List<NavDrawerItem> getData(SessionManagement session, String [] titles) {
        List<NavDrawerItem> data = new ArrayList<>();

        if(session.isLoggedIn()) {
            NavDrawerItem ndi = new NavDrawerItem();
            ndi.setTitle(session.getUsername());
            data.add(0, ndi);
        }


        // preparing navigation drawer items
        for (int i = 0; i < titles.length; i++) {
            NavDrawerItem navItem = new NavDrawerItem();
            navItem.setTitle(titles[i]);
            data.add(navItem);
        }
        return data;
    }*/

    public Map<Integer, NavDrawerItem> getData(SessionManagement session) {
        if(session.isLoggedIn()) {
            if(session.isSocialLogin())
                return loggedInSocialMenu();
            else
                return loggedInMenu();
        }
        else
            return notLoggedInMenu();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSession = new SessionManagement(getActivity());
        // drawer labels
        navDrawerList = new ArrayList<NavDrawerItem>();
        menuListData = new HashMap<>();
    }

    @Override
    public void onResume() {
        super.onResume();
        //Temporary, it can be removed once MainActivity and BaseActivity are merged as one.
        updateMenu();
    }

    /*public String[] loadDrawerLabels(SessionManagement session) {
        if(!session.isLoggedIn())
            titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels);
        *//*else if(session.isSocialLogin())
            titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels_logged_in_social);*//*
        else titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels_logged_in);

        drawerLayout.setBackgroundColor(getResources().getColor(R.color.white));
        menuTxtColor = R.color.navigationBarColor;

        return titles;
    }*/

    public void updateMenu() {
        try {
            if(adapter != null) {
                /*List<NavDrawerItem> list;
                list = getData(mSession, loadDrawerLabels(mSession));*/
                /*if(navDrawerList!= null) navDrawerList.clear();
                else navDrawerList = new ArrayList<>();

                for(NavDrawerItem item : list) navDrawerList.add(item);*/

                /*if(null != menuListData) menuListData.clear();
                else menuListData = new HashMap<>();
                menuListData = getData(mSession);
                adapter.notifyDataSetChanged();*/
            }
        }
        catch(Exception e){
            Log.e("Login", e.getMessage());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.fragment_nav_drawer, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        drawerLayout = (RelativeLayout) layout.findViewById(R.id.frag_drawer_layout);
        //closeButton = (ImageView) layout.findViewById(R.id.menu_close_btn);
        //navDrawerList = getData(mSession, loadDrawerLabels(mSession));
        //adapter = new NavigationDrawerAdapter(getActivity(), navDrawerList, this.menuTxtColor);
        drawerLayout.setBackgroundColor(getResources().getColor(R.color.white));
        menuTxtColor = R.color.navigationBarColor;

        menuListData = getData(mSession);
        adapter = new NavigationDrawerAdapter(getActivity(), menuListData, this.menuTxtColor, mSession);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(null != drawerListener) {
                    drawerListener.onDrawerItemSelected(view, position);
                    mDrawerLayout.closeDrawer(containerView);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        /*closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(containerView);
            }
        });*/

        return layout;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerLayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);

                if(getActivity() instanceof MainActivity) {
                    float moveFactor = (recyclerView.getWidth() * slideOffset);
                    mainContent = (LinearLayout) getActivity().findViewById(R.id.main_content);
                    mainContent.setTranslationX(-moveFactor);
                }
                else {
                    float moveFactor = (recyclerView.getWidth() * slideOffset);
                    RelativeLayout mainContent = (RelativeLayout) getActivity().findViewById(R.id.root);
                    mainContent.setTranslationX(-moveFactor);
                }
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    /*View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }*/
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {}

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public interface FragmentDrawerListener {
        void onDrawerItemSelected(View view, int position);
    }

    /*public int getIcon(int pos){
        int iconId;
        switch(pos) {
            case Flags.NavMenu.NOT_LOGGED_IN.SIGNUP:
        }
    }*/

    public Map<Integer, NavDrawerItem> notLoggedInMenu() {
        Map<Integer, NavDrawerItem> map = new HashMap<>();
        map.put(Flags.NavMenu.NOT_LOGGED_IN.SEARCH, new NavDrawerItem(getResources().getString(R.string.nav_item_search), R.drawable.ic_search_nav));
        map.put(Flags.NavMenu.NOT_LOGGED_IN.WALL_OF_DEALS, new NavDrawerItem(getResources().getString(R.string.nav_item_wall_of_deals), R.drawable.ic_wall_of_deals));
        map.put(Flags.NavMenu.NOT_LOGGED_IN.SIGNUP, new NavDrawerItem(getResources().getString(R.string.nav_item_signup), R.drawable.ic_sign_up));
        map.put(Flags.NavMenu.NOT_LOGGED_IN.LOGIN, new NavDrawerItem(getResources().getString(R.string.nav_item_login), R.drawable.ic_log_in));
        map.put(Flags.NavMenu.NOT_LOGGED_IN.ABOUT, new NavDrawerItem(getResources().getString(R.string.nav_item_about), 0));
        map.put(Flags.NavMenu.NOT_LOGGED_IN.COMPARE_CARS, new NavDrawerItem(getResources().getString(R.string.nav_item_compare_cars), R.drawable.ic_compare_cars));
        map.put(Flags.NavMenu.NOT_LOGGED_IN.CAR_INSURANCE, new NavDrawerItem(getResources().getString(R.string.nav_item_car_insurance), R.drawable.ic_car_insurance));
        map.put(Flags.NavMenu.NOT_LOGGED_IN.CAR_FINANCE, new NavDrawerItem(getResources().getString(R.string.nav_item_car_finance), R.drawable.ic_car_finance));
        map.put(Flags.NavMenu.NOT_LOGGED_IN.COUPON, new NavDrawerItem(getResources().getString(R.string.nav_item_coupon), R.drawable.ic_car_finance));
        map.put(Flags.NavMenu.NOT_LOGGED_IN.CAR_PARTS, new NavDrawerItem(getResources().getString(R.string.nav_item_car_parts), R.drawable.ic_car_finance));
        map.put(Flags.NavMenu.NOT_LOGGED_IN.WMCW, new NavDrawerItem(getResources().getString(R.string.nav_item_wmcw), R.drawable.ic_wmcw));
        //map.put(Flags.NavMenu.NOT_LOGGED_IN.TERMS_AND_CONDITIONS, new NavDrawerItem(getResources().getString(R.string.nav_item_tnc), 0));
        return map;
    }

    public Map<Integer, NavDrawerItem> loggedInMenu() {
        Map<Integer, NavDrawerItem> map = new HashMap<>();
        map.put(Flags.NavMenu.LOGGED_IN.SEARCH, new NavDrawerItem(getResources().getString(R.string.nav_item_search), R.drawable.ic_search_nav));
        map.put(Flags.NavMenu.LOGGED_IN.WALL_OF_DEALS, new NavDrawerItem(getResources().getString(R.string.nav_item_wall_of_deals), R.drawable.ic_wall_of_deals));
        map.put(Flags.NavMenu.LOGGED_IN.SAVED_SEARCHES, new NavDrawerItem(getResources().getString(R.string.nav_item_saved_searches), R.drawable.ic_saved_search));
        map.put(Flags.NavMenu.LOGGED_IN.NOTIFICATION_HISTORY, new NavDrawerItem(getResources().getString(R.string.nav_item_notif_history), R.drawable.ic_notifications));
        map.put(Flags.NavMenu.LOGGED_IN.GARAGE, new NavDrawerItem(getResources().getString(R.string.nav_item_garage), R.drawable.ic_garage_black));
        map.put(Flags.NavMenu.LOGGED_IN.SETTINGS, new NavDrawerItem(getResources().getString(R.string.nav_item_settings), R.drawable.ic_settings));
        map.put(Flags.NavMenu.LOGGED_IN.LOGOUT, new NavDrawerItem(getResources().getString(R.string.nav_item_logout), R.drawable.ic_log_out));
        map.put(Flags.NavMenu.LOGGED_IN.COMPARE_CARS, new NavDrawerItem(getResources().getString(R.string.nav_item_compare_cars), R.drawable.ic_compare_cars));
        map.put(Flags.NavMenu.LOGGED_IN.CAR_INSURANCE, new NavDrawerItem(getResources().getString(R.string.nav_item_car_insurance), R.drawable.ic_car_insurance));
        map.put(Flags.NavMenu.LOGGED_IN.CAR_FINANCE, new NavDrawerItem(getResources().getString(R.string.nav_item_car_finance), R.drawable.ic_car_finance));
        map.put(Flags.NavMenu.NOT_LOGGED_IN.COUPON, new NavDrawerItem(getResources().getString(R.string.nav_item_coupon), R.drawable.ic_car_finance));
        map.put(Flags.NavMenu.NOT_LOGGED_IN.CAR_PARTS, new NavDrawerItem(getResources().getString(R.string.nav_item_car_parts), R.drawable.ic_car_finance));
        map.put(Flags.NavMenu.LOGGED_IN.ABOUT, new NavDrawerItem(getResources().getString(R.string.nav_item_about), 0));
        map.put(Flags.NavMenu.LOGGED_IN.WMCW, new NavDrawerItem(getResources().getString(R.string.nav_item_wmcw), R.drawable.ic_wmcw));
        //map.put(Flags.NavMenu.LOGGED_IN.TERMS_AND_CONDITIONS, new NavDrawerItem(getResources().getString(R.string.nav_item_tnc), 0));

        return map;
    }

    public Map<Integer, NavDrawerItem> loggedInSocialMenu() {
        Map<Integer, NavDrawerItem> map = new HashMap<>();
        map.put(Flags.NavMenu.LOGGED_IN_SOCIAL.SEARCH, new NavDrawerItem(getResources().getString(R.string.nav_item_search), R.drawable.ic_search_nav));
        map.put(Flags.NavMenu.LOGGED_IN_SOCIAL.WALL_OF_DEALS, new NavDrawerItem(getResources().getString(R.string.nav_item_wall_of_deals), R.drawable.ic_wall_of_deals));
        map.put(Flags.NavMenu.LOGGED_IN_SOCIAL.SAVED_SEARCHES, new NavDrawerItem(getResources().getString(R.string.nav_item_saved_searches), R.drawable.ic_saved_search));
        map.put(Flags.NavMenu.LOGGED_IN_SOCIAL.NOTIFICATION_HISTORY, new NavDrawerItem(getResources().getString(R.string.nav_item_notif_history), R.drawable.ic_notifications));
        map.put(Flags.NavMenu.LOGGED_IN_SOCIAL.GARAGE, new NavDrawerItem(getResources().getString(R.string.nav_item_garage), R.drawable.ic_garage_black));
        map.put(Flags.NavMenu.LOGGED_IN_SOCIAL.LOGOUT, new NavDrawerItem(getResources().getString(R.string.nav_item_logout), R.drawable.ic_log_out));
        map.put(Flags.NavMenu.LOGGED_IN_SOCIAL.COMPARE_CARS, new NavDrawerItem(getResources().getString(R.string.nav_item_compare_cars), R.drawable.ic_compare_cars));
        map.put(Flags.NavMenu.LOGGED_IN_SOCIAL.CAR_INSURANCE, new NavDrawerItem(getResources().getString(R.string.nav_item_car_insurance), R.drawable.ic_car_insurance));
        map.put(Flags.NavMenu.LOGGED_IN_SOCIAL.CAR_FINANCE, new NavDrawerItem(getResources().getString(R.string.nav_item_car_finance), R.drawable.ic_car_finance));
        map.put(Flags.NavMenu.LOGGED_IN_SOCIAL.COUPON, new NavDrawerItem(getResources().getString(R.string.nav_item_coupon), R.drawable.ic_car_finance));
        map.put(Flags.NavMenu.LOGGED_IN_SOCIAL.CAR_PARTS, new NavDrawerItem(getResources().getString(R.string.nav_item_car_parts), R.drawable.ic_car_finance));
        map.put(Flags.NavMenu.LOGGED_IN_SOCIAL.ABOUT, new NavDrawerItem(getResources().getString(R.string.nav_item_about), 0));
        map.put(Flags.NavMenu.LOGGED_IN_SOCIAL.WMCW, new NavDrawerItem(getResources().getString(R.string.nav_item_wmcw), R.drawable.ic_wmcw));
        //map.put(Flags.NavMenu.LOGGED_IN_SOCIAL.TERMS_AND_CONDITIONS, new NavDrawerItem(getResources().getString(R.string.nav_item_tnc), 0));

        return map;
    }
}