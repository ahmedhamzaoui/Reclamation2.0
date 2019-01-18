package reclamation.dev.com.reclamation20.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import reclamation.dev.com.reclamation20.CommentActivity;
import reclamation.dev.com.reclamation20.Constants;
import reclamation.dev.com.reclamation20.Dialog.ConfirmPasswordDialog;
import reclamation.dev.com.reclamation20.Home.MainActivity;
import reclamation.dev.com.reclamation20.Models.Comment;
import reclamation.dev.com.reclamation20.Models.Photo;
import reclamation.dev.com.reclamation20.Models.User;
import reclamation.dev.com.reclamation20.Models.UserAccountSettings;
import reclamation.dev.com.reclamation20.MyModels.Likes;
import reclamation.dev.com.reclamation20.Profile.EditProfileFragment;
import reclamation.dev.com.reclamation20.Profile.ProfileActivity;
import reclamation.dev.com.reclamation20.R;
import reclamation.dev.com.reclamation20.ViewCommentsFragment;

import static reclamation.dev.com.reclamation20.Constants.URL_FAV;
import static reclamation.dev.com.reclamation20.Constants.URL_IMAGE;
import static reclamation.dev.com.reclamation20.Constants.URL_LIKE;
import static reclamation.dev.com.reclamation20.Constants.URL_LIST_FAV;
import static reclamation.dev.com.reclamation20.Constants.URL_LIST_LIKES;
import static reclamation.dev.com.reclamation20.Constants.URL_NB_COMM;
import static reclamation.dev.com.reclamation20.Constants.URL_NB_LIKES;
import static reclamation.dev.com.reclamation20.Login.LoginActivity.PREFS_NAME;

public class MainfeedListAdapter extends ArrayAdapter<Photo> {

    public interface OnLoadMoreItemsListener{
        void onLoadMoreItems();
    }
    OnLoadMoreItemsListener mOnLoadMoreItemsListener;

    private static final String TAG = "MainfeedListAdapter";

    private LayoutInflater mInflater;
    private int mLayoutResource;
    private Context mContext;


    private String currentUsername = "";


    public MainfeedListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Photo> objects) {
        super(context, resource, objects);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayoutResource = resource;
        this.mContext = context;

    }

    static class ViewHolder{
        CircleImageView mprofileImage;
        String likesString;
        TextView username, timeDetla, caption, likes, comments;
        public SquareImageView image;

        ImageView heartRed, heartWhite, comment,fav;

        UserAccountSettings settings = new UserAccountSettings();
        User user  = new User();
        StringBuilder users;
        String mLikesString;
        boolean likeByCurrentUser;
        Heart heart;
        GestureDetector detector;
        Photo photo;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ViewHolder holder;

        if(convertView == null){
            convertView = mInflater.inflate(mLayoutResource, parent, false);
            holder = new ViewHolder();

            holder.username = (TextView) convertView.findViewById(R.id.username);
            holder.image = (SquareImageView) convertView.findViewById(R.id.post_image);
            holder.heartRed = (ImageView) convertView.findViewById(R.id.image_heart_red);
            holder.heartWhite = (ImageView) convertView.findViewById(R.id.image_heart);
            holder.fav = (ImageView) convertView.findViewById(R.id.favorix);
            holder.comment = (ImageView) convertView.findViewById(R.id.speech_bubble);
            holder.likes = (TextView) convertView.findViewById(R.id.image_likes);
            holder.comments = (TextView) convertView.findViewById(R.id.image_comments_link);
            holder.caption = (TextView) convertView.findViewById(R.id.image_caption);
            holder.timeDetla = (TextView) convertView.findViewById(R.id.image_time_posted);
            holder.mprofileImage = (CircleImageView) convertView.findViewById(R.id.profile_photo);
            holder.heart = new Heart(holder.heartWhite, holder.heartRed);
            holder.photo = getItem(position);
            holder.detector = new GestureDetector(mContext, new GestureListener(holder));
            holder.users = new StringBuilder();

            convertView.setTag(holder);

        }else {
            holder = (ViewHolder)  convertView.getTag();
        }

        //get the current users username (need for checking likes string)
        //getCurrentUsername();

        //get likes string


        holder.caption.setText(getItem(position).getCaption());


        holder.heartWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue MyRequestQueue = Volley.newRequestQueue(getContext());


                final SharedPreferences settings = getContext().getSharedPreferences(PREFS_NAME, 0);

                StringRequest MyStringRequest = new StringRequest(Request.Method.POST, URL_LIKE, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //This code is executed if there is an error.
                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> MyData = new HashMap<String, String>();
                        MyData.put("userid",settings.getString("userid","")); //Add the data you'd like to send to the server.
                        MyData.put("idpost",getItem(position).getPhoto_id()+""); //Add the data you'd like to send to the server.

                        System.out.println(MyData.toString());
                        return MyData;
                    }
                };
                MyRequestQueue.add(MyStringRequest);

            }
        });


        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue MyRequestQueue = Volley.newRequestQueue(getContext());


                final SharedPreferences settings = getContext().getSharedPreferences(PREFS_NAME, 0);

                StringRequest MyStringRequest = new StringRequest(Request.Method.POST, URL_FAV, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        notifyDataSetChanged();

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
                        MyData.put("userid",settings.getString("userid","")); //Add the data you'd like to send to the server.
                        MyData.put("idpost",getItem(position).getPhoto_id()+""); //Add the data you'd like to send to the server.

                        System.out.println(MyData.toString());
                        return MyData;
                    }
                };
                MyRequestQueue.add(MyStringRequest);

            }
        });

        ArrayList<String> favoriss =new ArrayList<>();
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getContext());

        final SharedPreferences settings = getContext().getSharedPreferences(PREFS_NAME, 0);

        System.out.println(settings.getString("userid",""));
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, URL_LIST_FAV, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                            if (response.equals(getItem(position).getPhoto_id())){
                                holder.fav.setImageDrawable(mContext.getResources().getDrawable(R.drawable.fav));

                            }
                            else {
                                holder.fav.setImageDrawable(mContext.getResources().getDrawable(R.drawable.notfav));

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
                MyData.put("userid",settings.getString("userid","")); //Add the data you'd like to send to the server.
                MyData.put("idpost",getItem(position).getPhoto_id()); //Add the data you'd like to send to the server.
                return MyData;
            }
        };
        MyRequestQueue.add(MyStringRequest);




        System.out.println(settings.getString("userid",""));
        StringRequest MyStringRequest2 = new StringRequest(Request.Method.POST, URL_LIST_LIKES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                if (response.equals(getItem(position).getPhoto_id())){
                    holder.heartWhite.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_heart_red));

                }
                else {
                    holder.heartWhite.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_heart_white));

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
                MyData.put("userid",settings.getString("userid","")); //Add the data you'd like to send to the server.
                MyData.put("idpost",getItem(position).getPhoto_id()); //Add the data you'd like to send to the server.
                return MyData;
            }
        };
        MyRequestQueue.add(MyStringRequest2);




        StringRequest MyStringRequest3 = new StringRequest(Request.Method.POST, URL_NB_LIKES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                holder.likes.setText("Liked By "+response);
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("idpost",getItem(position).getPhoto_id()+""); //Add the data you'd like to send to the server.

                System.out.println(MyData.toString());
                return MyData;
            }
        };
        MyRequestQueue.add(MyStringRequest3);

        StringRequest MyStringRequest4 = new StringRequest(Request.Method.POST, URL_NB_COMM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                holder.comments.setText("View "+response+" comments...");
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("idpost",getItem(position).getPhoto_id()+""); //Add the data you'd like to send to the server.

                System.out.println(MyData.toString());
                return MyData;
            }
        };
        MyRequestQueue.add(MyStringRequest4);



        //set the comment
        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getContext(),CommentActivity.class);
                i.putExtra("userid",settings.getString("userid",""));
                i.putExtra("idpost",getItem(position).getPhoto_id());
                getContext().startActivity(i);
               /* ViewCommentsFragment fragment = new ViewCommentsFragment();
                Bundle args = new Bundle();
                args.putString("idpost",getItem(position).getPhoto_id());
                args.putString("userid",settings.getString("userid",""));

                fragment.setArguments(args);
                FragmentTransaction transaction =((AppCompatActivity) mContext).getSupportFragmentManager()
                        .beginTransaction();
                transaction.replace(R.id.container, fragment);
                transaction.commit();
           */ }
        });

        //set the profile image

        final ImageLoader imageLoader = ImageLoader.getInstance();
        String path = (URL_IMAGE+getItem(position).getImage_path());
        imageLoader.displayImage(path, holder.image);
        System.out.println("User id ..."+getItem(position).getUser_id());




        StringRequest stringRequest2 = new StringRequest(
                Request.Method.GET,
                Constants.URL_USER_SHOW+getItem(position).getUser_id()+"/show",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonArray = new JSONObject(response);
                            System.out.println("Adapter response"+response);
                            holder.username.setText(jsonArray.getString("fullname"));
                            imageLoader.displayImage(jsonArray.getString("path"),
                                    holder.mprofileImage);
                            System.out.println("username..."+jsonArray.getString("username"));



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        RequestQueue requestQueue1 = Volley.newRequestQueue(getContext());
        requestQueue1.add(stringRequest2);





        //get the profile image and username



        // get the user object





        return convertView;
    }

    private boolean reachedEndOfList(int position){
        return position == getCount() - 1;
    }

    private void loadMoreData(){

        try{
            mOnLoadMoreItemsListener = (OnLoadMoreItemsListener) getContext();
        }catch (ClassCastException e){
            Log.e(TAG, "loadMoreData: ClassCastException: " +e.getMessage() );
        }

        try{
            mOnLoadMoreItemsListener.onLoadMoreItems();
        }catch (NullPointerException e){
            Log.e(TAG, "loadMoreData: ClassCastException: " +e.getMessage() );
        }
    }


    public class GestureListener extends GestureDetector.SimpleOnGestureListener{

        ViewHolder mHolder;
        public GestureListener(ViewHolder holder) {
            mHolder = holder;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.d(TAG, "onDoubleTap: double tap detected.");



            return true;
        }
    }













    /**
     * Returns a string representing the number of days ago the post was made
     * @return
     */
    private String getTimestampDifference(Photo photo){
        Log.d(TAG, "getTimestampDifference: getting timestamp difference.");

        String difference = "";
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.FRANCE);
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));//google 'android list of timezones'
        Date today = c.getTime();
        sdf.format(today);
        Date timestamp;
        final String photoTimestamp = photo.getDate_created();
        try{
            timestamp = sdf.parse(photoTimestamp);
            difference = String.valueOf(Math.round(((today.getTime() - timestamp.getTime()) / 1000 / 60 / 60 / 24 )));
        }catch (ParseException e){
            Log.e(TAG, "getTimestampDifference: ParseException: " + e.getMessage() );
            difference = "0";
        }
        return difference;
    }

}
