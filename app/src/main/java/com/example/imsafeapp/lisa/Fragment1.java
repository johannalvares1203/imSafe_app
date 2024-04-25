package com.example.imsafeapp.lisa;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import android.graphics.Bitmap;
import android.net.Uri;

import android.provider.MediaStore;

import java.io.IOException;

import com.example.imsafeapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;


public class Fragment1 extends Fragment {
    private static final int PICK_IMAGE = 1;
    private ListView listView;
    private FloatingActionButton fab;
    private ArrayList<LvItem> arrayList = new ArrayList<>();
    private ImageView image;


    public static Fragment1 newInstance() {
        return new Fragment1();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_1, container, false);
        listView = view.findViewById(R.id.lv);
        fab = view.findViewById(R.id.fab);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up fab click listener
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(requireContext());
                dialog.setContentView(R.layout.fabitem);
                final EditText editTextText = dialog.findViewById(R.id.editTextText);
                Button btnUpload = dialog.findViewById(R.id.btnUpload);

                image = dialog.findViewById(R.id.Image);
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);

                        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
                    }
                });


                btnUpload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = editTextText.getText().toString();

                        LvItem lvItem = new LvItem();
                        lvItem.setName(name);
                        arrayList.add(lvItem);

                        dialog.dismiss();

                        ContactAdapter contactAdapter = new ContactAdapter(arrayList, requireContext());
                        listView.setAdapter(contactAdapter);
                    }
                });

                dialog.show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == getActivity().RESULT_OK) {
            if (data != null) {
                // Get the image URI
                final Uri imageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
                    image.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
