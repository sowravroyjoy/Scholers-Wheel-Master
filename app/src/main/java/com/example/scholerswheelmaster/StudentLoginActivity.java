package com.example.scholerswheelmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StudentLoginActivity extends AppCompatActivity {

    private EditText edEmail, edPassword;
    private Button edLogin, edRegister;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null){
                    startActivity(new Intent(StudentLoginActivity.this, StudentMapActivity.class));
                    finish();
                    return;
                }
            }
        };

        edEmail = findViewById(R.id.sEmail);
        edPassword = findViewById(R.id.sPassword);
        edLogin = findViewById(R.id.sLogin);
        edRegister = findViewById(R.id.sRegister);

        edRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String semail = edEmail.getText().toString();
                final String spassword = edPassword.getText().toString();

                firebaseAuth.createUserWithEmailAndPassword(semail,spassword).addOnCompleteListener(StudentLoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(StudentLoginActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                        }else{
                            String user_id = firebaseAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("users").child("students").child(user_id);
                            current_user_db.setValue(true);
                        }
                    }
                });
            }
        });

        edLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String dEmail = edEmail.getText().toString();
                final String dPassword = edPassword.getText().toString();

                firebaseAuth.signInWithEmailAndPassword(dEmail,dPassword).addOnCompleteListener(StudentLoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(StudentLoginActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(firebaseAuthListener);
    }
}