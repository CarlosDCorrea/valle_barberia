package com.example.vallebarberia.myAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.vallebarberia.R;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<String> {

    Activity context;
    ArrayList<String> list_ids;
    ArrayList<String> list_names;
    ArrayList<String> list_type_cuts;
    ArrayList<String> list_value_cuts;
    ArrayList<String> list_times;

    public MyAdapter(Activity c, ArrayList<String> list_ids,
                     ArrayList<String> list_names,
                     ArrayList<String> list_type_cuts,
                     ArrayList<String> list_value_cuts,
                     ArrayList<String> list_times){
        super(c, R.layout.list_row, list_ids);

        this.context = c;
        this.list_ids = list_ids;
        this.list_names = list_names;
        this.list_type_cuts = list_type_cuts;
        this.list_value_cuts = list_value_cuts;
        this.list_times = list_times;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_row, null, true);

        TextView et_id = (TextView) rowView.findViewById(R.id.et_id);
        TextView et_name = (TextView) rowView.findViewById(R.id.et_name);
        TextView et_cut_type = (TextView) rowView.findViewById(R.id.et_cut_type);
        TextView et_cut_value = (TextView) rowView.findViewById(R.id.et_cut_value);
        TextView et_time = (TextView) rowView.findViewById(R.id.et_time);

        et_id.setText(list_ids.get(position));
        et_name.setText(list_names.get(position));
        et_cut_type.setText(list_type_cuts.get(position));
        et_cut_value.setText(list_value_cuts.get(position));
        et_time.setText(list_times.get(position));


        return rowView;
    }
}
