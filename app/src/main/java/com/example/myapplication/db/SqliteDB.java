package com.example.myapplication.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.myapplication.context;

public class SqliteDB extends SQLiteOpenHelper {
    private final String CREATEMESSAGE = "create table message (" +
            "id integer primary key Autoincrement, title , messageInfo" +
            ")";

    public SqliteDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATEMESSAGE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        System.out.println("更新--了一条数据库");
    }
}
