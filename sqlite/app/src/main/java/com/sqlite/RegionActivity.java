package com.sqlite;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class RegionActivity extends AppCompatActivity {
    public static final String Region_SQL = "select * from quxian where id = ";
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region);
        String _id = getIntent().getStringExtra("id");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_region);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.listview_region);
        final List<City> list = new ArrayList<>();
        SQLiteDatabase sql = SQLTools.opendatabase(this);
        Cursor cursor = sql.rawQuery(Region_SQL + _id, null);
        int id = cursor.getColumnIndex("id");
        int name = cursor.getColumnIndex("name");
        int code = cursor.getColumnIndex("code");
        while (cursor.moveToNext()) {
            City city = new City();
            city.setId(cursor.getString(id));
            city.setName(cursor.getString(name));
            city.setCode(cursor.getString(code));
            list.add(city);
        }
        cursor.close();
        listView.setAdapter(new CityAdapter(list, this));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //将数据临时存储
                SQLTools.region.setCode(list.get(i).getCode());
                SQLTools.region.setName(list.get(i).getName());
                sendBroadcast(new Intent(SQLTools.REGIN_ACTION));
                SQLTools.pullStatckActivity(RegionActivity.this);
                SQLTools.clearStackActivity();
            }
        });
    }
}
