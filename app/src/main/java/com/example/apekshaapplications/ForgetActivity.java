package com.example.apekshaapplications;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ForgetActivity extends AppCompatActivity {

    EditText edt1OTP, edt2OTP, edt3OTP, edt4OTP;
    LinearLayout LineUpdate, LineVerify;

    String stredt1, stredt2, stredt3, stredt4;
    String strConcat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);


        edt1OTP = findViewById(R.id.edt1OTP);
        edt2OTP = findViewById(R.id.edt2OTP);
        edt3OTP = findViewById(R.id.edt3OTP);
        edt4OTP = findViewById(R.id.edt4OTP);
        LineUpdate = findViewById(R.id.LineUpdate);
        LineVerify = findViewById(R.id.LineVerify);


        LineUpdate.setVisibility(View.GONE);


        nextMoveCurser(edt1OTP, edt2OTP);
        nextMoveCurser(edt2OTP, edt3OTP);
        nextMoveCurser(edt3OTP, edt4OTP);


        findViewById(R.id.LineVerify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stredt1 = edt1OTP.getText().toString();
                stredt2 = edt2OTP.getText().toString();
                stredt3 = edt3OTP.getText().toString();
                stredt4 = edt4OTP.getText().toString();
                strConcat = stredt1.concat(stredt2).concat(stredt3).concat(stredt4);

                if (edt1OTP.getText().toString().isEmpty() && edt2OTP.getText().toString().isEmpty() && edt3OTP.getText().toString().isEmpty() && edt4OTP.getText().toString().isEmpty()) {
                    Toast.makeText(ForgetActivity.this, "Please Enter OTP for verification", Toast.LENGTH_SHORT).show();
                } else if (!edt1OTP.getText().toString().isEmpty() && !edt2OTP.getText().toString().isEmpty() && !edt3OTP.getText().toString().isEmpty() && !edt4OTP.getText().toString().isEmpty() && strConcat.equals("1111")) {
                    Toast.makeText(ForgetActivity.this, "Verify Successful..." + strConcat, Toast.LENGTH_SHORT).show();
                    LineUpdate.setVisibility(View.VISIBLE);
                    LineVerify.setVisibility(View.GONE);
                } else {
                    Toast.makeText(ForgetActivity.this, "OTP is incorrect", Toast.LENGTH_SHORT).show();

                }


            }
        });

    }

    void nextMoveCurser(EditText edt1, EditText edt2) {

        edt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (edt1.getText().toString().length() == 1) {
                    edt2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

}