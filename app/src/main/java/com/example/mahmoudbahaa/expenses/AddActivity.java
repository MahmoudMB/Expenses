package com.example.mahmoudbahaa.expenses;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mahmoudbahaa.expenses.data.AppDatabase;
import com.example.mahmoudbahaa.expenses.models.Account;
import com.example.mahmoudbahaa.expenses.models.Category;
import com.example.mahmoudbahaa.expenses.models.Expense;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;



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


    @BindView(R.id.AddActivity_CategoryText)
    TextView CategoryText;


    @BindView(R.id.AddActivity_AccountText)
    TextView AccountText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ButterKnife.bind(this);

        mDb = AppDatabase.getsInstance(getApplicationContext());
        initDatePicker();

        setTimeLineClick(0);
        askPermissions();

        PhotoLayout.setVisibility(View.GONE);
        MemoLayout.setVisibility(View.GONE);


        if (getIntent().hasExtra("Expense"))
        {
expense = (Expense) getIntent().getSerializableExtra("Expense");

            initFields();

            myCalendar.setTimeInMillis(expense.getCreatedAt().getTime());


        }

        updateLabel();


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


       AccountText.setText(account.getName());
       //ForAccountImage --------

       CategoryText.setText(category.getName());
       //ForCategoryIcon -----







   }


    void CheckDate(){



    }

    @OnClick(R.id.AddActivity_Category)
    void onCategoryChange(){
        Intent i = new Intent(AddActivity.this,EditCategory.class);
        i.putExtra("Type",Type);
        i.putExtra("Category",category);

        startActivityForResult(i,500);
    }


    @OnClick(R.id.AddActivity_Account)
    void onAccountChange(){
        Intent i = new Intent(AddActivity.this,EditAccount.class);
        i.putExtra("Account",account);
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

    }


    @OnClick(R.id.Add_Income)
    void OnInComeClick() {
        setTimeLineClick(1);
        Type = "Income";
    }



    @OnClick(R.id.AddActivity_done)
    void onDone(){




        if (TextUtils.isEmpty( this.Description.getText().toString()) || TextUtils.isEmpty(this.Price.getText().toString()))
        {

        }

        else {
         String Description = this.Description.getText().toString();
        float price = Float.parseFloat(this.Price.getText().toString());


         String Category = "بقالة";
         String account = "الاساسي";

         String Type = this.Type;


       Log.v("dateadded",myCalendar.getTime()+"");



    final    Expense expense = new Expense(Description, Category, account, Type, price, myCalendar.getTime(),Memo,FinalImagePath);

AppExecutors.getInstance().diskIO().execute(new Runnable() {
    @Override
    public void run() {
        mDb.expenseDao().insertExpense(expense);
        finish();

    }
});



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



                break;


            case 1:


                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    Add_Income.setBackgroundColor(getResources().getColor(R.color.white));

                else
                    Add_Income.setBackgroundTintList(ContextCompat.getColorStateList(AddActivity.this, R.color.white));


                Add_Income_Text.setTextColor(getResources().getColor(R.color.pink));


                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    Add_OutCome.setBackgroundColor(getResources().getColor(R.color.Add_pink_Category));

                else
                    Add_OutCome.setBackgroundTintList(ContextCompat.getColorStateList(AddActivity.this, R.color.Add_pink_Category));
                    Add_OutCome_Text.setTextColor(getResources().getColor(R.color.white));


                FirstLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.income));
                SecondLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.income));

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

        showPictureDialog();
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


                    ContextWrapper cw = new ContextWrapper(getApplicationContext());
                    // path to /data/data/yourapp/app_data/imageDir
                    File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                    File mypath =new File(directory,Calendar.getInstance().getTimeInMillis()+ ".jpg");
FinalImagePath = mypath.getPath();

                    try {
                        copyFile(new File(fileUri.getPath()),mypath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    FinalImagePath = mypath.getPath();

                    Glide.with(getApplicationContext())
                            .load(new File(mypath.getPath())).apply(new RequestOptions().centerCrop()).into(PhotoImage);

                    PhotoLayout.setVisibility(View.VISIBLE);
                    AddPhoto.setVisibility(View.GONE);


                    Log.v("g1",fileUri.getPath());
                    Log.v("g2",mypath.getPath());


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
                        copyFile(new File(fileUri.getPath()),mypath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    FinalImagePath = mypath.getPath();
                    Log.v("Filep",FinalImagePath);


                    Glide.with(getApplicationContext())
                            .load(new File(FinalImagePath)).apply(new RequestOptions().centerCrop()).into(PhotoImage);

                    PhotoLayout.setVisibility(View.VISIBLE);
                    AddPhoto.setVisibility(View.GONE);

                }
                else {
                    Log.v("Camera","rrrrrrd");

                }
                break;



            case 500:

                if (resultCode == RESULT_OK) {
                    Category category = (Category) data.getSerializableExtra("Category");
                    this.category = category;
                    CategoryText.setText(this.category.getName());
                }
                break;

            case 600:
                if (resultCode == RESULT_OK){
                Account account=(Account) data.getSerializableExtra("Account");
                this.account = account;
                AccountText.setText(this.account.getName());
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