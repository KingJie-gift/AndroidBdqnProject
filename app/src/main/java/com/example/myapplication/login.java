package com.example.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.myapplication.net.ClentUrl;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class login extends BaseAcitivty  implements View.OnClickListener {
    Handler handler = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.in);
        button.setOnClickListener(this);
        Button button1 = findViewById(R.id.up);
        button1.setOnClickListener(this);
        TextView textView = findViewById(R.id.forget);
        textView.setOnClickListener(this);
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(msg.what==0x11){
                    Toast.makeText(login.this, "注册失败请稍后再试", Toast.LENGTH_SHORT).show();
                }else if(msg.what==0x12){
                    Toast.makeText(login.this, "此用户名已存在，请更改名称", Toast.LENGTH_SHORT).show();
                }else if(msg.what==0x13){
                    Toast.makeText(login.this, "用户信息不完整，请重新填写", Toast.LENGTH_SHORT).show();
                }else if(msg.what==0x14){
                    Toast.makeText(login.this, "注册成功", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.in:
                this.sendInsertUser();
                break;
            case R.id.up:
                Intent intent = new Intent(login.this,MainActivity.class);
                startActivity(intent);
                finish();
            break;
            case R.id.forget:
                Intent intent1 = new Intent(login.this,foregt.class);
                startActivity(intent1);
                break;
        }
    }
    /**
     * 发送请求
     */
    public void sendInsertUser(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                EditText user = findViewById(R.id.user);
                EditText pwd = findViewById(R.id.pwd);
                EditText phone = findViewById(R.id.phone);
                if(TextUtils.isEmpty(user.getText().toString())||TextUtils.isEmpty(pwd.getText().toString())||TextUtils.isEmpty(phone.getText().toString())){
                    handler.sendEmptyMessage(0x13);
                    return;
                }
                try {
                    URL url = new URL(ClentUrl.SENDINSERTUSER+"&name="+user.getText().toString()+"&pwd="+pwd.getText().toString()+"&phone="+phone.getText().toString());
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream stream = urlConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                    String line = null;
                    StringBuffer str = new StringBuffer();
                    while ((line = bufferedReader.readLine())!=null){
                        str.append(line);
                    }
                    JSONObject jsonObject = new JSONObject(str.toString());
                    String ret = (String) jsonObject.get("result");
                    System.out.println(ret);
                    if("-1".equals(ret)){
                        handler.sendEmptyMessage(0x12);
                    }else if(!("0".equals(ret))){
                        handler.sendEmptyMessage(0x14);
                        Intent intent = new Intent(login.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        handler.sendEmptyMessage(0x11);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.remove(this);
    }
}