package com.ver2point0.kamcordandroidchallenge.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ver2point0.kamcordandroidchallenge.R;
import com.ver2point0.kamcordandroidchallenge.adapter.VideoAdapter;
import com.ver2point0.kamcordandroidchallenge.information.VideoInformation;
import com.ver2point0.kamcordandroidchallenge.network.VolleyNetwork;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements VideoAdapter.OnThumbnailClickListener {

    private static final String KAMCORD_FEED_URL = "https://app.kamcord.com/app/v3/feeds/featured_feed";
    private static final String JSON_KEY_RESPONSE = "response";
    private static final String JSON_KEY_VIDEO_LIST = "video_list";
    public static final String JSON_KEY_NEXT_PAGE = "next_page";
    private static final String JSON_KEY_TITLE = "title";
    private static final String JSON_KEY_THUMBNAILS = "thumbnails";
    private static final String JSON_KEY_REGULAR = "regular";
    private static final String JSON_KEY_VIDEO_URL = "video_url";
    public static final String INTENT_KEY_VIDEO_URL = "videoUrl";

    private RecyclerView mRecyclerView;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_video_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        // check for internet
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            mProgressDialog = ProgressDialog.show(MainActivity.this, getString(R.string.progress_dialog_title), getString(R.string.progress_dialog_info), true);
            mProgressDialog.setCancelable(true);
            // activity will close if user cancels while loading
            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    finish();
                }
            });
            getJSONinfo();
        } else {
            Toast.makeText(this, getString(R.string.network_unavailable), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void getJSONinfo() {
        RequestQueue requestQueue = VolleyNetwork.getInstance().getRequestQueue();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, KAMCORD_FEED_URL, (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseJSONResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    // parse needed data
    private void parseJSONResponse(JSONObject response) {
        if (response == null || response.length() == 0) {
            return;
        }

        try {
            JSONObject data = response.getJSONObject(JSON_KEY_RESPONSE);
            JSONArray videosArray = data.getJSONArray(JSON_KEY_VIDEO_LIST);
            populateVideoList(videosArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void populateVideoList(JSONArray videosArray) throws JSONException {
        List<VideoInformation> videoInformationList = new ArrayList<VideoInformation>();
        String title;
        String thumbnail;
        String videoUrl;

        for (int i = 0; i < videosArray.length(); i++) {
            JSONObject current = videosArray.getJSONObject(i);

            title = current.getString(JSON_KEY_TITLE);
            JSONObject thumbnails = current.getJSONObject(JSON_KEY_THUMBNAILS);
            thumbnail = thumbnails.getString(JSON_KEY_REGULAR);
            videoUrl = current.getString(JSON_KEY_VIDEO_URL);

            VideoInformation videoInformation = new VideoInformation(title, thumbnail, videoUrl);
            videoInformationList.add(videoInformation);
        }

        setupAdapter(videoInformationList);
        mProgressDialog.dismiss();
    }

    private void setupAdapter(List<VideoInformation> videoInformationList) {
        VideoAdapter videoAdapter = new VideoAdapter(this, videoInformationList);
        videoAdapter.setOnThumbnailClickListener(this);
        mRecyclerView.setAdapter(videoAdapter);
    }


    @Override
    public void onThumbnailClick(String videoUrl) {
        Intent intent = new Intent(MainActivity.this, VideoActivity.class);
        intent.putExtra(INTENT_KEY_VIDEO_URL, videoUrl);
        startActivity(intent);
    }
}
