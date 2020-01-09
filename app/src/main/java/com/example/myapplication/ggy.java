package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.myapplication.content.String_Input;
import com.example.myapplication.entity.Imgage;
import com.example.myapplication.net.ClentUrl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ggy extends AppCompatActivity {
    Handler handler = null;
    Integer time = 3 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ggy);
//        判断是否有wifi
        if(!isOnline()){
            return;
        }




        final Button button = findViewById(R.id.tiaoguo) ;

        final ImageView imageView = findViewById(R.id.imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.baidu.com");
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent = new Intent(ggy.this,MainActivity.class);
                startActivity(Intent);
                time = 100 ;
                finish();
            }
        });
        //加载页面显示数据
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(msg.what==0x11){
                    button.setText("剩余"+--time+"秒");
                    if(time==0){
                        Intent Intent = new Intent(ggy.this,MainActivity.class);
                        startActivity(Intent);
                        finish();
                    }
                }else if(msg.what==0x112){
                    Bundle bundle = msg.getData();
                    List<Bitmap> list = (List<Bitmap>) bundle.getSerializable("imgSer");
                    Bitmap bitmap1 = list.get(0);
                    imageView.setImageBitmap(bitmap1);
                }
            }
        };
        this.GGY();
        this.Img();
    }
    public void GGY(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0 ; i < 3 ; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessage(0x11);
                }
            }
        });
        thread.start();
    }

//加载广告位置
    public void Img(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = String_Input.getBitmapFromServer(ClentUrl.TUPIANIMG+"ggimg.png");
                Message message = new Message();
                List<Bitmap> bitmaps = new ArrayList<>();
                bitmaps.add(bitmap);
                Bundle bundle = new Bundle();
                bundle.putSerializable("imgSer", (Serializable) bitmaps);
                message.setData(bundle);
                message.what = 0x112;
                handler.sendMessage(message);
            }
        });
        thread.start();
    }

    @SuppressLint("WrongConstant")
    private Boolean isOnline()  {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if(ni != null && ni.isConnected())
        {
//        Toast.makeText(this, "网络连接正常", 1000).show();
            return true;
        }
        else{
            Toast.makeText(ggy.this, "网络连接失败请点击菜单设置,已进入离线模式", 3000).show();
            Intent integer = new Intent(ggy.this,lixianmoshi.class);
            startActivity(integer);
            return false;
        }
    }
}