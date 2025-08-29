package com.mobilemd.cameralibrary.listener;


public interface CaptureListener {
    void takePictures();

    void recordShort(long time);

    void recordStart();

    void recordMillions(long time);

    void recordEnd(long time);

    void recordZoom(float zoom);

    void recordError();

    boolean canTakePhoto();

    boolean canRecorder();
}
