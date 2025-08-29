package com.mobilemd.cameralibrary;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.mobilemd.cameralibrary.util.UploadUtils;
import com.mobilemd.cameralibrary.view.IVideoView;

import java.io.IOException;


/**
 * @Author: 成晓知
 * @DateTime: 5/29/21 13:37
 * @Description:
 **/
public class VideoActivity extends AppCompatActivity {

    private IVideoView videoView ;

    private MediaPlayer mMediaPlayer;


    public static void startVideoActivity(Activity activity, String url){
        Intent it = new Intent(activity, VideoActivity.class);
        it.putExtra("data", url);
        activity.startActivity(it);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);


        videoView = findViewById(R.id.jz_video);

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        videoView.requestFocus();

        String urlPath = getIntent().getStringExtra("data");

        if(urlPath.startsWith("http")){
            if(!urlPath.substring(urlPath.lastIndexOf("/") + 1).contains(".")) {
                urlPath += "?TM_token=" + UploadUtils.token;
            }
        }
        videoView.setVideoPath(urlPath);

        videoView.start();


//        new Thread(new Runnable() {
//            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//            @Override
//            public void run() {
//                try {
//                    if (mMediaPlayer == null) {
//                        mMediaPlayer = new MediaPlayer();
//                    } else {
//                        mMediaPlayer.reset();
//                    }
//                    mMediaPlayer.setDataSource(urlPath);
//                    mMediaPlayer.setLooping(false);
//                    Thread.sleep(100);
//                    mMediaPlayer.setSurface(videoView.getHolder().getSurface());
//                    mMediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);
//                    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                    mMediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer
//                            .OnVideoSizeChangedListener() {
//                        @Override
//                        public void
//                        onVideoSizeChanged(MediaPlayer mp, int width, int height) {
//                            updateVideoViewSize(mMediaPlayer.getVideoWidth(), mMediaPlayer
//                                    .getVideoHeight());
//                        }
//                    });
//                    mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                        @Override
//                        public void onPrepared(MediaPlayer mp) {
//                            mMediaPlayer.start();
//                        }
//                    });
//                    mMediaPlayer.prepare();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();

    }



    private void updateVideoViewSize(float videoWidth, float videoHeight) {
        if (videoWidth > videoHeight) {
            FrameLayout.LayoutParams videoViewParam;
            int height = (int) ((videoHeight / videoWidth) * getWindow().getDecorView().getHeight());
            videoViewParam = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, height);
            videoViewParam.gravity = Gravity.CENTER;
            videoView.setLayoutParams(videoViewParam);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.stopPlayback();
    }
}
