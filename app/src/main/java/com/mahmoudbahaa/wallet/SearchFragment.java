package com.mahmoudbahaa.expenses;

import android.app.DatePickerDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mahmoudbahaa.expenses.adapters.AccountAdapter;
import com.mahmoudbahaa.expenses.adapters.CategoryAdapter;
import com.mahmoudbahaa.expenses.data.AppDatabase;
import com.mahmoudbahaa.expenses.models.Account;
import com.mahmoudbahaa.expenses.models.Category;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements AccountAdapter.ListItemClickListener,CategoryAdapter.ListItemClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;






View rootView ;


    int categoryId = 0;
    int accountId = 0;
    String type = "All";
    long start  = 0;
    long end =0;

    DatePickerDialog.OnDateSetListener dateFrom;

    final Calendar myCalendarFrom = Calendar.getInstance();



    DatePickerDialog.OnDateSetListener dateTo;

    final Calendar myCalendarTo = Calendar.getInstance();



    @BindView(R.id.Search_All)
    LinearLayout Search_All;




    @BindView(R.id.Add_OutCome)
    LinearLayout Add_OutCome;

    @BindView(R.id.Add_Income)
    LinearLayout Add_Income;


    @BindView(R.id.Search_All_Text)
    TextView Search_All_Text;


    @BindView(R.id.Add_OutCome_Text)
    TextView Add_OutCome_Text;

    @BindView(R.id.Add_Income_Text)
    TextView Add_Income_Text;






    @BindView(R.id.Search_AccountText)
    TextView Search_AccountText;

    @BindView(R.id.Search_CategoryText)
    TextView Search_CategoryText;


    @BindView(R.id.Search_DateFromText)
    TextView Search_DateFromText;


    @BindView(R.id.Search_DateToText)
    TextView Search_DateToText;



    private int CurrentAccount =0;

    List<Account> accounts = new ArrayList<>();

    private AppDatabase mDb;


    Account initAccount = new Account(0,"جميع الحسابات",0,"#47d469",false);
    private RecyclerView accountRecycler;
    private AccountAdapter accountAdapter;

    int CurrentAccountId = 0;


    AlertDialog dialogAccount;
    View yourCustomViewAccount;




    ///////////////////////////


    private int CurrentCategoryOutcome =0;

    List<Category> CategoryOutcome = new ArrayList<>();



    Category initCategoryOutcome =new Category(0,"جميع المصروفات","#47d469","Outcome",false);
    private RecyclerView CategoryOutcomeRecycler;
    private CategoryAdapter CategoryOutcomeAdapter;

    int CurrentCategoryOutcomeId = 0;


    AlertDialog dialogCategoryOutcome;
    View yourCustomViewCategoryOutcome;

////////////////////////////////////////



    private int CurrentCategoryIncome =0;

    List<Category> CategoryIncome = new ArrayList<>();



    Category initCategoryIncome = new Category(0,"جميع الايرادات","#47d469","Income",false);
    private RecyclerView CategoryIncomeRecycler;
    private CategoryAdapter CategoryIncomeAdapter;

    int CurrentCategoryIncomeId = 0;


    AlertDialog dialogCategoryIncome;
    View yourCustomViewCategoryIncome;



    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this,rootView);
        mDb = AppDatabase.getsInstance(getActivity());

        initAccount.setStatus(true);
        initCategoryOutcome.setStatus(true);
        initCategoryIncome.setStatus(true);
        rootView.findViewById(R.id.Search_Category).setVisibility(View.GONE);
        rootView.findViewById(R.id.Search_Category_line).setVisibility(View.GONE);
        setTimeLineClick(0);



        updateLabelfrom();
        updateLabelTo();




        initDialougeous();
        LoadAccountsCategories();
        initDatePickerFrom();
        initDatePickerTo();










        return rootView;
    }





    void initDialougeous(){



        LayoutInflater inflater1 = LayoutInflater.from(getActivity());

        yourCustomViewAccount = inflater1.inflate(R.layout.dialouge_accounts, null);


        dialogAccount = new AlertDialog.Builder(getActivity())
                .setView(yourCustomViewAccount)
                .create();


        LayoutInflater inflater2 = LayoutInflater.from(getActivity());

        yourCustomViewCategoryIncome = inflater2.inflate(R.layout.dialouge_accounts, null);


        dialogCategoryIncome = new AlertDialog.Builder(getActivity())
                .setView(yourCustomViewCategoryIncome)
                .create();



        LayoutInflater inflater3 = LayoutInflater.from(getActivity());

        yourCustomViewCategoryOutcome = inflater3.inflate(R.layout.dialouge_accounts, null);


        dialogCategoryOutcome = new AlertDialog.Builder(getActivity())
                .setView(yourCustomViewCategoryOutcome)
                .create();


        initRecycler();


    }


    void initRecycler(){


        accountRecycler = yourCustomViewAccount.findViewById(R.id.Statistics_AccountRecycler);
        accountAdapter = new AccountAdapter(getActivity(),accounts,this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        accountRecycler.setLayoutManager(layoutManager);
        accountRecycler.setAdapter(accountAdapter);




        CategoryIncomeRecycler = yourCustomViewCategoryIncome.findViewById(R.id.Statistics_AccountRecycler);
        CategoryIncomeAdapter = new CategoryAdapter(getActivity(),CategoryIncome,this);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        CategoryIncomeRecycler.setLayoutManager(layoutManager1);
        CategoryIncomeRecycler.setAdapter(CategoryIncomeAdapter);




        CategoryOutcomeRecycler = yourCustomViewCategoryOutcome.findViewById(R.id.Statistics_AccountRecycler);
        CategoryOutcomeAdapter = new CategoryAdapter(getActivity(),CategoryOutcome,this);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        CategoryOutcomeRecycler.setLayoutManager(layoutManager2);
        CategoryOutcomeRecycler.setAdapter(CategoryOutcomeAdapter);






    }




    @OnClick(R.id.Search_Account)
    void OnAccountClick(){


dialogAccount.show();


    }


    @OnClick(R.id.Search_Category)
    void OnCategoryClick(){




if (type.equals("Income"))
{
    dialogCategoryIncome.show();
}
else if (type.equals("Outcome"))
{
 dialogCategoryOutcome.show();

}


    }



    @OnClick(R.id.Search_DateFrom)
    void OnDateFromClick(){

        DatePickerDialog d =   new DatePickerDialog(getActivity(), dateFrom, myCalendarFrom
                .get(Calendar.YEAR), myCalendarFrom.get(Calendar.MONTH),
                myCalendarFrom.get(Calendar.DAY_OF_MONTH));

        d.setTitle("");
        d.show();


    }


    @OnClick(R.id.Search_DateTo)
    void OnDateToClick(){

        DatePickerDialog d =   new DatePickerDialog(getActivity(), dateTo, myCalendarTo
                .get(Calendar.YEAR), myCalendarTo.get(Calendar.MONTH),
                myCalendarTo.get(Calendar.DAY_OF_MONTH));

        d.setTitle("");
        d.show();
    }






    void initDatePickerFrom(){


        dateFrom = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub


                myCalendarFrom.set(Calendar.YEAR, year);
                myCalendarFrom.set(Calendar.MONTH, monthOfYear);
                myCalendarFrom.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabelfrom();
            }

        };




    }


    void initDatePickerTo(){


        dateTo = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub


                myCalendarTo.set(Calendar.YEAR, year);
                myCalendarTo.set(Calendar.MONTH, monthOfYear);
                myCalendarTo.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabelTo();
            }

        };




    }

    void updateLabelfrom(){



        String myFormat =  "EEEE, dd MMM yyyy"; //In which you need put here



        //   SimpleDateFormat sdf = new SimpleDateFormat(DayFormat, Locale.US);

        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        Search_DateFromText.setText(sdf.format(myCalendarFrom.getTime()));


    }


    void updateLabelTo(){


        String myFormat =  "EEEE, dd MMM yyyy"; //In which you need put here



        //   SimpleDateFormat sdf = new SimpleDateFormat(DayFormat, Locale.US);

        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        Search_DateToText.setText(sdf.format(myCalendarTo.getTime()));
    }




    @OnClick(R.id.Search_All)
    void OnAllClick() {
        setTimeLineClick(0);
        type = "All";
    }



    @OnClick(R.id.Add_OutCome)
    void OnOutComeClick() {
        setTimeLineClick(1);
        type = "Outcome";
    }


    @OnClick(R.id.Add_Income)
    void OnInComeClick() {
        setTimeLineClick(2);
        type = "Income";
    }





    void setTimeLineClick(int index) {


        switch (index) {


            case 0:

                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    Search_All.setBackgroundColor(getResources().getColor(R.color.TimeLine_Acitve));

                else
                    Search_All.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.TimeLine_Acitve));


                Search_All_Text.setTextColor(getResources().getColor(R.color.white));




                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    Add_OutCome.setBackgroundColor(getResources().getColor(R.color.TimeLine_InAcitve));

                else
                    Add_OutCome.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.TimeLine_InAcitve));
                Add_OutCome_Text.setTextColor(getResources().getColor(R.color.TimeLineInActive));




                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    Add_Income.setBackgroundColor(getResources().getColor(R.color.TimeLine_InAcitve));

                else
                    Add_Income.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.TimeLine_InAcitve));
                Add_Income_Text.setTextColor(getResources().getColor(R.color.TimeLineInActive));



                //   OutComeRecycler.setVisibility(View.VISIBLE);
                // IncomeRecycler.setVisibility(View.GONE);



                rootView.findViewById(R.id.Search_Category).setVisibility(View.GONE);
                rootView.findViewById(R.id.Search_Category_line).setVisibility(View.GONE);



                break;



            case 1:

                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    Add_OutCome.setBackgroundColor(getResources().getColor(R.color.TimeLine_Acitve));

                else
                    Add_OutCome.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.TimeLine_Acitve));


                Add_OutCome_Text.setTextColor(getResources().getColor(R.color.white));




                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    Add_Income.setBackgroundColor(getResources().getColor(R.color.TimeLine_InAcitve));

                else
                    Add_Income.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.TimeLine_InAcitve));
                Add_Income_Text.setTextColor(getResources().getColor(R.color.TimeLineInActive));




                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    Search_All.setBackgroundColor(getResources().getColor(R.color.TimeLine_InAcitve));

                else
                    Search_All.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.TimeLine_InAcitve));
                Search_All_Text.setTextColor(getResources().getColor(R.color.TimeLineInActive));



                //   OutComeRecycler.setVisibility(View.VISIBLE);
               // IncomeRecycler.setVisibility(View.GONE);
                rootView.findViewById(R.id.Search_Category).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.Search_Category_line).setVisibility(View.VISIBLE);


                CurrentCategoryIncomeId = 0;
                CurrentCategoryIncome = 0;

                CurrentCategoryOutcomeId = 0;
                CurrentCategoryOutcome = 0;

                break;


            case 2:


                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    Add_OutCome.setBackgroundColor(getResources().getColor(R.color.TimeLine_InAcitve));

                else
                    Add_OutCome.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.TimeLine_InAcitve));


                Add_OutCome_Text.setTextColor(getResources().getColor(R.color.TimeLineInActive));


                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    Add_Income.setBackgroundColor(getResources().getColor(R.color.TimeLine_Acitve));

                else
                    Add_Income.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.TimeLine_Acitve));
                Add_Income_Text.setTextColor(getResources().getColor(R.color.white));



                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    Search_All.setBackgroundColor(getResources().getColor(R.color.TimeLine_InAcitve));

                else
                    Search_All.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.TimeLine_InAcitve));
                Search_All_Text.setTextColor(getResources().getColor(R.color.TimeLineInActive));




                rootView.findViewById(R.id.Search_Category).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.Search_Category_line).setVisibility(View.VISIBLE);

            //    OutComeRecycler.setVisibility(View.GONE);
             //   IncomeRecycler.setVisibility(View.VISIBLE);
                CurrentCategoryOutcomeId = 0;
                CurrentCategoryOutcome = 0;

                break;


        }


    }





    @Override
    public void onCategoryListItemClick(int clickedItemIndex) {

      if (type.equals("Income")) {
    CategoryIncome.get(clickedItemIndex).setStatus(true);
    CategoryIncome.get(CurrentCategoryIncome).setStatus(false);
    CategoryIncomeAdapter.notifyDataSetChanged();
    CurrentCategoryIncome = clickedItemIndex;
    CurrentCategoryIncomeId = CategoryIncome.get(clickedItemIndex).getId();

    Search_CategoryText.setText(CategoryIncome.get(clickedItemIndex).getName() + "");


    dialogCategoryIncome.hide();

}

else if (type.equals("Outcome")) {
    CategoryOutcome.get(clickedItemIndex).setStatus(true);
    CategoryOutcome.get(CurrentCategoryOutcome).setStatus(false);
    CategoryOutcomeAdapter.notifyDataSetChanged();
    CurrentCategoryOutcome = clickedItemIndex;
    CurrentCategoryOutcomeId = CategoryOutcome.get(clickedItemIndex).getId();

    Search_CategoryText.setText(CategoryOutcome.get(clickedItemIndex).getName() + "");


    dialogCategoryOutcome.hide();

}



    }


    @Override
    public void onListItemClick(int clickedItemIndex) {



        accounts.get(clickedItemIndex).setStatus(true);
        accounts.get(CurrentAccount).setStatus(false);
        accountAdapter.notifyDataSetChanged();
        CurrentAccount = clickedItemIndex;
        CurrentAccountId = accounts.get(clickedItemIndex).getId();

        Search_AccountText.setText(accounts.get(clickedItemIndex).getName()+"");


        dialogAccount.hide();




    }



    void LoadAccountsCategories()
    {

        final LiveData<List<Account>> c = mDb.accountDao().loadAllAccounts();

        c.observe(this, new Observer<List<Account>>() {
            @Override
            public void onChanged(@Nullable List<Account> accounts1) {

                accounts.clear();
                accounts.addAll(accounts1);


                initAccount.setTotal(voidGetTotalAccountsPrices());
                accounts.add(0,initAccount);
                accountAdapter.notifyDataSetChanged();

                c.removeObserver(this);

            }
        });



        final LiveData<List<Category>> cI = mDb.categoryDao().loadIncomesCategories();

        cI.observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> category1) {

                CategoryIncome.clear();
                CategoryIncome.addAll(category1);


                CategoryIncome.add(0,initCategoryIncome);
                CategoryIncomeAdapter.notifyDataSetChanged();

                cI.removeObserver(this);

            }
        });




        final LiveData<List<Category>> cO = mDb.categoryDao().loadOutcomesCategories();

        cO.observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> category1) {

                CategoryOutcome.clear();
                CategoryOutcome.addAll(category1);


                CategoryOutcome.add(0,initCategoryOutcome);
                CategoryOutcomeAdapter.notifyDataSetChanged();

                cO.removeObserver(this);

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


    @OnClick(R.id.Search_Btn)
        void OnSearchClickedBtn(){

        Intent i  = new Intent(getActivity(),SearchResults.class);



        i.putExtra("accountId",CurrentAccountId);
        i.putExtra("type",type);
        i.putExtra("start",myCalendarFrom.getTimeInMillis());
        i.putExtra("end",myCalendarTo.getTimeInMillis());

        if (type.equals("Income"))
        i.putExtra("categoryId",CurrentCategoryIncomeId);
        else if (type.equals("Outcome"))
            i.putExtra("categoryId",CurrentCategoryOutcomeId);
        else
            i.putExtra("categoryId",0);




        startActivity(i);





    }


}
