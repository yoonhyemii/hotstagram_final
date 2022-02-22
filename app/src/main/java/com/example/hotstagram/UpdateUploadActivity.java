package com.example.hotstagram;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hotstagram.util.GlideApp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class UpdateUploadActivity extends AppCompatActivity {

    ImageView update_select_img;
    Intent intent;
    String getimg;
    String getname;
    String getTime;
    String getNum;
    String getUid;
    int pos;
    EditText update_et_letter;

    FirebaseStorage storage;
    StorageReference storageRef;

    String updatetext;

    FirebaseFirestore firebaseFirestore;
    ArrayList<QueryDocumentSnapshot> sendDocument;

    View include;
    ImageView iv_cancle;
    ImageView iv_complete;
    CircleImageView ivupdateprofile;
    TextView tvupdatename;
    TextView tvupdatetime;

    FirebaseUser firebaseUser;
    StorageReference storageRefpr2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_update);


        update_select_img = findViewById(R.id.update_select_img);
        ivupdateprofile = findViewById(R.id.iv_updateprofile);
        tvupdatename = findViewById(R.id.tv_updatename);
        tvupdatetime = findViewById(R.id.tv_updatetime);

        include = findViewById(R.id.upload_update_toolbar);
        iv_cancle = include.findViewById(R.id.iv_cancle);
        iv_complete = include.findViewById(R.id.iv_complete);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        intent = getIntent();
        getimg = intent.getExtras().getString("setimg").substring(1, intent.getExtras().getString("setimg").length() - 1);
        getname = intent.getExtras().getString("setname");
        getTime = intent.getExtras().getString("settime");
        pos = intent.getExtras().getInt("setpos");
        getNum = intent.getExtras().getString("setNum");
        getUid = intent.getExtras().getString("setUid");

        String[] splitcommant = getimg.split(",");

        //게시물사진
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://hotstagram-cd509.appspot.com/"+splitcommant[0]);
        GlideApp.with(getApplicationContext()).load(storageRef).into(update_select_img);

        //게시물 이름, 시간
        tvupdatename.setText(getname);
        tvupdatetime.setText(getTime);

        //프로필 이미지
        firebaseFirestore.collection("Inter_Test").document(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                String myimguri = documentSnapshot.get("ProfileURI").toString();
                storageRefpr2 = storage.getReferenceFromUrl("gs://hotstagram-cd509.appspot.com/" + myimguri);
                GlideApp.with(getApplicationContext()).load(storageRefpr2).error(R.drawable.basic_fill).into(ivupdateprofile);
            }
        });

        //취소
        iv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //업데이트
        iv_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseFirestore = FirebaseFirestore.getInstance();
                sendDocument = new ArrayList<>();
                update_et_letter = findViewById(R.id.update_et_letter);
                updatetext = update_et_letter.getText().toString();

                GetPostDataBase getPostDataBase = new GetPostDataBase(getApplicationContext());
                getPostDataBase.UpdataPostDataBase(getUid+"_" + getNum,updatetext);
                finish();


            }
        });


    }

}
