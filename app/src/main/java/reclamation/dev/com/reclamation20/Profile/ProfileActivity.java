package reclamation.dev.com.reclamation20.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import reclamation.dev.com.reclamation20.Home.MainActivity;
import reclamation.dev.com.reclamation20.R;
import reclamation.dev.com.reclamation20.Profile.ProfileFragment;
import reclamation.dev.com.reclamation20.Utils.ButtomNavigationHelper;


public class ProfileActivity extends FragmentActivity {

    private static final String TAG="ProfileActivity";

    BottomNavigationView navigation;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ProfileFragment fragment = new ProfileFragment();
        FragmentTransaction transaction = ProfileActivity.this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(getString(R.string.profile_fragment));
        transaction.commit();
    }


}
