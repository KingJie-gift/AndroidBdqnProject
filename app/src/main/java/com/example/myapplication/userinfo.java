package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.myapplication.content.String_Input;
import com.example.myapplication.entity.User;
import com.example.myapplication.net.ClentUrl;
import okhttp3.internal.framed.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class userinfo extends AppCompatActivity {
    Handler handler = null;
    ArrayList<String> context = new ArrayList<>();
    ArrayList<Integer> idList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
//        加载用户信息
        final TextView textView = findViewById(R.id.name);
        final ListView listView = findViewById(R.id.userMessage);
        final TextView suncount = findViewById(R.id.suncount);
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(msg.what==0x11){
                    Bundle bundle = msg.getData();
                    ArrayList<com.example.myapplication.entity.Message> arrayList = (ArrayList<com.example.myapplication.entity.Message>) bundle.getSerializable("list");
                    textView.setText(arrayList.get(0).getUserId().getName());
                    System.out.println(arrayList.get(0)+"地址");
                    System.out.println(arrayList.get(0).getMessage()+"信息");
                    if(!(TextUtils.isEmpty(arrayList.get(0).getMessage()))){
                        ArrayList<String> arrayList1 = new ArrayList<>();
//                        加载标题
                        for(com.example.myapplication.entity.Message message:arrayList){
                            arrayList1.add(message.getMessage());
                        }
//                    加载内容
                        for(com.example.myapplication.entity.Message message:arrayList){
                            context.add(message.getMessageInfo());
                        }
//                        删除编号
                        for(com.example.myapplication.entity.Message message:arrayList){
                            idList.add(message.getId());
                        }

                        suncount.setText(arrayList1.size()+"条数据");
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(userinfo.this,android.R.layout.simple_list_item_1,arrayList1);
                        listView.setAdapter(arrayAdapter);
                    }else {
                        suncount.setText("尚未发布信息");
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(userinfo.this,android.R.layout.simple_list_item_1,new ArrayList<String>());
                        listView.setAdapter(arrayAdapter);
                    }
                }
            }
        };
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = context.get(i);
                Intent intent = new Intent(userinfo.this, messageInfo.class);
                intent.putExtra("message", name);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(userinfo.this);

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
                        del(idList.get(i));
                    }
                });
                builder.create().show();
                return true;
            }
        });
        this.loadUserInfo();
    }
    public void loadUserInfo(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences userId = getSharedPreferences("userId", MODE_PRIVATE);
                String str = String_Input.resultRequest(ClentUrl.LOADUSERINFO+"&id="+userId.getString("id",""));
                ArrayList<com.example.myapplication.entity.Message> arrayList = new ArrayList<>();
                try {
//                    JSONArray jsonArrayUser = new JSONArray(str);
//                    for(int i = 0 ; i < jsonArrayUser.length();i++){
//                        JSONObject jsonObjectUser = jsonArrayUser.getJSONObject(i);
//                        Log.i("userinfo", "run: "+jsonObjectUser.has("userId"));
//                    }

                    JSONArray jsonArray = new JSONArray(str);
                    for(int i = 0 ; i < jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        com.example.myapplication.entity.Message message = new com.example.myapplication.entity.Message();
                        User userObj = new User();
                        String user = jsonObject.getString("userId");
                        JSONObject userJson = new JSONObject(user);
                        System.out.println(userJson);
                        userObj.setName(userJson.getString("name"));
                        if((jsonObject.has("message"))){
                            message.setMessage(jsonObject.getString("message"));
                            message.setMessageInfo(jsonObject.getString("messageInfo"));
                            message.setId(Integer.parseInt(jsonObject.getString("id")));
                        }
                        message.setUserId(userObj);
                        System.out.println(userJson.getString("name"));
                        arrayList.add(message);
                    }
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("list",arrayList);
                    message.setData(bundle);
                    message.what = 0x11;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

//    删除信息
    public void del(final Integer id){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String obj = String_Input.resultRequest(ClentUrl.DELMESSAGE+"&id="+id);
                try {
                    JSONObject jsonObject = new JSONObject(obj);
                    String ret = jsonObject.getString("result");
                    if (!("0".equals(ret))) {
                        loadUserInfo();
                    } else {
                        Toast.makeText(userinfo.this, "发布失败，请稍后再试", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}