package com.example.myapplication;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<>();
    public static void addActivity(Activity activity){
        activities.add(activity);
    }
    public static void remove(Activity activity){
        activities.remove(activity);
    }
    public static void finisAll(){
        for(Activity activity:activities){
//            判断活动是否运行，如果运行则进行销毁
            if(!activity.isFinishing()){
                activity.finish();
//                杀死当前线程
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }
    }
}
