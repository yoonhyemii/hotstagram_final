package com.example.hotstagram.ui.basic;

import android.app.Activity;
import android.content.ContentResolver;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.hotstagram.MainActivity;
import com.example.hotstagram.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class BasicFragment extends Fragment {

    private final static String TAG = BaseAdapter.class.getSimpleName();

    Toolbar toolbar;
    View include;

    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    ArrayList<QueryDocumentSnapshot> sendDocument1;
    ArrayList<QueryDocumentSnapshot> sendDocument2;

    private ListView listView;
    private  ListViewAdapter listViewAdapter;
    ArrayList<String> sUid;
    ArrayList<String> sUri;
    ArrayList<String> sName;
    ArrayList<String> sPosturi;
    ArrayList<String> sInfo;
    ArrayList<Long> sTime;
    ArrayList<String> sMsg;
    long regTime;

    private static class TIME_MAXIMUM {
        public static final int SEC = 60;
        public static final int MIN = 60;
        public static final int HOUR = 24;
        public static final int DAY = 30;
        public static final int MONTH = 12;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_basic, container, false);
        include = root.findViewById(R.id.toolbar);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //툴바
        toolbar = include.findViewById(R.id.toolbar);
        MainActivity activity = (MainActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("");

        listViewAdapter = new ListViewAdapter(getContext());

        listView = root.findViewById(R.id.basic_listView);

        //데이터베이스 가져오기
        firebaseFirestore = FirebaseFirestore.getInstance();
        sendDocument1 = new ArrayList<>();
        sendDocument2 = new ArrayList<>();

        firebaseFirestore.collection("Testt")
                .orderBy("postNum").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //문서에 있는 값 모드 가져오기
                        sendDocument1.add(0, document);
                    }
                    getImg(sendDocument1);
                } else {
                    Log.d("가져오기 실패", "Error getting documents: ", task.getException());
                }
            }
        });

        return root;
    }


    public void getImg(final ArrayList<QueryDocumentSnapshot> queryDocumentSnapshots) {

        sName = new ArrayList<>();
        sPosturi = new ArrayList<>();
        sInfo = new ArrayList<>();
        sTime = new ArrayList<>();
        sMsg = new ArrayList<>();
        sUid = new ArrayList<>();
        sUri = new ArrayList<>();

        //문서에서 필드 getImgUri에 해당하는 값 getimguri에 넣기
        for (int getImgCount = 0; getImgCount < queryDocumentSnapshots.size(); getImgCount++) {
            String name = queryDocumentSnapshots.get(getImgCount).get("name").toString();
            sName.add(name);
            String posturi = queryDocumentSnapshots.get(getImgCount).get("getImgUri").toString().substring(1,57);;
            sPosturi.add(posturi);

            long time = Long.parseLong(queryDocumentSnapshots.get(getImgCount).get("PostTime").toString());
            Log.e(TAG, "게시물 올린 시간 : " + getImgCount + " : " + time);
            long curTime = System.currentTimeMillis();
            long diffTime = (curTime - time) / 1000;
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
            Log.e(TAG, "게시물 올린 시간 : " + getImgCount + " : " + diffTime);
            sTime.add(time);
            sMsg.add(msg);

            String uid = queryDocumentSnapshots.get(getImgCount).get("getUid").toString();
            sUid.add(uid);
            sUri.add("");
        }


        firebaseFirestore.collection("Inter_Test")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //문서에 있는 값 모드 가져오기
                        for(int i=0;i<sUid.size();i++){
                            if(sUid.get(i).equals(document.get("UID"))){
                            /*    Log.e(TAG, "sUid get(" + i + ") 값 : " + sUid.get(i));
                                Log.e(TAG, "UID 값 : " + document.get("UID"));*/
                                if(document.get("ProfileURI")!=null) {
                                    sUri.set(i, document.get("ProfileURI").toString());
                                    // Log.e(TAG,"sUid : "+ sUid.get(i) + "\nsUri : " + sUri.get(i));
                                }
                            }
                        }
                    }

                    for (int i = 0; i < sName.size(); i++) {
                        // Log.e("sUid",sUid.get(i) + "\nsUri : " + sUri.get(i));
                       /* if(sUri.get(i) == null){
                            sUri.get(i) =
                        }*/
                        listViewAdapter.addItem(sName.get(i), sUri.get(i) , sMsg.get(i), sPosturi.get(i));
                        listView.setAdapter(listViewAdapter);
                    }

                } else {
                    Log.d("가져오기 실패", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void getImageList(Activity activity, String path) {
        Log.e("firstImagePath", path + "");
    }

}