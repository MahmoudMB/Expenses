package com.example.mahmoudbahaa.expenses;

import android.app.DatePickerDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.LinearLayout;

import com.example.mahmoudbahaa.expenses.adapters.ExpenseAdapter;
import com.example.mahmoudbahaa.expenses.data.AppDatabase;
import com.example.mahmoudbahaa.expenses.models.Expense;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;




public class MainActivity extends AppCompatActivity implements ExpenseAdapter.ListItemClickListener {
    final Calendar myCalendar = Calendar.getInstance();



    DatePickerDialog.OnDateSetListener date;

    @BindView(R.id.Main_Day)
    TextView DayText;

    @BindView(R.id.Main_Month)
    TextView MonthText;


    @BindView(R.id.Main_Year)
    TextView YearText;

    @BindView(R.id.Main_dateLayout)
    LinearLayout MainDateLayout;





    private UpdateUi listener ;

    public void setListener(UpdateUi listener)
    {
        this.listener = listener ;
    }





    Boolean CalenderFragment = false;
    private AppDatabase mDb;

    ArrayList<Expense> expensesForFragment = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mDb = AppDatabase.getsInstance(getApplicationContext());

        setLocale();

        initDatePicker();


        if (savedInstanceState == null) {
            initCalenderFragment();
            CalenderFragment = true;


        }

        else{

            CalenderFragment = savedInstanceState.getBoolean("CalenderFragment");
            if (CalenderFragment)
                initCalenderFragment();

        }

        initDate();


        GetExpenses();
    }


    public void GetExpenses(){
        Long start = getStartOfDayInMillis(myCalendar);
        Long end = getEndOfDayInMillis(start);
        final LiveData< List<Expense>> expenses1 = mDb.expenseDao().loadAllExpenses(start,end);

        expenses1.observe(this, new Observer<List<Expense>>() {
            @Override
            public void onChanged(@Nullable List<Expense> expenses) {
                Log.v("changed eense observer","changed");

                Log.v("size",expenses.size()+"");

                expensesForFragment.clear();
                expensesForFragment.addAll(expenses);
                listener.refreshCalendar(expenses);


            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putBoolean("CalenderFragment",CalenderFragment);

        super.onSaveInstanceState(outState);
    }

    @OnClick(R.id.Main_dateLayout)
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
                GetExpenses();

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

        startActivity(i);
    }


    @OnClick(R.id.Main_Calendar)
    void OpenCalendarFragment(){
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

        MainDateLayout.setVisibility(View.VISIBLE);
        CalenderFragment  = true;
     //   listener.refreshCalendar(expensesForFragment);

    }

    @OnClick(R.id.Main_Statistics)
    void OpenStatisticsFragment(){
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
    }

    @OnClick(R.id.Main_Services)
    void OpenServicesFragment(){
        MainDateLayout.setVisibility(View.GONE);

        CalenderFragment = false;
    }

    @OnClick(R.id.Main_Settings)
    void OpenSettingsFragment(){
        MainDateLayout.setVisibility(View.GONE);
        CalenderFragment = false;
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
        Calendar calendar =c;
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public long getEndOfDayInMillis(Long start) {
// Add one day's time to the beginning of the day.
// 24 hours * 60 minutes * 60 seconds * 1000 milliseconds = 1 day
        return start + (24 * 60 * 60 * 1000);
    }


}
