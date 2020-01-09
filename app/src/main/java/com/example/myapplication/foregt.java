package com.example.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.myapplication.content.String_Input;
import com.example.myapplication.net.ClentUrl;
import org.json.JSONException;
import org.json.JSONObject;

public class foregt extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foregt);

        Button button = findViewById(R.id.forgetButton);
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(msg.what==0x13){
                    Toast.makeText(foregt.this, "此账号错误", Toast.LENGTH_SHORT).show();
                }
            }
        };
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        EditText phone = findViewById(R.id.phone);
                        EditText user = findViewById(R.id.user);
                        String obj = String_Input.resultRequest(ClentUrl.USERCONN+"&phone="+phone.getText().toString()+"&user="+user.getText().toString());
                        try {
                            JSONObject jsonObject = new JSONObject(obj);
                            String ok = (String) jsonObject.get("result");
                            if("-1".equals(ok)){
                                handler.sendEmptyMessage(0x13);
                            }else{
                                handler.sendEmptyMessage(0x11);
                                Intent intent = new Intent(foregt.this,pwd.class);
                                intent.putExtra("ok",ok);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
        });
    }
}