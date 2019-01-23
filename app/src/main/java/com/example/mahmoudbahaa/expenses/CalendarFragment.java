package com.example.mahmoudbahaa.expenses;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.mahmoudbahaa.expenses.adapters.ExpenseAdapter;
import com.example.mahmoudbahaa.expenses.adapters.ExpenseSwipeToDeleteCallback;
import com.example.mahmoudbahaa.expenses.data.AppDatabase;
import com.example.mahmoudbahaa.expenses.models.Account;
import com.example.mahmoudbahaa.expenses.models.Category;
import com.example.mahmoudbahaa.expenses.models.Expense;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;


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

@BindView(R.id.Main_NoOp)
    LinearLayout NoOp;





    private AppDatabase mDb;


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
            List<Expense> es = getArguments().getParcelableArrayList("expenses");
            if (es!=null)
            expenses.addAll(es);
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
        adapter.notifyDataSetChanged();

        //  dummeyData();

        if (expenses.size()<=0)
        {
            NoOp.setVisibility(View.VISIBLE);

        }
        else{
            NoOp.setVisibility(View.GONE);

        }



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


        Intent i = new Intent(getActivity(),AddActivity.class);
        Log.v("expensesSize",expenses.size()+"");
        i.putExtra("Expense",expenses.get(clickedItemIndex));

        startActivity(i);


    }

    @Override
    public void refreshCalendar(List<Expense> expenses1) {


expenses.clear();
expenses.addAll(expenses1);
adapter.notifyDataSetChanged();

if (expenses1.size()<=0)
{
NoOp.setVisibility(View.VISIBLE);

}
else{
    NoOp.setVisibility(View.GONE);

}

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
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        recyclerView.setLayoutManager(layoutManager);


        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new ExpenseSwipeToDeleteCallback(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);



        recyclerView.setAdapter(adapter);

    }


}
