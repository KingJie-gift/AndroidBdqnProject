package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.myapplication.db.OpenSql;
import com.example.myapplication.db.SqliteDB;
import com.example.myapplication.entity.Message;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class lixianmoshi extends AppCompatActivity {
    List<Integer> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lixianmoshi);
        ListView listView = findViewById(R.id.listview);
        final SQLiteDatabase sqLiteDatabase = new OpenSql().getSqliteObject(lixianmoshi.this,"message");
        Cursor cursor = sqLiteDatabase.query("message",null,null,null,null,null,"id desc",null);
        List<String> title = new ArrayList<>();
        final List<String> message = new ArrayList<>();
        while (cursor.moveToNext()){
            title.add(cursor.getString(1));
            message.add(cursor.getString(2));
            list.add(cursor.getInt(0));
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(lixianmoshi.this,android.R.layout.simple_list_item_1,title);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(lixianmoshi.this);

                builder.setMessage("确认删除吗");
                builder.setTitle("提示");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        int message1 = sqLiteDatabase.delete("message", "id=?", new String[]{list.get(i)+""});
                        Intent intent = new Intent(lixianmoshi.this,lixianmoshi.class);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.create().show();
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(lixianmoshi.this,showMessageLi.class);
                intent.putExtra("messageInfo",message.get(i));
                startActivity(intent);
            }
        });
    }
}
