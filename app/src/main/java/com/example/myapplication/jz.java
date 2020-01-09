package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.myapplication.content.String_Input;
import com.example.myapplication.net.ClentUrl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class jz extends AppCompatActivity {
    Handler handler = null;
    private Log LogUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jz);
        getBitmapFromServer();
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(msg.what==0x11){
                    Bundle build = msg.getData();
                    List<Bitmap> bitmaps = (List<Bitmap>) build.getSerializable("list");
                    ImageView imageView = findViewById(R.id.zfb);
                       imageView.setImageBitmap(bitmaps.get(0));
                      imageView = findViewById(R.id.wx);
                        imageView.setImageBitmap(bitmaps.get(1));
                }
            }
        };
        ImageView imageView = findViewById(R.id.zfb);

    }




    // 调起支付宝并跳转到指定页面
    private void startAlipayActivity(String url) {
        Intent intent;
        try {
            intent = Intent.parseUri(url,
                    Intent.URI_INTENT_SCHEME);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setComponent(null);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getBitmapFromServer() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Bitmap> bitmaps = new ArrayList<>();
                bitmaps.add(String_Input.getBitmapFromServer(ClentUrl.TUPIANIMG + "zfb.jpg"));
                bitmaps.add(String_Input.getBitmapFromServer(ClentUrl.TUPIANIMG + "wx.jpg"));
                Message message = new Message();
                Bundle bundle = new Bundle();
                message.what = 0x11;
                bundle.putSerializable("list", (Serializable) bitmaps);
                message.setData(bundle);
                handler.sendMessage(message);
            }
        });
        thread.start();
    }
}