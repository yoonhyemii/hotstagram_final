package com.example.hotstagram.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.hotstagram.R;

public class CameraFragment extends Fragment {

    public CameraFragment(){

    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_main_camera, container, false);
        TextView textView = root.findViewById(R.id.textView);
        textView.setText("sadsadasd");

        return root;
    }
}