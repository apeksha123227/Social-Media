package com.example.apekshaapplications;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    EditText edtMobile, edtPassword;
    TextView txtSignIn, txtVerifyPhone, txtGetOtp;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-e4caa-default-rtdb.firebaseio.com/");

    String strMobile, strPassword;
    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;
    private FirebaseAuth firebaseAuth;
    EditText edtMobileNum, edtOTP;
    String strOTPId;
    LinearLayout lineGetOtp, lineVerifyOtp;
    CountryCodePicker countryCodePicker;
    CallbackManager callbackManager;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtMobile = findViewById(R.id.edtMobile);
        edtPassword = findViewById(R.id.EdtPass);
        txtSignIn = findViewById(R.id.txtSignIn);


        firebaseAuth = FirebaseAuth.getInstance();
        signInRequest();

        findViewById(R.id.aldrRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });


        findViewById(R.id.txtForget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgetActivity.class));

            }
        });
        findViewById(R.id.loginGoogle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        findViewById(R.id.loginPhone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signInPhone();
            }
        });
        findViewById(R.id.loginFacebook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(LoginActivity.this, FacebookAuhActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);*/
                facebookLogin();
            }
        });

        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginMethod();
            }
        });


    }

    private void facebookLogin() {
        callbackManager = CallbackManager.Factory.create();
        //loginButton.setReadPermissions("email", "public_profile");
        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile"));

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(@NonNull FacebookException e) {

            }
        });

    }

    private void handleFacebookAccessToken(AccessToken token) {
        //  Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            // Log.d(TAG, "signInWithCredential:success");
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            //   Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void signInPhone() {

        Window window = this.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        BottomSheetDialog dialog = new BottomSheetDialog(LoginActivity.this);
        //  dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.setContentView(R.layout.phonelogin_desin);

//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //   dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        //dialog.getWindow().setBackgroundDrawableResource(R.drawable.main_bg);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setBackgroundDrawable(getDrawable(android.R.color.transparent));
        lineGetOtp = dialog.findViewById(R.id.LineGetOtp);
        lineVerifyOtp = dialog.findViewById(R.id.LineVerifyOtp);
        edtMobileNum = dialog.findViewById(R.id.edtMobileNum);
        edtOTP = dialog.findViewById(R.id.edt1OTP);
        txtGetOtp = dialog.findViewById(R.id.txtGetOtp);
        txtVerifyPhone = dialog.findViewById(R.id.txtVerify);
        countryCodePicker = dialog.findViewById(R.id.contrycodePicker);

        lineVerifyOtp.setVisibility(View.GONE);
        //  lineVerifyOtp.setVisibility(View.GONE);

        txtGetOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edtMobileNum.getText().toString().isEmpty()) {
                    //edtMobileNum.setError("please Enter Mobile Number");
                    Toast.makeText(LoginActivity.this, "please Enter Mobile number", Toast.LENGTH_SHORT).show();

                } else {
                    lineVerifyOtp.setVisibility(View.VISIBLE);
                    lineGetOtp.setVisibility(View.GONE);

                    countryCodePicker.registerCarrierNumberEditText(edtMobileNum);
                    intiateOTP();

                }
            }
        });
        txtVerifyPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

/*
                countryCodePicker.registerCarrierNumberEditText(edtMobileNum);
                intiateOTP();*/

                if (edtOTP.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "OTP is Required for Login", Toast.LENGTH_SHORT).show();
                } else if (edtOTP.getText().toString().length() != 6) {
                    Toast.makeText(LoginActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                } else {
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(strOTPId, edtOTP.getText().toString());
                    signInWithPhoneAuthCredential(phoneAuthCredential);
                }

            }
        });


        dialog.findViewById(R.id.cancleicon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void intiateOTP() {
        String countryCodeWithMobile = countryCodePicker.getFullNumberWithPlus().replace("", "");
        Log.d(TAG, "intiateOTP: "+countryCodeWithMobile);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(String.valueOf(countryCodeWithMobile),// Phone number to verify
                60, TimeUnit.SECONDS,// Timeout and unit
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                        strOTPId = s;
                    }

                    @Override
                    public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                        super.onCodeAutoRetrievalTimeOut(s);
                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    void signInRequest() {

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(LoginActivity.this, googleSignInOptions);

    }

    void signIn() {

        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, 101);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

      callbackManager.onActivityResult(requestCode, resultCode, data);
        /* if (resultCode != RESULT_CANCELED)*/
        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + googleSignInAccount.getId());
                firebaseAuthWIthGoogle(googleSignInAccount.getIdToken());

            } catch (ApiException e) {
                Log.d(TAG, "Google sign In Field");


            }
        }

    }

    void firebaseAuthWIthGoogle(String idToken) {

        AuthCredential googleCredential = GoogleAuthProvider.getCredential(idToken, null);

        firebaseAuth.signInWithCredential(googleCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            // updateUI(user);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Problem found in login", Toast.LENGTH_LONG).show();
                            //  updateUI(null);
                        }
                    }
                });
    }

    void LoginMethod() {

        strMobile = edtMobile.getText().toString();
        strPassword = edtPassword.getText().toString();

        if (strMobile.isEmpty() || strPassword.isEmpty()) {
            Toast.makeText(LoginActivity.this, "please fill the details", Toast.LENGTH_SHORT).show();
        } else if (strMobile.isEmpty()) {
            edtMobile.setError("please Enter your Email Id or mobile No.");
        } else if (strPassword.isEmpty()) {
            edtPassword.setError("please Enter your Password ");
        } else {
            databaseReference.child("Registration-Data").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(strMobile)) {
                        String getPassword = snapshot.child(strMobile).child("Password").getValue(String.class);
                        if (getPassword.equals(strPassword)) {
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finishAffinity();
                        } else {
                            Toast.makeText(LoginActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Something went to Wrong", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}