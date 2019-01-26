package com.mahmoudbahaa.wallet;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mahmoudbahaa.wallet.data.AppDatabase;
import com.mahmoudbahaa.wallet.models.Account;
import com.mahmoudbahaa.wallet.models.Category;
import com.mahmoudbahaa.wallet.models.Expense;
import com.mahmoudbahaa.wallet.models.Sequence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;


public class AddActivity extends AppCompatActivity {


    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int RESULT_GALLERY = 0;


    public static final int MEDIA_TYPE_IMAGE = 1;



     Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date;



    @BindView(R.id.Add_OutCome)
    LinearLayout Add_OutCome;

    @BindView(R.id.Add_Income)
    LinearLayout Add_Income;


    @BindView(R.id.Add_OutCome_Text)
    TextView Add_OutCome_Text;

    @BindView(R.id.Add_Income_Text)
    TextView Add_Income_Text;


    @BindView(R.id.Add_Category)
    LinearLayout Add_Category;



    @BindView(R.id.AddActivity_FirstLayout)
LinearLayout FirstLayout;


    @BindView(R.id.AddActivity_SecondLayout)
    LinearLayout SecondLayout;



    @BindView(R.id.AddActivity_Date)
    LinearLayout DateLayout;

    @BindView(R.id.AddActivity_DateText)
    TextView DateText;



    @BindView(R.id.AddActivity_Description)
    EditText Description;

    @BindView(R.id.AddActivity_Price)
    EditText Price;



    @BindView(R.id.AddActivity_PhotoImage)
            ImageView PhotoImage;




    @BindView(R.id.AddActivity_PhotoLayout)
            LinearLayout PhotoLayout;


    @BindView(R.id.AddActivity_MemoLayout)
            LinearLayout MemoLayout;


    @BindView(R.id.AddActivity_MemoText)
            TextView MemoText;



    @BindView(R.id.AddActivity_AddCameraPhoto)
            ImageView AddPhoto;

    @BindView(R.id.AddActivity_AddMemo)
    ImageView AddMemo;






    String Type = "Outcome";

String FileName = "";
    Date dateChosen = new Date();

    private AppDatabase mDb;



    String Memo = "";

    String FinalImagePath = "";




    private Uri fileUri;



    Account account;
    Category category;
    Expense expense;


    Category categoryIncome;
    Category categoryOutcome;


    @BindView(R.id.AddActivity_CategoryText)
    TextView CategoryText;


    @BindView(R.id.AddActivity_AccountText)
    TextView AccountText;


    Boolean Update = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ButterKnife.bind(this);

        mDb = AppDatabase.getsInstance(getApplicationContext());

        if (getIntent().hasExtra("date"))
        {
            long date = getIntent().getLongExtra("date", myCalendar.getTimeInMillis());
            myCalendar.setTimeInMillis(date);
        }


        initDatePicker();

        setTimeLineClick(0);

        PhotoLayout.setVisibility(View.GONE);
        MemoLayout.setVisibility(View.GONE);

   if (savedInstanceState==null) {
    if (getIntent().hasExtra("Expense")) {

        Update = true;

        expense = (Expense) getIntent().getParcelableExtra("Expense");

        getAccount();
        getCategory();

        initFields();

        myCalendar.setTimeInMillis(expense.getCreatedAt().getTime());

    } else {
        GetDefaultAccount();
        GetDefaultCategory();
    }
}

else {

 if (getIntent().hasExtra("Expense")){

     expense = (Expense) getIntent().getParcelableExtra("Expense");


     myCalendar.setTimeInMillis(expense.getCreatedAt().getTime());
     Update = true;

 }

  account = (Account) savedInstanceState.getSerializable("Account");
  categoryIncome = (Category) savedInstanceState.getSerializable("categoryIncome");
  categoryOutcome = (Category) savedInstanceState.getSerializable("categoryOutcome") ;
   Type = savedInstanceState.getString("Type");
    myCalendar.setTimeInMillis(savedInstanceState.getLong("Date"));

    Memo = savedInstanceState.getString("Memo");
    FinalImagePath = savedInstanceState.getString("FinalImagePath");
    fileUri = savedInstanceState.getParcelable("fileUri");

  //  if (fileUri!=null)


    if (Type.equals("Income"))
    {
        setTimeLineClick(1);
        Type = "Income";
        CategoryText.setText(categoryIncome.getName());

    }
    else{
        setTimeLineClick(0);
        Type  = "Outcome";
        CategoryText.setText(categoryOutcome.getName());
    }

    AccountText.setText(account.getName());




    if (!TextUtils.isEmpty(Memo))
    {
        MemoLayout.setVisibility(View.VISIBLE);
        AddMemo.setVisibility(View.GONE);
        MemoText.setText(Memo);
    }




    if (!TextUtils.isEmpty(FinalImagePath))
    {

        Glide.with(getApplicationContext())
                .load(new File(FinalImagePath)).apply(new RequestOptions().centerCrop()).into(PhotoImage);

        PhotoLayout.setVisibility(View.VISIBLE);
        AddPhoto.setVisibility(View.GONE);

    }


}


        updateLabel();


    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("Account",account);
        outState.putSerializable("categoryIncome",categoryIncome);
        outState.putSerializable("categoryOutcome",categoryOutcome);
        outState.putString("Type",Type);
        outState.putLong("Date",myCalendar.getTimeInMillis());
        outState.putString("FinalImagePath",FinalImagePath);
        outState.putString("Memo",Memo);
        outState.putParcelable("fileUri",fileUri);

    }

    void getAccount()
   {
     final  LiveData<Account> account1   = mDb.accountDao().findAccount(expense.getAccountId());

       account1.observe(this, new Observer<Account>() {
           @Override
           public void onChanged(@Nullable Account account2) {
               account = account2;

               AccountText.setText(account.getName());
           }
       });

   }


    void getCategory()
    {
        final LiveData<Category> category2   = mDb.categoryDao().findCategory(expense.getCategoryId());

        category2.observe(this, new Observer<Category>() {
            @Override
            public void onChanged(@Nullable Category category1) {


             //   category = category1;

                Log.v("ExpenseCategoryType",category1.getType()+"1");

                if (category1.getType().equals("Income"))
                {

LoadDefaultOutcomeCategory();

                    categoryIncome = category1;
                    Log.v("ExpenseCategoryType",categoryIncome.getType()+"2");
                }
                else if (category1.getType().equals("Outcome"))
                {
                    LoadDeafultIncomeCategory();

                    categoryOutcome = category1;
                    Log.v("ExpenseCategoryType",categoryOutcome.getType()+"3");
                }

                category2.removeObserver(this);

                CategoryText.setText(category1.getName());


            }
        });    }





        void GetDefaultAccount(){

            final LiveData<Account> account2   = mDb.accountDao().LoadDefaultAccount(true);

            account2.observe(this, new Observer<Account>() {
                @Override
                public void onChanged(@Nullable Account account1) {
                    account = account1;

                    AccountText.setText(account.getName());

                    account2.removeObserver(this);
                }
            });

        }


        void GetDefaultCategory(){

            final LiveData<Category> category1Live   = mDb.categoryDao().LoadDefaultIncome(true);

            category1Live.observe(this, new Observer<Category>() {
                @Override
                public void onChanged(@Nullable Category category1) {
              //      category = category1;

                //    CategoryText.setText(category.getName());

                    categoryIncome = category1;

                    if (Type.equals("Income"))
                        CategoryText.setText(categoryIncome.getName());

                    category1Live.removeObserver(this);
                }
            });



           final LiveData<Category> category2Live  = mDb.categoryDao().LoadDefaultOutCome(true);

            category2Live.observe(this, new Observer<Category>() {
                @Override
                public void onChanged(@Nullable Category category1) {
                         category = category1;

                    //    CategoryText.setText(category.getName());

                    categoryOutcome = category1;

                    if (Type.equals("Outcome"))
                        CategoryText.setText(categoryOutcome.getName());


                    category2Live.removeObserver(this);
                }
            });

        }





        void LoadDeafultIncomeCategory(){

            final LiveData<Category> category1Live   = mDb.categoryDao().LoadDefaultIncome(true);

            category1Live.observe(this, new Observer<Category>() {
                @Override
                public void onChanged(@Nullable Category category1) {
                    //      category = category1;

                    //    CategoryText.setText(category.getName());

                    categoryIncome = category1;

                    if (Type.equals("Income"))
                        CategoryText.setText(categoryIncome.getName());

                    category1Live.removeObserver(this);
                }
            });

        }

        void LoadDefaultOutcomeCategory(){

            final LiveData<Category> category2Live  = mDb.categoryDao().LoadDefaultOutCome(true);

            category2Live.observe(this, new Observer<Category>() {
                @Override
                public void onChanged(@Nullable Category category1) {
                    category = category1;

                    //    CategoryText.setText(category.getName());

                    categoryOutcome = category1;

                    if (Type.equals("Outcome"))
                        CategoryText.setText(categoryOutcome.getName());


                    category2Live.removeObserver(this);
                }
            });

        }

   void initFields()
   {

    if (!TextUtils.isEmpty( expense.getDescription()))
    {
        Description.setText(expense.getDescription());
    }


       if (!TextUtils.isEmpty( expense.getType()))
       {

           if (expense.getType().equals("Income"))
           {
               setTimeLineClick(1);
               Type = "Income";
           }
           else{
               setTimeLineClick(0);
               Type  = "Outcome";
           }

       }


Price.setText(expense.getPrice()+"");



    if (!TextUtils.isEmpty(expense.getMemo()))
    {
        MemoLayout.setVisibility(View.VISIBLE);
        AddMemo.setVisibility(View.GONE);
        MemoText.setText(expense.getMemo());
    }




    if (!TextUtils.isEmpty(expense.getImagePath()))
    {
        FinalImagePath = expense.getImagePath();

        Glide.with(getApplicationContext())
                .load(new File(expense.getImagePath())).apply(new RequestOptions().centerCrop()).into(PhotoImage);

        PhotoLayout.setVisibility(View.VISIBLE);
        AddPhoto.setVisibility(View.GONE);

    }


    //   AccountText.setText(account.getName());
       //ForAccountImage --------

     //  CategoryText.setText(category.getName());
       //ForCategoryIcon -----







   }


    void CheckDate(){



    }

    @OnClick(R.id.AddActivity_Category)
    void onCategoryChange(){

        if (Type.equals("Outcome")){
            Intent i = new Intent(AddActivity.this,EditCategoryOutcome.class);
            i.putExtra("Category",categoryOutcome);
            i.putExtra("Type",Type);
            i.putExtra("ScreenType","Add");
            startActivityForResult(i,500);


        }
        else if (Type.equals("Income")) {
            Intent i = new Intent(AddActivity.this,EditCategoryIncome.class);
            i.putExtra("Type",Type);
            i.putExtra("Category", categoryIncome);
            i.putExtra("ScreenType","Add");
            startActivityForResult(i,500);


        }




    }


    @OnClick(R.id.AddActivity_Account)
    void onAccountChange(){
        Intent i = new Intent(AddActivity.this,EditAccount.class);
        i.putExtra("Account",account);
        i.putExtra("ScreenType","Add");
        i.putExtra("a","a");

        startActivityForResult(i,600);
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
                updateLabel();
            }

        };




    }



    @OnClick(R.id.AddActivity_PhotoImage)
    void EnlargeImage(){

        Intent i = new Intent(AddActivity.this,EnlargeImage.class);
        i.putExtra("ImagePath",FinalImagePath);
        startActivity(i);


    }

    private void updateLabel() {

        String DayFormatName = "ddd";
        String DateFormat = "EEEE, dd MMM yyyy";


        //   SimpleDateFormat sdf = new SimpleDateFormat(DayFormat, Locale.US);
        Locale locale = new Locale( "ar", "SA" );
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat,locale);

        DateText.setText(sdf.format(myCalendar.getTime()));

    }




    @OnClick(R.id.Add_OutCome)
    void OnOutComeClick() {
        setTimeLineClick(0);
        Type = "Outcome";



        CategoryText.setText(categoryOutcome.getName());

    }


    @OnClick(R.id.Add_Income)
    void OnInComeClick() {
        setTimeLineClick(1);
        Type = "Income";
        CategoryText.setText(categoryIncome.getName());

    }



    @OnClick(R.id.AddActivity_done)
    void onDone(){

Log.v("Update",Update+"");


        if (TextUtils.isEmpty( this.Description.getText().toString()) || TextUtils.isEmpty(this.Price.getText().toString()))
        {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"ادخل الوصف والمبلغ", Snackbar.LENGTH_LONG);
            snackbar.show();
        }

        else {
         String Description = this.Description.getText().toString();
        float price = Float.parseFloat(this.Price.getText().toString());



         String Type = this.Type;


       Log.v("dateadded",myCalendar.getTime()+"");


       if (!Update){

 if (Type.equals("Income")) {


    Sequence seq = mDb.sequenceDao().loadExpenseSeq();

    seq.setSeq(seq.getSeq() + 1);
    final Expense expense = new Expense(seq.getSeq(), Description, Type, price, myCalendar.getTime(), Memo, FinalImagePath, account.getId(), categoryIncome.getId());

    float currentTotal = account.getTotal();
    currentTotal = currentTotal + price;

    account.setTotal(currentTotal);

    final Sequence seqUpdated = seq;

    AppExecutors.getInstance().diskIO().execute(new Runnable() {
        @Override
        public void run() {
            mDb.expenseDao().insertExpense(expense);
            mDb.accountDao().UpdateAccount(account);
            mDb.sequenceDao().UpdateExpenseSeq(seqUpdated);
            finish();

        }
    });


}

           else if (Type.equals("Outcome")) {



                    Sequence seq = mDb.sequenceDao().loadExpenseSeq();

                seq.setSeq(seq.getSeq() + 1);

                final Expense expense = new Expense(seq.getSeq(), Description, Type, price, myCalendar.getTime(), Memo, FinalImagePath, account.getId(), categoryOutcome.getId());

                float currentTotal = account.getTotal();
                currentTotal = currentTotal - price;

                account.setTotal(currentTotal);

                final Sequence seqUpdated = seq;
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.sequenceDao().UpdateExpenseSeq(seqUpdated);
                        mDb.expenseDao().insertExpense(expense);
                        mDb.accountDao().UpdateAccount(account);

                        finish();

                    }
                });



            }


            //////

       }

       else if (Update){

           if (expense.getType().equals("Income")){


               float currentTotal = account.getTotal();


               currentTotal =   currentTotal  - this.expense.getPrice();

               if (Type.equals(this.expense.getType()))
                   currentTotal =   currentTotal + price;

               else if (!Type.equals(this.expense.getType()))
                   currentTotal =   currentTotal - price;


               account.setTotal(currentTotal);
               final Expense expense = new Expense(this.expense.getId(), Description, Type, price, myCalendar.getTime(), Memo, FinalImagePath, account.getId(), categoryIncome.getId());



               AppExecutors.getInstance().diskIO().execute(new Runnable() {
                   @Override
                   public void run() {
                       mDb.expenseDao().UpdateExpense(expense);
                       mDb.accountDao().UpdateAccount(account);
                       finish();

                   }
               });


           }


           else if (expense.getType().equals("Outcome")){



               float currentTotal = account.getTotal();


               currentTotal =   currentTotal  + this.expense.getPrice();

               if (Type.equals(this.expense.getType()))
                   currentTotal =   currentTotal - price;

               else if (!Type.equals(this.expense.getType()))
                   currentTotal =   currentTotal +  price;


               account.setTotal(currentTotal);
               final Expense expense = new Expense(this.expense.getId(), Description, Type, price, myCalendar.getTime(), Memo, FinalImagePath, account.getId(), categoryOutcome.getId());



               AppExecutors.getInstance().diskIO().execute(new Runnable() {
                   @Override
                   public void run() {
                       mDb.expenseDao().UpdateExpense(expense);
                       mDb.accountDao().UpdateAccount(account);
                       finish();

                   }
               });




           }






       }


        }
    }



    @OnClick(R.id.AddActivity_Date)
    void OpenDate(){

        DatePickerDialog d =   new DatePickerDialog(AddActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        d.setTitle("");
        d.show();

    }


    void setTimeLineClick(int index) {


        switch (index) {

            case 0:

                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    Add_OutCome.setBackgroundColor(getResources().getColor(R.color.white));

                else
                    Add_OutCome.setBackgroundTintList(ContextCompat.getColorStateList(AddActivity.this, R.color.white));


                Add_OutCome_Text.setTextColor(getResources().getColor(R.color.pink));


                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    Add_Income.setBackgroundColor(getResources().getColor(R.color.Add_pink_Category));

                else
                    Add_Income.setBackgroundTintList(ContextCompat.getColorStateList(AddActivity.this, R.color.Add_pink_Category));

                Add_Income_Text.setTextColor(getResources().getColor(R.color.white));

               FirstLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.expense));
                SecondLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.expense));


                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    Add_Category.setBackgroundColor(getResources().getColor(R.color.Add_pink_Category));

                else
                    Add_Category.setBackgroundTintList(ContextCompat.getColorStateList(AddActivity.this, R.color.Add_pink_Category));





                break;


            case 1:


                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    Add_Income.setBackgroundColor(getResources().getColor(R.color.white));

                else
                    Add_Income.setBackgroundTintList(ContextCompat.getColorStateList(AddActivity.this, R.color.white));


                Add_Income_Text.setTextColor(getResources().getColor(R.color.Income_Rect_Text));


                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    Add_OutCome.setBackgroundColor(getResources().getColor(R.color.Income_RectSmall_BachGround));

                else
                    Add_OutCome.setBackgroundTintList(ContextCompat.getColorStateList(AddActivity.this, R.color.Income_RectSmall_BachGround));
                    Add_OutCome_Text.setTextColor(getResources().getColor(R.color.white));



                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    Add_Category.setBackgroundColor(getResources().getColor(R.color.Income_RectSmall_BachGround));

                else
                    Add_Category.setBackgroundTintList(ContextCompat.getColorStateList(AddActivity.this, R.color.Income_RectSmall_BachGround));




                FirstLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Income_RectBig_BachGround));
                SecondLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Income_RectBig_BachGround));

                break;


        }



    }



    @OnClick(R.id.AddActivity_Exit)
    void ExitAdd(){
        finish();
    }

    @OnClick(R.id.AddActivity_AddCameraPhoto)
    void OpenAddCameraPhoto(){
        //openGallery();
       // captureImage();
      //  askPermissions();


        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},200);

            }
            else{
                showPictureDialog();

            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 200: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! do the
                    // calendar task you need to do.

                    showPictureDialog();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'switch' lines to check for other
            // permissions this app might request
        }
    }


    @OnClick(R.id.AddActivity_AddMemo)
    void OpenAddMemo(){


        Intent i = new Intent(AddActivity.this,Addmemo.class);
        i.putExtra("Memo",Memo);
        startActivityForResult(i,300);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 300 :
                if (resultCode == Activity.RESULT_OK) {
                    Log.v("reqCode",requestCode+"");
                    Log.v("data",data.getStringExtra("msgStatus")+"");

                    Memo = data.getStringExtra("Memo");


                    if (!TextUtils.isEmpty(Memo))
                    {
                        MemoLayout.setVisibility(View.VISIBLE);
                        MemoText.setText(Memo);
                        AddMemo.setVisibility(View.GONE);
                    }
                    else {
                        MemoLayout.setVisibility(View.GONE);
                        AddMemo.setVisibility(View.VISIBLE);

                    }

                }
                break;



            case RESULT_GALLERY:

                if (resultCode == RESULT_OK) {

                    fileUri = Uri.parse( getPathForImage(getApplicationContext(),data.getData()));

                 //   String FileName = Calendar.getInstance().getTimeInMillis()+ ".jpg";

                    ContextWrapper cw = new ContextWrapper(getApplicationContext());
                    // path to /data/data/yourapp/app_data/imageDir
                    File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                  //  File mypath =new File(directory,Calendar.getInstance().getTimeInMillis()+ ".jpg");



                  //  FinalImagePath = mypath.getPath();
                    File  compressedImageFile;
                    try {
                      //  copyFile(new File(fileUri.getPath()),mypath);
                         Log.v("FileUriPath",fileUri.getPath());
                          compressedImageFile = new Compressor(this).setDestinationDirectoryPath(directory.getAbsolutePath()).compressToFile(new File(fileUri.getPath()));;


                        FinalImagePath = compressedImageFile.getPath();

                         Log.v("FinalImagePathfromCom",FinalImagePath);

                        Glide.with(getApplicationContext())
                                .load(new File(FinalImagePath)).apply(new RequestOptions().centerCrop()).into(PhotoImage);

                        PhotoLayout.setVisibility(View.VISIBLE);
                        AddPhoto.setVisibility(View.GONE);




                    } catch (IOException e) {
                        e.printStackTrace();

                    }




                   // FinalImagePath = mypath.getPath();



                    // successfully captured the image
                    // launching upload activity



                } else if (resultCode == RESULT_CANCELED) {

                    // user cancelled Image capture
                    Toast.makeText(getApplicationContext(),
                            "User cancelled Gallery", Toast.LENGTH_SHORT)
                            .show();


                } else {
                    // failed to capture image
                    Toast.makeText(getApplicationContext(),
                            "Sorry! Failed to Open Gallery", Toast.LENGTH_SHORT)
                            .show();
                }

break;


            case CAMERA_CAPTURE_IMAGE_REQUEST_CODE:

                Log.v("Camera","dddd");

                if (resultCode == RESULT_OK)
                {
                    Log.v("Camera","ggg");


                    ContextWrapper cw = new ContextWrapper(getApplicationContext());
                    // path to /data/data/yourapp/app_data/imageDir
                    File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                    File mypath =new File(directory,FileName+ ".jpg");




                    try {
                     //   copyFile(new File(fileUri.getPath()),mypath);
                     File   compressedImageFile = new Compressor(this).setQuality(200).setDestinationDirectoryPath(directory.getAbsolutePath()).compressToFile(new File(fileUri.getPath()));;

                        FinalImagePath = compressedImageFile.getPath();

                        Log.v("FinalImagePathfromCom",FinalImagePath);


                        Glide.with(getApplicationContext())
                                .load(new File(FinalImagePath)).apply(new RequestOptions().centerCrop()).into(PhotoImage);

                        PhotoLayout.setVisibility(View.VISIBLE);
                        AddPhoto.setVisibility(View.GONE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                  //  FinalImagePath = mypath.getPath();



                }
                else {
                    Log.v("Camera","rrrrrrd");

                }
                break;



            case 500:
Log.v("ResultCode",resultCode+"");
                if (resultCode == RESULT_OK) {
                    Category category = (Category) data.getSerializableExtra("Category");

                    if (category.getType().equals("Outcome"))
                        this.categoryOutcome = category;
                    else
                        this.categoryIncome = category;

                    Log.v("Add3","add3");
Log.v("Add3",category.getName()+"");
                   CategoryText.setText(category.getName()+"");


                }
                break;

            case 600:
                if (resultCode == RESULT_OK){
                Account account=(Account) data.getSerializableExtra("Account");
                this.account = account;
                AccountText.setText(this.account.getName()+"");
                }
                break;


        }


    }




    public void openGallery(){


        Intent galleryIntent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent , RESULT_GALLERY );


    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
      //  pictureDialog.setTitle("اختر");
        String[] pictureDialogItems = {
                "الصور",
                "الكاميرا" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                openGallery();
                                break;
                            case 1:
                                captureImage();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }



    public static String getPathForImage(Context context, Uri uri)
    {
        String result = null;
        Cursor cursor = null;

        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(uri, proj, null, null, null);
            if (cursor == null) {
                result = uri.getPath();
            } else {
                cursor.moveToFirst();
                int column_index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(column_index);
                cursor.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }
    private void captureImage() {





         FileName  = Calendar.getInstance().getTimeInMillis()+"";
         Log.v("Filename",FileName);
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),FileName);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

     fileUri =  Uri.fromFile(mediaStorageDir);


        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

    }


    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }


    /**
     * returning image / video
     */
    private  File getOutputMediaFile(int type) {

        // External sdcard location

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath =new File(directory,myCalendar.getTimeInMillis()+"");


        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),myCalendar.getTimeInMillis()+"");





        Log.v("fiii",mediaStorageDir.getPath());

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {

                return null;
            }
        }

// Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {

   /*         mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");

            */
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp);

        }  else {
            return null;
        }

        return mediaFile;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }

    private void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }

        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }


    }





    @OnClick(R.id.AddActivity_MemoCancel)
    void CancelMemo(){


        Memo = "";
        MemoLayout.setVisibility(View.GONE);
        MemoText.setText(Memo);
AddMemo.setVisibility(View.VISIBLE);

    }


    @OnClick(R.id.AddActivity_PhotoCancel)
    void CancelPhoto(){

        FinalImagePath = "";
        PhotoLayout.setVisibility(View.GONE);
     AddPhoto.setVisibility(View.VISIBLE);

    }














}