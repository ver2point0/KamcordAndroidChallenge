package com.ver2point0.kamcordandroidchallenge.ui.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.ver2point0.kamcordandroidchallenge.R;

public class VideoActivity extends Activity {

    private ProgressDialog mProgressDialog;
    private VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get video url from main activity
        String videoUrl = getIntent().getStringExtra(MainActivity.INTENT_KEY_VIDEO_URL);
        setContentView(R.layout.activity_video);
        mVideoView = (VideoView) findViewById(R.id.video_view);

        mProgressDialog = ProgressDialog.show(VideoActivity.this,
                        getString(R.string.progress_dialog_title), getString(R.string.progress_dialog_buff), true);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            // ends video activity if user cancels video before fully loaded
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });

        try {
            // UI video controls
            MediaController mediaController = new MediaController(VideoActivity.this);
            mediaController.setAnchorView(mVideoView);

            // get URI from videoUrl
            Uri videoUri = Uri.parse(videoUrl);
            mVideoView.setMediaController(mediaController);
            mVideoView.setVideoURI(videoUri);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mVideoView.requestFocus();
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mProgressDialog.dismiss();
                mVideoView.start();
            }
        });

    }
}
