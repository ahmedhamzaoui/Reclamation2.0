package reclamation.dev.com.reclamation20.Share;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;



import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.Manifest;


import reclamation.dev.com.reclamation20.Models.Image;
import reclamation.dev.com.reclamation20.Utils.SquareImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Permission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import reclamation.dev.com.reclamation20.Constants;
import reclamation.dev.com.reclamation20.Home.HomeFragment;
import reclamation.dev.com.reclamation20.Home.MainActivity;
import reclamation.dev.com.reclamation20.Home.SectionPagerAdapter;
import reclamation.dev.com.reclamation20.R;
import reclamation.dev.com.reclamation20.Utils.ButtomNavigationHelper;
import reclamation.dev.com.reclamation20.Utils.SquareImageView;

import static reclamation.dev.com.reclamation20.Login.LoginActivity.PREFS_NAME;

public class AddPostActivity extends AppCompatActivity {
    private Context mContext;


    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS
    };
    private static final int INITIAL_REQUEST=1337;
    private static final int LOCATION_REQUEST=INITIAL_REQUEST+1;
    SharedPreferences settings;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    private Boolean mLocationPermissionsGranted = false;





    private final int IMG_REQUEST=1;
    private Bitmap bitmap;
    ImageView choixPic;
    EditText titre,description,tag;
    TextView addbout;
    int b;
    String longitude,latitude,url;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private static final String ADDBOUT_URL = Constants.URL_ADD;

    private static final int ACTIVITY_NUM = 2;
    BottomNavigationView navigation;

    private FrameLayout mFrameLayout;
    private RelativeLayout mRelativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
          settings = getSharedPreferences(PREFS_NAME, 0);
        ///GEOLOCALISATION


        //requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);






        //getting the current user


        choixPic = (ImageView) findViewById(R.id.picimg);

        titre = (EditText) findViewById(R.id.titre);
        description = (EditText) findViewById(R.id.description);
        tag = (EditText) findViewById(R.id.tag);
        addbout = (TextView) findViewById(R.id.addbout);

        getLocationPermission();
        choixPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        addbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                uploadImage();
                finish();
               // startActivity(new Intent(AddPostActivity.this, MainActivity.class));
            }
        });





    }

    private void selectImage()
    {
        Intent intent1 = new Intent();
        intent1.setType("image/*");
        intent1.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent1,IMG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMG_REQUEST && resultCode==RESULT_OK && data!=null)
        {
            Uri path= data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                Bitmap resized = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
                choixPic.setImageBitmap(resized);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private String imageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }


    private void uploadImage()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,ADDBOUT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String Response = jsonObject.getString("response");
                    Toast.makeText(AddPostActivity.this, "Reclamation Ajoutée", Toast.LENGTH_SHORT).show();
                    Toast.makeText(AddPostActivity.this,Response, Toast.LENGTH_LONG).show();



                    Intent intent = new Intent(AddPostActivity.this, MainActivity.class);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddPostActivity.this, "Try Again"+error, Toast.LENGTH_SHORT).show();
                System.out.println(error);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("titre",titre.getText().toString().trim());
                params.put("description",description.getText().toString().trim());
                params.put("tag",tag.getText().toString().trim());
                params.put("userid",settings.getString("userid",""));
                params.put("longitude","38.2222222222222222");
                params.put("latitude","38.2222222222222222");
                params.put("image",imageToString(bitmap));
                     System.out.println("params: "+ settings.getString("userid",""));
                return params;

            }
        };
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());

        MyRequestQueue.add(stringRequest);

    }



    private void getLocationPermission(){

        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;


                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        longitude = location.getLongitude()+"";
                        latitude = location.getLatitude()+"";
                        Log.d("geolocalisation","  longitude :"+longitude+"  latitude :"+latitude);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {}
                    @Override
                    public void onProviderEnabled(String provider) {}

                    @Override
                    public void onProviderDisabled(String provider) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                };

                locationManager.requestLocationUpdates("gps",50,0,locationListener);





            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

}
