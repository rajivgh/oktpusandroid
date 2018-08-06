package com.app.oktpus.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.oktpus.fragment.FragmentDrawer;
import com.app.oktpus.R;
import com.app.oktpus.utils.SessionManagement;
import com.facebook.FacebookSdk;
import com.kyleduo.switchbutton.SwitchButton;

import static com.app.oktpus.utils.NavigationMenuItems.selectDrawerItem;

public class BaseActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener{

    public static final String TAG  = "BaseActivity";
    public Toolbar mToolbar;
    public AppBarLayout mAppBarLayout;
    private FragmentDrawer drawerFragment;
    private DrawerLayout mDrawerLayout;
    public ActionBar actionBar;
    public FloatingActionButton mFabFilter, mBackToTopFab;
    private CoordinatorLayout mcoordinatorLayout;
    public CardView mBottomLayout, mBottomLayoutContent;
    private SessionManagement mSession;

    //public ProgressBar pbSearchLayout;
    public TextView tvSearchButton, tvCreateAcLink;
    public RelativeLayout btnSearch, rootContainer, mBottomStickyButton;
    public RelativeLayout saveSearchLayout, receiveNotificationLayout;
    public SwitchButton receiveNotificationSwitch, saveSearchSwitch;
    public ImageView ivReceiveNotifFeatureHelp;
    public LinearLayout llCreateAcInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Facebook SDK initialisation
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_base);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mBottomLayout = (CardView) findViewById(R.id.ll_bottom);
        mBottomLayoutContent = (CardView) findViewById(R.id.sf_fixed_bottom_layout);
        rootContainer = (RelativeLayout) findViewById(R.id.root);
        //mBottomStickyButton = (RelativeLayout) findViewById(R.id.rl_sticky_bottom_button);
        //Search Form Bottom Layout
        btnSearch = (RelativeLayout) findViewById(R.id.rl_btn_search);
        saveSearchLayout = (RelativeLayout) findViewById(R.id.save_search_layout);
        receiveNotificationLayout = (RelativeLayout) findViewById(R.id.receive_notif_layout);
        saveSearchSwitch = (SwitchButton) findViewById(R.id.save_search_checkbox);
        receiveNotificationSwitch = (SwitchButton) findViewById(R.id.chkbox_receive_notif);
        ivReceiveNotifFeatureHelp = (ImageView) findViewById(R.id.iv_receive_notif_help);

        //pbSearchLayout = (ProgressBar) findViewById(R.id.pb_search);
        tvSearchButton = (TextView) findViewById(R.id.tv_search_button);

        mSession = new SessionManagement(this);
        if(!mSession.isLoggedIn()) {
            tvCreateAcLink = (TextView) findViewById(R.id.tv_sf_create_ac_msg);
            llCreateAcInfo = (LinearLayout) findViewById(R.id.ll_sf_create_ac_msg);


            SpannableString ss = new SpannableString(getResources().getString(R.string.create_new_ac)
                    + " " + getResources().getString(R.string.sf_create_ac_desc));
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    startActivity(new Intent(getApplicationContext(), SignupActivity.class));
                }
            };
            ss.setSpan(clickableSpan, 0, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.link)), 0, 18, 0);
            ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.sf_create_ac_msg)), 19, ss.length(), 0);

            tvCreateAcLink.setText(ss);
            tvCreateAcLink.setMovementMethod(LinkMovementMethod.getInstance());
            llCreateAcInfo.setVisibility(View.VISIBLE);
        }


        mFabFilter = (FloatingActionButton) findViewById(R.id.fab);
        mBackToTopFab = (FloatingActionButton) findViewById(R.id.next_item_fab);
        mBackToTopFab.hide();
        mFabFilter.hide();

        /*FAB buttons*/
            /*fabBehaviour = new FABBehaviour();
            CoordinatorLayout.LayoutParams params =
                    (CoordinatorLayout.LayoutParams) mFabFilter.getLayoutParams();
            params.setBehavior(fabBehaviour);*/

        setSupportActionBar(mToolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);


        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        drawerFragment.setUp(R.id.fragment_navigation_drawer, mDrawerLayout, mToolbar);
        drawerFragment.setDrawerListener(this);
        //fragmentHolder = (FrameLayout)findViewById(R.id.fragment_placeholder_base);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /*private CharSequence wrapInSpan(CharSequence value) {
        SpannableStringBuilder sb = new SpannableStringBuilder(value);
        sb.setSpan(AppController.getFontType(getApplicationContext()), 0, value.length(), 0);
        return sb;
    }*/

    /*private ViewTreeObserver.OnGlobalLayoutListener keyboardLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            int heightDiff = rootLayout.getRootView().getHeight() - rootLayout.getHeight();
            int contentViewTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();

            LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(BaseActivity.this);

            if(heightDiff <= contentViewTop){
                onHideKeyboard();

                Intent intent = new Intent("KeyboardWillHide");
                broadcastManager.sendBroadcast(intent);
            } else {
                int keyboardHeight = heightDiff - contentViewTop;
                onShowKeyboard(keyboardHeight);

                Intent intent = new Intent("KeyboardWillShow");
                intent.putExtra("KeyboardHeight", keyboardHeight);
                broadcastManager.sendBroadcast(intent);
            }
        }
    };*/

    private boolean keyboardListenersAttached = false;
    private ViewGroup rootLayout;

    protected void onShowKeyboard(int keyboardHeight) {}
    protected void onHideKeyboard() {}

    /*protected void attachKeyboardListeners() {
        if (keyboardListenersAttached) {
            return;
        }

        rootLayout = (ViewGroup) findViewById(R.id.main_content);
        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(keyboardLayoutListener);

        keyboardListenersAttached = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (keyboardListenersAttached) {
            rootLayout.getViewTreeObserver().removeGlobalOnLayoutListener(keyboardLayoutListener);
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base_title, menu);
        LinearLayout iv = (LinearLayout) menu.getItem(0).getActionView().findViewById(R.id.ac_title);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                /*if(!mSession.isLoggedIn()) {
                    i = new Intent(getApplicationContext(), MainActivity.class);
                }
                else*/
                i = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(i);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        selectDrawerItem(this, position, mSession);
    }

    public void includeNavigationMenu(Context context, int resource)
    {
        try {
            ViewGroup vg = (ViewGroup) findViewById(R.id.main_content);
            ViewGroup.inflate(context, resource, vg);
        } catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}