package com.mahmoudbahaa.expenses.services;

import android.content.Context;
import android.util.Log;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

/**
 * Created by MahmoudBahaa on 24/01/2019.
 */

public class BackUpNowJobFireBase {

    private Context context;
    private  static BackUpNowJobFireBase instance;

    Job myJob;
    FirebaseJobDispatcher dispatcher;

    private BackUpNowJobFireBase(Context context) {

        Log.v("BackUpNowJobFireBase","initilized");

        this.context = context;

        dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        myJob = dispatcher.newJobBuilder()
                .setService(BacUpNowJobService.class) // the JobService that will be called
                .setTag("BackUpNow")
                .setRecurring(false)
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                //    .setTrigger(Trigger.executionWindow(
                //              REMINDER_INTERVAL_SECONDS,
                //              REMINDER_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))



                .setTrigger(Trigger.NOW)
                .setReplaceCurrent(true)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setConstraints(
                        // only run on an unmetered network
                        Constraint.ON_UNMETERED_NETWORK
                        // only run when the device is charging
                )

                .build();

        dispatcher.mustSchedule(myJob);


    }

    public static synchronized BackUpNowJobFireBase getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new BackUpNowJobFireBase(context);

        }

        return instance;
    }



    public  void CancelSync(){

        dispatcher.cancel("BackUpNow");

    }

    public void StartSync(){
        dispatcher.mustSchedule(myJob);

    }





}
