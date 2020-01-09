package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class SheZhi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_she_zhi);
        final EditText editText = findViewById(R.id.sizeedit);

//        给下拉框设置值
        final String[] ctype = new String[]{"白色", "蓝色", "护眼模式", "夜间"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ctype);  //创建一个数组适配器
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);     //设置下拉列表框的下拉选项样式
        Spinner spinner = super.findViewById(R.id.spinner);
        spinner.setAdapter(adapter);

        SharedPreferences editor = getSharedPreferences("su",MODE_PRIVATE);
        String back = "#FFFFFF";
        if( editor.getString("白色","").equals("#FFFFFF")){
            spinner.setSelection(0);
        }else if(editor.getString("蓝色","").equals("#0000FF")){
            spinner.setSelection(1);
        }else if(editor.getString("护眼模式","").equals("#ADD8E6")){
            spinner.setSelection(2);
        }else if(editor.getString("夜间","").equals("#708090")){
            spinner.setSelection(3);
        }else {
            spinner.setSelection(0);
        }
        SharedPreferences sh = getSharedPreferences("sz",MODE_PRIVATE);
        editText.setText(sh.getFloat("size",12)+"");
        editText.addTextChangedListener(new TextWatcher(){
//            获取焦点
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
//        改变内容
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
//        失去焦点
            @Override
            public void afterTextChanged(Editable editable) {
                SharedPreferences.Editor editor = getSharedPreferences("sz",MODE_PRIVATE).edit();
                editor.putFloat("size",TextUtils.isEmpty(editText.getText())?12:Float.parseFloat(editText.getText().toString()));
                editor.commit();
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            显示当前项
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String ctyp = ctype[i];
                System.out.println(ctyp);
                SharedPreferences.Editor editor = getSharedPreferences("su",MODE_PRIVATE).edit();
                editor.clear();
                editor.commit();
                if(ctyp.equals("白色")){
                    editor.putString("白色","#FFFFFF");
                }else if("蓝色".equals(ctyp)){
                    editor.putString("蓝色","#0000FF");
                }else if("护眼模式".equals(ctyp)){
                    System.out.println("执行");
                    editor.putString("护眼模式","#ADD8E6");
                }else if("夜间".equals(ctyp)){
                    editor.putString("夜间","#708090");
                }else {
                    editor.putString("白色","#FFFFFF");
                }
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
}
