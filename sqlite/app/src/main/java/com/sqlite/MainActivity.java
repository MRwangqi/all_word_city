package com.sqlite;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String COUNTRY_SQL = "select * from country";
    public static final String PROVINCE_SQL = "select * from province where  id = ";

    private ListView listView;
    private TextView text;
    MyReceiver myReceiver;
    List<City> list = new ArrayList<>();
    private String Name;
    private String Code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.listview);
        text = (TextView) findViewById(R.id.text_main);

        SQLiteDatabase sql = SQLTools.opendatabase(this);
        Cursor cursor = sql.rawQuery(COUNTRY_SQL, null);
        int id = cursor.getColumnIndex("id");
        int name = cursor.getColumnIndex("name");
        final int code = cursor.getColumnIndex("code");
        Cursor cursor_next;
        while (cursor.moveToNext()) {
            City city = new City();
            city.setId(cursor.getString(id));
            city.setName(cursor.getString(name));
            city.setCode(cursor.getString(code));

            cursor_next = sql.rawQuery(PROVINCE_SQL + cursor.getString(id), null);
            if (cursor_next.moveToNext()) {//判断这个国家是否有省份
                city.setNext(true);
            } else {
                city.setNext(false);
            }
            list.add(city);
        }
        cursor.close();
        listView.setAdapter(new CityAdapter(list, this));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String id = list.get(i).getId();
                boolean next = list.get(i).isNext();
                Code = list.get(i).getCode();
                Name = list.get(i).getName();

                //将数据临时存储
                SQLTools.country.setCode(Code);
                SQLTools.country.setName(Name);
                if (next) {
                    Intent intent = new Intent(MainActivity.this, ProvinceActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                } else {
                    text.setText(Name + ":" + Code);

                }
            }
        });
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SQLTools.COUNTR_ACTION);
        intentFilter.addAction(SQLTools.PROVINCE_ACTION);
        intentFilter.addAction(SQLTools.CITY_ACTION);
        intentFilter.addAction(SQLTools.REGIN_ACTION);
        registerReceiver(myReceiver, intentFilter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }

    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //
            String result = Name + ":" + Code+"   ";
            if (intent.getAction() == SQLTools.PROVINCE_ACTION) {
                result += SQLTools.province.getName() + ":" + SQLTools.province.getCode()+"   ";
            } else if (intent.getAction() == SQLTools.CITY_ACTION) {
                result += SQLTools.province.getName() + ":" + SQLTools.province.getCode()+"   ";
                result += SQLTools.city.getName() + ":" + SQLTools.city.getCode();
            } else if (intent.getAction() == SQLTools.REGIN_ACTION) {
                result += SQLTools.province.getName() + ":" + SQLTools.province.getCode()+"   ";
                result += SQLTools.city.getName() + ":" + SQLTools.city.getCode()+"   ";
                result += SQLTools.region.getName() + ":" + SQLTools.region.getCode();
            }

            text.setText(result);
        }
    }
}
