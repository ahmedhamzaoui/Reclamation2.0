package reclamation.dev.com.reclamation20.Home;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;

import reclamation.dev.com.reclamation20.Models.Photo;
import reclamation.dev.com.reclamation20.Models.UserAccountSettings;
import reclamation.dev.com.reclamation20.R;
import reclamation.dev.com.reclamation20.Utils.ButtomNavigationHelper;
import reclamation.dev.com.reclamation20.Utils.MainfeedListAdapter;
import reclamation.dev.com.reclamation20.Utils.UniversalImageLoader;

public class  MainActivity extends AppCompatActivity implements
        MainfeedListAdapter.OnLoadMoreItemsListener {





    @Override
    public void onLoadMoreItems() {
        Log.d(TAG, "onLoadMoreItems: displaying more photos");
        HomeFragment fragment = (HomeFragment)getSupportFragmentManager()
        .findFragmentByTag("android:switcher:" + R.id.container12 + ":" + mViewPager.getCurrentItem());
        if(fragment != null){
            fragment.displayMorePhotos();
        }
    }



    private static final String  TAG="MainActivity";

    private static final int ACTIVITY_NUM = 0;
    BottomNavigationView navigation;
    private Context mContext = MainActivity.this;



    private ViewPager mViewPager;
    private FrameLayout mFrameLayout;
    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigation = (BottomNavigationView) findViewById(R.id.bottomNavViewBar);

        //navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        ButtomNavigationHelper.enableNavigation(MainActivity.this, this, navigation);
        Menu menu = navigation.getMenu();
        mFrameLayout = (FrameLayout) findViewById(R.id.container);
            mRelativeLayout = (RelativeLayout) findViewById(R.id.relLayoutParent);
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
        initImageLoader();
        setupViewPager();



       // mAuth.signOut();
    }

    public void onCommentThreadSelected(Photo photo, UserAccountSettings settings){
        Log.d(TAG, "onCommentThreadSelected: selected a comment thread");

      /*  ViewCommentsFragment fragment  = new ViewCommentsFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.bundle_photo), photo);
        args.putParcelable(getString(R.string.bundle_user_account_settings), settings);
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(getString(R.string.view_comments_fragment));
        transaction.commit();
*/
    }


    public void hideLayout(){
        Log.d(TAG, "hideLayout: hiding layout");
        mRelativeLayout.setVisibility(View.GONE);
        mFrameLayout.setVisibility(View.VISIBLE);
    }


    public void showLayout(){
        Log.d(TAG, "hideLayout: showing layout");
        mRelativeLayout.setVisibility(View.VISIBLE);
        mFrameLayout.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(mFrameLayout.getVisibility() == View.VISIBLE){
            showLayout();
        }
    }


    private void initImageLoader(){
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }


    private void setupViewPager (){
        SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment());

        ViewPager viewPager = (ViewPager) findViewById(R.id.container12);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById( R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        //tabLayout.getTabAt(0).setIcon(R.drawable.ic_house);
        //tabLayout.getTabAt(1).setIcon(R.drawable.ic_arrow);

    }





}
