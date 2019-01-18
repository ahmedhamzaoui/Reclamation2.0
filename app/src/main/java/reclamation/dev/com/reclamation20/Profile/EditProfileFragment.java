package reclamation.dev.com.reclamation20.Profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import reclamation.dev.com.reclamation20.Dialog.ConfirmPasswordDialog;
import reclamation.dev.com.reclamation20.Models.Image;
import reclamation.dev.com.reclamation20.R;
import reclamation.dev.com.reclamation20.Utils.CommentListAdapter;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;
import static reclamation.dev.com.reclamation20.Login.LoginActivity.PREFS_NAME;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class EditProfileFragment extends Fragment implements ConfirmPasswordDialog.OnConfirmPasswordListener {
    // TODO: Rename parameter arguments, choose names that match

    private EditText mDisplayName,mUsername ,mEmail,mdesc ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_editprofile, container, false);

        mDisplayName = (EditText) view.findViewById(R.id.display_name);
        mUsername = (EditText) view.findViewById(R.id.username);
        mdesc = (EditText) view.findViewById(R.id.description);
        mEmail = (EditText) view.findViewById(R.id.email);

        Bundle bundle = this.getArguments();
        mDisplayName.setText(bundle.getString("fullname"));
        mUsername.setText(bundle.getString("username"));
        mdesc.setText(bundle.getString("description"));
        mEmail.setText(bundle.getString("email"));

        ImageView mbackArrow=(ImageView) view.findViewById(R.id.backArrow);
        mbackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment fragment = new ProfileFragment();
                Bundle args = new Bundle();
                fragment.setArguments(args);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragment);
                transaction.addToBackStack(getString(R.string.profile_fragment));
                transaction.commit();
            }
        });


        ImageView checkmark = (ImageView) view.findViewById(R.id.saveChanges);
        checkmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                RequestQueue MyRequestQueue = Volley.newRequestQueue(getActivity());


                final SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);

                String url = "http://192.168.1.20/rec/web/app_dev.php/s/users/profileedit/"+settings.getString("userid","");
                System.out.println(url);
                StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        System.out.println("response:         "+response);

                    }
                }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //This code is executed if there is an error.
                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> MyData = new HashMap<String, String>();
                        MyData.put("username",mUsername.getText().toString()); //Add the data you'd like to send to the server.
                        System.out.println("bundle:"+bundle);
                        if (!mEmail.getText().toString().equals(bundle.getString("email"))) {
                            ConfirmPasswordDialog dialog = new ConfirmPasswordDialog();
                            dialog.show(getFragmentManager(),getString(R.string.confirm_password_dialog));
                            dialog.setTargetFragment(EditProfileFragment.this, 1);
                            bundle.putString("email",mEmail.getText().toString());
                            System.out.println(bundle);

                            MyData.put("email", mEmail.getText().toString());
                        }else {

                            MyData.put("email", bundle.getString("email"));

                        }
                        if (!mUsername.getText().toString().equals(bundle.getString("username"))) {
                            ConfirmPasswordDialog dialog = new ConfirmPasswordDialog();
                            dialog.show(getFragmentManager(),getString(R.string.confirm_password_dialog));
                            dialog.setTargetFragment(EditProfileFragment.this, 1);
                            MyData.put("username", mUsername.getText().toString());
                        }else {
                            MyData.put("username", bundle.getString("username"));

                        }
                        MyData.put("fullname",mDisplayName.getText().toString()); //Add the data you'd like to send to the server.
                        MyData.put("description",mdesc.getText().toString()); //Add the data you'd like to send to the server.

                        System.out.println(MyData.toString());
                        return MyData;
                    }
                };
                MyRequestQueue.add(MyStringRequest);




            }
        });


        return view;
    }

    @Override
    public void onConfirmPassword(String password) {

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
