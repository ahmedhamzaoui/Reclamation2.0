package reclamation.dev.com.reclamation20.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

import reclamation.dev.com.reclamation20.Profile.ProfileActivity;
import reclamation.dev.com.reclamation20.R;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    public static final String PREFS_NAME = "LoginPrefs";


    private Context mContext;
    private ProgressBar mProgressBar;
    private EditText mEmail, mPassword;
    private TextView mPleaseWait,mlink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mProgressBar = (ProgressBar) findViewById(R.id.loginRequestLoadingProgressbar);
        mPleaseWait = (TextView) findViewById(R.id.pleaseWait);
        mlink = (TextView) findViewById(R.id.link_signup);
        mEmail = (EditText) findViewById(R.id.input_email);
        mPassword = (EditText) findViewById(R.id.input_password);
        mContext = LoginActivity.this;
        Log.d(TAG, "onCreate: started.");

        final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getString("logged", "").toString().equals("logged")) {
            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
            startActivity(intent);

        }
        mPleaseWait.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });
        //initialize the button for logging in
        Button btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: attempting to log in.");

                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();

                if (isStringNull(email) || isStringNull(password)) {
                    Toast.makeText(mContext, "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                } else {

                    RequestQueue MyRequestQueue = Volley.newRequestQueue(mContext);


                    String url = "http://192.168.1.20/rec/web/app_dev.php/s/users/auth";
                    System.out.println(url);
                    StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //This code is executed if the server responds, whether or not the response contains data.
                            //The String 'response' contains the server's response.

                            System.out.println("idddddddddddddddddddddd"+response);
                            if (!settings.getString("logged", "").toString().equals("logged")){
                                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("logged", "logged");
                                editor.putString("iduser",""+response);
                                editor.apply();
                                Toast.makeText(getApplicationContext(), "Successfull Login", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                                startActivity(intent);
                            }
                            mProgressBar.setVisibility(View.GONE);
                            mPleaseWait.setVisibility(View.GONE);

                            System.out.println("response" + response);


                        }
                    }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //This code is executed if there is an error.
                        }
                    }) {
                        protected Map<String, String> getParams() {
                            Map<String, String> MyData = new HashMap<String, String>();
                            MyData.put("email", email); //Add the data you'd like to send to the server.
                            MyData.put("password", password); //Add the data you'd like to send to the server.
                            return MyData;
                        }
                    };
                    MyRequestQueue.add(MyStringRequest);

                }


            }
        });
    }


    private boolean isStringNull(String string) {
        Log.d(TAG, "isStringNull: checking string if null.");

        if (string.equals("")) {
            return true;
        } else {
            return false;
        }
    }


}
