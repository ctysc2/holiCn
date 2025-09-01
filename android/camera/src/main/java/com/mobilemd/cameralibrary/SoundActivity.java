package com.mobilemd.cameralibrary;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mobilemd.cameralibrary.util.AudioRecorder;
import com.mobilemd.cameralibrary.util.FileUtil;
import com.mobilemd.cameralibrary.util.TimeUtils;
import com.mobilemd.cameralibrary.util.ToastUtils;
import com.mobilemd.cameralibrary.util.UploadUtils;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @Author: 成晓知
 * @DateTime: 5/29/21 15:41
 * @Description:
 **/
public class SoundActivity extends AppCompatActivity {

    private TextView tvCountTime;
    private Button btnConfirm;
    private RelativeLayout rlPlay;
    private ImageView btnBack;
    private ImageView imagePlay;

    private int currentStatus = 0;

    private String filePath = "";

    private List<String> voiceList ;

    private String playUrl ;
    private boolean isPlayer;

    private MediaRecorder mediaRecorder;
    private AudioRecorder audioRecorder;

    private long duration;

    private Timer timer;

    private TimerTask mTask ;

    private long currentTime;

    private boolean hasAdd;

    private boolean taskIsCancel;

    private int voiceMax;

    public static void startSoundActivity(Activity activity, String url, long duration, int max, int code){
        Intent it = new Intent(activity, SoundActivity.class);
        it.putExtra("data", url);
        it.putExtra("duration", duration);
        it.putExtra("voiceMax", max);
        activity.startActivityForResult(it, code);
    }


    public void startAndCreateRecorder() {
        mediaRecorder = new MediaRecorder();
        try {
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            filePath = FileUtil.getPath("eVisit", "mp3");
            mediaRecorder.setOutputFile(filePath);
            mediaRecorder.prepare();
            mediaRecorder.start();

            currentStatus = 1;
            currentTime = 0;
            startTimer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);

        tvCountTime = findViewById(R.id.tv_count_time);
        btnConfirm = findViewById(R.id.video_confirm);
        rlPlay = findViewById(R.id.video_play);
        btnBack = findViewById(R.id.back);
        imagePlay = findViewById(R.id.img_play);

        voiceList = new ArrayList<>();

        duration = getIntent().getLongExtra("duration", 60 * 1000);
        playUrl = getIntent().getStringExtra("data");
        voiceMax = getIntent().getIntExtra("voiceMax", 9);
        isPlayer = !TextUtils.isEmpty(playUrl);

        tvCountTime.setText(TimeUtils.formatTime(0, duration));

        if(isPlayer){
            audioRecorder = new AudioRecorder(this);
            btnConfirm.setVisibility(View.GONE);
            if(playUrl.startsWith("http")){
                if(!playUrl.substring(playUrl.lastIndexOf("/") + 1).contains(".")) {
                    playUrl += "?tms_t=" + UploadUtils.tms_t;
                }
            }
            audioRecorder.play(playUrl);
            startTimer();
            currentTime = 0;
            duration = audioRecorder.getMediaPlayer().getDuration();
        }
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaRecorder != null){
                    mediaRecorder.stop();
                }
                Intent it = new Intent();
                if(!isPlayer){
                    JSONArray js = new JSONArray();
                    for(String str : voiceList){
                        js.put(str);
                    }
                    it.putExtra("json", js.toString());
                }
                setResult(RESULT_OK, it);
                finish();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaRecorder != null){
                    mediaRecorder.stop();
                }
                Intent it = new Intent();
                if(!isPlayer){
                    JSONArray js = new JSONArray();
                    for(String str : voiceList){
                        js.put(str);
                    }
                    it.putExtra("json", js.toString());
                }
                setResult(RESULT_OK, it);
                finish();
            }
        });

        //开始录制
        rlPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlayer){
                    if(currentTime >= duration || currentTime == 0) {
                        if(audioRecorder.getMediaPlayer().isPlaying()){
                            audioRecorder.getMediaPlayer().reset();
                        }
                        audioRecorder.play(playUrl);
                        startTimer();
                    }else {
                        if(audioRecorder.getMediaPlayer().isPlaying()){
                            destroyTimer();
                            audioRecorder.getMediaPlayer().pause();
                            rlPlay.setBackgroundResource(R.drawable.confirm_bg);
                            imagePlay.setBackgroundResource(R.drawable.confirm);
                        }else {
                            startTimer();
                            audioRecorder.getMediaPlayer().start();
                        }
                    }
                }
                //录制语音
                else {
                    if (currentStatus == 0) {
                        if(voiceList.size() >= voiceMax) {
                            ToastUtils.showToast(SoundActivity.this, "语音最多可上传为" + voiceMax);
                            return;
                        }
                        startAndCreateRecorder();
                    }else {
                        destroyTimer();
                        mediaRecorder.stop();
                        mediaRecorder.release();
                        mediaRecorder = null;
                        if(currentTime < 3000){
                            ToastUtils.showToast(SoundActivity.this, "录制时间小于3秒");
                        }

                        currentStatus = 0;
                        currentTime = 0;
                        tvCountTime.setText(TimeUtils.formatTime(0, duration));
                        hasAdd = false;
                        rlPlay.setBackgroundResource(R.drawable.confirm_bg);
                        imagePlay.setBackgroundResource(R.drawable.confirm);
                        if(voiceList.size() > 0){
                            btnConfirm.setText("确定(" + voiceList.size() + ")");
                        }
                    }
                }
            }
        });
    }


    public TimerTask initTimerTask(){
        return new TimerTask() {
            @Override
            public void run() {
                if(!taskIsCancel){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(!taskIsCancel){
                                currentTime += 300;
                                Log.e("Media", "结束" + currentTime);
                                if(isPlayer){
                                    //超过录制时长结束
                                    if(currentTime  >= duration ){
                                        Log.e("Media", "结束");
                                        destroyTimer();
                                        currentTime = 0;
                                        tvCountTime.setText(TimeUtils.formatTime(0, duration));
                                        rlPlay.setBackgroundResource(R.drawable.confirm_bg);
                                        imagePlay.setBackgroundResource(R.drawable.confirm);
                                    }else{
//                                        Log.e("Media", "结束" + diffTime);
                                        tvCountTime.setText(TimeUtils.formatTime(currentTime, duration));
                                        rlPlay.setBackgroundResource(R.drawable.confirm_play_bg);
                                        imagePlay.setBackgroundResource(R.drawable.play_stop);
                                    }
                                }else if(mediaRecorder != null){
                                    //超过录制时长结束
                                    if(currentTime  >= duration ){
                                        destroyTimer();
                                        mediaRecorder.stop();
                                        currentStatus = 0;
                                        currentTime = 0;
                                        tvCountTime.setText(TimeUtils.formatTime(0, duration));
                                        hasAdd = false;
                                        rlPlay.setBackgroundResource(R.drawable.confirm_bg);
                                        imagePlay.setBackgroundResource(R.drawable.confirm);
                                        if(voiceList.size() > 0){
                                            btnConfirm.setText("确定(" + voiceList.size() + ")");
                                        }
                                    }else {
                                        tvCountTime.setText(TimeUtils.formatTime(currentTime, duration));
                                        rlPlay.setBackgroundResource(R.drawable.confirm_play_bg);
                                        imagePlay.setBackgroundResource(R.drawable.play_stop);
                                        if(currentTime >= 3000 && !hasAdd){
                                            voiceList.add(filePath);
                                            hasAdd = true;
                                        }

                                    }
                                }
                            }
                        }
                    });
                }
            }
        };
    }

    public void startTimer(){
        taskIsCancel = false;
        mTask = initTimerTask();
        timer = new Timer();
        timer.schedule(mTask, 0, 300);
    }

    public void destroyTimer(){
        taskIsCancel = true;
        if(mTask != null){
            mTask.cancel();
        }
        if(timer != null){
            timer.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyTimer();
        if(audioRecorder != null && audioRecorder.getMediaPlayer() != null){
            audioRecorder.getMediaPlayer().release();
            audioRecorder.getAudioPlayer().release();
        }
        if(mediaRecorder != null){
            mediaRecorder.release();
        }
    }
}



