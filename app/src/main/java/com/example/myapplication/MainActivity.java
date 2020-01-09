package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.content.String_Input;
import com.example.myapplication.net.ClentUrl;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MainActivity extends BaseAcitivty  implements View.OnClickListener {
    Handler handler = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //自己的代码
        TextView in = findViewById(R.id.in);
        in.setOnClickListener(this);

        Button button1 = findViewById(R.id.up);
        button1.setOnClickListener(this);

        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(msg.what==0x11){
                    Toast.makeText(MainActivity.this, "用户名或者密码错误", Toast.LENGTH_SHORT).show();
                }else if(msg.what==0x14){
                    Toast.makeText(MainActivity.this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                }
            }
        };
        ActivityCollector.addActivity(this);
        TextView textView = findViewById(R.id.forget);
        textView.setOnClickListener(this);
        this.loginAuto();
    }


//    点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.in:
                Intent integer = new Intent(MainActivity.this,login.class);
                startActivity(integer);
                break;
            case R.id.up:
                this.loginUser();
                break;
            case R.id.forget:
                Intent intent1 = new Intent(MainActivity.this,foregt.class);
                startActivity(intent1);
                break;
        }
    }



    public void loginUser(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                EditText user = findViewById(R.id.user);
                EditText pwd = findViewById(R.id.pwd);
                if(TextUtils.isEmpty(user.getText().toString())||TextUtils.isEmpty(pwd.getText().toString())){
                    handler.sendEmptyMessage(0x14);
                    return;
                }
                try {
                    URL url = new URL(ClentUrl.LOGINURL+"&name="+user.getText().toString()+"&pwd="+pwd.getText().toString());
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
                    if(!("0".equals(ret))){
                        SharedPreferences sh = getSharedPreferences("user",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sh.edit();
                        editor.putString("user",user.getText().toString());
                        editor.putString("pwd",pwd.getText().toString());
                        editor.commit();
                        Intent intent = new Intent(MainActivity.this,context.class);
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

//    刚进入进行登入
    public void loginAuto(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
                String user = sharedPreferences.getString("user","").toString();
                String pwd = sharedPreferences.getString("pwd","").toString();
                System.out.println(user+"---------");
                System.out.println(pwd+"--------------");
                String url = ClentUrl.LOGINURL+"&name="+user.toString()+"&pwd="+pwd.toString();
                System.out.println(url);
                String result = String_Input.resultRequest(url);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    String ret = (String) jsonObject.get("result");
                    System.out.println(ret+"------------------");
                    if(!("0".equals(ret))){
                        Intent intent = new Intent(MainActivity.this,context.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}