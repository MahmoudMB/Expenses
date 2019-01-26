package com.mahmoudbahaa.wallet;

/**
 * Created by MahmoudBahaa on 20/01/2019.
 */



public class backupAsync   {
/*
private OnEventListener mCallBack;
    private Context mContext;
    public Exception mException;


    public backupAsync(Context context, OnEventListener callback) {
        mCallBack = callback;
        mContext = context;
    }



    @Override
    protected String doInBackground(String... params) {






        return "Done";
    }

    @Override
    protected void onPostExecute(String result) {

        if (mCallBack != null) {
            if (mException == null) {
                try {
                    mCallBack.onSuccess(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                mCallBack.onFailure(mException);
            }
        }

    }
    @Override
    protected void onPreExecute() {



    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }

    void CreateDbCopy(){

         final AppDatabase mDb = AppDatabase.getsInstance(mContext);


        final  String userId = GetUserId();





        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<Expense> expenses = mDb.expenseDao().loadAllExpensesAllList();
                Map map;
                for (int i=0;i<expenses.size();i++)

                {

                    map=new HashMap<>();
                    map.put("id",expenses.get(i).getId());
                    map.put("description",expenses.get(i).getDescription());
                    map.put("category",expenses.get(i).getCategory());
                    map.put("account",expenses.get(i).getAccount());
                    map.put("type",expenses.get(i).getType());
                    map.put("price",expenses.get(i).getPrice());
                    map.put("memo",expenses.get(i).getMemo());
                    map.put("imagePath",expenses.get(i).getImagePath());
                    map.put("createdAt",expenses.get(i).getCreatedAt().getTime());
                    map.put("accountId",expenses.get(i).getAccountId());
                    map.put("categoryId",expenses.get(i).getCategoryId());

                    FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Expenses").child(String.valueOf(expenses.get(i).getId())).setValue(map);
                }

            }
        });


        final LiveData<List<Expense>> expenses1   = mDb.expenseDao().loadAllExpensesAll();



        expenses1.observe(mContext, new Observer<List<Expense>>() {
            @Override
            public void onChanged(@Nullable List<Expense> expenses) {




                expenses1.removeObserver(this);



            }
        });


        final LiveData<List<Account>> c = mDb.accountDao().loadAllAccounts();

        c.observe(this, new Observer<List<Account>>() {
            @Override
            public void onChanged(@Nullable List<Account> accounts1) {



                Map map;
                for (int i=0;i<accounts1.size();i++)
                {

                    map=new HashMap<>();
                    map.put("id",accounts1.get(i).getId());
                    map.put("name",accounts1.get(i).getName());
                    map.put("icon",accounts1.get(i).getIcon());
                    map.put("total",accounts1.get(i).getTotal());
                    map.put("defaultAccount",accounts1.get(i).getDefaultAccount());


                    FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Accounts").child(String.valueOf(accounts1.get(i).getId())).setValue(map);
                }




                c.removeObserver(this);


            }
        });





        final  LiveData<List<Category>> a = mDb.categoryDao().loadAllCategories();

        a.observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {



                Map map;
                for (int i=0;i<categories.size();i++)
                {

                    map=new HashMap<>();
                    map.put("id",categories.get(i).getId());
                    map.put("name",categories.get(i).getName());
                    map.put("icon",categories.get(i).getIcon());
                    map.put("type",categories.get(i).getType());
                    map.put("defaultAccount",categories.get(i).getDefaultCategory());


                    FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Categories").child(String.valueOf(categories.get(i).getId())).setValue(map);


                }





                a.removeObserver(this);




            }
        });



    }


    String GetUserId(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String Id = preferences.getString("Id", "");


        return Id;

    }
    void CreateDbCopy(){

        final  String userId = GetUserId();

        final LiveData<List<Expense>> expenses1   = mDb.expenseDao().loadAllExpensesAll();



        expenses1.observe(this, new Observer<List<Expense>>() {
            @Override
            public void onChanged(@Nullable List<Expense> expenses) {


                Map map;
                for (int i=0;i<expenses.size();i++)
                {

                    map=new HashMap<>();
                    map.put("id",expenses.get(i).getId());
                    map.put("description",expenses.get(i).getDescription());
                    map.put("category",expenses.get(i).getCategory());
                    map.put("account",expenses.get(i).getAccount());
                    map.put("type",expenses.get(i).getType());
                    map.put("price",expenses.get(i).getPrice());
                    map.put("memo",expenses.get(i).getMemo());
                    map.put("imagePath",expenses.get(i).getImagePath());
                    map.put("createdAt",expenses.get(i).getCreatedAt().getTime());
                    map.put("accountId",expenses.get(i).getAccountId());
                    map.put("categoryId",expenses.get(i).getCategoryId());

                    FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Expenses").child(String.valueOf(expenses.get(i).getId())).setValue(map);
                }

                expenses1.removeObserver(this);



            }
        });


        final LiveData<List<Account>> c = mDb.accountDao().loadAllAccounts();

        c.observe(this, new Observer<List<Account>>() {
            @Override
            public void onChanged(@Nullable List<Account> accounts1) {



                Map map;
                for (int i=0;i<accounts1.size();i++)
                {

                    map=new HashMap<>();
                    map.put("id",accounts1.get(i).getId());
                    map.put("name",accounts1.get(i).getName());
                    map.put("icon",accounts1.get(i).getIcon());
                    map.put("total",accounts1.get(i).getTotal());
                    map.put("defaultAccount",accounts1.get(i).getDefaultAccount());


                    FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Accounts").child(String.valueOf(accounts1.get(i).getId())).setValue(map);
                }




                c.removeObserver(this);


            }
        });





        final  LiveData<List<Category>> a = mDb.categoryDao().loadAllCategories();

        a.observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {



                Map map;
                for (int i=0;i<categories.size();i++)
                {

                    map=new HashMap<>();
                    map.put("id",categories.get(i).getId());
                    map.put("name",categories.get(i).getName());
                    map.put("icon",categories.get(i).getIcon());
                    map.put("type",categories.get(i).getType());
                    map.put("defaultAccount",categories.get(i).getDefaultCategory());


                    FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Categories").child(String.valueOf(categories.get(i).getId())).setValue(map);


                }





                a.removeObserver(this);




            }
        });



    }


    */

}
