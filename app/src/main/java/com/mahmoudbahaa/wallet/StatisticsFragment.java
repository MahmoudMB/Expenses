package com.mahmoudbahaa.expenses;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mahmoudbahaa.expenses.adapters.AccountAdapter;
import com.mahmoudbahaa.expenses.data.AppDatabase;
import com.mahmoudbahaa.expenses.models.Account;
import com.mahmoudbahaa.expenses.models.Expense;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StatisticsFragment extends Fragment implements AccountAdapter.ListItemClickListener {


    @BindView(R.id.Statistics_TimeLine_days)
    LinearLayout TimeLine_days;

    @BindView(R.id.Statistics_TimeLine_month)
    LinearLayout TimeLine_month;

    @BindView(R.id.Statistics_TimeLine_6months)
    LinearLayout TimeLine_6months;

    @BindView(R.id.Statistics_TimeLine_year)
    LinearLayout TimeLine_year;


    @BindView(R.id.Statistics_TimeLine_days_Text)
    TextView TimeLine_days_Text;

    @BindView(R.id.Statistics_TimeLine_month_Text)
    TextView TimeLine_month_Text;

    @BindView(R.id.Statistics_TimeLine_6months_Text)
    TextView TimeLine_6months_Text;

    @BindView(R.id.Statistics_TimeLine_year_Text)
    TextView TimeLine_year_Text;


    @BindView(R.id.Statistics_AccountArrow)
    ImageView AccountArrow;


    @BindView(R.id.Statistics_CurrentAccountName)
    TextView CurrentAccountName;



    @BindView(R.id.Statistics_PieChart)
    com.github.mikephil.charting.charts.PieChart pie;


    @BindView(R.id.Statistics_NoData)
    TextView NoDataText;


    int TimeLine_ID = 0;

    /*
    @BindView(R.id.Statistics_Overlay)
    RelativeLayout OverLay;*/

/*
    @BindView(R.id.RecyclerLayout)
            LinearLayout RecyclerLayout;
    */

    @BindView(R.id.ShowLastLayout)
    LinearLayout ShowLastLayout;



    private int CurrentAccount =0;

    List<Account> accounts = new ArrayList<>();



    Account init = new Account(0,"جميع الحسابات",0,"#47d469",false);
    private RecyclerView accountRecycler;
    private AccountAdapter accountAdapter;
    View rootView;


    final Calendar myCalendar = Calendar.getInstance();
    private AppDatabase mDb;

     int CurrentAccountId = 0;



    AlertDialog dialog;
    View yourCustomView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.activity_statistics, container, false);
        mDb = AppDatabase.getsInstance(getActivity());

        ButterKnife.bind(this,rootView);
        initDialouge();
        init.setStatus(true);

      //  initStatisticsRecycler();
        //initPieChart();
     //   insertDummyAccounts();

        LoadAccounts();


        LocalDate weekBeforeToday = LocalDate.now().minusDays(7);
        Date date = weekBeforeToday.toDateTimeAtStartOfDay().toDate();




        Log.v("Joda LastDay",weekBeforeToday+"");
        Log.v("date LastDay",date+"");


        Log.v("Joda nowDay",LocalDate.now()+"");
        Log.v("date nowDay",new Date()+"");




        GetLast7Days();


        return rootView;

    }





    void ShowData(){
        pie.setVisibility(View.VISIBLE);
                NoDataText.setVisibility(View.GONE);
    }

    void ShowNoData(){
        pie.setVisibility(View.INVISIBLE);
        NoDataText.setVisibility(View.VISIBLE);

    }

    void initDialouge(){
        LayoutInflater inflater2 = LayoutInflater.from(getActivity());

        yourCustomView = inflater2.inflate(R.layout.dialouge_accounts, null);
        initStatisticsRecycler();


        dialog = new AlertDialog.Builder(getActivity())
                .setView(yourCustomView)

                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {

                        AccountArrow.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.ic_baseline_keyboard_arrow_down_24px));
                        AccountArrow.setTag("false");
                    }
                })


                .create();
        /*


                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        // final TextView etName = (EditText) yourCustomView.findViewById(R.id.EnterCategoryOption);





                    }
                })
                .setNegativeButton("Cancel", null).create();*/

        /*
        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        dialog.getWindow().setLayout((int)(displayRectangle.width() *
                0.9f), (int)(displayRectangle.height() * 0.9f));


        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
*/




    }

    void GetLast7Days(){

     //   Long start = getStartOfLast7Days(myCalendar);

   //     Long end = getEndOfToDay(myCalendar);




        Long start =  LocalDate.now().minusDays(7).toDateTimeAtStartOfDay().getMillis();
        Long end =  LocalDate.now().plusDays(1).toDateTimeAtStartOfDay().getMillis();


        Log.v("start",start+"");
        Log.v("end",end+"");
        final LiveData<List<Expense>> expenses1   = mDb.expenseDao().loadAllExpenses(start,end);

        expenses1.observe(this, new Observer<List<Expense>>() {
            @Override
            public void onChanged(@Nullable List<Expense> expenses) {



                ArrayList<Float> OutcomesAndIncomes  =   CalculateOutcomesAndIncomes(expenses);

                initPieChart(OutcomesAndIncomes.get(0).intValue(),OutcomesAndIncomes.get(1).intValue());

                expenses1.removeObserver(this);



            }
        });





    }

    void GetLastMonth(){

        Long start =  LocalDate.now().minusMonths(1).toDateTimeAtStartOfDay().getMillis();
        Long end =  LocalDate.now().plusDays(1).toDateTimeAtStartOfDay().getMillis();




        Log.v("start",start+"");
        Log.v("end",end+"");
        final LiveData<List<Expense>> expenses1   = mDb.expenseDao().loadAllExpenses(start,end);

        expenses1.observe(this, new Observer<List<Expense>>() {
            @Override
            public void onChanged(@Nullable List<Expense> expenses) {


Log.v("size",expenses.size()+"");
                ArrayList<Float> OutcomesAndIncomes  =   CalculateOutcomesAndIncomes(expenses);

                initPieChart(OutcomesAndIncomes.get(0).intValue(),OutcomesAndIncomes.get(1).intValue());

                expenses1.removeObserver(this);
            }
        });





    }

    void GetLast6Months(){


        Long start =  LocalDate.now().minusMonths(6).toDateTimeAtStartOfDay().getMillis();
        Long end =  LocalDate.now().plusDays(1).toDateTimeAtStartOfDay().getMillis();


        Log.v("start",start+"");
        Log.v("end",end+"");

        final LiveData<List<Expense>> expenses1   = mDb.expenseDao().loadAllExpenses(start,end);

        expenses1.observe(this, new Observer<List<Expense>>() {
            @Override
            public void onChanged(@Nullable List<Expense> expenses) {



                ArrayList<Float> OutcomesAndIncomes  =   CalculateOutcomesAndIncomes(expenses);

                initPieChart(OutcomesAndIncomes.get(0).intValue(),OutcomesAndIncomes.get(1).intValue());

                expenses1.removeObserver(this);
            }
        });





    }

    void GetLastYear(){
        Long start =  LocalDate.now().minusYears(1).toDateTimeAtStartOfDay().getMillis();

        Long end =  LocalDate.now().plusDays(1).toDateTimeAtStartOfDay().getMillis();


        Log.v("start",start+"");
        Log.v("end",end+"");

        final LiveData<List<Expense>> expenses1   = mDb.expenseDao().loadAllExpenses(start,end);

        expenses1.observe(this, new Observer<List<Expense>>() {
            @Override
            public void onChanged(@Nullable List<Expense> expenses) {



                ArrayList<Float> OutcomesAndIncomes  =   CalculateOutcomesAndIncomes(expenses);

                initPieChart(OutcomesAndIncomes.get(0).intValue(),OutcomesAndIncomes.get(1).intValue());

                expenses1.removeObserver(this);
            }
        });





    }


    void GetLast7Days(int id){

        //   Long start = getStartOfLast7Days(myCalendar);

        //     Long end = getEndOfToDay(myCalendar);




        Long start =  LocalDate.now().minusDays(7).toDateTimeAtStartOfDay().getMillis();
        Long end =  LocalDate.now().plusDays(1).toDateTimeAtStartOfDay().getMillis();


        Log.v("start",start+"");
        Log.v("end",end+"");
        final LiveData<List<Expense>> expenses1   = mDb.expenseDao().loadExpeseByAccountId(start,end,CurrentAccountId);

        expenses1.observe(this, new Observer<List<Expense>>() {
            @Override
            public void onChanged(@Nullable List<Expense> expenses) {



                ArrayList<Float> OutcomesAndIncomes  =   CalculateOutcomesAndIncomes(expenses);

                initPieChart(OutcomesAndIncomes.get(0).intValue(),OutcomesAndIncomes.get(1).intValue());

                expenses1.removeObserver(this);
            }
        });





    }

    void GetLastMonth(int id){

        Long start =  LocalDate.now().minusMonths(1).toDateTimeAtStartOfDay().getMillis();
        Long end =  LocalDate.now().plusDays(1).toDateTimeAtStartOfDay().getMillis();




        Log.v("start",start+"");
        Log.v("end",end+"");
        final LiveData<List<Expense>> expenses1   = mDb.expenseDao().loadExpeseByAccountId(start,end,CurrentAccountId);

        expenses1.observe(this, new Observer<List<Expense>>() {
            @Override
            public void onChanged(@Nullable List<Expense> expenses) {


                Log.v("size",expenses.size()+"");
                ArrayList<Float> OutcomesAndIncomes  =   CalculateOutcomesAndIncomes(expenses);

                initPieChart(OutcomesAndIncomes.get(0).intValue(),OutcomesAndIncomes.get(1).intValue());

                expenses1.removeObserver(this);
            }
        });





    }

    void GetLast6Months(int id){


        Long start =  LocalDate.now().minusMonths(6).toDateTimeAtStartOfDay().getMillis();
        Long end =  LocalDate.now().plusDays(1).toDateTimeAtStartOfDay().getMillis();


        Log.v("start",start+"");
        Log.v("end",end+"");

        final LiveData<List<Expense>> expenses1   = mDb.expenseDao().loadExpeseByAccountId(start,end,CurrentAccountId);

        expenses1.observe(this, new Observer<List<Expense>>() {
            @Override
            public void onChanged(@Nullable List<Expense> expenses) {



                ArrayList<Float> OutcomesAndIncomes  =   CalculateOutcomesAndIncomes(expenses);

                initPieChart(OutcomesAndIncomes.get(0).intValue(),OutcomesAndIncomes.get(1).intValue());

                expenses1.removeObserver(this);
            }
        });





    }

    void GetLastYear(int id){
        Long start =  LocalDate.now().minusYears(1).toDateTimeAtStartOfDay().getMillis();

        Long end =  LocalDate.now().plusDays(1).toDateTimeAtStartOfDay().getMillis();


        Log.v("start",start+"");
        Log.v("end",end+"");

        final LiveData<List<Expense>> expenses1   = mDb.expenseDao().loadExpeseByAccountId(start,end,CurrentAccountId);

        expenses1.observe(this, new Observer<List<Expense>>() {
            @Override
            public void onChanged(@Nullable List<Expense> expenses) {



                ArrayList<Float> OutcomesAndIncomes  =   CalculateOutcomesAndIncomes(expenses);

                initPieChart(OutcomesAndIncomes.get(0).intValue(),OutcomesAndIncomes.get(1).intValue());

                expenses1.removeObserver(this);
            }
        });





    }



    void initStatisticsRecycler(){

       accountRecycler = yourCustomView.findViewById(R.id.Statistics_AccountRecycler);
       accountAdapter = new AccountAdapter(getActivity(),accounts,this);

       LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
       layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
       accountRecycler.setLayoutManager(layoutManager);
       accountRecycler.setAdapter(accountAdapter);


    }




    void LoadAccounts()
    {

        final LiveData<List<Account>> c = mDb.accountDao().loadAllAccounts();

        c.observe(this, new Observer<List<Account>>() {
            @Override
            public void onChanged(@Nullable List<Account> accounts1) {

                accounts.clear();
                accounts.addAll(accounts1);


                init.setTotal(voidGetTotalAccountsPrices());
                accounts.add(0,init);
                accountAdapter.notifyDataSetChanged();

                c.removeObserver(this);

            }
        });

    }


  float  voidGetTotalAccountsPrices(){

        float total = 0;

        for(int i=0;i<accounts.size();i++)
        {
            total+=accounts.get(i).getTotal();
        }
        return total;
    }

   ArrayList<Float> CalculateOutcomesAndIncomes(List<Expense> expenses){

        ArrayList<Float> OutcomesAndIncomes = new ArrayList<>();

        float totalOutComes = 0;
        float totalIncomes = 0;

        for (int i = 0 ; i<expenses.size();i++)
        {
            if (expenses.get(i).getType().equals("Outcome"))
            {

                Log.v("Outcome",expenses.get(i).getPrice()+"");
                totalOutComes+=expenses.get(i).getPrice();
            }

            else{
                Log.v("Income",expenses.get(i).getPrice()+"");
                totalIncomes+=expenses.get(i).getPrice();
            }
        }


        OutcomesAndIncomes.add(totalOutComes);
        OutcomesAndIncomes.add(totalIncomes);

        return OutcomesAndIncomes;


   }


    @Override
    public void onListItemClick(int clickedItemIndex) {



         accounts.get(clickedItemIndex).setStatus(true);
         accounts.get(CurrentAccount).setStatus(false);
         accountAdapter.notifyDataSetChanged();
        CurrentAccount = clickedItemIndex;
        CurrentAccountId = accounts.get(clickedItemIndex).getId();

        CurrentAccountName.setText(accounts.get(clickedItemIndex).getName()+"");


        //Hide Dialouge
        AccountArrow.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.ic_baseline_keyboard_arrow_down_24px));
        AccountArrow.setTag("false");
        dialog.hide();

        GetStatistticsData(TimeLine_ID);

    }


    void Get7Days(){
        if (CurrentAccount>0)
            GetLast7Days(CurrentAccountId);

        else
            GetLast7Days();

    }

    void GetMonth(){
        if (CurrentAccount>0)
            GetLastMonth(CurrentAccountId);
        else
            GetLastMonth();
    }


    void Get6Months(){
        if (CurrentAccount>0)
            GetLast6Months(CurrentAccountId);
        else
            GetLast6Months();
    }

    void GetYear(){
        if (CurrentAccount>0)
            GetLastYear(CurrentAccountId);
        else
            GetLastYear();
    }



    void GetStatistticsData(int id){

        switch (id){

            case 0:
                Get7Days();
                break;

            case 1:
                GetMonth();
                break;

            case 2:
                Get6Months();
                break;
            case 3:
                GetYear();
                break;


        }


    }



    @OnClick(R.id.Statistics_TimeLine_days)
    void daysClick(){
        TimeLine_ID = 0;
        setTimeLineClick(0);
 Get7Days();
    }

    @OnClick(R.id.Statistics_TimeLine_month)
    void monthClick(){
        TimeLine_ID = 1;
        setTimeLineClick(1);
GetMonth();
    }

    @OnClick(R.id.Statistics_TimeLine_6months)
    void monthsClick(){
        TimeLine_ID = 2;
        setTimeLineClick(2);
Get6Months();

    }

    @OnClick(R.id.Statistics_TimeLine_year)
    void yearClick(){
        TimeLine_ID = 3;
        setTimeLineClick(3);
        GetYear();

    }




    void setTimeLineClick(int index){


        switch (index){

            case 0:

                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    TimeLine_days.setBackgroundColor(getResources().getColor(R.color.TimeLine_Acitve));

                else
                    TimeLine_days.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.TimeLine_Acitve));


                TimeLine_days_Text.setTextColor(getResources().getColor(R.color.white));



                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    TimeLine_month.setBackgroundColor(getResources().getColor(R.color.TimeLine_InAcitve));

                else

                TimeLine_month.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.TimeLine_InAcitve));

                TimeLine_month_Text.setTextColor(getResources().getColor(R.color.TimeLineInActive));


                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    TimeLine_6months.setBackgroundColor(getResources().getColor(R.color.TimeLine_InAcitve));

                else
                    TimeLine_6months.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.TimeLine_InAcitve));


                TimeLine_6months_Text.setTextColor(getResources().getColor(R.color.TimeLineInActive));



                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    TimeLine_year.setBackgroundColor(getResources().getColor(R.color.TimeLine_InAcitve));

                else
                    TimeLine_year.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.TimeLine_InAcitve));
                TimeLine_year_Text.setTextColor(getResources().getColor(R.color.TimeLineInActive));

                break;



            case 1:

                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    TimeLine_days.setBackgroundColor(getResources().getColor(R.color.TimeLine_InAcitve));

                else
                    TimeLine_days.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.TimeLine_InAcitve));


                TimeLine_days_Text.setTextColor(getResources().getColor(R.color.TimeLineInActive));



                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    TimeLine_month.setBackgroundColor(getResources().getColor(R.color.TimeLine_Acitve));

                else

                    TimeLine_month.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.TimeLine_Acitve));

                TimeLine_month_Text.setTextColor(getResources().getColor(R.color.white));


                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    TimeLine_6months.setBackgroundColor(getResources().getColor(R.color.TimeLine_InAcitve));

                else
                    TimeLine_6months.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.TimeLine_InAcitve));


                TimeLine_6months_Text.setTextColor(getResources().getColor(R.color.TimeLineInActive));



                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    TimeLine_year.setBackgroundColor(getResources().getColor(R.color.TimeLine_InAcitve));

                else
                    TimeLine_year.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.TimeLine_InAcitve));
                TimeLine_year_Text.setTextColor(getResources().getColor(R.color.TimeLineInActive));

                break;



            case 2:

                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    TimeLine_days.setBackgroundColor(getResources().getColor(R.color.TimeLine_InAcitve));

                else
                    TimeLine_days.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.TimeLine_InAcitve));


                TimeLine_days_Text.setTextColor(getResources().getColor(R.color.TimeLineInActive));



                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    TimeLine_month.setBackgroundColor(getResources().getColor(R.color.TimeLine_InAcitve));

                else

                    TimeLine_month.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.TimeLine_InAcitve));

                TimeLine_month_Text.setTextColor(getResources().getColor(R.color.TimeLineInActive));


                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    TimeLine_6months.setBackgroundColor(getResources().getColor(R.color.TimeLine_Acitve));

                else
                    TimeLine_6months.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.TimeLine_Acitve));


                TimeLine_6months_Text.setTextColor(getResources().getColor(R.color.white));



                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    TimeLine_year.setBackgroundColor(getResources().getColor(R.color.TimeLine_InAcitve));

                else
                    TimeLine_year.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.TimeLine_InAcitve));
                TimeLine_year_Text.setTextColor(getResources().getColor(R.color.TimeLineInActive));

                break;




            case 3:

                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    TimeLine_days.setBackgroundColor(getResources().getColor(R.color.TimeLine_InAcitve));

                else
                    TimeLine_days.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.TimeLine_InAcitve));


                TimeLine_days_Text.setTextColor(getResources().getColor(R.color.TimeLineInActive));



                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    TimeLine_month.setBackgroundColor(getResources().getColor(R.color.TimeLine_InAcitve));

                else

                    TimeLine_month.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.TimeLine_InAcitve));

                TimeLine_month_Text.setTextColor(getResources().getColor(R.color.TimeLineInActive));


                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    TimeLine_6months.setBackgroundColor(getResources().getColor(R.color.TimeLine_InAcitve));

                else
                    TimeLine_6months.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.TimeLine_InAcitve));


                TimeLine_6months_Text.setTextColor(getResources().getColor(R.color.TimeLineInActive));



                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    TimeLine_year.setBackgroundColor(getResources().getColor(R.color.TimeLine_Acitve));

                else
                    TimeLine_year.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.TimeLine_Acitve));
                TimeLine_year_Text.setTextColor(getResources().getColor(R.color.white));

                break;



        }

    }

    void initPieChart(int Outcomes,int Incomes){




        if (Outcomes <= 0 && Incomes <=0)
        {
            ShowNoData();
        }
        else{
            ShowData();


        List<PieEntry> entries = new ArrayList<>();
        Chart<PieData> pieChart = rootView.findViewById(R.id.Statistics_PieChart);

        entries.add(new PieEntry(Outcomes, "المصروفات"));
        entries.add(new PieEntry(Incomes, "الايرادات"));


        final int[] MY_COLORS = {Color.rgb(190,0,0),Color.rgb(0,126,0)
                };
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for(int c: MY_COLORS) colors.add(c);


        PieDataSet set = new PieDataSet(entries, "");
        set.setColors(colors);
        set.setValueTextSize(25f);
        set.setValueTextColor(Color.rgb(255,255,255));


        PieData data = new PieData(set);
        data.setValueTextSize(25f);
        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);



        pieChart.getLegend().setEnabled(true);


            pieChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            pieChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            pieChart.getLegend().setOrientation(Legend.LegendOrientation.HORIZONTAL);
            pieChart.getLegend().setDrawInside(true);



            pieChart.animate();

        pieChart.setData(data);
        pieChart.invalidate(); // refresh

        }
    }



    @OnClick(R.id.Statistics_AccountSpinner)
    void OpenAccounts(){

        if (AccountArrow.getTag().equals("false"))
        {

        AccountArrow.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.ic_baseline_keyboard_arrow_up_24px));

            AccountArrow.setTag("true");

            dialog.show();


        }

        else
            {

                AccountArrow.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.ic_baseline_keyboard_arrow_down_24px));
                AccountArrow.setTag("false");
                dialog.hide();
            }




    }










}
