package reclamation.dev.com.reclamation20.Dialog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import reclamation.dev.com.reclamation20.Login.LoginActivity;
import reclamation.dev.com.reclamation20.Profile.EditProfileFragment;
import reclamation.dev.com.reclamation20.Profile.ProfileActivity;
import reclamation.dev.com.reclamation20.R;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;
import static reclamation.dev.com.reclamation20.Login.LoginActivity.PREFS_NAME;


public class ConfirmPasswordDialog extends DialogFragment {

    private static final String TAG="ConfirmPasswordDialog";


    //vars
    TextView mPassword;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_confirm_password, container, false);
        mPassword = (TextView) view.findViewById(R.id.confirm_password);

        Log.d(TAG, "onCreateView: started.");



        TextView confirmDialog = (TextView) view.findViewById(R.id.dialogConfirm);
        confirmDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: captured password and confirming.");

                String password = mPassword.getText().toString();
                if(!password.equals("")){


                    onConfirmPassword(password);
                    getDialog().dismiss();
                }else{
                    Toast.makeText(getActivity(), "you must enter a password", Toast.LENGTH_SHORT).show();
                }

            }
        });

        TextView cancelDialog = (TextView) view.findViewById(R.id.dialogCancel);
        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing the dialog");
                getDialog().dismiss();
            }
        });


        return view;
    }

    public void onConfirmPassword(String password){
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getActivity());
        final SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);


        String url = "http://192.168.1.20/rec/web/app_dev.php/s/users/confirme";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
                if (settings.getString("iduser", "").equals(response)) {



                }
                else{
                    Toast.makeText(getApplicationContext(), "Password incorrect", Toast.LENGTH_SHORT).show();

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
                MyData.put("id", settings.getString("iduser", "")); //Add the data you'd like to send to the server.
                MyData.put("password", password); //Add the data you'd like to send to the server.
                return MyData;
            }
        };
        MyRequestQueue.add(MyStringRequest);

    }
}
