package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

public class taobleBur extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taoble_bur);

        String[] str = new String[]{"用户信息", "联系作者", "捐赠", "退出账号", "反馈", "查看收藏", "设置"};

        ListView listView = findViewById(R.id.listItem);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(taobleBur.this, android.R.layout.simple_list_item_1, str);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    Intent user = new Intent(taobleBur.this, userinfo.class);
                    startActivity(user);
                } else if (i == 1) {
//                     跳转之前，可以先判断手机是否安装QQ
                    if (isQQClientAvailable(taobleBur.this)) {
                        // 跳转到客服的QQ
                        String url = "mqqwpa://im/chat?chat_type=wpa&uin=2479744143";
                        Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        // 跳转前先判断Uri是否存在，如果打开一个不存在的Uri，App可能会崩溃
                        if (isValidIntent(taobleBur.this, intent1)) {
                            startActivity(intent1);
                        }
                    } else {
                        Toast.makeText(taobleBur.this, "请先安装QQ在进行联系", Toast.LENGTH_SHORT).show();
                    }
                } else if (i == 2) {
                    Intent in = new Intent(taobleBur.this, jz.class);
                    startActivity(in);
                } else if (i == 3) {
                    Intent intent = new Intent(taobleBur.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else if (i == 4) {
                    Intent fa = new Intent(taobleBur.this, fankui.class);
                    startActivity(fa);
                } else if (i == 5) {
                    Intent show = new Intent(taobleBur.this, ShowCollect.class);
                    startActivity(show);
                } else if (i == 6) {
                    Intent sz = new Intent(taobleBur.this, SheZhi.class);
                    startActivity(sz);
                }
            }
        });
    }

    public boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equalsIgnoreCase("com.tencent.qqlite") || pn.equalsIgnoreCase("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 判断 Uri是否有效
     */
    public boolean isValidIntent(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        return !activities.isEmpty();
    }

}



