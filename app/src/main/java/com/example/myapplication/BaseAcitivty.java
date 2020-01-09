package com.example.myapplication;

import android.app.Application;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

public class BaseAcitivty extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("BaseActitvty"+getClass().getSimpleName());
        Log.w("BaseActitvty", "onCreate: "+getClass().getSimpleName() );
    }
}
