package com.sqlite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2016/8/24.
 */
public class CityAdapter extends BaseAdapter {
    private Context context;
    private List<City> list;

    public CityAdapter(List<City> list, Context context) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tv = (TextView) convertView.findViewById(R.id.text_item);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.image_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        City city = list.get(position);
        viewHolder.tv.setText(city.getName());
        if (city.isNext()) {
            viewHolder.img.setVisibility(View.VISIBLE);
        } else {
            viewHolder.img.setVisibility(View.GONE);
        }
        return convertView;
    }


    class ViewHolder {
        TextView tv;
        ImageView img;
    }
}
