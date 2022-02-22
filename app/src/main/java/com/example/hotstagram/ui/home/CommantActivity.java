package com.example.hotstagram.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hotstagram.GetPostDataBase;
import com.example.hotstagram.R;
import com.example.hotstagram.ui.basic.BasicFragment;
import com.example.hotstagram.util.GlideApp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommantActivity extends AppCompatActivity {
    TextView tvpostletter;
    TextView tvpostname;
    Intent intent;
    String postletter;
    String postname;
    String proimg;
    CircleImageView ivprofile;
    CircleImageView ivmyprofile;

    String commant;
    ListView listView;
    int pos;
    String documentUid;

    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    FirebaseStorage storage;
    StorageReference storageRefpr;
    StorageReference storageRefpr2;

    EditText etcommant;
    View include;
    ImageView iv_cancle;
    TextView tvwrite;
    String getCommant;

    ArrayList<String> namelist;
    ArrayList<String> prourllist;
    ArrayList<String> commantlist;
    ArrayList<Long> commanttimelist;

    CommantListViewAdapter commantListViewAdapter;
    DocumentReference documentReference;



    long curTime = System.currentTimeMillis();

    private static class TIME_MAXIMUM {
        public static final int SEC = 60;
        public static final int MIN = 60;
        public static final int HOUR = 24;
        public static final int DAY = 30;
        public static final int MONTH = 12;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home_commant);

        namelist = new ArrayList<>();
        prourllist = new ArrayList<>();
        commantlist = new ArrayList<>();
        commanttimelist = new ArrayList<>();

        include = findViewById(R.id.home_commant_toolbar);
        tvpostletter = findViewById(R.id.postletter);
        tvpostname = findViewById(R.id.postname);
        ivprofile = findViewById(R.id.iv_commantprofile);
        listView = findViewById(R.id.commant_listView);
        etcommant = findViewById(R.id.et_commant);
        tvwrite = findViewById(R.id.tv_write);
        ivmyprofile = findViewById(R.id.iv_commantprofile2);
        iv_cancle = findViewById(R.id.iv_cancle);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        intent = getIntent();
        postletter = intent.getExtras().getString("postletter");
        postname = intent.getExtras().getString("postname");
        proimg = intent.getExtras().getString("proimg");
        commant = intent.getExtras().getString("commant");
        pos = intent.getExtras().getInt("pos");
        documentUid = intent.getExtras().getString("document");
        commantListViewAdapter = new CommantListViewAdapter(getApplicationContext());

        //댓글상단 등록
        tvpostletter.setText(postletter);
        tvpostname.setText(postname);
        storage = FirebaseStorage.getInstance();
        storageRefpr = storage.getReferenceFromUrl("gs://hotstagram-cd509.appspot.com/" + proimg);
        GlideApp.with(getApplicationContext()).load(storageRefpr).error(R.drawable.basic_fill).into(ivprofile);

        firebaseFirestore.collection("Inter_Test").document(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                String myimguri = documentSnapshot.get("ProfileURI").toString();
                storageRefpr2 = storage.getReferenceFromUrl("gs://hotstagram-cd509.appspot.com/" + myimguri);
                GlideApp.with(getApplicationContext()).load(storageRefpr2).error(R.drawable.basic_fill).into(ivmyprofile);
            }
        });

        //댓글보기
        documentReference = firebaseFirestore.collection("Testt").document(documentUid);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                final DocumentSnapshot document2 = task.getResult();
                if(document2.get("commantList") != null) {
                    String getcommants = document2.get("commantList").toString();

                    String[] splitcommant = getcommants.substring(1, getcommants.length() - 1).split(",");
                    for (int i = 0; i < splitcommant.length; i++) {
                        if (i % 4 == 0) { namelist.add(splitcommant[i]); }
                        if (i % 4 == 1) { prourllist.add(splitcommant[i]); }
                        if (i % 4 == 2) { commantlist.add(splitcommant[i]); }
                        if (i % 4 == 3) { commanttimelist.add(Long.parseLong(splitcommant[i])); }
                    }
                    if(commanttimelist.size()>0) {
                        for (int i = 0; i < namelist.size(); i++) {
                            long diffTime = (curTime - commanttimelist.get(i)) / 1000;
                            String msg = null;
                            if (diffTime < TIME_MAXIMUM.SEC) {
                                msg = "방금 전";
                            } else if ((diffTime /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN) {
                                msg = diffTime + "분 전";
                            } else if ((diffTime /= TIME_MAXIMUM.MIN) < TIME_MAXIMUM.HOUR) {
                                msg = (diffTime) + "시간 전";
                            } else if ((diffTime /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.DAY) {
                                msg = (diffTime) + "일 전";
                            } else if ((diffTime /= TIME_MAXIMUM.DAY) < TIME_MAXIMUM.MONTH) {
                                msg = (diffTime) + "달 전";
                            } else {
                                msg = (diffTime) + "년 전";
                            }
                            commantListViewAdapter.addItem(namelist.get(i), prourllist.get(i), commantlist.get(i), msg);
                            listView.setAdapter(commantListViewAdapter);
                        }
                    }
                }
            }
        });



        //댓글 등록
        tvwrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("클릭","클릭");
                getCommant = etcommant.getText().toString();

                firebaseFirestore.collection("Inter_Test").document(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Log.e("가져오기", "ㄱㄱ" + firebaseUser.getUid());
                        DocumentSnapshot document = task.getResult();
                        Log.e("documentName", document.getData().toString()+"");
                        Log.e("documentUri", document.get("ProfileURI").toString()+"");
                        Log.e("getCommant", getCommant+"");

                        commantListViewAdapter.addItem(document.get("NickName").toString(), document.get("ProfileURI").toString(), getCommant, "방금 전");

                        String commantAll = document.get("NickName").toString() + "," + document.get("ProfileURI").toString() + "," + getCommant +  "," + curTime;
                        GetPostDataBase getPostDataBase = new GetPostDataBase(getApplicationContext());
                        getPostDataBase.UpdataPostCommantDataBase(documentUid,commantAll);
                        listView.setAdapter(commantListViewAdapter);
                    }
                });
            }
        });

        iv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });




    }
}
