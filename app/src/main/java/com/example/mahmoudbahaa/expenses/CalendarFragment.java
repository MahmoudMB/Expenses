package com.example.mahmoudbahaa.expenses;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mahmoudbahaa.expenses.adapters.ExpenseAdapter;
import com.example.mahmoudbahaa.expenses.data.AppDatabase;
import com.example.mahmoudbahaa.expenses.models.Expense;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;


public class CalendarFragment extends Fragment implements ExpenseAdapter.ListItemClickListener,UpdateUi {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    View rootView;
    List<Expense> expenses = new ArrayList<>();
    ExpenseAdapter adapter;
    RecyclerView recyclerView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private Long start;
    private Long end;

    private OnFragmentInteractionListener mListener;




    public CalendarFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
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
           // mParam1 = getArguments().getString(ARG_PARAM1);
           // mParam2 = getArguments().getString(ARG_PARAM2);

         //   start = getArguments().getLong("start");
          //  end = getArguments().getLong("end");
            expenses = getArguments().getParcelableArrayList("expenses");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView =  inflater.inflate(R.layout.fragment_calendar, container, false);
        ButterKnife.bind(this,rootView);

Log.v("create","createCalender");

        initRecyclerView();
      //  dummeyData();



adapter.setExpenses(expenses);


        new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return 0;
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
int position = viewHolder.getAdapterPosition();
List<Expense> expenses = adapter.getExpenses();
//mDb.expenseDao().deleteExpense(expenses.get(position));



                    }
                });

            }
        }).attachToRecyclerView(recyclerView);

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }





    @Override
    public void onListItemClick(int clickedItemIndex) {

    }

    @Override
    public void refreshCalendar(List<Expense> expenses) {



        adapter.setExpenses(expenses);


    }

    void UpdateUi(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {





Log.v("excute1","1");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Log.v("excute2","2");


                    }
                });


            }
        });
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



    private void initRecyclerView(){



        recyclerView = rootView.findViewById(R.id.Main_Recycler);
        adapter = new ExpenseAdapter(getActivity(),expenses,this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    private void dummeyData(){

        List<Expense> expensesTemp = new ArrayList<>();
        Expense e = new Expense();

        e.setAccount("الحساب الاساسي");
        e.setCategory("مشتريات");
        e.setDescription("بقالة");
        e.setPrice(2222);
        e.setType("Outcome");

        expensesTemp.add(e);


        e = new Expense();

        e.setAccount("حساب الراجحي");
        e.setCategory("مشتريات");
        e.setDescription("مكافأة");
        e.setPrice(222);
        e.setType("Income");

        expensesTemp.add(e);


        e = new Expense();

        e.setAccount("الحساب الاساسي");
        e.setCategory("مشتريات");
        e.setDescription("مشتريات بنده");
        e.setPrice(222);
        e.setType("Outcome");

        expensesTemp.add(e);


        e = new Expense();

        e.setAccount("حساب الراجحي");
        e.setCategory("مشتريات");
        e.setDescription("راتب");
        e.setPrice(222);
        e.setType("Income");

        expensesTemp.add(e);

        expenses.addAll(expensesTemp);
        adapter.notifyDataSetChanged();

    }
}
