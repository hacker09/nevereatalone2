package com.example.lambui.nevereatalone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText txtNewPassword;
    private Button btnLuu, btnHuy;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        txtNewPassword = (EditText) findViewById(R.id.txt_new_password);
        btnHuy = (Button) findViewById(R.id.btn_huy);
        btnLuu = (Button) findViewById(R.id.btn_luu);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            startActivity(new Intent(this, LoginActivity.class));
        }
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Luu();
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Huy();
            }
        });
    }
    private void Luu(){
        String pass = txtNewPassword.getText().toString().trim();
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this, R.string.enter_password, Toast.LENGTH_SHORT).show();
            return;
        }
        user = firebaseAuth.getCurrentUser();
        user.updatePassword(pass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(ChangePasswordActivity.this, R.string.change_password_successful, Toast.LENGTH_SHORT).show();
            }
        });
        finish();
        startActivity(new Intent(this, Home_page.class));
    }
    private void Huy(){
        finish();
        startActivity(new Intent(this, Home_page.class));
    }

}
