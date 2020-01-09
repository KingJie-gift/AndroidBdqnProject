package com.example.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.*;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import com.example.myapplication.content.String_Input;
import com.example.myapplication.entity.AppInfo;
import com.example.myapplication.net.ClentUrl;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateMode extends AppCompatActivity {
    Context context = null;
    AppInfo appInfo = null;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x122) {
                Bundle bundle = msg.getData();
                appInfo = (AppInfo) bundle.getSerializable("appinfo");
//                    下面要执行更新的代码
                System.out.println(appInfo.getUrl() + "url");
            }
        }
    };
    //   检查是否有新的版本
//    检查是否更新操作
    public void isUpdate(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(String_Input.resultRequest(ClentUrl.ISUPDATA));
                    AppInfo appInfo = new AppInfo(jsonObject.getInt("appCode")
                            ,jsonObject.getInt("appId"),jsonObject.getString("appName"),
                            jsonObject.getString("appUpdateMessage"),jsonObject.getString("myAppInfo"),
                            jsonObject.getString("url")
                    );
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("appinfo",appInfo);
                    message.setData(bundle);
                    message.what = 0x122;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //    获取当前的版本号
    public void isVersion(Integer code){
        isUpdate();
        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        PackageManager packageManager = getPackageManager();
////                0代表版本信息
//        try {
//            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(),0);
//            Log.i("mainActivit", packageInfo.versionName);
//            Log.i("mainActivit", packageInfo.versionCode+"");
//                    从服务器获取是否更新的版本信息

            if(appInfo.getAppCode()>code){
                //弹出更新的对话框
                AlertDialog.Builder a = new AlertDialog.Builder(context);
                a.setTitle("版本更新");
                a.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                                更新开启子线程
                        update();
                    }
                });
                a.setNegativeButton("取消",null);
                a.create().show();
            }

    }
    public void update(){
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载中");
        pd.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
//                弹出对话框
                String url = appInfo.getUrl();
                try {
                    File file = getFileFromServer(url,pd);
                    Thread.sleep(3000);
                    installApk(file);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
    public File getFileFromServer(String urk , ProgressDialog pd) throws IOException {
        URL url = new URL(urk);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setConnectTimeout(5000);
        pd.setMax(httpURLConnection.getContentLength());
        InputStream is = httpURLConnection.getInputStream();
        File file = new File(Environment.getExternalStorageDirectory(),"aa.apk");
        FileOutputStream fos = new FileOutputStream(file);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(is);
        byte [] buff = new byte[1024];
        int total = 0 ;
        int len = 0 ;
        while ((len = bufferedInputStream.read(buff))!=-1){
            fos.write(buff,0,len);
            total+=len;
            pd.setProgress(total);
        }
        fos.close();
        bufferedInputStream.close();
        is.close();
        return file;
    }

//    安装apk


    public void installApk(File file){
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型

        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }

    public static void installApkFile(Context context, String filePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, "com.example.myapplication.fileprovider", new File(filePath));
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

}
