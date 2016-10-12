package com.solstice.atoi.rlbet;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Atoi on 28.09.2016.
 */
public class AppControler extends Application {
    public static final String TAG = AppControler.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private static AppControler mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized AppControler getInstance() {
        if(mInstance == null)
        {
            mInstance = new AppControler();
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue(Context c) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(c);
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Context c, Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue(c).add(req);
    }

    public <T> void addToRequestQueue(Context c, Request<T> req) {
        req.setTag(TAG);
        getRequestQueue(c).add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
