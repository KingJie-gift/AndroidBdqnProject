package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.content.String_Input;
import com.example.myapplication.net.ClentUrl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class messageInfo extends AppCompatActivity {
    Handler handler = null;
    Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_info);



        intent = getIntent();
        String context = intent.getStringExtra("message");
        ScrollView sc = findViewById(R.id.allScanner);
        FrameLayout frameLayout = findViewById(R.id.from);
        TextView textView = findViewById(R.id.textmessage);



        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0x11) {
                    Toast.makeText(messageInfo.this, "收藏成功", Toast.LENGTH_SHORT).show();
                } else if (msg.what == 0x12) {
                    Toast.makeText(messageInfo.this, "收藏失败，请稍后再试", Toast.LENGTH_SHORT).show();
                }else if(msg.what == 0x112){
                    meiSc();
                }else if(msg.what == 0x113){
                    showcang();
                }else if(msg.what==0x114){
                    Toast.makeText(messageInfo.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(messageInfo.this,ShowCollect.class);
                    startActivity(intent);
                    finish();

                }else if(msg.what==0x124){
                    Toast.makeText(messageInfo.this, "取消收藏失败，请稍后再试", Toast.LENGTH_SHORT).show();
                }

            }
        };
//        在主线程不能进行耗时操作
//        在子线程不能更新ul组件
//        所以我们把数据，通过Handler进行发送处理



        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


//                &messageId=81&userId=7
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String obj = String_Input.JsonObj(String_Input.resultRequest(ClentUrl.SCASSICE + "&messageId=" + intent.getIntExtra("id", -1) + "&userId=" + getSharedPreferences("userId", MODE_PRIVATE).getString("id", "")));
                        System.out.println(obj);
                        if ("0".equals(obj)) {
                            handler.sendEmptyMessage(0x112);
                        } else {
                            handler.sendEmptyMessage(0x113);
                        }
                    }
                });
                thread.start();
                return true;
            }
        });
        textView.setText(context);
        SharedPreferences sh = getSharedPreferences("sz",MODE_PRIVATE);
        System.out.println(sh.getFloat("size",12));
        textView.setTextSize(sh.getFloat("size",12));
        System.out.println(textView.getTextSize()+"字体");


        SharedPreferences editor = getSharedPreferences("su",MODE_PRIVATE);
        String back = "#FFFFFF";
        if( editor.getString("白色","").equals("#FFFFFF")){
            back="#FFFFFF";
        }else if(editor.getString("蓝色","").equals("#0000FF")){
            back = "#0000FF";
        }else if(editor.getString("护眼模式","").equals("#ADD8E6")){
           back = "#ADD8E6";
        }else if(editor.getString("夜间","").equals("#708090")){
            back = "#708090";
        }else {
            back="#FFFFFF";
        }
        System.out.println(back);


//给背景设置颜色
        frameLayout.setBackgroundColor(Color.parseColor(back));
        textView.setBackgroundColor(Color.parseColor(back));

    }
    public void meiSc(){
        AlertDialog.Builder builder = new AlertDialog.Builder(messageInfo.this);
        builder.setMessage("确认收藏吗");
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
//                        再次发送网络请求进行收藏
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences userId = getSharedPreferences("userId", MODE_PRIVATE);
                        System.out.println(intent.getIntExtra("id", -1));
                        String obj = String_Input.resultRequest(ClentUrl.SC + "&messageId=" + intent.getIntExtra("id", 0) + "&userId=" + userId.getString("id", ""));
                        String result = String_Input.JsonObj(obj);
                        if (!("0".equals(result))) {
                            handler.sendEmptyMessage(0x11);
                        } else {
                            handler.sendEmptyMessage(0x12);
                        }
                    }
                });
                thread.start();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {


            }
        });
        builder.create().show();
    }
    public void showcang(){
        AlertDialog.Builder builder = new AlertDialog.Builder(messageInfo.this);
        builder.setMessage("确认取消收藏吗");
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
//                        再次发送网络请求进行收藏

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences userId = getSharedPreferences("userId", MODE_PRIVATE);
                        System.out.println(intent.getIntExtra("id", -1));
                        String obj = String_Input.resultRequest(ClentUrl.SCQX + "&messageId=" + intent.getIntExtra("id", 0) + "&userId=" + userId.getString("id", ""));
                        String result = String_Input.JsonObj(obj);
                        if (!("0".equals(result))) {
                            handler.sendEmptyMessage(0x114);
                        } else {
                            handler.sendEmptyMessage(0x124);
                        }
                    }
                });
                thread.start();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {


            }
        });
        builder.create().show();
    }

    public void registerForContextMenu(View view) {

        view.setOnCreateContextMenuListener(this);

    }
}