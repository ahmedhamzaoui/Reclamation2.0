package reclamation.dev.com.reclamation20;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import reclamation.dev.com.reclamation20.Home.MainActivity;
import reclamation.dev.com.reclamation20.Models.Comment;
import reclamation.dev.com.reclamation20.Utils.CommentListAdapter;
import reclamation.dev.com.reclamation20.Utils.MainfeedListAdapter;

import static reclamation.dev.com.reclamation20.Constants.URL_NEW_COMMENT;

public class ViewCommentsFragment extends Fragment {


    //vars
    private ArrayList<Comment> mComments;
    CommentListAdapter adapter;
    private ArrayList<Comment> mPaginatedComments;
    private int mResults;
    private static final String TAG = "ViewCommentsFragment";

    public ViewCommentsFragment(){
        super();
        setArguments(new Bundle());
    }


    final ArrayList<Comment> comms = new ArrayList<>();

    //widgets
    private ImageView mBackArrow, mCheckMark;
    private EditText mComment;
    private ListView mListView;

    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_comments, container, false);

        mBackArrow = (ImageView) view.findViewById(R.id.backArrow);
        mCheckMark = (ImageView) view.findViewById(R.id.ivPostComment);
        mComment = (EditText) view.findViewById(R.id.comment);
        mListView = (ListView) view.findViewById(R.id.listView);
        mContext = getActivity();
        mComments = new ArrayList<>();

        Bundle bundle = getArguments();

        mCheckMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mComment.getText().toString().equals("")){
                    RequestQueue MyRequestQueue = Volley.newRequestQueue(getContext());

                    StringRequest MyStringRequest = new StringRequest(Request.Method.POST, URL_NEW_COMMENT, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            mComment.setText("");
                            adapter.notifyDataSetChanged();
                        }
                    }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //This code is executed if there is an error.
                        }
                    }) {
                        protected Map<String, String> getParams() {
                            Map<String, String> MyData = new HashMap<String, String>();
                            MyData.put("userid",bundle.getString("userid")); //Add the data you'd like to send to the server.
                            MyData.put("idpost",bundle.getString("idpost")); //Add the data you'd like to send to the server.
                            MyData.put("comment",mComment.getText().toString()); //Add the data you'd like to send to the server.
                            MyData.put("createdAt", Calendar.getInstance().getTime().toString()); //Add the data you'd like to send to the server.

                            System.out.println(MyData.toString());
                            return MyData;
                        }
                    };
                    MyRequestQueue.add(MyStringRequest);

                }
            }
        });

        getAllComments();



        return view;
    }


    private void getAllComments(){

        Bundle bundoul = getArguments();

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                Constants.URL_COMMENTS+"?idpost="+bundoul.getString("idpost"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Comment comm = new Comment();
                                comm.setComment(((JSONObject)jsonArray.get(i)).get("textcomment").toString());
                                comm.setDate_created(((JSONObject)jsonArray.get(i)).get("createdAt").toString());
                                comm.setUser_id(((JSONObject)jsonArray.get(i)).get("userid").toString());
                                comms.add(comm);
                            }
                            setupWidgets();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println("erreur");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    private void closeKeyboard(){
        View view = getActivity().getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    private void setupWidgets() {

        mPaginatedComments = new ArrayList<Comment>();
        System.out.println("photoooos" + comms);
        if (comms != null) {
            try {
                Collections.sort(comms, new Comparator<Comment>() {
                    @Override
                    public int compare(Comment o1, Comment o2) {
                        return o2.getDate_created().compareTo(o1.getDate_created());
                    }
                });

                int iterations = comms.size();

                if (iterations > 10) {
                    iterations = 10;
                }

                mResults = 10;
                for (int i = 0; i < iterations; i++) {
                    mPaginatedComments.add(comms.get(i));
                }


                 adapter = new CommentListAdapter(getActivity(),
                        R.layout.layout_comment, mPaginatedComments);
                mListView.setAdapter(adapter);

            } catch (NullPointerException e) {
                Log.e(TAG, "displayPhotos: NullPointerException: " + e.getMessage());
            } catch (IndexOutOfBoundsException e) {
                Log.e(TAG, "displayPhotos: IndexOutOfBoundsException: " + e.getMessage());
            }

            mBackArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: navigating back");

                    Intent i = new Intent(getActivity(), MainActivity.class);
                    startActivity(i);

                }
            });

        }
    }

            private String getTimestamp () {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.FRANCE);
                sdf.setTimeZone(TimeZone.getTimeZone("Europe/Paris "));
                return sdf.format(new Date());
            }

        }

