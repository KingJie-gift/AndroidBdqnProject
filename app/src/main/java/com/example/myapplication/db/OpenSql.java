package com.example.myapplication.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class OpenSql {
    private SqliteDB dbHelper;
    private SQLiteDatabase sqlite;
    public  SQLiteDatabase getSqliteObject(Context context, String db_name){
        dbHelper = new SqliteDB(context,db_name,null,1);
        sqlite = dbHelper.getWritableDatabase();
        return sqlite;
    }
}
