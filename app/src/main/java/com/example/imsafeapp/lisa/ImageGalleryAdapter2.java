package com.example.imsafeapp.lisa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imsafeapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImageGalleryAdapter2 extends RecyclerView.Adapter<announceViewHolder> {

    private List<announceData> list = Collections.emptyList();
    private Context context;
    private ClickListener listener;



    public ImageGalleryAdapter2(List<announceData> list, Context context,ClickListener listener)
    {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public announceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the layout
        View photoView = inflater.inflate(R.layout.announce_card, parent, false);

        return new announceViewHolder(photoView);
    }


    @Override
    public void onBindViewHolder(@NonNull announceViewHolder viewHolder, @SuppressLint("RecyclerView") final int position)
    {
        //final int index = viewHolder.getAdapterPosition();
        viewHolder.announceName.setText(list.get(position).name);
        viewHolder.announceDate.setText(list.get(position).date);
        viewHolder.announceTime.setText(list.get(position).time);
        viewHolder.announceLocation.setText(list.get(position).location);


        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                listener.click(position);
            }
        });
    }


    @Override
    public int getItemCount()
    {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }

}