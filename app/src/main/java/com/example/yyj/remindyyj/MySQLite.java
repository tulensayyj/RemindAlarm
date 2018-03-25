package com.example.yyj.remindyyj;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yyj on 2018/3/12.
 */

public class MySQLite extends SQLiteOpenHelper {
    public MySQLite(Context context){
        super(context,"Alarms.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Alarms(_id integer primary key autoincrement, Time varchar)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
