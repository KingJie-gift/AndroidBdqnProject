package com.example.myapplication.content;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.example.myapplication.net.ClentUrl;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class String_Input {
    //    发送网络请求，获取返回结果
    public static String resultRequest(String uurl) {
        URL url = null;
        StringBuffer str = null;
        try {
            URL urlx = new URL(uurl);
        // 将url 以 open方法返回的urlConnection 连接强转为HttpURLConnection连接
        // (标识一个url所引用的远程对象连接)
            HttpURLConnection connection = (HttpURLConnection) urlx.openConnection();// 此时cnnection只是为一个连接对象,待连接中
        // 设置连接输出流为true,默认false (post 请求是以流的方式隐式的传递参数)
            connection.setDoOutput(true);
        // 设置连接输入流为true
            connection.setDoInput(true);
        // 设置请求方式为post
            connection.setRequestMethod("POST");
        // post请求缓存设为false
            connection.setUseCaches(false);
            connection.setConnectTimeout(20000);
            connection.setReadTimeout(20000);
        // 设置该HttpURLConnection实例是否自动执行重定向
            connection.setInstanceFollowRedirects(true);
        // 设置请求头里面的各个属性 (以下为设置内容的类型,设置为经过urlEncoded编码过的from参数)
        // application/x-javascript text/xml->xml数据
        // application/x-javascript->json对象
        // application/x-www-form-urlencoded->表单数据
            connection.setRequestProperty("Content-Type", "application/text;charset=utf-8");
        // 建立连接
        // (请求未开始,直到connection.getInputStream()方法调用时才发起,以上各个参数设置需在此方法之前进行)
            connection.connect();
            InputStream stream = null;
            if (connection.getResponseCode() == 200) {
                stream = connection.getInputStream();
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
            String line = null;
            str = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                str.append(line);
            }
            connection.disconnect();
            stream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();
    }

    public static Bitmap getBitmapFromServer(String path) {
        Bitmap pic = null;
        URL url = null;
        try {
            url = new URL(path);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(8000);
            urlConnection.setReadTimeout(8000);
            urlConnection.setRequestMethod("POST");
            urlConnection.connect();
            InputStream stream = null;
            if (urlConnection.getResponseCode() == 200) {
                stream = urlConnection.getInputStream();
            }
            pic = BitmapFactory.decodeStream(stream);
            stream.close();
            urlConnection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pic;
    }

    //    处理单个JSON对象
    public static String JsonObj(String jsonStr) {
        String result = null;
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            result = (String) jsonObject.get("result");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

//   chuliokHttl支持传输中文
    public static String okHettP(String url , Map<String , String> map) {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder f = new FormBody.Builder();
        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()){
            String key = iterator.next();
            f.add(key, (String) map.get(key));
        }
        RequestBody requestBody = f.build();
        Request request = new Request.Builder().addHeader("Content-Type", "application/text;charset=utf-8").url(url).post(requestBody).build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String resp = null;
        try {
            resp = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resp;
    }


}
