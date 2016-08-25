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

public class ProvinceActivity extends AppCompatActivity {
    private ListView listView;
    public static final String PROVINCE_SQL = "select * from province where  id = ";
    public static final String CITY_SQL = "select * from city where id =";
    public static final String Region_SQL = "select * from quxian where id = ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String _id = getIntent().getStringExtra("id");
        setContentView(R.layout.activity_province);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_province);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.listview_province);

        final List<City> list = new ArrayList<>();
        SQLiteDatabase sql = SQLTools.opendatabase(this);
        Cursor cursor = sql.rawQuery(PROVINCE_SQL + _id, null);
        int id = cursor.getColumnIndex("id");
        int name = cursor.getColumnIndex("name");
        int cid = cursor.getColumnIndex("cid");
        int code = cursor.getColumnIndex("code");
        Cursor cursor_next;
        while (cursor.moveToNext()) {
            if (cursor.getString(name).equals("0")) {//没有省份的直接跳区县
                cursor = sql.rawQuery(CITY_SQL + cursor.getString(cid), null); //将省份的id去匹配城市列表
                cursor.moveToNext();//移动到表头
                cursor_next = sql.rawQuery(Region_SQL + cursor.getString(cid), null);//判断是否还有区县
            } else {
                cursor_next = sql.rawQuery(CITY_SQL + cursor.getString(cid), null);//判断是否还有城市
            }

            City city = new City();

            if (cursor_next.moveToNext()) {//判断这个国家是否有省份
                city.setNext(true);
            } else {
                city.setNext(false);
            }
            city.setCode(cursor.getString(code));
            city.setId(cursor.getString(id));
            city.setName(cursor.getString(name));
            city.setCid(cursor.getString(cid));
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
                SQLTools.province.setCode(list.get(i).getCode());
                SQLTools.province.setName(list.get(i).getName());
                SQLTools.pullStatckActivity(ProvinceActivity.this);
                if (next) {
                    Intent intent = new Intent(ProvinceActivity.this, CityActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }else{
                    sendBroadcast(new Intent(SQLTools.PROVINCE_ACTION));
                    SQLTools.clearStackActivity();

                }
            }
        });
    }
}
