package com.example.app.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    TextView userLogin;
    EditText ePhone, efio, eEmail, ePassword;
    TextInputLayout titPhone, titFio, titEmail, titPassword;
    Button btnResgister;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getSupportActionBar().hide();

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        efio = findViewById(R.id.text_fio);
        ePhone = findViewById(R.id.text_number);
        eEmail = findViewById(R.id.text_login);
        ePassword = findViewById(R.id.text_password);

        titPhone = findViewById(R.id.tit_phone);
        titFio = findViewById(R.id.tit_fio);
        titEmail = findViewById(R.id.tit_email);
        titPassword = findViewById(R.id.tit_password);

        userLogin = findViewById(R.id.btn_login);
        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });

        btnResgister = findViewById(R.id.regUser);
        btnResgister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fio = efio.getText().toString().trim();
                String phone = ePhone.getText().toString().trim();
                String email = eEmail.getText().toString().trim();
                String password = ePassword.getText().toString().trim();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if(fio.isEmpty()){
                    titPhone.setError("?????????????? ??????");
                    return;
                }else{
                    titPhone.setError(null);
                }

                if(phone.isEmpty()){
                    titFio.setError("?????????????? ?????????? ????????????????");
                    return;
                }else{
                    titFio.setError(null);
                }

                if(email.isEmpty()){
                    titEmail.setError("?????????????? ??????????");
                    return;
                }else{
                    titEmail.setError(null);
                }

                if(email.matches(emailPattern)){
                    titEmail.setError(null);
                } else{
                    titEmail.setError("???????????????????????? ??????????");
                    return;
                }

                if(password.isEmpty()){
                    titPassword.setError("?????????????? ????????????");
                    return;
                } else{
                    titPassword.setError(null);
                }

                if(password.length() <= 6){
                    titPassword.setError("?????????????? ???????????? ???????????? 6 ????????????????");
                    return;
                }else{
                    titPassword.setError(null);
                }

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser users = fAuth.getCurrentUser();
                            users.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("??????????????????:", "???????????? ????????????????????");
                                    Toast.makeText(RegistrationActivity.this,"???????????? ?? ???????????????????????????? ?????????? ????????????????????!",Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("?????????????????? ???? ????????????", "???????????? ???? ???????? ???????????????????? " + e.getMessage());
                                }
                            });
                            Toast.makeText(RegistrationActivity.this,"?????????????????????? ???????????? ??????????????!",Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("Users").document(userID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("FIO", fio);
                            user.put("Number", phone);
                            user.put("Email", email);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("??????????????????", "???????????????????????? ???????????? c ???????????????????? ??????????:" + userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("?????????????????? ???? ????????????", "???????????????????????? ???? ?????? ???????????? " + e.getMessage());
                                }
                            });
                            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        }else {
                            Toast.makeText(RegistrationActivity.this,"???????????? ?? ??????????????????????!"+ task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
}