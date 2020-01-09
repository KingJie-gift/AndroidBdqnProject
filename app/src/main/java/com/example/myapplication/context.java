package com.example.myapplication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.*;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.*;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import com.example.myapplication.content.String_Input;
import com.example.myapplication.db.OpenSql;
import com.example.myapplication.entity.AppInfo;
import com.example.myapplication.entity.Type;
import com.example.myapplication.net.ClentUrl;
import com.example.myapplication.util.Page;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class context extends AppCompatActivity implements View.OnClickListener {
    ListView listView = null;
    Handler handler = null;
    String name = "";
    String type = "";
    ArrayList<Integer> type_id = new ArrayList<>();
    ArrayList<String> listTitle = null;
    Page<com.example.myapplication.entity.Message> page = null;
    AppInfo appInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context);


        ActivityCollector.addActivity(this);
//        提前加载数据
        type_id.add(0);
        listView = findViewById(R.id.listview);
        Button pageButton = findViewById(R.id.page);
        Button pageEnd = findViewById(R.id.pageEnt);
        Button pageSub = findViewById(R.id.pageSub);
        Button pageNext = findViewById(R.id.pageNext);
        ActivityCollector.addActivity(this);
        Button selButton = findViewById(R.id.selButton);
        selButton.setOnClickListener(this);
        pageSub.setOnClickListener(this);
        pageNext.setOnClickListener(this);

        pageButton.setOnClickListener(this);
        pageEnd.setOnClickListener(this);
        final Spinner spinner = findViewById(R.id.typeItem);
//        执行改变发送请求刷新数据

//

        this.addContextMessage( "1", "","");
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0x115) {
                    Bundle bundle = msg.getData();
                    page = (Page<com.example.myapplication.entity.Message>) bundle.getSerializable("page");
                  listTitle = new ArrayList<>();
                    for (com.example.myapplication.entity.Message message : page.getGetListLimit()) {
                        listTitle.add(message.getMessage());
                    }
                    TextView textView = findViewById(R.id.indexPage);
                    textView.setText("当前第" + page.getIndexPage() + "页");
                    TextView textView1 = findViewById(R.id.sumPage);
                    textView1.setText("共" + page.getSumPage() + "页");
                    //ArrayAdapter<List<String>> arrayAdapter = new ArrayAdapter<List<String>>(context.class,android.R.layout.simple_list_item_1,list);
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context.this, android.R.layout.simple_expandable_list_item_1, listTitle);
                    listView.setAdapter(arrayAdapter);
                }else if(msg.what==0x11){
                    Bundle bundle = msg.getData();
                    List<Type> list = (List<Type>) bundle.getSerializable("list");
                    ArrayList<String> list1 = new ArrayList<>();
                    list1.add("全部");
                    for(Type type:list){
                        list1.add(type.getType());
                        type_id.add(type.getId());
                    }
                    //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context.this, android.R.layout.simple_expandable_list_item_1, listTitle);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context.this, android.R.layout.simple_spinner_item, list1);  //创建一个数组适配器
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);     //设置下拉列表框的下拉选项样式
                    //rrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context.this,android.R.layout.simple_list_item_1,list1);

                    spinner.setAdapter(adapter);


//                    final String[] ctype = new String[]{"白色", "蓝色", "护眼模式", "夜间"};
//                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ctype);  //创建一个数组适配器
//                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);     //设置下拉列表框的下拉选项样式
//                    Spinner spinner = super.findViewById(R.id.spinner);
//                    spinner.setAdapter(adapter);
                }else if(msg.what==0x1153){
                    Toast.makeText(context.this, "保存本地成功", Toast.LENGTH_SHORT).show();
                }else if(msg.what == 0x122){
                    Bundle bundle = msg.getData();
                    appInfo = (AppInfo) bundle.getSerializable("appinfo");
//                    下面要执行更新的代码
                    System.out.println(appInfo.getUrl()+"url");
                    isVersion();
                }else if(msg.what==0x113){
                    Toast.makeText(context.this,"下载成功",Toast.LENGTH_SHORT).show();
                }
            }
        };
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                com.example.myapplication.entity.Message message = page.getGetListLimit().get(i);
                Intent intent = new Intent(context.this, messageInfo.class);
                intent.putExtra("message", message.getMessageInfo());
                intent.putExtra("id",message.getId());
                startActivity(intent);
            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                //                是否要保存到本地数据库

                AlertDialog.Builder builder = new AlertDialog.Builder(context.this);
                builder.setMessage("确认要存到本地吗");
                builder.setTitle("提示");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        SQLiteDatabase sqLiteDatabase = new OpenSql().getSqliteObject(context.this,"message");
                        ContentValues contentValues = new ContentValues();
                        com.example.myapplication.entity.Message message = page.getGetListLimit().get(i);
                        contentValues.put("title",message.getMessage());
                        contentValues.put("messageInfo",message.getMessageInfo());
                        sqLiteDatabase.insert("message","",contentValues);
                        handler.sendEmptyMessage(0x1153);
                    }
                });
                builder.create().show();
                return true;
            }
        });

        //加载用户编号
        this.addById();
        ListSpinner();


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                addContextMessage( "1", name,type_id.get(i)+"");
                type = type_id.get(i)+"";
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        创建数据库

        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }
        if(null==appInfo) {
            isUpdate();
        }
    }

    //是否返回
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context.this);
            alertDialog.setTitle("温馨提示");
            alertDialog.setMessage("是否退出");
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCollector.finisAll();
                }
            });
            alertDialog.setNegativeButton("取消", null);
            alertDialog.show();
        }
        return true;
    }

//    获取menu的点击事件



    //销毁刺活动
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.remove(this);
    }

    //    进来加载的数据//
    public void addContextMessage(final String indexPage, final String rname, final String type) {
        //String [] data = new String[]{"abc"};
        //第一个参数是上下文，第二个参数是样式，第三个参数是数据
        final Thread thread = new Thread(new Runnable() {
            ArrayList<com.example.myapplication.entity.Message> list = new ArrayList<>();

            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder().add("indexPage", indexPage).add("name", rname).add("type",type).build();
                Request request = new Request.Builder().addHeader("Content-Type", "application/text;charset=utf-8").url(ClentUrl.USERINFOMESSAGE).post(requestBody).build();
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
                // String str = String_Input.resultRequest(messageurl);
                try {
                    JSONArray jsonArray = new JSONArray(new JSONObject(resp).getString("getListLimit"));
                    Page<com.example.myapplication.entity.Message> page = new Page<com.example.myapplication.entity.Message>();
                    page.setIndexPage(Integer.parseInt(new JSONObject(resp).getString("indexPage")));
                    page.setSumPage(Integer.parseInt(new JSONObject(resp).getString("sumPage")));
                    name = new JSONObject(resp).getString("name");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        com.example.myapplication.entity.Message message = new com.example.myapplication.entity.Message();
                        message.setId(jsonObject.getInt("id"));
                        // message.setDatetiem(Date.valueOf(jsonObject.getString("datetime")));
                        message.setMessage(jsonObject.getString("message"));
                        message.setId(Integer.parseInt(jsonObject.getString("id")));
                        System.out.println(message);
                        message.setMessageInfo(jsonObject.getString("messageInfo"));
                        list.add(message);
                    }
                    page.setGetListLimit(list);
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("page", page);// 将服务器返回的订单号传到Bundle中，，再通过handler传出
                    msg.setData(bundle);
                    msg.what = 0x115;
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }



//用户，刚进来进行登入判断
    public void addById() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                String user = sharedPreferences.getString("user", "").toString();
                String pwd = sharedPreferences.getString("pwd", "").toString();
                System.out.println(ClentUrl.BYID + "&user=" + user + "&pwd=" + pwd + "密码");
                String resutl = String_Input.JsonObj(String_Input.resultRequest(ClentUrl.BYID + "&user=" + user + "&pwd=" + pwd));
                SharedPreferences.Editor userId = getSharedPreferences("userId", MODE_PRIVATE).edit();
                userId.clear();
                userId.commit();
                userId.putString("id", resutl);
                userId.commit();
            }
        });
        thread.start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.page:
                this.addContextMessage( "1", name,type);
                break;
            case R.id.pageEnt:
                this.addContextMessage( page.getSumPage() + "", name,type);
                break;
            case R.id.pageSub:
                this.addContextMessage( (page.getIndexPage() > 1 ? page.getIndexPage() - 1 : 1) + "", name,type);
                break;
            case R.id.pageNext:
                this.addContextMessage( (page.getSumPage() < page.getIndexPage() ? page.getIndexPage() + 1 : page.getSumPage()) + "", name,type);
                break;
            case R.id.selButton:
                this.yesPost();
                break;
        }
    }

    //    获取文本框的值，提交
    public void yesPost() {
        EditText editText = findViewById(R.id.edit_query);
        name = editText.getText().toString();
        Log.i("context", "yesPost: 执行");
        System.out.println(name + "xxxxxxxxxxxxxxxxxxxxxxxxxxx执行");
        this.addContextMessage( "1", name,type);
    }
//    重新获取焦点
//    :OnResume()

    @Override
    protected void onResume() {
        super.onResume();
     //   this.addContextMessage( page.getIndexPage()+"", name);
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

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        android.support.multidex.MultiDex.install(this);
    }
//   检查是否有新的版本
//    检查是否更新操作
public void isUpdate(){
    Thread thread  = new Thread(new Runnable() {
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
    });
    thread.start();
}

    //    获取当前的版本号
    public void isVersion(){
        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        PackageManager packageManager = getPackageManager();
//                0代表版本信息
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(),0);
            Log.i("mainActivit", packageInfo.versionName);
            Log.i("mainActivit", packageInfo.versionCode+"");
//                    从服务器获取是否更新的版本信息
            System.out.println(appInfo+"appInfo");
            Integer integer = appInfo.getAppCode();
            if(integer>packageInfo.versionCode){
                //弹出更新的对话框
                AlertDialog.Builder a = new AlertDialog.Builder(context.this);
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
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void update(){
        final ProgressDialog pd = new ProgressDialog(context.this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载中");
        pd.setCancelable(false);
        pd.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
//                弹出对话框
                String url = appInfo.getUrl();
                try {
                    File file = getFileFromServer(url,pd);
                    handler.sendEmptyMessage(0x113);
                    Thread.sleep(1000);
                    installApkFile(context.this,file.getPath());
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
        File file = new File(Environment.getExternalStorageDirectory(),"作业助手.apk");
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
        pd.dismiss();
        return file;
    }

//    安装apk


//    public void installApk(File file){
//        Intent intent = new Intent();
//        //执行动作
//        intent.setAction(Intent.ACTION_VIEW);
//        //执行的数据类型
//        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//        startActivity(intent);
//    }


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





//    加载menu


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.listview,menu);
        return true;
    }

        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.sz:
                    Intent sz = new Intent(context.this,SheZhi.class);
                    startActivity(sz);
                    break;
                case R.id.userItem:
                    Intent user = new Intent(context.this, userinfo.class);
                    startActivity(user);
                    break;
                case R.id.userMessageItem:
                    Intent intentx = new Intent(context.this, editplus.class);
                    startActivity(intentx);
                    break;
                case R.id.exit:
                    SharedPreferences.Editor sh = getSharedPreferences("user", MODE_PRIVATE).edit();
                    sh.clear();
                    sh.commit();
                    Intent intent = new Intent(context.this, MainActivity.class);
                    startActivity(intent);
                    ActivityCollector.remove(this);
                    break;
                case R.id.author:
                    // 跳转之前，可以先判断手机是否安装QQ
                    if (isQQClientAvailable(context.this)) {
                        // 跳转到客服的QQ
                        String url = "mqqwpa://im/chat?chat_type=wpa&uin=2479744143";
                        Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        // 跳转前先判断Uri是否存在，如果打开一个不存在的Uri，App可能会崩溃
                        if (this.isValidIntent(context.this, intent1)) {
                            startActivity(intent1);
                        }
                    } else {
                        Toast.makeText(context.this, "请先安装QQ在进行联系", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.jz:
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context.this);
//                alertDialog.setIcon(R.drawable.wx);
//                alertDialog.setMessage("ajva");
//                alertDialog.show();
                    Intent in = new Intent(context.this, jz.class);
                    startActivity(in);
                    break;
                case R.id.fankui:
                    Intent fa = new Intent(context.this, fankui.class);
                    startActivity(fa);
                    break;
                case R.id.shouchang:
                    Intent show = new Intent(context.this, ShowCollect.class);
                    startActivity(show);
                    break;
            }
            return true;
        }


    /**
     * 判断 用户是否安装QQ客户端
     */
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