package com.example.apekshaapplications;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    EditText edtPassword, conedtPassword, edtUserName, edtEmail, edtMobileno;
    TextView txtSignUp;
    static String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-e4caa-default-rtdb.firebaseio.com/");
    String strUserName, strEmail, strMobileNum, strPassword, strConPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtPassword = findViewById(R.id.EdtPassword);
        conedtPassword = findViewById(R.id.ConEdtPassword);
        txtSignUp = findViewById(R.id.txtSignUp);
        edtUserName = findViewById(R.id.edtUserName);
        edtEmail = findViewById(R.id.edtEmail);
        edtMobileno = findViewById(R.id.edtMobile);

        findViewById(R.id.alrdAccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterMethod();
            }
        });


    }

    void RegisterMethod() {

        strUserName = edtUserName.getText().toString();
        strEmail = edtEmail.getText().toString();
        strMobileNum = edtMobileno.getText().toString();
        strPassword = edtPassword.getText().toString();
        strConPassword = conedtPassword.getText().toString();


        if (strUserName.isEmpty() || strEmail.isEmpty() || strMobileNum.isEmpty() || strPassword.isEmpty() || strConPassword.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "All Fields are required for sign Up", Toast.LENGTH_SHORT).show();
        } else if (strEmail.isEmpty()) {
            edtEmail.setError("please Enter your Email Id");
        } else if (!strEmail.trim().matches(emailPattern)) {
            edtEmail.setError("Invalid email address");
        } else if (strMobileNum.isEmpty()) {
            edtMobileno.setError("please Enter your Mobile No");
        } else if (strPassword.isEmpty()) {
            edtPassword.setError("please Enter your password");
        } else if (strConPassword.isEmpty()) {
            conedtPassword.setError("please Enter your conform password");
        } else if (!strConPassword.equals(strPassword)) {
            conedtPassword.setError("Password doesn't match");
        } else if (strUserName.isEmpty()) {
            edtUserName.setError("please Enter Your Name");
        } else {

            databaseReference.child("Registration-Data").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(strMobileNum)) {
                        Toast.makeText(getApplicationContext(), "Phone Number is Already Registered", Toast.LENGTH_LONG).show();
                    } else {
                        databaseReference.child("Registration-Data").child(strMobileNum).child("UserName").setValue(strUserName);
                        databaseReference.child("Registration-Data").child(strMobileNum).child("Email").setValue(strEmail);
                        databaseReference.child("Registration-Data").child(strMobileNum).child("Password").setValue(strPassword);
                        databaseReference.child("Registration-Data").child(strMobileNum).child("ConfirmPassword").setValue(strConPassword);

                        Toast.makeText(RegisterActivity.this, "Register Successfully Done", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finishAffinity();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

}