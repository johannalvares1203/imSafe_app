package com.example.imsafeapp.lisa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.imsafeapp.R;

import java.util.ArrayList;

public class ContactAdapter extends BaseAdapter {
    ArrayList<LvItem> arrayList;
    Context context;
    LayoutInflater layoutInflater;

    public ContactAdapter(ArrayList<LvItem> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") View view1= layoutInflater.inflate(R.layout.lvitem,viewGroup,false);
        TextView msg = view1.findViewById(R.id.text);

        msg.setText(arrayList.get(i).getName());
        return view1;
    }


}
