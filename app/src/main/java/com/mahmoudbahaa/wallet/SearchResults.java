package com.mahmoudbahaa.wallet;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.mahmoudbahaa.wallet.adapters.ExpenseAdapter;
import com.mahmoudbahaa.wallet.data.AppDatabase;
import com.mahmoudbahaa.wallet.models.Expense;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchResults extends AppCompatActivity implements ExpenseAdapter.ListItemClickListener {

    @BindView(R.id.SearchResult_Recycler)
    RecyclerView recyclerView;


    @BindView(R.id.Total_Income)
    TextView Total_Income;

    @BindView(R.id.Total_OutCome)
    TextView Total_OutCome;



    List<Expense> expensesData = new ArrayList<>();
    ExpenseAdapter adapter;


    private AppDatabase mDb;


    int categoryId = 0;
    int accountId = 0;
    String type = "";
    long start  = 0;
    long end =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        ButterKnife.bind(this);
        mDb = AppDatabase.getsInstance(getApplicationContext());

        categoryId = getIntent().getIntExtra("categoryId",0);
        accountId = getIntent().getIntExtra("accountId",0);
        type =  getIntent().getStringExtra("type");
        start = getIntent().getLongExtra("start",0);
        end = getIntent().getLongExtra("end",0);




 if (categoryId != 0 && accountId !=0 && !type.equals("All"))
{

    Long start1 = getStartOfDayInMillis(start);
    Long end1 = getEndOfDayInMillis(end);


    final LiveData<List<Expense>> expenses1   = mDb.expenseDao().loadExpese_t_a_c(start1,end1,type,accountId,categoryId);
    expenses1.observe(this, new Observer<List<Expense>>() {
        @Override
        public void onChanged(@Nullable List<Expense> expenses) {
            expensesData.addAll(expenses);
            adapter.notifyDataSetChanged();
            expenses1.removeObserver(this);
            CalculateTotals();
        }
    });


}


else  if (categoryId == 0 && accountId !=0 && !type.equals("All"))
        {

            Long start1 = getStartOfDayInMillis(start);
            Long end1 = getEndOfDayInMillis(end);


            final LiveData<List<Expense>> expenses1   = mDb.expenseDao().loadExpese_t_a(start1,end1,type,accountId);
            expenses1.observe(this, new Observer<List<Expense>>() {
                @Override
                public void onChanged(@Nullable List<Expense> expenses) {
                    expensesData.addAll(expenses);
                    adapter.notifyDataSetChanged();
                    expenses1.removeObserver(this);
                    CalculateTotals();

                }
            });


        }

        else  if (categoryId != 0 && accountId ==0 && !type.equals("All"))
        {

            Long start1 = getStartOfDayInMillis(start);
            Long end1 = getEndOfDayInMillis(end);


            final LiveData<List<Expense>> expenses1   = mDb.expenseDao().loadExpese_t_c(start1,end1,type,categoryId);
            expenses1.observe(this, new Observer<List<Expense>>() {
                @Override
                public void onChanged(@Nullable List<Expense> expenses) {
                    expensesData.addAll(expenses);
                    adapter.notifyDataSetChanged();
                    expenses1.removeObserver(this);
                    CalculateTotals();

                }
            });
        }




        else if (categoryId == 0 && accountId ==0 && !type.equals("All"))
        {

            Long start1 = getStartOfDayInMillis(start);
            Long end1 = getEndOfDayInMillis(end);


            final LiveData<List<Expense>> expenses1   = mDb.expenseDao().loadExpese_t(start1,end1,type);
            expenses1.observe(this, new Observer<List<Expense>>() {
                @Override
                public void onChanged(@Nullable List<Expense> expenses) {
                    expensesData.addAll(expenses);
                    adapter.notifyDataSetChanged();
                    expenses1.removeObserver(this);
                    CalculateTotals();

                }
            });
        }






        else   if (categoryId != 0 && accountId !=0 && type.equals("All"))
        {


            Long start1 = getStartOfDayInMillis(start);
            Long end1 = getEndOfDayInMillis(end);


            final LiveData<List<Expense>> expenses1   = mDb.expenseDao().loadExpese_a_c(start1,end1,accountId,categoryId);
            expenses1.observe(this, new Observer<List<Expense>>() {
                @Override
                public void onChanged(@Nullable List<Expense> expenses) {
                    expensesData.addAll(expenses);
                    adapter.notifyDataSetChanged();
                    expenses1.removeObserver(this);
                    CalculateTotals();

                }
            });

        }



        else   if (categoryId == 0 && accountId !=0 && type.equals("All"))
        {

            Long start1 = getStartOfDayInMillis(start);
            Long end1 = getEndOfDayInMillis(end);


            final LiveData<List<Expense>> expenses1   = mDb.expenseDao().loadExpese_a(start1,end1,accountId);
            expenses1.observe(this, new Observer<List<Expense>>() {
                @Override
                public void onChanged(@Nullable List<Expense> expenses) {
                    expensesData.addAll(expenses);
                    adapter.notifyDataSetChanged();
                    expenses1.removeObserver(this);
                    CalculateTotals();

                }
            });

        }


        else  if (categoryId != 0 && accountId ==0 && type.equals("All"))
        {

            Long start1 = getStartOfDayInMillis(start);
            Long end1 = getEndOfDayInMillis(end);


            final LiveData<List<Expense>> expenses1   = mDb.expenseDao().loadExpese_c(start1,end1,categoryId);
            expenses1.observe(this, new Observer<List<Expense>>() {
                @Override
                public void onChanged(@Nullable List<Expense> expenses) {
                    expensesData.addAll(expenses);
                    adapter.notifyDataSetChanged();
                    expenses1.removeObserver(this);
                    CalculateTotals();

                }
            });


        }


        //////////


        else if (categoryId == 0 && accountId ==0 && type.equals("All"))
        {


            Long start1 = getStartOfDayInMillis(start);
            Long end1 = getEndOfDayInMillis(end);


            final LiveData<List<Expense>> expenses1   = mDb.expenseDao().loadAllExpenses(start1,end1);
            expenses1.observe(this, new Observer<List<Expense>>() {
                @Override
                public void onChanged(@Nullable List<Expense> expenses) {
                    expensesData.addAll(expenses);
                    adapter.notifyDataSetChanged();
                    expenses1.removeObserver(this);
                    CalculateTotals();

                }
            });


        }




        initRecyclerView();

    }



    void CalculateTotals(){

        float incomeTotal = 0;
        float outcomeTotal = 0;


        for(int i=0;i<expensesData.size();i++)
        {

            if (expensesData.get(i).getType().equals("Income"))
            {
                incomeTotal += expensesData.get(i).getPrice();
            }

           else if (expensesData.get(i).getType().equals("Outcome"))
            {
                outcomeTotal += expensesData.get(i).getPrice();
            }

        }


        Total_Income.setText(incomeTotal+"");
        Total_OutCome.setText(outcomeTotal+"");

    }

    private void initRecyclerView(){

        adapter = new ExpenseAdapter(getApplicationContext(),expensesData,this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }





    @Override
    public void onListItemClick(int clickedItemIndex) {


        Intent i = new Intent(SearchResults.this,AddActivity.class);
        i.putExtra("Expense",expensesData.get(clickedItemIndex));

        startActivity(i);


    }

    @OnClick(R.id.EditCategory_Back)
    void EndResult(){
        finish();
    }


    public long getStartOfDayInMillis(long time) {
      //  Calendar c = Calendar.getInstance();
     //   c.setTimeInMillis(time);
       // c.set(Calendar.HOUR_OF_DAY, 0);
       // c.set(Calendar.MINUTE, 0);
       // c.set(Calendar.SECOND, 0);
      //  c.set(Calendar.MILLISECOND, 0);

        DateTime someDate = new DateTime(time);

        return someDate.getMillis();
    }

    public long getEndOfDayInMillis(long time) {
// Add one day's time to the beginning of the day.
// 24 hours * 60 minutes * 60 seconds * 1000 milliseconds = 1 day


        DateTime someDate = new DateTime(time);

        return someDate.plusDays(1).withTime(0,0,0,0).getMillis();
    }





}
