package com.example.imsafeapp.lisa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = layoutInflater.inflate(R.layout.lvitem, viewGroup, false);
        }

        LvItem lvItem = arrayList.get(i);

        TextView textView = view.findViewById(R.id.text);
        textView.setText(lvItem.getName());

        ImageView imageView = view.findViewById(R.id.imageView);
        if (lvItem.getImage() != null) {
            imageView.setImageDrawable(lvItem.getImage());
        } else {
            // If no image is set, you can optionally set a placeholder image
            imageView.setImageResource(R.drawable.ic_baseline_calendar);
        }

        return view;
    }


}
