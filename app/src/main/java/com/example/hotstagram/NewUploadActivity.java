package com.example.hotstagram;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class NewUploadActivity extends AppCompatActivity {

    Intent intent;

    int GALLERY_CODE;
    /* private Uri imguri;*/

    FirebaseStorage firebaseStorage;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    StorageReference storageReference;

    ImageView select_img;
    View include;
    ImageView iv_share;
    ImageView iv_cancle;
    EditText et_letter;

    Date now = new Date();
    ImageView ivprofile;
    ArrayList<String> imglist;

    long time = System.currentTimeMillis();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_new);

        include = findViewById(R.id.upload_toolbar);

        intent = getIntent();
        GALLERY_CODE = intent.getExtras().getInt("GALLERY_IMG");
        /*imguri = Uri.parse(intent.getExtras().getString("GALLERY_URI"));*/

        imglist = getIntent().getStringArrayListExtra("GALLERY_URI");

        for(int i=0;i<imglist.size();i++){
            Log.d("imglist",imglist.get(i));
        }

        select_img = findViewById(R.id.select_img);
        ivprofile = findViewById(R.id.iv_profile);

        if(GALLERY_CODE == 100){
            select_img.setImageURI(Uri.parse(imglist.get(0)));
            //select_img.setImageURI(imguri);
        }


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //공유 클릭
        iv_share = include.findViewById(R.id.iv_share);

        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> getimglist = new ArrayList<>();
                for (int i = 0; i < imglist.size(); i++) {
                    String imgname = "upload/" + firebaseUser.getUid()+ "_" + formatter.format(now)+ "_" + i + ".png";
                    getimglist.add(imgname);
                }
                setFirebaseStorage(getimglist,imglist);
                setDataBase(getimglist);
            }
        });

        iv_cancle = include.findViewById(R.id.iv_cancle);
        iv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "취소", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


    }


    public void setFirebaseStorage(ArrayList<String> imgname, ArrayList<String> putimguri) {

        firebaseStorage = FirebaseStorage.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //firebaseStorage 이미지 업로드

        for(int i=0; i<imgname.size(); i++) {
            storageReference = firebaseStorage.getReferenceFromUrl("gs://hotstagram-cd509.appspot.com/")
                    .child(imgname.get(i));


            storageReference.putFile(Uri.parse(putimguri.get(i))).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                    ((MainActivity) MainActivity.mainContext).onResume();
                    finish();
                }
            }).addOnCanceledListener(new OnCanceledListener() {
                @Override
                public void onCanceled() {
                    Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }


    }



    public void setDataBase(ArrayList<String> imgname){
        // firebaseFirestore 아이디랑 이미지 주소 저장
        final SetPostDataBase setPostDataBase = new SetPostDataBase(getApplicationContext());
        et_letter = findViewById(R.id.et_letter);
        String letter = et_letter.getText().toString();
        ArrayList<String> arraylikeList = new ArrayList<>();
        ArrayList<String> arraycommantList = new ArrayList<>();

        setPostDataBase.SetPostDatabase(time, formatter.format(now), firebaseUser.getUid(),firebaseUser.getDisplayName(), imgname ,letter,arraylikeList,arraycommantList);

        //setPostDataBase.SetPostDatabase(formatter.format(now), firebaseUser.getUid(),firebaseUser.getDisplayName(),"upload/" + firebaseUser.getUid() + "_" + filename,letter,arraylikeList);
    }


}