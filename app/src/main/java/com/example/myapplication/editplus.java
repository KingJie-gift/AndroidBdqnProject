package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.content.String_Input;
import com.example.myapplication.entity.Type;
import com.example.myapplication.net.ClentUrl;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class editplus extends AppCompatActivity {
    Handler handler = null;
    ArrayList<Integer> type_id = null;
    Integer indexType = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editplus);
        final Spinner spinner = findViewById(R.id.spinner_Type);
        Button button = findViewById(R.id.yescontext);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        EditText editText = findViewById(R.id.title);
                        EditText text = findViewById(R.id.content);
                        if (TextUtils.isEmpty(editText.getText().toString()) || TextUtils.isEmpty(text.getText().toString())) {
                            return;
                        }
                        SharedPreferences userId = getSharedPreferences("userId", MODE_PRIVATE);
                        //new StringPart("dealTotalPrice",actual_total_price.getText().toString(),"utf-8")
                        String resutl = null;
                        OkHttpClient okHttpClient = new OkHttpClient();
                        RequestBody requestBody = new FormBody.Builder().add("op", "insertInfoMessage")
                                .add("title", editText.getText().toString()).add("messageInfo", text.getText().toString())
                                .add("id", userId.getString("id", "")).add("type",indexType+"").build();
                        Request request = new Request.Builder().addHeader("Content-Type", "application/text;charset=utf-8").url(ClentUrl.INSERTMESSAGES).post(requestBody).build();
                        try {
                            Response response = okHttpClient.newCall(request).execute();
                            String resp = response.body().string();
                            System.out.println(resp);
                            resutl = String_Input.JsonObj(resp);
                            if (!("0".equals(resutl))) {
                                Intent intent = new Intent(editplus.this, context.class);
                                startActivity(intent);
                                finish();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
                thread.start();
            }
        });
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0x11) {
                    Bundle bundle = msg.getData();
                    List<Type> list = (List<Type>) bundle.getSerializable("list");
                    ArrayList<String> list1 = new ArrayList<>();
                    type_id = new ArrayList<>();
                    for (Type type : list) {
                        list1.add(type.getType());
                        type_id.add(type.getId());
                    }
                    //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context.this, android.R.layout.simple_expandable_list_item_1, listTitle);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(editplus.this, android.R.layout.simple_spinner_item, list1);  //创建一个数组适配器
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);     //设置下拉列表框的下拉选项样式
                    //rrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context.this,android.R.layout.simple_list_item_1,list1);

                    spinner.setAdapter(adapter);


//                    final String[] ctype = new String[]{"白色", "蓝色", "护眼模式", "夜间"};
//                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ctype);  //创建一个数组适配器
//                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);     //设置下拉列表框的下拉选项样式
//                    Spinner spinner = super.findViewById(R.id.spinner);
//                    spinner.setAdapter(adapter);

                }
            }

            ;
        };
        this.ListSpinner();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println(indexType);
                indexType = type_id.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    //    加载Spinner下拉框
    public void ListSpinner(){
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                String objList = String_Input.resultRequest(ClentUrl.TYPE);
                ArrayList<Type> arrayList = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(objList);
                    for(int i = 0 ; i < jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Type type = new Type();
                        type.setType(jsonObject.getString("type"));
                        type.setId(jsonObject.getInt("id"));
                        arrayList.add(type);
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
        th.start();
    }
}