package reclamation.dev.com.reclamation20.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;


import reclamation.dev.com.reclamation20.Home.MainActivity;
import reclamation.dev.com.reclamation20.MapActivity;
import reclamation.dev.com.reclamation20.Profile.ProfileActivity;
import reclamation.dev.com.reclamation20.R;
import reclamation.dev.com.reclamation20.Share.AddPostActivity;

public class ButtomNavigationHelper {
    private static final String TAG = "ButtomNavigation";

    public static void setupButtomNavigationView(BottomNavigationView bottomNavigationView){

    }
    public static void  enableNavigation(final Context context, final Activity callingActivity, BottomNavigationView view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.ic_house:
                        Intent intent1 = new Intent(context,MainActivity.class);
                        context.startActivity(intent1);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        return true;
                    case R.id.ic_map:
                        Intent intent2 = new Intent(context,MapActivity.class);
                        context.startActivity(intent2);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        //mTextMessage.setText(R.string.title_dashboard);
                        return true;
                    case R.id.ic_circle:
                        //TO DOOOOO


                        Intent intent3 = new Intent(context,AddPostActivity.class);
                        context.startActivity(intent3);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        //mTextMessage.setText(R.string.title_notifications);
                        return true;

                    case R.id.ic_fav:
                     /*   Intent intent4 = new Intent(context,FavoritesActivity.class);
                        context.startActivity(intent4);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        //mTextMessage.setText(R.string.title_notifications);
                        return true;*/

                    case R.id.ic_profile:
                        Intent intent5 = new Intent(context,ProfileActivity.class);
                        context.startActivity(intent5);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        //mTextMessage.setText(R.string.title_notifications);
                        return true;

                }
                return false;
            }

        });
    }

}
