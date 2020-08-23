package com.example.s6.ui.notifications;


import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.s6.R;
import com.github.gcacace.signaturepad.views.SignaturePad;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.sql.Blob;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {

    SignaturePad signaturePad;
    Button saveButton, clearButton;
    Drawable drawable;
    public BlankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_blank, container, false);
        signaturePad = (SignaturePad)root.findViewById(R.id.signaturePad);
        saveButton = (Button)root.findViewById(R.id.saveButton);
        clearButton = (Button)root.findViewById(R.id.clearButton);

        //disable both buttons at start
        saveButton.setEnabled(false);
        clearButton.setEnabled(false);


        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                saveButton.setEnabled(true);
                clearButton.setEnabled(true);
                Bitmap bitmapTrimmed = signaturePad.getTransparentSignatureBitmap();
                drawable = new BitmapDrawable(getResources(), bitmapTrimmed);
            }

            @Override
            public void onClear() {
                saveButton.setEnabled(false);
                clearButton.setEnabled(false);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //write code for saving the signature here

                Toast.makeText(getActivity(), "Signature Saved", Toast.LENGTH_SHORT).show();

            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signaturePad.clear();
            }
        });
        return root;
    }

}
