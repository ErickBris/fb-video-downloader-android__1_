

package com.apps.videodownloader;

import java.util.EmptyStackException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

public class StreamVideo extends Activity
{

    private static final String TAG_VIDURL = "video_url";
    String VideoURL;
    ProgressDialog pDialog;
    VideoView videoview;

    public StreamVideo()
    {
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.video_view);
       // getActionBar().hide();
        VideoURL = getIntent().getStringExtra("video_url");
        videoview = (VideoView)findViewById(R.id.streaming_video);
        pDialog = new ProgressDialog(this);
        pDialog.setTitle("Video Stream");
        pDialog.setMessage("Buffering...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
        try
        {
            
            Uri uri = Uri.parse(VideoURL);
            videoview.setMediaController(new MediaController(this));
            videoview.setVideoURI(uri);
        }
        // Misplaced declaration of an exception variable
        catch (EmptyStackException bundleey)
        {
            Log.e("Error", bundleey.getMessage());
           
        }
        videoview.requestFocus();
        videoview.setOnPreparedListener(new android.media.MediaPlayer.OnPreparedListener() {

           
            public void onPrepared(MediaPlayer mediaplayer)
            {
                pDialog.dismiss();
                videoview.start();
            }

            
             
        });
    }
}
