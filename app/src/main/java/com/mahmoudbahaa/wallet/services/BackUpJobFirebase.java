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

public class BackUpJobFirebase {


    private Context context;
    private  static BackUpJobFirebase instance;

   // private static final int REMINDER_INTERVAL_Days = 4;
//    private static final int REMINDER_INTERVAL_SECONDS = (int) (TimeUnit.DAYS.toSeconds(REMINDER_INTERVAL_Days));
  //  private static final int SYNC_FLEXTIME_SECONDS = REMINDER_INTERVAL_SECONDS;


    private static final int REMINDER_INTERVAL_Mintues = 1;
  //
  //
  // private static final int REMINDER_INTERVAL_SECONDS = (int) (TimeUnit.MINUTES.toSeconds(REMINDER_INTERVAL_Mintues));
     private static final int REMINDER_INTERVAL_SECONDS = 10;

    private static final int SYNC_FLEXTIME_SECONDS = REMINDER_INTERVAL_SECONDS;


     Job myJob;
    FirebaseJobDispatcher dispatcher;

    private BackUpJobFirebase(Context context) {

        Log.v("Job Sync Started","initilized");

        this.context = context;

         dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
         myJob = dispatcher.newJobBuilder()
                .setService(BackUpJobService.class) // the JobService that will be called
                .setTag("BackUp")
                .setRecurring(true)
                .setLifetime(Lifetime.FOREVER)
               .setTrigger(Trigger.executionWindow(
                       REMINDER_INTERVAL_SECONDS,
                        REMINDER_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))



              //   .setTrigger(Trigger.NOW)
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



    public static synchronized BackUpJobFirebase getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new BackUpJobFirebase(context);

        }

        return instance;
    }


  public  void CancelSync(){

        dispatcher.cancel("BackUp");

    }

   public void StartSync(){
        dispatcher.mustSchedule(myJob);

    }


}
