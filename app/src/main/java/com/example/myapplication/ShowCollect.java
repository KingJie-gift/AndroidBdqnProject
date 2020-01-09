package com.example.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.myapplication.content.String_Input;
import com.example.myapplication.entity.Message;
import com.example.myapplication.net.ClentUrl;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.xml.transform.sax.TemplatesHandler;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShowCollect extends AppCompatActivity {
    Handler handler = null;
    List<Message> list = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_collect);
        this.addUserMessage();
        final ListView listView = findViewById(R.id.showUserMessage);

        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull android.os.Message msg) {
                super.handleMessage(msg);
                if(msg.what==0x11){
                    Bundle bundle = msg.getData();
                    list = (List<Message>) bundle.getSerializable("list");
                    List<String> arrayList = new ArrayList<>();
                    for(Message message:list){
                        arrayList.add(message.getMessage());
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ShowCollect.this,android.R.layout.simple_list_item_1,arrayList);
                    listView.setAdapter(arrayAdapter);
                }
            }
        };
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ShowCollect.this, messageInfo.class);
                intent.putExtra("message", list.get(i).getMessageInfo());
                intent.putExtra("id",list.get(i).getId());
                startActivity(intent);
            }
        });
    }

    private void addUserMessage() {
//        加载用户信息
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONArray jsonArray = new JSONArray(String_Input.resultRequest(ClentUrl.SCUSER+"&id="+getSharedPreferences("userId", MODE_PRIVATE).getString("id","")));
                    List<Message> list= new ArrayList<>();
                    for(int i = 0 ; i < jsonArray.length() ; i ++ ){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Message message = new Message();
                        message.setId(Integer.parseInt(jsonObject.getString("id")));
                        message.setMessage(jsonObject.getString("message"));
                        message.setMessageInfo(jsonObject.getString("messageInfo"));
                        list.add(message);
                    }
                    android.os.Message message = new android.os.Message();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("list", (Serializable) list);
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
}
