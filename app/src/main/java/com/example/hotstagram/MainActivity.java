package com.example.hotstagram;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.hotstagram.push.MyFirebaseInstanceIDService;
import com.example.hotstagram.ui.basic.BasicFragment;
import com.example.hotstagram.ui.home.HomeFragment;
import com.example.hotstagram.ui.search.fragment.SearchFragment;
import com.example.hotstagram.ui.upload.UploadFragment;
import com.example.hotstagram.ui.user.fragment.UserFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();

    Fragment fragment_home, fragment_search, fragment_basic, fragment_user;
    private static final int GALLERY_IMG = 10;
    Intent intent;
    BottomNavigationView navView;
    public static Context mainContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");*/
        mainContext = this;

        navView = findViewById(R.id.nav_view);
        fragment_home = new HomeFragment();
        fragment_search = new SearchFragment();
        fragment_basic = new BasicFragment();
        fragment_user = new UserFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, fragment_home).commitAllowingStateLoss();


        //bottom
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, fragment_home, getString(R.string.fragment_home)).commitAllowingStateLoss();
                        return true;
                    case R.id.navigation_search_bold:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, fragment_search, getString(R.string.fragment_search)).commitAllowingStateLoss();
                        return true;
                    case R.id.navigation_upload_01: {
                        Uploadimg();
                        //getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, new UploadFragment()).commitAllowingStateLoss();
                        return true;
                    }
                    case R.id.navigation_basic:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, fragment_basic, getString(R.string.fragment_basic)).commitAllowingStateLoss();
                        return true;
                    case R.id.navigation_user:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, fragment_user, getString(R.string.fragment_user)).commitAllowingStateLoss();
                        return true;

                    default:
                        return false;
                }
            }
        });

        // 디바이스 토큰 값 알아내기
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(MyFirebaseInstanceIDService.class.getSimpleName(), "getInstanceId failed", task.getException());
                            return;
                        }
                        try {
                            // Get new Instance ID token
                            String ttoken = FirebaseInstanceId.getInstance().getToken();
                            Log.d(TAG, "device token : " + ttoken);

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                });

        Intent intent = getIntent();
        if (intent != null) {//푸시알림을 선택해서 실행한것이 아닌경우 예외처리
            String notificationData = intent.getStringExtra("test");
            if (notificationData != null)
                Log.d("FCM_TEST", notificationData);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case GALLERY_IMG: {
                if (resultCode == RESULT_OK) {
                    ArrayList<String> imageList = new ArrayList<>();

                    if (data.getClipData() == null) {
                        Log.i("1. single choice", String.valueOf(data.getData()));
                        imageList.add(String.valueOf(data.getData()));
                    } else {
                        ClipData clipData = data.getClipData();
                        Log.i("clipdata", String.valueOf(clipData.getItemCount()));
                        if (clipData.getItemCount() > 10) {
                            Toast.makeText(MainActivity.this, "사진은 10개까지 선택가능 합니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // 멀티 선택에서 하나만 선택했을 경우
                        else if (clipData.getItemCount() == 1) {
                            String dataStr = String.valueOf(clipData.getItemAt(0).getUri());
                            Log.i("2. clipdata choice", String.valueOf(clipData.getItemAt(0).getUri()));
                            Log.i("2. single choice", clipData.getItemAt(0).getUri().getPath());
                            imageList.add(dataStr);

                        } else if (clipData.getItemCount() > 1 && clipData.getItemCount() < 10) {
                            for (int i = 0; i < clipData.getItemCount(); i++) {
                                Log.i("3. single choice", String.valueOf(clipData.getItemAt(i).getUri()));
                                imageList.add(String.valueOf(clipData.getItemAt(i).getUri()));
                            }
                        }
                    }

                    Intent intent = new Intent(getApplicationContext(), NewUploadActivity.class);
                    intent.putExtra("GALLERY_IMG", 100);
                    intent.putStringArrayListExtra("GALLERY_URI", imageList);
                    startActivity(intent);
                    navView.setSelectedItemId(R.id.navigation_home);
                    /*try {
                        Intent intent = new Intent(getApplicationContext(), NewUploadActivity.class);
                        intent.putExtra("GALLERY_IMG",100);
                        intent.putExtra("GALLERY_URI",data.getData().toString());
                        startActivity(intent);
                        navView.setSelectedItemId(R.id.navigation_home);
                    } catch (Exception e) {
                    }*/
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
                    navView.setSelectedItemId(R.id.navigation_home);
                }
            }
            break;
        }

    }

    public void Uploadimg() {
        intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, GALLERY_IMG);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("activity", "refresh");
        HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("fragment_home");
        UserFragment userFragment = (UserFragment) getSupportFragmentManager().findFragmentByTag("fragment_user");
        if(homeFragment!=null) {
            homeFragment.homerefresh();
        }else{
            Log.e("homeFragment","null");
        }

        if(userFragment!=null) {
            userFragment.userrefresh();
        }else{
            Log.e("userFragment","null");
        }

    }
}