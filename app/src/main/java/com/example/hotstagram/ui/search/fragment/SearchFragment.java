package com.example.hotstagram.ui.search.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.hotstagram.R;
import com.example.hotstagram.ui.search.GridViewItem;
import com.example.hotstagram.ui.search.adapter.GridViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private final static String REFERENCE_URL = "gs://hotstagram-cd509.appspot.com";

    Toolbar toolbar;
    ActionBar actionBar;
    ViewGroup rootView;

    GridView gridView;
    GridViewAdapter gridViewAdapter;

    FirebaseFirestore firebaseFirestore;
    ArrayList<QueryDocumentSnapshot> sendDocument;
    GridViewItem gridViewItem;



    ImageView imageView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup)inflater.inflate(R.layout.fragment_search, container, false);

        /*//툴바
        View view = (View) inflater.inflate(R.layout.fragment_home, container, false);
        MainActivity activity = (MainActivity) getActivity();
        toolbar = view.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);*/

        //tab
        EditText et_search = rootView.findViewById(R.id.et_search);
        et_search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ConstraintLayout constraintLayout = rootView.findViewById(R.id.tab_fragmentLayout);
                constraintLayout.setVisibility(View.VISIBLE);
                gridView.setVisibility(View.INVISIBLE);
                getFragmentManager().beginTransaction().replace(R.id.tab_fragmentLayout, new TablayoutFragment() ).commit();
                return false;
            }
        });


        //그리드뷰
        gridView = rootView.findViewById(R.id.gridview);
        gridViewAdapter = new GridViewAdapter();

       /* gridViewAdapter.addItem(ContextCompat.getDrawable(getActivity(),R.drawable.eximg));
        gridViewAdapter.addItem(ContextCompat.getDrawable(getActivity(),R.drawable.eximg));
        gridViewAdapter.addItem(ContextCompat.getDrawable(getActivity(),R.drawable.eximg));*/

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


        return rootView;
    }

    public void getImg(final ArrayList<QueryDocumentSnapshot> queryDocumentSnapshots) {

        //문서에서 필드 getImgUri에 해당하는 값 getimguri에 넣기
        for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
            String getimguri = queryDocumentSnapshots.get(i).get("getImgUri").toString().substring(1,57);
            Log.e("getimguri" + i, getimguri + "");

            gridViewAdapter.addItem(getimguri);
            gridView.setAdapter(gridViewAdapter);
        }



    }

}


