package com.example.hotstagram.ui.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotstagram.R;

public class InformationActivity extends AppCompatActivity {

    Button btn_access;
    Button btn_privacy;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_information);

        btn_access = (Button)findViewById(R.id.btn_push);
        btn_privacy = (Button)findViewById(R.id.btn_privacy);

        btn_access.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), AccessActivity.class);
                startActivity(intent);
            }
        });

        btn_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), PrivacyActivity.class);
                startActivity(intent);
            }
        });
    }
}
