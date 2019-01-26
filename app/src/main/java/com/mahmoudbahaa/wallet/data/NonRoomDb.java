package com.mahmoudbahaa.wallet.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by MahmoudBahaa on 20/01/2019.
 */

public class NonRoomDb extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "data.db";

    private Context context;
    private static final int DATABASE_VERSION = 1;

    SQLiteDatabase sql;

    public NonRoomDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
