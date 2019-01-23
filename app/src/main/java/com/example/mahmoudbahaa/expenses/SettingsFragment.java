package com.example.mahmoudbahaa.expenses;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.mahmoudbahaa.expenses.data.AppDatabase;
import com.example.mahmoudbahaa.expenses.models.Account;
import com.example.mahmoudbahaa.expenses.models.Category;
import com.example.mahmoudbahaa.expenses.models.Expense;
import com.example.mahmoudbahaa.expenses.models.ExpenseDao;
import com.example.mahmoudbahaa.expenses.models.Sync;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    View rootView;
    private AppDatabase mDb;


    @BindView(R.id.Settings_EmailText)
    TextView EmailText;

    @BindView(R.id.Settings_SyncDate)
        TextView SyncDate;


    @BindView(R.id.Settings_SyncSwitch)
    SwitchCompat SyncSwitch;





    Boolean e1 = false;
    Boolean a1 = false;
    Boolean c1 = false;

      //  private OnFragmentInteractionListener mListener;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        rootView =  inflater.inflate(R.layout.fragment_settings, container, false);

        ButterKnife.bind(this,rootView);
        mDb = AppDatabase.getsInstance(getActivity());

        initEmailAndSyncStatus();


        SyncSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                ChageSyncStatus(b);
            }
        });


        GetLastSyncDate();
    return rootView;
    }


    @OnClick(R.id.Settings_General)
    void OpenGeneral(){

        Intent i = new Intent(getActivity(),GeneralActivity.class);
        startActivity(i);


    }

    @OnClick(R.id.Settings_Logout)
    void SignOut(){

       // FirebaseAuth.getInstance().signOut();


        SignOutandBackup();

    //    SyncFromDatabase();
    }


    @OnClick(R.id.Settings_EditCategory)
    void OpenEditCategories(){
        Intent i = new Intent(getActivity(),EditCategories.class);
        startActivity(i);
    }


    @OnClick(R.id.Settings_EditAccount)
    void OpenEditAccounts(){
        Intent i = new Intent(getActivity(),EditAccount.class);
        startActivity(i);
    }



    void SignOutandBackup(){

       // CreateDbCopy();


        Sync sync = new Sync(1,new Date().getTime(),true);

        mDb.syncDao().UpdateSync(sync);

        Intent i = new Intent(getActivity(),BackUpService.class);
        i.putExtra("UserId",GetUserId());
         i.setAction("SignOut");
        getActivity().startService(i);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("LoggingStatus","FALSE");

        editor.apply();

        Intent i1 = new Intent(getActivity(),Login.class);
        startActivity(i1);
        getActivity().finish();


    }


    void ChageSyncStatus(Boolean b){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = preferences.edit();

        if (b)
        editor.putString("Sync","ON");
        else
            editor.putString("Sync","OFF");


        editor.apply();


    }
    void initEmailAndSyncStatus(){


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String Id = preferences.getString("Id", "");
        String Email = preferences.getString("Email", "");
        String Sync = preferences.getString("Sync", "");


        EmailText.setText(Email+"");

        if (Sync.equals("ON"))
            SyncSwitch.setChecked(true);
        else
            SyncSwitch.setChecked(false);




    }











void GetLastSyncDate(){

    final LiveData<Sync> syncLive = mDb.syncDao().loadSyncDataLive();


    syncLive.observe(this, new Observer<Sync>() {
        @Override
        public void onChanged(@Nullable Sync sync) {

            Long SyncDa = sync.getDate();


            String DateFormat = "dd MMM yyyy";

            //   SimpleDateFormat sdf = new SimpleDateFormat(DayFormat, Locale.US);
        //    Locale locale = new Locale( "ar", "SA" );
          //  SimpleDateFormat sdf = new SimpleDateFormat(DateFormat,locale);
            SimpleDateFormat sdf = new SimpleDateFormat(DateFormat);


            SyncDate.setText("اخر مزامنة: "+sdf.format(SyncDa));


            syncLive.removeObserver(this);
        }
    });




}





    String GetUserId(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String Id = preferences.getString("Id", "");


        return Id;

    }

    // TODO: Rename method, update argument and hook method into UI event
/*
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
*/
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
     */
}
