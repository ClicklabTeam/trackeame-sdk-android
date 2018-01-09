package com.clicklab.sdk.model;


import android.os.Handler;
import android.os.HandlerThread;

public class WorkerThread extends HandlerThread {

    public WorkerThread(String name) {
        super(name);
    }

    private Handler handler;

    void post(Runnable r) {
        waitForInitialization();
        handler.post(r);
    }

    private synchronized void waitForInitialization() {
        if (handler == null) {
            handler = new Handler(getLooper());
        }
    }
}

