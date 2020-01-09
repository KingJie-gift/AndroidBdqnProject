package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.myapplication.content.String_Input;
import com.example.myapplication.net.ClentUrl;
import okhttp3.internal.framed.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class fankui extends AppCompatActivity {
    Handler handler = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fankui);
        Button button = findViewById(R.id.fankuiButton);
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(msg.what==0x11){
                    Toast.makeText(fankui.this, "反馈成功，感谢支持", Toast.LENGTH_SHORT).show();
                }else if(msg.what==0x12){
                    Toast.makeText(fankui.this, "反馈失败，请稍后再试", Toast.LENGTH_SHORT).show();
                }else if(msg.what==0x15){
                    Toast.makeText(fankui.this, "请填写标题和内容后在进行反馈", Toast.LENGTH_SHORT).show();
                }
            }
        };
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("点击");
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        EditText messageT = findViewById(R.id.titlef);
                        EditText contentf = findViewById(R.id.contentf);
                        EditText userQQ = findViewById(R.id.userQQ);
                        if(TextUtils.isEmpty(messageT.getText().toString())||TextUtils.isEmpty(contentf.getText().toString())){
                            handler.sendEmptyMessage(0x15);
                            return;
                        }
                        Map<String,String> map = new HashMap<>();
                        map.put("title",messageT.getText().toString());
                        map.put("message",contentf.getText().toString());
                        map.put("userQQ",userQQ.getText().toString());
                        SharedPreferences userId = getSharedPreferences("userId", MODE_PRIVATE);
                        map.put("userId",userId.getString("id",""));
                        System.out.println("执行");

                        String obj = String_Input.okHettP(ClentUrl.USERFREEBACK,map);
                        try {
                            JSONObject jsonObject = new JSONObject(obj);
                            String ret = jsonObject.getString("result");
                            if (!("0".equals(ret))) {
                                handler.sendEmptyMessage(0x11);
                                Intent intent = new Intent(fankui.this, context.class);
                                startActivity(intent);
                                finish();
                            } else {
                                handler.sendEmptyMessage(0x12);
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