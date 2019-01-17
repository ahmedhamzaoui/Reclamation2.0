package reclamation.dev.com.reclamation20.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import reclamation.dev.com.reclamation20.Profile.ProfileActivity;
import reclamation.dev.com.reclamation20.R;

import static reclamation.dev.com.reclamation20.Login.LoginActivity.PREFS_NAME;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private static final String URL_REGISTER = "http://192.168.1.20/rec/web/app_dev.php/s/users/new";

    private Context mContext;
    private String email, username, password;
    private EditText mEmail, mPassword, mUsername;
    private TextView loadingPleaseWait;
    private Button btnRegister;
    private ProgressBar mProgressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContext = RegisterActivity.this;
        mEmail = (EditText) findViewById(R.id.input_email1);
        mPassword = (EditText) findViewById(R.id.input_password1);
        mUsername = (EditText) findViewById(R.id.username1);


        btnRegister = (Button) findViewById(R.id.btn_register);

        final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: attempting to log in.");
                email = mEmail.getText().toString();
                username = mUsername.getText().toString();
                password = mPassword.getText().toString();
                System.out.println(email+" "+password+" "+username);
                if (isStringNull(email)) {
                    Toast.makeText(mContext, "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                } else if (isStringNull(password)) {
                    Toast.makeText(mContext, "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                } else if (isStringNull(username)) {
                    Toast.makeText(mContext, "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                } else {

                    RequestQueue MyRequestQueue = Volley.newRequestQueue(mContext);


                    String url = "http://192.168.1.20/rec/web/app_dev.php/s/users/new";
                    System.out.println(url);
                    StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println(response);
                                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("logged", "logged");
                                editor.putString("iduser",""+response);
                                editor.apply();
                                Toast.makeText(getApplicationContext(), "Successfull Registred", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(RegisterActivity.this, ProfileActivity.class);
                                startActivity(intent);
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
                            MyData.put("username", username);
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
