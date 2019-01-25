package com.mahmoudbahaa.expenses;

import android.app.DatePickerDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;

import com.mahmoudbahaa.expenses.adapters.ExpenseAdapter;
import com.mahmoudbahaa.expenses.data.AppDatabase;
import com.mahmoudbahaa.expenses.models.Account;
import com.mahmoudbahaa.expenses.models.Category;
import com.mahmoudbahaa.expenses.models.Expense;
import com.mahmoudbahaa.expenses.models.Sequence;
import com.mahmoudbahaa.expenses.models.Sync;
import com.mahmoudbahaa.expenses.services.BackUpJobService;
import com.mahmoudbahaa.expenses.services.BackUpNowJobFireBase;
import com.mahmoudbahaa.expenses.services.BackUpService;
import com.mahmoudbahaa.expenses.services.SyncDataService;
import com.mahmoudbahaa.expenses.services.BackUpJobFirebase;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;




public class MainActivity extends AppCompatActivity implements ExpenseAdapter.ListItemClickListener,MyResultReceiver.Receiver {
    final Calendar myCalendar = Calendar.getInstance();


    LiveData<List<Expense>> expenses1;
    DatePickerDialog.OnDateSetListener date;

    @BindView(R.id.Main_Day)
    TextView DayText;

    @BindView(R.id.Main_Month)
    TextView MonthText;


    @BindView(R.id.Main_Year)
    TextView YearText;

    @BindView(R.id.Main_dateLayout)
    LinearLayout MainDateLayout;






    @BindView(R.id.Main_CalendarIcon)
    ImageView Main_CalendarIcon;


    @BindView(R.id.Main_StatisticsIcon)
    ImageView Main_StatisticsIcon;


    @BindView(R.id.Main_ServicesIcon)
    ImageView Main_ServicesIcon;


    @BindView(R.id.Main_SettingsIcon)
    ImageView Main_SettingsIcon;



    @BindView(R.id.Prograss_Text)
    TextView Prograss_Text;

    @BindView(R.id.viewB)
    LinearLayout viewB;


    @BindView(R.id.Main)
    LinearLayout Main_Layout;

    @BindView(R.id.Main_Prograss)
    LinearLayout Main_Prograss;

    @BindView(R.id.Main_fab)
    FloatingActionButton fab;

    private UpdateUi listener ;

    public void setListener(UpdateUi listener)
    {
        this.listener = listener ;
    }

    public MyResultReceiver mReceiver;




    Boolean CalenderFragment = false;
    Boolean StatisticsFragment = false;
    Boolean ServicesFragment = false;
    Boolean SettingsFragment = false;

    private AppDatabase mDb;

    ArrayList<Expense> expensesForFragment = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mDb = AppDatabase.getsInstance(getApplicationContext());

        JodaTimeAndroid.init(this);


        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0.6f
            );
viewB.setLayoutParams(param);

        }




        setLocale();

        initDatePicker();


        if (savedInstanceState == null) {
            initCalenderFragment();
            CalenderFragment = true;

        }

        else{

            CalenderFragment = savedInstanceState.getBoolean("CalenderFragment");

            StatisticsFragment = savedInstanceState.getBoolean("StatisticsFragment");
             ServicesFragment = savedInstanceState.getBoolean("ServicesFragment");
             SettingsFragment = savedInstanceState.getBoolean("SettingsFragment");
Long MyCalenderTime = savedInstanceState.getLong("myCalendar");
myCalendar.setTimeInMillis(MyCalenderTime);

            if (CalenderFragment)
                initCalenderFragment();
            SetIcononRotate();
        }

        initDate();


        SetupMainViewModel();




        FrameLayout l = findViewById(R.id.Main_FragmentContainer);

        l.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            @Override
            public void onSwipeLeft() {
                // Whatever

                myCalendar.add(Calendar.DAY_OF_MONTH, 1);
                RemoveObservers();
                SetupMainViewModel();

                updateLabel();
            }

            @Override
            public void onSwipeRight() {


                myCalendar.add(Calendar.DAY_OF_MONTH, -1);
                RemoveObservers();
                SetupMainViewModel();

                updateLabel();

            }
        });


        if (getIntent().hasExtra("status")) {
            initCalendar(getIntent().getStringExtra("status"));

        }
    }

    void initCalendar(String status){

        switch (status){

            case "Normal":
                Main_Prograss.setVisibility(View.GONE);
                Main_Layout.setVisibility(View.VISIBLE);
                fab.setVisibility(View.VISIBLE);
                break;

            case "LogIn":

                Boolean sameUser = getIntent().getBooleanExtra("sameUser",false);


                if (!sameUser) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    Main_Prograss.setVisibility(View.VISIBLE);
                    Prograss_Text.setText(getResources().getText(R.string.SyncData));
                    Main_Layout.setVisibility(View.GONE);
                    fab.setVisibility(View.GONE);


                    mReceiver = new MyResultReceiver(new Handler());

                    mReceiver.setReceiver(this);


                    Intent i = new Intent(MainActivity.this, SyncDataService.class);
                    i.putExtra("receiverTag", mReceiver);
                    startService(i);




                  //  SyncNowJobFirebase.getInstance(this).StartSync();



/*
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                Log.i("tag", "This'll run 300 milliseconds later");



                            }
                        },
                        5000);
*/
                }





                break;

            case "SignUp":
                Log.v("SignUp","Opened");
                Main_Prograss.setVisibility(View.VISIBLE);
                Prograss_Text.setText(getResources().getText(R.string.initData));

                Main_Layout.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);


                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {

                        mDb.categoryDao().DeleteTable();
                        mDb.accountDao().DeleteTable();
                        mDb.expenseDao().DeleteTable();
                        mDb.sequenceDao().DeleteTable();
                        mDb.syncDao().DeleteTable();

                        mDb.accountDao().insertAll(Account.populateData());
                        mDb.categoryDao().insertAll(Category.populateData());
                        mDb.sequenceDao().insertAll(Sequence.populateData());
                        mDb.syncDao().insertSync(new Sync(1,new Date().getTime(),true));

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Main_Prograss.setVisibility(View.GONE);
                                Main_Layout.setVisibility(View.VISIBLE);
                                fab.setVisibility(View.VISIBLE);

                            //    CreateDbCopy(GetUserId());
                                BackUpNowJobFireBase.getInstance(getApplicationContext()).StartSync();

                            }
                        });


                    }
                });

                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {




                    }
                });

                break;


        }


    }


    void CreateDbCopy(String UserId){

        Intent i = new Intent(MainActivity.this,BackUpService.class);
        i.putExtra("UserId",UserId);
        i.setAction("CreateCopy");
        startService(i);
    }


    String GetUserId(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String Id = preferences.getString("Id", "");


        return Id;

    }








public void RemoveObservers(){
    expenses1.removeObservers(this);
}
    public void SetupMainViewModel(){
        Long start = getStartOfDayInMillis(myCalendar);
        Long end = getEndOfDayInMillis(start);
      //  final LiveData< List<Expense>> expenses1 = mDb.expenseDao().loadAllExpenses(start,end);

         expenses1   = mDb.expenseDao().loadAllExpenses(start,end);



            expenses1.observe(this, new Observer<List<Expense>>() {
                @Override
                public void onChanged(@Nullable List<Expense> expenses) {
                    expensesForFragment.clear();
                    expensesForFragment.addAll(expenses);
                    if (CalenderFragment)
                    listener.refreshCalendar(expenses);
                }
            });


        }



        void SetIcononRotate(){
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP && CalenderFragment) {
                Main_CalendarIcon.setImageTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.ActiveIcon));
                Main_ServicesIcon.setImageTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.InActiveIcon));
                Main_SettingsIcon.setImageTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.InActiveIcon));
                Main_StatisticsIcon.setImageTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.InActiveIcon));
            }

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP && StatisticsFragment) {
                Main_CalendarIcon.setImageTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.InActiveIcon));
                Main_ServicesIcon.setImageTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.InActiveIcon));
                Main_SettingsIcon.setImageTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.InActiveIcon));
                Main_StatisticsIcon.setImageTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.ActiveIcon));
            }

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP && ServicesFragment) {
                Main_CalendarIcon.setImageTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.InActiveIcon));
                Main_ServicesIcon.setImageTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.ActiveIcon));
                Main_SettingsIcon.setImageTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.InActiveIcon));
                Main_StatisticsIcon.setImageTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.InActiveIcon));
            }

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP && SettingsFragment) {
                Main_CalendarIcon.setImageTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.InActiveIcon));
                Main_ServicesIcon.setImageTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.InActiveIcon));
                Main_SettingsIcon.setImageTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.ActiveIcon));
                Main_StatisticsIcon.setImageTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.InActiveIcon));
            }



        }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putBoolean("CalenderFragment",CalenderFragment);


        outState.putBoolean("StatisticsFragment",StatisticsFragment);
        outState.putBoolean("ServicesFragment",ServicesFragment);
        outState.putBoolean("SettingsFragment",SettingsFragment);
         outState.putLong("myCalendar",myCalendar.getTimeInMillis());

        super.onSaveInstanceState(outState);
    }

    @OnClick(R.id.Main_Date_Pickup)
    void OpenDatePicker(){

        DatePickerDialog d =   new DatePickerDialog(MainActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        d.setTitle("");
        d.show();

    }



    void initDatePicker(){


         date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub


                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                RemoveObservers();
                SetupMainViewModel();

                updateLabel();
            }

        };




    }


    private void initDate(){
        String DayFormat = "dd";
        String MonthFormat = "MMM";
        String YearFormat = "yyyy";

        //   SimpleDateFormat sdf = new SimpleDateFormat(DayFormat, Locale.US);

        SimpleDateFormat sdf = new SimpleDateFormat(DayFormat);

        DayText.setText(sdf.format(myCalendar.getTime()));

        sdf = new SimpleDateFormat(MonthFormat);

        MonthText.setText(sdf.format(myCalendar.getTime()));

        sdf = new SimpleDateFormat(YearFormat);

        YearText.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        
        String DayFormat = "dd";
        String MonthFormat = "MMM";
        String YearFormat = "yyyy";

     //   SimpleDateFormat sdf = new SimpleDateFormat(DayFormat, Locale.US);

        SimpleDateFormat sdf = new SimpleDateFormat(DayFormat);

        DayText.setText(sdf.format(myCalendar.getTime()));

        sdf = new SimpleDateFormat(MonthFormat);

        MonthText.setText(sdf.format(myCalendar.getTime()));

        sdf = new SimpleDateFormat(YearFormat);

        YearText.setText(sdf.format(myCalendar.getTime()));



        Log.v("date2",myCalendar.getTime()+"");


    }




    void initCalenderFragment(){

        Bundle bundle = new Bundle();

     //   String ChatsType = "All";
      //  bundle.putString("Type",ChatsType);
      //  bundle.putString("userId",userId);
        CalendarFragment f = new CalendarFragment();
        f.setArguments(bundle);

        setListener(f);

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.Main_FragmentContainer, f)
                .commit();

        MainDateLayout.setVisibility(View.VISIBLE);
        CalenderFragment = true;

    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

    }


    @OnClick(R.id.Main_fab)
    void OpenAddActivity(){

        Intent i = new Intent(MainActivity.this,AddActivity.class);
      i.putExtra("date",myCalendar.getTimeInMillis());
        startActivity(i);
    }


    @OnClick(R.id.Main_Calendar)
    void OpenCalendarFragment(){

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        MainDateLayout.setVisibility(View.VISIBLE);
        CalenderFragment  = true;

        StatisticsFragment = false;
         ServicesFragment = false;
         SettingsFragment = false;

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            Main_CalendarIcon.setImageTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.ActiveIcon));
            Main_ServicesIcon.setImageTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.InActiveIcon));
            Main_SettingsIcon.setImageTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.InActiveIcon));
            Main_StatisticsIcon.setImageTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.InActiveIcon));
        }


        Bundle bundle = new Bundle();

        //   String ChatsType = "All";
        Long start = getStartOfDayInMillis(myCalendar);
        Long end = getEndOfDayInMillis(start);
          bundle.putLong("start",start);
          bundle.putLong("end",end);

          bundle.putParcelableArrayList("expenses",expensesForFragment);

        CalendarFragment f = new CalendarFragment();
        f.setArguments(bundle);

        setListener(f);

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.Main_FragmentContainer, f)
                .commit();


     //   listener.refreshCalendar(expensesForFragment);

    }

    @OnClick(R.id.Main_Statistics)
    void OpenStatisticsFragment(){

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            Main_CalendarIcon.setImageTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.InActiveIcon));
            Main_ServicesIcon.setImageTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.InActiveIcon));
            Main_SettingsIcon.setImageTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.InActiveIcon));
            Main_StatisticsIcon.setImageTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.ActiveIcon));
        }


        Bundle bundle = new Bundle();

        //   String ChatsType = "All";
        //  bundle.putString("Type",ChatsType);
        //  bundle.putString("userId",userId);
        StatisticsFragment f = new StatisticsFragment();
        f.setArguments(bundle);


        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.Main_FragmentContainer, f)
                .commit();
        MainDateLayout.setVisibility(View.GONE);
        CalenderFragment = false;
        StatisticsFragment = true;
        ServicesFragment = false;
        SettingsFragment = false;
    }

    @OnClick(R.id.Main_Services)
    void OpenServicesFragment(){

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            Main_CalendarIcon.setImageTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.InActiveIcon));
            Main_ServicesIcon.setImageTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.ActiveIcon));
            Main_SettingsIcon.setImageTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.InActiveIcon));
            Main_StatisticsIcon.setImageTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.InActiveIcon));
        }




        Bundle bundle = new Bundle();

        //   String ChatsType = "All";
        //  bundle.putString("Type",ChatsType);
        //  bundle.putString("userId",userId);
        SearchFragment f = new SearchFragment();
        f.setArguments(bundle);


        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.Main_FragmentContainer, f)
                .commit();




        MainDateLayout.setVisibility(View.GONE);

        CalenderFragment = false;
        StatisticsFragment = false;
        ServicesFragment = true;
        SettingsFragment = false;
    }

    @OnClick(R.id.Main_Settings)
    void OpenSettingsFragment(){
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);


        MainDateLayout.setVisibility(View.GONE);
        CalenderFragment = false;
        StatisticsFragment = false;
        ServicesFragment = false;
        SettingsFragment = true;


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            Main_CalendarIcon.setImageTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.InActiveIcon));
            Main_ServicesIcon.setImageTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.InActiveIcon));
            Main_SettingsIcon.setImageTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.ActiveIcon));
            Main_StatisticsIcon.setImageTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.InActiveIcon));
        }

        Bundle bundle = new Bundle();

        //   String ChatsType = "All";
     //   Long start = getStartOfDayInMillis(myCalendar);
     //   Long end = getEndOfDayInMillis(start);
      //  bundle.putLong("start",start);
      //  bundle.putLong("end",end);

       // bundle.putParcelableArrayList("expenses",expensesForFragment);

        SettingsFragment f = new SettingsFragment();
        f.setArguments(bundle);

     //   setListener(f);

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.Main_FragmentContainer, f)
                .commit();



    }


    void setLocale(){
        Locale locale = new Locale("ar");
        Locale.setDefault(locale);
        Configuration config =
                getBaseContext().getResources().getConfiguration();
        config.setLocale(locale);
        createConfigurationContext(config);
    }


    @Override
    protected void onResume() {
        super.onResume();

        Log.v("date1",myCalendar.getTime()+"");

        if (CalenderFragment)
        {
    //    Long start = getStartOfDayInMillis(myCalendar);
    //    Long end = getEndOfDayInMillis(start);
    //    listener.refreshCalendar(start,end);
        }
        else {
            MainDateLayout.setVisibility(View.GONE);
        }

    }


    public long getStartOfDayInMillis(Calendar c) {
     //   Calendar calendar =c;
     //   c.set(Calendar.HOUR_OF_DAY, 0);
      //  c.set(Calendar.MINUTE, 0);
      //  c.set(Calendar.SECOND, 0);
      //  c.set(Calendar.MILLISECOND, 0);

        DateTime someDate = new DateTime(c.getTimeInMillis());


        return someDate.withTimeAtStartOfDay().getMillis();
    }

    public long getEndOfDayInMillis(Long start) {
// Add one day's time to the beginning of the day.
// 24 hours * 60 minutes * 60 seconds * 1000 milliseconds = 1 day


        DateTime someDate = new DateTime(start);

        return someDate.plusDays(1).withTime(0,0,0,0).getMillis();
    }



    @OnClick(R.id.next)
    void onNextDay(){

        myCalendar.add(Calendar.DAY_OF_MONTH, 1);
        RemoveObservers();
        SetupMainViewModel();

        updateLabel();


    }

    @OnClick(R.id.prev)
    void onPrevDay(){

        myCalendar.add(Calendar.DAY_OF_MONTH, -1);
        RemoveObservers();
        SetupMainViewModel();

        updateLabel();


    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        mReceiver.setReceiver(null);

        Main_Prograss.setVisibility(View.GONE);
        Main_Layout.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        BackUpJobFirebase.getInstance(getApplicationContext()).StartSync();

    }

    void InitFirebaseJobDispatcher(){

        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getApplicationContext()));
        Job myJob = dispatcher.newJobBuilder()
                .setService(BackUpJobService.class) // the JobService that will be called
                .setTag("BackUp")
                .setRecurring(true)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(0, 60))
                .setReplaceCurrent(true)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setConstraints(
                        // only run on an unmetered network
                        Constraint.ON_UNMETERED_NETWORK,
                        // only run when the device is charging
                        Constraint.DEVICE_CHARGING
                )


                .build();

        dispatcher.mustSchedule(myJob);

    }
}
