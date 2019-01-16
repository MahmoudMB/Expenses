package com.example.mahmoudbahaa.expenses;

import android.support.v4.app.Fragment;

import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mahmoudbahaa.expenses.adapters.AccountAdapter;
import com.example.mahmoudbahaa.expenses.models.Account;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
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


    @BindView(R.id.Statistics_Overlay)
    RelativeLayout OverLay;


    @BindView(R.id.RecyclerLayout)
            LinearLayout RecyclerLayout;
    @BindView(R.id.ShowLastLayout)
    LinearLayout ShowLastLayout;



    private int CurrentAccount = 0;

    List<Account> accounts = new ArrayList<>();
    private RecyclerView accountRecycler;
    private AccountAdapter accountAdapter;
    View rootView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.activity_statistics, container, false);

        ButterKnife.bind(this,rootView);
        initStatisticsRecycler();
        initPieChart();
        insertDummyAccounts();

        return rootView;

    }


   void initStatisticsRecycler(){

       accountRecycler = rootView.findViewById(R.id.Statistics_AccountRecycler);
       accountAdapter = new AccountAdapter(getActivity(),accounts,this);

       LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
       layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
       accountRecycler.setLayoutManager(layoutManager);
       accountRecycler.setAdapter(accountAdapter);


    }


    void insertDummyAccounts(){


       Account a = new Account();

       a.setStatus(true);
       a.setId(1);
       a.setName("الحساب الاساسي");
       a.setIcon("ic_baseline_account_balance_24px");
       accounts.add(a);


        a = new Account();

        a.setStatus(false);
        a.setId(2);
        a.setName("حساب الراجحي");
        a.setIcon("ic_baseline_account_balance_24px");
        accounts.add(a);



        a = new Account();

        a.setStatus(false);
        a.setId(3);
        a.setName("حساب الاهلي");
        a.setIcon("ic_baseline_account_balance_24px");
        accounts.add(a);



   }



    @Override
    public void onListItemClick(int clickedItemIndex) {

         accounts.get(clickedItemIndex).setStatus(true);
         accounts.get(CurrentAccount).setStatus(false);
         accountAdapter.notifyDataSetChanged();
        CurrentAccount = clickedItemIndex;




    }


    @OnClick(R.id.Statistics_TimeLine_days)
    void daysClick(){
        setTimeLineClick(0);
    }

    @OnClick(R.id.Statistics_TimeLine_month)
    void monthClick(){
        setTimeLineClick(1);
    }

    @OnClick(R.id.Statistics_TimeLine_6months)
    void monthsClick(){
        setTimeLineClick(2);
    }

    @OnClick(R.id.Statistics_TimeLine_year)
    void yearClick(){
        setTimeLineClick(3);
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

                TimeLine_month_Text.setTextColor(getResources().getColor(R.color.white));


                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    TimeLine_6months.setBackgroundColor(getResources().getColor(R.color.TimeLine_InAcitve));

                else
                    TimeLine_6months.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.TimeLine_InAcitve));


                TimeLine_6months_Text.setTextColor(getResources().getColor(R.color.white));



                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    TimeLine_year.setBackgroundColor(getResources().getColor(R.color.TimeLine_InAcitve));

                else
                    TimeLine_year.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.TimeLine_InAcitve));
                TimeLine_year_Text.setTextColor(getResources().getColor(R.color.white));

                break;



            case 1:

                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    TimeLine_days.setBackgroundColor(getResources().getColor(R.color.TimeLine_InAcitve));

                else
                    TimeLine_days.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.TimeLine_InAcitve));


                TimeLine_days_Text.setTextColor(getResources().getColor(R.color.white));



                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    TimeLine_month.setBackgroundColor(getResources().getColor(R.color.TimeLine_Acitve));

                else

                    TimeLine_month.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.TimeLine_Acitve));

                TimeLine_month_Text.setTextColor(getResources().getColor(R.color.white));


                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    TimeLine_6months.setBackgroundColor(getResources().getColor(R.color.TimeLine_InAcitve));

                else
                    TimeLine_6months.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.TimeLine_InAcitve));


                TimeLine_6months_Text.setTextColor(getResources().getColor(R.color.white));



                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    TimeLine_year.setBackgroundColor(getResources().getColor(R.color.TimeLine_InAcitve));

                else
                    TimeLine_year.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.TimeLine_InAcitve));
                TimeLine_year_Text.setTextColor(getResources().getColor(R.color.white));

                break;



            case 2:

                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    TimeLine_days.setBackgroundColor(getResources().getColor(R.color.TimeLine_InAcitve));

                else
                    TimeLine_days.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.TimeLine_InAcitve));


                TimeLine_days_Text.setTextColor(getResources().getColor(R.color.white));



                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    TimeLine_month.setBackgroundColor(getResources().getColor(R.color.TimeLine_InAcitve));

                else

                    TimeLine_month.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.TimeLine_InAcitve));

                TimeLine_month_Text.setTextColor(getResources().getColor(R.color.white));


                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    TimeLine_6months.setBackgroundColor(getResources().getColor(R.color.TimeLine_Acitve));

                else
                    TimeLine_6months.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.TimeLine_Acitve));


                TimeLine_6months_Text.setTextColor(getResources().getColor(R.color.white));



                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    TimeLine_year.setBackgroundColor(getResources().getColor(R.color.TimeLine_InAcitve));

                else
                    TimeLine_year.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.TimeLine_InAcitve));
                TimeLine_year_Text.setTextColor(getResources().getColor(R.color.white));

                break;




            case 3:

                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    TimeLine_days.setBackgroundColor(getResources().getColor(R.color.TimeLine_InAcitve));

                else
                    TimeLine_days.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.TimeLine_InAcitve));


                TimeLine_days_Text.setTextColor(getResources().getColor(R.color.white));



                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    TimeLine_month.setBackgroundColor(getResources().getColor(R.color.TimeLine_InAcitve));

                else

                    TimeLine_month.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.TimeLine_InAcitve));

                TimeLine_month_Text.setTextColor(getResources().getColor(R.color.white));


                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    TimeLine_6months.setBackgroundColor(getResources().getColor(R.color.TimeLine_InAcitve));

                else
                    TimeLine_6months.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.TimeLine_InAcitve));


                TimeLine_6months_Text.setTextColor(getResources().getColor(R.color.white));



                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    TimeLine_year.setBackgroundColor(getResources().getColor(R.color.TimeLine_Acitve));

                else
                    TimeLine_year.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.TimeLine_Acitve));
                TimeLine_year_Text.setTextColor(getResources().getColor(R.color.white));

                break;



        }

    }

    void initPieChart(){


        List<PieEntry> entries = new ArrayList<>();
        Chart<PieData> pieChart = rootView.findViewById(R.id.Statistics_PieChart);

        entries.add(new PieEntry(3308, "المصروفات"));
        entries.add(new PieEntry(6000, "الايرادات"));


        final int[] MY_COLORS = {Color.rgb(0,100,0)
                , Color.rgb(255,0,0)};
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for(int c: MY_COLORS) colors.add(c);


        PieDataSet set = new PieDataSet(entries, "Election Results");
        set.setColors(colors);
        set.setValueTextSize(25f);
        set.setValueTextColor(Color.rgb(255,255,255));


        PieData data = new PieData(set);
        data.setValueTextSize(25f);
        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);

        pieChart.getLegend().setEnabled(false);
        pieChart.animate();

        pieChart.setData(data);
        pieChart.invalidate(); // refresh
    }



    @OnClick(R.id.Statistics_AccountArrow)
    void OpenAccounts(){

        if (AccountArrow.getTag().equals("false"))
        {

        AccountArrow.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.ic_baseline_keyboard_arrow_up_24px));

            AccountArrow.setTag("true");

            RecyclerLayout.setVisibility(View.VISIBLE);
            OverLay.setVisibility(View.VISIBLE);

            ShowLastLayout.setVisibility(View.GONE);

        }

        else
            {

                AccountArrow.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.ic_baseline_keyboard_arrow_down_24px));


                AccountArrow.setTag("false");


                RecyclerLayout.setVisibility(View.GONE);
                OverLay.setVisibility(View.GONE);
                ShowLastLayout.setVisibility(View.VISIBLE);

            }


    }






}
