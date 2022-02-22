package com.example.hotstagram.ui.user.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.hotstagram.R;
import com.example.hotstagram.ui.user.adapter.UserGridViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {
    private final String TAG = ProfileFragment.class.getSimpleName();

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    GridView userGridView;
    ArrayList<QueryDocumentSnapshot> sendDocument;
    private UserGridViewAdapter userGridViewAdapter;
    ArrayList<String> sUid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user_profile, container, false);


        //현재 사용자
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


        // GridView 적용
        userGridView = root.findViewById(R.id.user_gridview);
        userGridViewAdapter = new UserGridViewAdapter();

        //데이터베이스 가져오기
        firebaseFirestore = FirebaseFirestore.getInstance();
        sendDocument = new ArrayList<>();
        firebaseFirestore.collection("Testt")
                .orderBy("postNum").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //문서에 있는 값 모드 가져오기
                        sendDocument.add(0, document);
                    }
                    getImg(sendDocument);

                } else {
                    Log.d("가져오기 실패", "Error getting documents: ", task.getException());
                }
            }
        });

        return root;
    }

    public void getImg(final ArrayList<QueryDocumentSnapshot> queryDocumentSnapshots) {

        sUid = new ArrayList<>();
        for (int getImgCount = 0; getImgCount < queryDocumentSnapshots.size(); getImgCount++) {

            String uid = queryDocumentSnapshots.get(getImgCount).get("getUid").toString();
            sUid.add(uid);
            if (sUid.get(getImgCount).equals(firebaseUser.getUid())) {

                String getimguri = queryDocumentSnapshots.get(getImgCount).get("getImgUri").toString().substring(1,57);
                Log.e(TAG, getImgCount + "번째 Photo URI 값 : " + getimguri);
                userGridViewAdapter.addItem(getimguri);
                userGridView.setAdapter(userGridViewAdapter);

            }
        }

        //Fragment userFragment = new UserFragment();
        //Fragment profileFragment = new ProfileFragment();

      /*  int cnt = userGridViewAdapter.getCount();
        String post = String.valueOf(cnt);

        Log.e(TAG, " Count : " + cnt);

        //fragment 생성
        UserFragment fragment = new UserFragment();

        getFragmentManager().beginTransaction().replace(R.id.request_user , fragment).commit();

        Bundle bundle = new Bundle(1);
        bundle.putString("postCount", post);

       // FragmentManager fm = getFragmentManager();
       // FragmentTransaction fmt= fm.beginTransaction();
        fragment.setArguments(bundle);
       // fmt.replace(R.id.fragmentLayout, ).addToBackStack(null).commit();

*/
    }
    /*
    public int getPostCount(){
        int cnt = userGridViewAdapter.getCount();
        return cnt;
    }*/
}



