package com.example.hotstagram;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SetPostDataBase {
    FirebaseFirestore firebaseFirestore;
    Context context;


    public SetPostDataBase(Context context) {
        this.context = context;
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void SetPostDatabase(Long time, String postNum, String getUid, String name,  ArrayList<String> ImgUri, String letter, ArrayList<String> likelist, ArrayList<String> commant){

        Map<String, Object> setmapuserimg = new HashMap<>();

        setmapuserimg.put("PostTime", time);
        setmapuserimg.put("postNum", postNum);
        setmapuserimg.put("getUid", getUid);
        setmapuserimg.put("name",name);
        setmapuserimg.put("getImgUri", ImgUri);
        setmapuserimg.put("letter", letter);
        setmapuserimg.put("likeList",likelist);
        setmapuserimg.put("commantList",commant);


        firebaseFirestore.collection("Testt").document(getUid+ "_" + postNum)
                .set(setmapuserimg)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e("성공 : ", "" + FirebaseAuth.getInstance().getCurrentUser());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }



}