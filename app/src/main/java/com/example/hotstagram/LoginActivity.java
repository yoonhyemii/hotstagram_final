package com.example.hotstagram;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.OnConnectionFailedListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity implements OnConnectionFailedListener, GoogleApiClient.OnConnectionFailedListener {

    private final String TAG = LoginActivity.class.getSimpleName();

    private CallbackManager mCallbackManager;
    private SignInButton btn_google; // ?????? ????????? ??????
    private FirebaseUser user; //?????????????????? ??????
    private FirebaseAuth auth; // ?????????????????? ?????? ??????
    private FirebaseAuth.AuthStateListener authStateListener;
    private GoogleApiClient googleApiClient; // ?????? API ??????????????? ??????
    private static final int REQ_SIGN_GOOGLE = 100; // ?????? ????????? ?????? ??????
    final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext()); // setContentView?????? ?????????
        setContentView(R.layout.activity_login);

        Intent intent = new Intent(this, LoadingPageActivity.class);
        startActivity(intent);

        mCallbackManager = CallbackManager.Factory.create(); // ???????????? ?????? ??????
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        auth = FirebaseAuth.getInstance(); //?????????????????? ?????? ?????? ?????????
        user = auth.getCurrentUser();

        btn_google = findViewById(R.id.btn_google);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        btn_google.setOnClickListener(new View.OnClickListener() { // ?????? ????????? ????????? ???????????? ??? ??????
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, REQ_SIGN_GOOGLE);
            }
        });

        //???????????? ?????? ??????
        final LoginButton btn_facebook_login = findViewById(R.id.btn_facebook_login);
        //final Button btn_facebook = findViewById(R.id.btn_facebook);

        btn_facebook_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                facebookLoginOnClick(view);
            }
        });

        //???????????????????????? ?????? HomeActivity
        if (isLogin()) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //onActivityResult????????? callbackManager??? ????????? ????????? ?????????
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data); //callbackManager.onAcitivyResult??? ????????? onSuccess??? ??????

        if (requestCode == REQ_SIGN_GOOGLE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) { // ?????? ????????? ???????????? ???
                GoogleSignInAccount account = result.getSignInAccount();
                resultLogin(account);
            }
        }
    }

    //???????????? ?????????
    public void facebookLoginOnClick(View v) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this,
                Arrays.asList("email"));
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    //???????????? auth
    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // ????????? ??????
                            storeUserInfo();
                            Toast.makeText(LoginActivity.this, "????????? ??????", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            // ????????? ??????
                            Toast.makeText(LoginActivity.this, "????????? ??????", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        auth.signOut();
    }

    //?????? ?????????
    private void resultLogin(final GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) { // ???????????? ?????? ?????? ???
                            storeUserInfo();
                            Toast.makeText(LoginActivity.this, firebaseUser.getDisplayName()+"??? ???????????????.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else { // ???????????? ???????????? ???
                            Toast.makeText(LoginActivity.this, "????????? ??????", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean isLogin() {
        AccessToken token = AccessToken.getCurrentAccessToken();
        return token != null;

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onBackPressed() {
        signOut();
        LoginManager.getInstance().logOut();
    }

    // ????????????
    public void signOut() {
        googleApiClient.connect();
        googleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                auth.signOut();
                if (googleApiClient.isConnected()) {
                    Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if (status.isSuccess()) {
                                Log.v("??????", "???????????? ??????");
                                setResult(1);
                            } else {
                                setResult(0);
                            }
                            finish();
                        }
                    });
                }
            }
            @Override
            public void onConnectionSuspended(int i) {
            }
        });
    }

    public void storeUserInfo(){
        final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //?????? ??? ?????? ????????????
        DocumentReference docRef = firebaseFirestore.collection("Inter_Test").document(firebaseUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "Document ????????? ??????: " + document.getData());
                    } else {
                        Log.e(TAG, "Document ????????? ??????");
                    }

                    String mUid = (String) firebaseUser.getUid();
                    String fUid = (String) document.get("UID");

                    // ?????? ????????? ????????? ???????????? ????????? ?????? ???????????? ?????? ?????? ???????????? X
                    if(mUid.equals(fUid)) {
                        Log.d(TAG, " ????????? ????????? ?????? " + firebaseUser.getUid());

                        // ?????? ????????? ????????? ???????????? ????????? ?????? ???????????? ?????? ?????? ?????? ??????
                    } else {
                        final Map<String, Object> Mapuser = new HashMap<>();
                        Mapuser.put("Name", firebaseUser.getDisplayName());
                        Mapuser.put("UID", firebaseUser.getUid());
                        Mapuser.put("E-mail", firebaseUser.getEmail());
                        Mapuser.put("Message", null);
                        Mapuser.put("NickName", null);
                        Mapuser.put("ProfileURI", null);

                        firebaseFirestore.collection("Inter_Test").document(firebaseUser.getUid())
                                .set(Mapuser)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.e(" ????????? ??? DB ?????? ??????  ", "" );
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

    }
}


