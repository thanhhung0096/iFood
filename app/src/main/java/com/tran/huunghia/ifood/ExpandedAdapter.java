package com.tran.huunghia.ifood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by eddy102 on 04/05/2018.
 */

public class ExpandedAdapter extends BaseAdapter {
    Context context;
    String header[];
    String content[];
    LayoutInflater inflater;

    public ExpandedAdapter(Context context,String header[], String content[]){
        this.context = context;
        this.header = header;
        this.content = content;
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return header.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.view_row,null);
        TextView tvHeader = (TextView) convertView.findViewById(R.id.header_text);
        TextView tvContent = (TextView) convertView.findViewById(R.id.content_text);
        tvHeader.setText(header[position]);
        tvContent.setText(content[position]);
        return convertView;
    }
}
