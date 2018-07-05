package com.example.shrikanthravi.outpassgenerator;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Shrikanth Ravi on 06-03-2017.
 */

public class CustomList1 extends ArrayAdapter<String> {
    private String[] names;
    private String[] desc;
    private String[] year;
    private Integer[] imageid;
    private Activity context;

    public CustomList1(Activity context, String[] names, String[] desc, String[] year,Integer[] imageid) {
        super(context,R.layout.list_layout, names);
        this.context = context;
        this.names = names;
        this.desc = desc;
        this.year = year;
        this.imageid = imageid;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewDesc = (TextView) listViewItem.findViewById(R.id.textViewDesc);
        TextView textViewYear = (TextView) listViewItem.findViewById(R.id.textViewYear);
        ImageView image = (ImageView) listViewItem.findViewById(R.id.imageView);

        textViewName.setText(names[position]);
        textViewDesc.setText(desc[position]);
        textViewYear.setText(year[position]);
        image.setImageResource(imageid[position]);
        return  listViewItem;
    }
}