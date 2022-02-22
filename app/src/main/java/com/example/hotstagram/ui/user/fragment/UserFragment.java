package com.example.hotstagram.ui.user.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.hotstagram.LoginActivity;
import com.example.hotstagram.MainActivity;
import com.example.hotstagram.R;
import com.example.hotstagram.ui.home.HomeFragment;
import com.example.hotstagram.ui.user.activity.AdvertisementActivity;
import com.example.hotstagram.ui.user.activity.InformationActivity;
import com.example.hotstagram.ui.user.activity.NotificationActivity;
import com.example.hotstagram.ui.user.activity.ProfileModifyActivity;
import com.example.hotstagram.ui.user.adapter.UserGridViewAdapter;
import com.example.hotstagram.util.GlideApp;
import com.facebook.login.LoginManager;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class UserFragment extends Fragment {

    private static final String TAG = UserFragment.class.getSimpleName();
    private final String REFERENCE_URL = "gs://hotstagram-cd509.appspot.com";

    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    StorageReference storageRef;
    private FirebaseAuth auth; // 파이어베이스 인증 객체
    FirebaseStorage storage;

    Fragment profileFragment;
    UserGridViewAdapter userGridViewAdapter;
    ArrayList<QueryDocumentSnapshot> sendDocument;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    TextView toolbar_user_name;
    TextView nav_user_name;
    TextView user_message;
    TextView user_name;
    TextView post_num;
    TextView user_bio;
    ImageView user_profile_add;
    ImageView nav_user_photo;
    ImageView user_Profile;
    ImageView user_profile;
    Button userButton;
    Context context;
    Toolbar toolbar;
    Intent intent;
    View include;
    View header;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user, container, false);

        getFragmentManager().beginTransaction().replace(R.id.tab_fragment_user, new UserTablayoutFragment()).commit();

        include = root.findViewById(R.id.include);
        context = container.getContext();

        profileFragment = new ProfileFragment();

        //Firebase 로그인한 사용자 정보
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        drawerLayout = root.findViewById(R.id.drawer_setting);
        navigationView = root.findViewById(R.id.navigation_setting);
        user_Profile = root.findViewById(R.id.user_profile);
        header = navigationView.getHeaderView(0);
        nav_user_photo = header.findViewById(R.id.nav_user_photo);

        user_profile = include.findViewById(R.id.user_profile);
        user_profile_add = include.findViewById(R.id.user_profile_add);
        nav_user_name = header.findViewById(R.id.nav_user_name);
        toolbar_user_name = root.findViewById(R.id.toolbar_user_name);
        user_name = root.findViewById(R.id.tv_user_name);
        user_message = root. findViewById(R.id.tv_bio);
        post_num = root.findViewById(R.id.post_num);
        user_bio = root.findViewById(R.id.tv_bio);


        nav_user_name.setText(firebaseUser.getDisplayName());
        toolbar_user_name.setText(firebaseUser.getDisplayName());
        user_name.setText(firebaseUser.getDisplayName());

        // 툴바
        toolbar = include.findViewById(R.id.toolbar);
        MainActivity activity = (MainActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("");

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //문서 값 전체 불러오기
        DocumentReference docRef = firebaseFirestore.collection("Inter_Test").document(firebaseUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                String mNickName;
                String mMessage;
                String mName;
                String mBio = "User Statement";

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        mName = (String)document.get("Name");
                        if(document.get("NickName") != null) {
                            mNickName = (String) document.get("NickName");
                        }else{
                            mNickName = (String) document.get("Name");
                        }
                        mMessage = (String)document.get("Message");
                        if(document.get("ProfileURI") != null) {
                            Log.d("ProfileUri",document.get("ProfileURI")+" ");
                            storage = FirebaseStorage.getInstance();
                            storageRef = storage.getReferenceFromUrl("gs://hotstagram-cd509.appspot.com/"+document.get("ProfileURI"));
                            GlideApp.with(context).load(storageRef).into(user_profile);
                            GlideApp.with(context).load(storageRef).into(nav_user_photo);
                        }

                        user_name.setText(mName);
                        nav_user_name.setText(mNickName);
                        toolbar_user_name.setText(mNickName);
                        user_message.setText(mMessage);
                        user_bio.setText(mBio);

                    } else {
                        Log.e(TAG, "Document 데이터 없음");
                    }
                }
            }
        });

        actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // 네비게이션 드로어
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                //  intent = getActivity().getIntent();
                switch (menuItem.getItemId())
                {
                    case R.id.notification:
                        Toast.makeText(context, "공지창입니다.", Toast.LENGTH_SHORT).show();
                        intent= new Intent(getApplicationContext(), NotificationActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.information:
                        Toast.makeText(context, "정보창입니다.", Toast.LENGTH_SHORT).show();
                        intent = new Intent(getApplicationContext(), InformationActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.advertising:
                        Toast.makeText(context, "광고창입니다.", Toast.LENGTH_SHORT).show();
                        intent = new Intent(getApplicationContext(), AdvertisementActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.logout:
                        // FirebaseAuth.getInstance().signOut();
                        auth.signOut();
                        LoginManager.getInstance().logOut();
                        Toast.makeText(context, "로그아웃.", Toast.LENGTH_SHORT).show();
                        intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        break;

                    default: return true;
                }

                DrawerLayout drawer = getActivity().findViewById(R.id.drawer_setting);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        // 프로필 수정 버튼
        userButton = include.findViewById(R.id.user_button);
        userButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), ProfileModifyActivity.class);
                startActivity(intent);
            }
        });

        // 프로필 사진 클릭 시 동작
        user_profile_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivity(intent);
            }
        });
        user_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivity(intent);
            }
        });

      /*  Bundle bundle = getArguments();
        if(bundle != null) {
            String count = bundle.getString("postCount");
            post_num.setText(count);

        }*/

        return root;
    }
/*

    public int count(){
        int cnt = ((ProfileFragment) getFragmentManager().findFragmentByTag("profileFragment")).getPostCount();

        return cnt;
    }
*/

    public void userrefresh(){
        Log.d("fragment","refresh");
        assert getFragmentManager() != null;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

}