package ixigo.example.apple.ixigohack.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.FirebaseDatabase;

import io.branch.referral.Branch;

/**
 * Created by Ashish on 30/08/16.
 */
public class AppApplication extends Application {

    private static AppApplication sInstance;

    private RequestQueue mRequestQueue;
    private static FirebaseDatabase mDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        Branch.getAutoInstance(this);
    }

    public static FirebaseDatabase getFirebaseInstance() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }

    public static AppApplication getInstance() {
        if (sInstance == null)
            sInstance = new AppApplication();

        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public void clearAllVolleyCache() {
        try {
            getRequestQueue().getCache().clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> void addToRequestQueue(Request<T> req, @NonNull String tag) {
        RetryPolicy retryPolicy = new DefaultRetryPolicy(8 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        addToRequestQueue(req, tag, true, retryPolicy);
    }

    public <T> void addToRequestQueue(Request<T> req, @NonNull String tag, boolean cache, RetryPolicy retryPolicy) {
        req.setTag(tag);
        req.setShouldCache(cache);
        req.setRetryPolicy(retryPolicy);
        getRequestQueue().add(req);
    }
}
