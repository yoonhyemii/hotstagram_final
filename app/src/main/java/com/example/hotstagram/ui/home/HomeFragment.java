package com.example.hotstagram.ui.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.hotstagram.GetPostDataBase;
import com.example.hotstagram.MainActivity;
import com.example.hotstagram.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    Context context;

    Toolbar toolbar;
    ActionBar actionBar;

    //데이터베이스
    FirebaseFirestore firebaseFirestore;
    FirebaseUser user;
    RecycleAdapter recycleAdapter;
    PostInfo postInfo = new PostInfo();
    ArrayList<QueryDocumentSnapshot> sendDocument;
    ArrayList<String> getPostNumList;
    ArrayList<String> getNameList ;
    ArrayList<Uri> getUriList ;
    ArrayList<String> getLetterList ;

    View include;
    SwipeRefreshLayout swipeRefreshLayout;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        //데이터베이스
        firebaseFirestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mRecyclerView = rootView.findViewById(R.id.recycler_view);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);

        recycleAdapter = new RecycleAdapter(getContext());
        mRecyclerView.setAdapter(recycleAdapter);


        sendDocument = new ArrayList<>();
        getPostNumList = new ArrayList<>();
        getNameList = new ArrayList<>();
        getUriList = new ArrayList<>();
        getLetterList = new ArrayList<>();

        swipeRefreshLayout = rootView.findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                homerefresh();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        //데이터베이스 가져온후 어댑처 등록
        GetPostDataBase getPostDataBase = new GetPostDataBase(getActivity());
        getPostDataBase.getPostDataBase(recycleAdapter);

        //툴바
        include = rootView.findViewById(R.id.toolbar);
        MainActivity activity = (MainActivity) getActivity();
        toolbar = include.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        actionBar = ((MainActivity) getActivity()).getSupportActionBar();

        //카메라
        ImageView camera = rootView.findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivity(intent);

            }
        });


        return rootView;

    }


    public void homerefresh(){
        Log.d("fragment","refresh");
        assert getFragmentManager() != null;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

}