package com.example.app.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.NavigationMenuActivity;
import com.example.app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout titemail, titpassword;
    EditText eEmail, ePassword;
    TextView userRegister;
    Button userLog;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        fAuth = FirebaseAuth.getInstance();
        eEmail = findViewById(R.id.atLogin);
        ePassword = findViewById(R.id.atPassword);
        titemail = findViewById(R.id.titEmail);
        titpassword = findViewById(R.id.titPassword);

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), NavigationMenuActivity.class));
            finish();
        }

        userLog = findViewById(R.id.login);
        userLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = eEmail.getText().toString().trim();
                String password = ePassword.getText().toString().trim();

                if(email.isEmpty()){
                    titemail.setError("Введите почту");
                    return;
                }else{
                    titemail.setError(null);
                }
                if(password.isEmpty()){
                    titpassword.setError("Введите пароль");
                    return;
                } else{
                    titpassword.setError(null);
                }

                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this,"Вы успешно вошли в свой аккаунт!",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, NavigationMenuActivity.class));
                            finish();
                        }else {
                            Toast.makeText(LoginActivity.this,"Неверные введенные данные!"+
                                    task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


        userRegister = findViewById(R.id.btn_registration);
        userRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });
    }
}