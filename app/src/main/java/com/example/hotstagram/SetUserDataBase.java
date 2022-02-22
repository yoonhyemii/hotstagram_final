package com.example.hotstagram;/*
package com.example.hotstagram;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SetUserDataBase {

    private final String TAG = SetUserDataBase.class.getSimpleName();
    FirebaseFirestore firebaseFirestore;
    Context context;

    public SetUserDataBase(Context context) {
        this.context = context;
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void SetUserDatabase(final String Email, final String Message, final String Name, final String NickName, final String ProfileURI, final String UID){

        final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //문서 값 전체 불러오기
        DocumentReference docRef = firebaseFirestore.collection("Inter_Test").document(firebaseUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "Document 데이터 존재: " + document.getData());
                    } else {
                        Log.e(TAG, "Document 데이터 없음");
                    }

                    String mUid = firebaseUser.getUid();
                    String fUid = (String) document.get("UID");

                    // 현재 로그인 하려는 사용자와 문서에 있는 사용자가 같은 경우 덮어쓰기 X
                    if(mUid.equals(fUid)) {
                        Log.e(TAG, " 동일한 사용자 존재 " + firebaseUser.getUid());

                        // 현재 로그인 하려는 사용자와 문서에 있는 사용자가 다를 경우 새로 생성
                    } else if(mUid == null){

                        Map<String, Object> setuserinfo = new HashMap<>();

                        setuserinfo.put("E-mail", Email);
                        setuserinfo.put("Message", Message);
                        setuserinfo.put("Name",Name);
                        setuserinfo.put("NickName", NickName);
                        setuserinfo.put("ProfileURI", ProfileURI);
                        setuserinfo.put("UID",UID);

                     */
/*   final Map<String, Object> Mapuser = new HashMap<>();
                        Mapuser.put("Name", firebaseUser.getDisplayName());
                        Mapuser.put("UID", firebaseUser.getUid());
                        Mapuser.put("E-mail", firebaseUser.getEmail());
                        Mapuser.put("Message", null);
                        Mapuser.put("NickName", null);
                        Mapuser.put("ProfileURI", null);*//*


                        firebaseFirestore.collection("Inter_Test").document(firebaseUser.getUid())
                                .set(setuserinfo)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.e(" 로그인 시 DB 저장 성공  ", "" );
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });




      */
/*  firebaseFirestore.collection("Inter_Test").document(UID)
                .set(setuserinfo)
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
                });*//*

    }
}
*/
