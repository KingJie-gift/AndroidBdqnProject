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

public class pwd extends AppCompatActivity implements View.OnClickListener {
    String id;
    Handler handler = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd);
        Intent intent = getIntent();
        id = intent.getStringExtra("ok");
        Button button = findViewById(R.id.pwdbutton);
        button.setOnClickListener(this);
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(msg.what==0x11){
                    Toast.makeText(pwd.this, "修改成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(pwd.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else if(msg.what==0x12){
                    Toast.makeText(pwd.this, "修改失败请稍后再试", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.pwdbutton:
                this.updatePwd();
                break;
        }
    }
    public void updatePwd(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                EditText editText = findViewById(R.id.pwd);
                System.out.println(id+"\\\\\\\\");
                String url = ClentUrl.UPDATEPWD+"&id="+id+"&pwd="+editText.getText().toString();
                System.out.println(url);
                String resutl = String_Input.resultRequest(url);
                try {
                    JSONObject jsonObject = new JSONObject(resutl);
                    String value = (String) jsonObject.get("result");
                    if(!("0".equals(value))){
                        handler.sendEmptyMessage(0x11);
                    }else{
                        handler.sendEmptyMessage(0x12);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
