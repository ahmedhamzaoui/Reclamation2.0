package reclamation.dev.com.reclamation20.Profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import reclamation.dev.com.reclamation20.MapActivity;
import reclamation.dev.com.reclamation20.R;
import reclamation.dev.com.reclamation20.Utils.ButtomNavigationHelper;
import reclamation.dev.com.reclamation20.Utils.UniversalImageLoader;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;
import static reclamation.dev.com.reclamation20.Login.LoginActivity.PREFS_NAME;


public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private static final int ACTIVITY_NUM = 4;
    private static final int NUM_GRID_COLUMNS = 3;


    private TextView mPosts, mFollowers, mFollowing, mDsiplayName, mUserName, mWebSite, mDescription;
    private ProgressBar progressBar;
    private CircleImageView mProfilePhoto;
    private GridView gridView;
    private Toolbar toolbar;
    private ImageView profileMenu;
    private BottomNavigationView bottomNavigationView;
    private Context mcontext;
    Context context;

    private String email;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mDsiplayName = (TextView) view.findViewById(R.id.display_name);
        mUserName = (TextView) view.findViewById(R.id.username);
        mDescription = (TextView) view.findViewById(R.id.description);
        mProfilePhoto = (CircleImageView) view.findViewById(R.id.profile_photo);
        mPosts = (TextView) view.findViewById(R.id.tvPosts);
        mFollowers = (TextView) view.findViewById(R.id.tvFollowers);
        mFollowing = (TextView) view.findViewById(R.id.tvFollowing);
        progressBar = (ProgressBar) view.findViewById(R.id.profileProgressBar);
        gridView = (GridView) view.findViewById(R.id.gridView);
        toolbar = (Toolbar) view.findViewById(R.id.profileToolBar);
        profileMenu = (ImageView) view.findViewById(R.id.profileImage);
        bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottomNavViewBar);
        TextView editProfile = (TextView) view.findViewById(R.id.textEditProfile);
        mcontext=getActivity();
        ButtomNavigationHelper.enableNavigation(mcontext,getActivity(),  bottomNavigationView);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);


        final SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);

        System.out.println("userrrrrrrrrrrrrrrrrrrrrrrr"+settings.getString("userid",""));
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getActivity());


        String url = "http://192.168.8.105/rec/web/app_dev.php/s/users/profile";
        System.out.println(url);
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.println("response:         "+response);

                try {
                    JSONObject jsonArray = new JSONObject(response);
                        mDsiplayName.setText(jsonArray.get("fullname").toString());
                        mDescription.setText(jsonArray.get("description").toString());

                        if (jsonArray.get("idflowers").toString().equals("null")){

                        mFollowers.setText("0");
                    }else {

                        mFollowers.setText(jsonArray.get("idflowers").toString());
                    }
                    if (jsonArray.get("idfollowing").toString().equals("null")){

                        mFollowing.setText("0");
                    }else {

                        mFollowing.setText(jsonArray.get("idfollowing").toString());
                    }
                    mUserName.setText(jsonArray.get("username").toString());

                    System.out.println(jsonArray.get("path").toString());

                    email=jsonArray.get("email").toString();
                    mPosts.setText(jsonArray.get("idfollowing").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("id", settings.getString("userid","")); //Add the data you'd like to send to the server.
                return MyData;
            }
        };
        MyRequestQueue.add(MyStringRequest);




        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"on click navigating to:");

                EditProfileFragment fragment = new EditProfileFragment();
                Bundle args = new Bundle();
                args.putString("fullname",mDsiplayName.getText().toString());
                args.putString("email",email);
                args.putString("description",mDescription.getText().toString());
                args.putString("username",mUserName.getText().toString());
                fragment.setArguments(args);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragment);
                transaction.addToBackStack(getString(R.string.profile_fragment));
                transaction.commit();

            }
        });


        return view;
    }

}

