package com.example.hotstagram.ui.user.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.hotstagram.MainActivity;
import com.example.hotstagram.R;
import com.example.hotstagram.ui.user.UserGridItemView;
import com.example.hotstagram.ui.user.fragment.UserFragment;
import com.example.hotstagram.util.GlideApp;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ProfileModifyActivity extends AppCompatActivity {

    private final String TAG = ProfileModifyActivity.class.getSimpleName();
    private final String REFERENCE_URL = "gs://hotstagram-cd509.appspot.com";
    private static final int GALLERY_CODE = 1;
    long now ;
    Date mDate;
    SimpleDateFormat simpleDate;


    private FirebaseAuth auth;
    FirebaseStorage storage;
    FirebaseFirestore firebaseFirestore;
    StorageReference storageRef;
    FirebaseUser firebaseUser;

    private Uri photouri;
    Intent intent;
    Toolbar toolbar;
    EditText et_name;
    EditText et_info;
    EditText et_nickname;
    ImageView iv_user_profile;
    ImageView modify_complete;
    ImageView modify_cancle;
    Context context;

    private ArrayList<UserGridItemView> userArrayList = new ArrayList<>();

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_modify);

        context = this;
        et_name = findViewById(R.id.et_name);
        et_nickname = findViewById(R.id.et_nickname);
        et_info = findViewById(R.id.et_info);
        modify_cancle = findViewById(R.id.modify_cancle);
        modify_complete = findViewById(R.id.modify_complete);

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        // 툴바 적용
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 권한 부여
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE},0);
        }

        iv_user_profile = findViewById(R.id.iv_user_profile);
        iv_user_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(1);
            }
        });

        // 프로필 수정 시 기존 값 입력 되어있게 하기
        existProfile();

        // 프로필 수정 취소
        modify_cancle.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                Toast.makeText(ProfileModifyActivity.this,"프로필 수정 취소", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // 프로필 수정 완료
        modify_complete.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                modifyProfile();
                if(photouri!=null) {
                    storeProfilePhoto(photouri);
                }
                Toast.makeText(ProfileModifyActivity.this,"프로필 수정 완료", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE) {
            photouri = data.getData();
        }

        // 이미지 uri 이미지뷰에 적용
        iv_user_profile.setImageURI(photouri);
    }


    // 프로필 사진 변경 다이얼로그 띄우기
    @Override
    protected Dialog onCreateDialog ( int id){
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileModifyActivity.this);
        builder.setTitle("프로필 사진 변경");
        builder.setItems(R.array.user_profile_photo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("which",which +" ");
                switch (which) {
                    case 0:
                        // 갤러리 불러오기
                        intent = new Intent(Intent.ACTION_PICK);
                        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                        intent.putExtra("user_profile",100);
                        startActivityForResult(intent, GALLERY_CODE);
                        break;
                    case 1:
                        Toast.makeText(ProfileModifyActivity.this, "삭제할 이미지가 없습니다.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        return builder.create();
    }


    // 프로필 수정 시 이미 존재하는 데이터 넣어놓기
    public void existProfile(){

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final DocumentReference docRef = firebaseFirestore.collection("Inter_Test").document(firebaseUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    String mName = (String)document.get("Name");
                    String mNickName = (String)document.get("NickName");
                    String mMessage = (String)document.get("Message");
                    Uri mPhotouri = null;
                    if(document.get("ProfileURI") != null) {
                        Log.d("ProfileUri",document.get("ProfileURI")+" ");
                        storage = FirebaseStorage.getInstance();
                        storageRef = storage.getReferenceFromUrl("gs://hotstagram-cd509.appspot.com/"+document.get("ProfileURI"));
                        GlideApp.with(context).load(storageRef).into(iv_user_profile);
                    }

                    et_name.setText(mName);
                    et_nickname.setText(mNickName);
                    et_info.setText(mMessage);

                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }


    // 프로필 수정 시 수정된 값으로 DB Update
    public void modifyProfile(){

        String name = et_name.getText().toString();
        String nickname = et_nickname.getText().toString();
        String info = et_info.getText().toString();

        DocumentReference docRef = firebaseFirestore.collection("Inter_Test").document(firebaseUser.getUid());
        docRef.update(
                "Name", name,
                "Message", info,
                "NickName", nickname)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "DB 변경 성공" );
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "DB 변경 실패" );
                    }
                });
    }

    // Storage에 추가
    public void storeProfilePhoto(Uri photouri){

        // 현재시간 불러오기
        now = System.currentTimeMillis();
        mDate = new Date(now);
        simpleDate = new SimpleDateFormat("yyyy-MM-dd-hh:mm:ss");
        String getTime = simpleDate.format(mDate);

        // Storage에 프로필 사진 등록
        storageRef = storage.getReferenceFromUrl("gs://hotstagram-cd509.appspot.com/")
                .child("user_profile/" +  firebaseUser.getUid() + "_" + getTime + ".png");

        storageRef.putFile(photouri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(), "프로필 사진 등록 성공", Toast.LENGTH_SHORT).show();
                intent = new Intent(ProfileModifyActivity.this, ProfileModifyActivity.class);
                finish();
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Toast.makeText(getApplicationContext(), "프로필 사진 등록 실패", Toast.LENGTH_SHORT).show();
            }
        });

        // DB에 프로필 사진 추가하여 저장
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DocumentReference docRef = firebaseFirestore.collection("Inter_Test").document(firebaseUser.getUid());
        docRef.update("ProfileURI" ,"user_profile/" + firebaseUser.getUid() + "_" + getTime + ".png")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "DB 변경 성공" );
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "DB 변경 실패" );
                    }
                });

    }

}