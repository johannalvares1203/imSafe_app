package com.example.imsafeapp.lisa;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.imsafeapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class Fragment2 extends Fragment {

    private ImageGalleryAdapter2 adapter;
    private RecyclerView recyclerView;
    private ClickListener listener;
    private FloatingActionButton fab;
    private String name;
    private String date;
    private String time;
    private String location;

    private List<announceData> list = new ArrayList<>();

    public static Fragment2 newInstance() {
        return new Fragment2();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_2, container, false);

        // Find RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);

        // Set up RecyclerView
        list = getData(); // Assuming getData() is a method to get your data
        listener = new ClickListener() {
            @Override
            public void click(int index) {
                Toast.makeText(getActivity(), "clicked item index is " + index, Toast.LENGTH_LONG).show();
            }
        };
        adapter = new ImageGalleryAdapter2(list, getActivity(), listener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Find FAB
        fab = view.findViewById(R.id.fab);

        // Set up FAB click listener
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.additem);
                final EditText editText1 = dialog.findViewById(R.id.editText1);
                final EditText editText2 = dialog.findViewById(R.id.editText2);
                final EditText editText3 = dialog.findViewById(R.id.editText3);
                final EditText editText4 = dialog.findViewById(R.id.editText4);
                Button btnUpload = dialog.findViewById(R.id.btnUpload);

                btnUpload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = editText1.getText().toString();
                        String date = editText2.getText().toString();
                        String time = editText3.getText().toString();
                        String location = editText4.getText().toString();

                        announceData lvItem = new announceData(name, date, time, location);
                        list.add(lvItem);

                        dialog.dismiss();

                        ImageGalleryAdapter2 contactAdapter = new ImageGalleryAdapter2(list, getActivity(), listener);
                        recyclerView.setAdapter(contactAdapter);
                    }
                });

                dialog.show();
            }
        });

        return view;
    }

    private List<announceData> getData() {
        List<announceData> list = new ArrayList<>();
        list.add(new announceData("Self Defense Course",
                "03 March 2024, Sunday",
                "4-6 p.m.",
                "Panjim Bus Stand"));
        list.add(new announceData("First Aid Certification Course",
                "25 February 2024, Sunday",
                "10 a.m.-12 noon",
                "Vasco Community Hall"));
        return list;
    }
}