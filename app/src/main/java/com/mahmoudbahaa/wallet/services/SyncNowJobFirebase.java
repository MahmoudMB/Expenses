package com.mahmoudbahaa.expenses.services;

import android.content.Context;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

/**
 * Created by MahmoudBahaa on 24/01/2019.
 */

public class SyncNowJobFirebase {

    private Context context;
    private  static SyncNowJobFirebase instance;

    Job myJob;
    FirebaseJobDispatcher dispatcher;

    private SyncNowJobFirebase(Context context) {

        Log.v("Job Sync Started","initilized");

        this.context = context;

        dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        myJob = dispatcher.newJobBuilder()
                .setService(SyncNowJobService.class) // the JobService that will be called
                .setTag("SyncNowOneTime")
                .setRecurring(false)
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                .setTrigger(Trigger.NOW)
                .setReplaceCurrent(true)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setConstraints()
                .build();

        dispatcher.mustSchedule(myJob);


    }


    public static synchronized SyncNowJobFirebase getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new SyncNowJobFirebase(context);

        }

        return instance;
    }


    public  void CancelSync(){

        dispatcher.cancelAll();

    }

    public void StartSync(){
        dispatcher.mustSchedule(myJob);

    }




}
