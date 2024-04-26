package com.example.imsafeapp.lisa;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imsafeapp.R;

public class announceViewHolder extends RecyclerView.ViewHolder {
    TextView announceName;
    TextView announceDate;
    TextView announceTime;
    TextView announceLocation;
    View view;

    announceViewHolder(View itemView)
    {
        super(itemView);
        announceName
                = (TextView)itemView
                .findViewById(R.id.announceName);
        announceDate
                = (TextView)itemView
                .findViewById(R.id.announceDate);
        announceTime
                = (TextView)itemView
                .findViewById(R.id.announceTime);
        announceLocation
                = (TextView)itemView
                .findViewById(R.id.announceLocation);

        view = itemView;
    }


}

