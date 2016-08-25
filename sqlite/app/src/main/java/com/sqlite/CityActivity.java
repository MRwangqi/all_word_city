package com.sqlite;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class CityActivity extends AppCompatActivity {
    public static final String Region_SQL = "select * from quxian where id = ";
    public static final String CITY_SQL = "select * from city where id =";
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        String _id = getIntent().getStringExtra("id");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_city);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.listview_city);

        final List<City> list = new ArrayList<>();
        SQLiteDatabase sql = SQLTools.opendatabase(this);
        Cursor cursor = sql.rawQuery(CITY_SQL + _id, null);
        int id = cursor.getColumnIndex("id");
        int name = cursor.getColumnIndex("name");
        int cid = cursor.getColumnIndex("cid");
        int code = cursor.getColumnIndex("code");
        Cursor cursor_next;
        while (cursor.moveToNext()) {
            City city = new City();
            city.setId(cursor.getString(id));
            city.setName(cursor.getString(name));
            city.setCid(cursor.getString(cid));
            city.setCode(cursor.getString(code));
            cursor_next = sql.rawQuery(Region_SQL + cursor.getString(cid), null);
            if (cursor_next.moveToNext()) {//判断这个城市是否还有区县
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
                String id = list.get(i).getCid();
                boolean next = list.get(i).isNext();
                //将数据临时存储
                SQLTools.city.setCode(list.get(i).getCode());
                SQLTools.city.setName(list.get(i).getName());
                SQLTools.pullStatckActivity(CityActivity.this);
                if (next) {
                    Intent intent = new Intent(CityActivity.this, RegionActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                } else {
                    sendBroadcast(new Intent(SQLTools.CITY_ACTION));
                    SQLTools.clearStackActivity();
                }
            }
        });
    }
}
