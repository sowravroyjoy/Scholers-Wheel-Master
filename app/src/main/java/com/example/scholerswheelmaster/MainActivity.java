package com.example.scholerswheelmaster;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    EditText emailBox, passwordBox;
    CardView loginBtn;
   /* @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch eSwitch;
    TextView eSwitchText;*/
    User userDetails;

    FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();

        emailBox = findViewById(R.id.email);
        passwordBox = findViewById(R.id.password);
        loginBtn = findViewById(R.id.login_btn);

       // eSwitch = findViewById(R.id.toggleSwitch);
        //eSwitchText = findViewById(R.id.tvtoggle);

        userDetails = new User();
        progressDialog = new ProgressDialog(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            return;
        }else{
            checkLocationPermission();
        }

        /*eSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    eSwitchText.setText("I am a Student");
                    userDetails.setUserDetails("Student");

                } else {
                    eSwitchText.setText("I am a Driver");
                    userDetails.setUserDetails("Driver");
                }
            }
        });*/

        FirebaseUser fuser = firebaseAuth.getCurrentUser();
        if(fuser!=null) {
            String uid = firebaseAuth.getCurrentUser().getUid();

            db.child("user").child("Student").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User currentUser = dataSnapshot.getValue(User.class);
                    if(dataSnapshot.exists()) {
                        if (currentUser.getUserDetails() == "Driver") {
                            handleDriver();
                        } else {
                            handleStudent();
                        }
                    }
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            db.child("user").child("Driver").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User currentUser = dataSnapshot.getValue(User.class);
                    if(dataSnapshot.exists()) {
                        if (currentUser.getUserDetails() == "Driver") {
                            handleDriver();
                            //startActivity(new Intent(MainActivity.this, DriverMapActivity.class));
                        } else {
                            handleStudent();
                           // startActivity(new Intent(MainActivity.this, StudentMapActivity.class));
                        }
                    }
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        loginBtn.setOnClickListener((v)->{
            validate(emailBox.getText().toString().trim(), passwordBox.getText().toString());
            /*String email, password;
            email = emailBox.getText().toString().trim();
            password = passwordBox.getText().toString().trim();
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnFailureListener(MainActivity.this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Snackbar.make(v, "Login Failed: " + e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(MainActivity.this, new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Snackbar.make(v, "Login Successful", Snackbar.LENGTH_LONG).show();
                    String uid = firebaseAuth.getCurrentUser().getUid();

                    db.child("user").child("Student").child(uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User currentUser = dataSnapshot.getValue(User.class);
                            if(dataSnapshot.exists()) {
                                if (currentUser.getUserDetails() == "Driver") {
                                    handleDriver();
                                } else {
                                    handleStudent();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    db.child("user").child("Driver").child(uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User currentUser = dataSnapshot.getValue(User.class);
                            if(dataSnapshot.exists()) {
                                if (currentUser.getUserDetails().equals("Driver")) {
                                    handleDriver();
                                } else {
                                    handleStudent();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });*/
        });
    }

    private void validate( String username, String password){
        progressDialog.setMessage("processing...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    checkEmailVerification();
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "login unsuccessful!", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private void checkEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        Boolean emailFlag = firebaseUser.isEmailVerified();
        if(emailFlag){
            Toast.makeText(this, "login successful!", Toast.LENGTH_SHORT).show();
            String uid = firebaseAuth.getCurrentUser().getUid();

            db.child("user").child("Student").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User currentUser = dataSnapshot.getValue(User.class);
                    if(dataSnapshot.exists()) {
                        if (currentUser.getUserDetails() == "Driver") {
                            handleDriver();
                        } else {
                            handleStudent();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            db.child("user").child("Driver").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User currentUser = dataSnapshot.getValue(User.class);
                    if(dataSnapshot.exists()) {
                        if (currentUser.getUserDetails().equals("Driver")) {
                            handleDriver();
                        } else {
                            handleStudent();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else{
            Toast.makeText(this, "verify your email", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }

    private void checkLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 44);
    }

    private void handleStudent() {
        progressDialog.dismiss();
            startActivity(new Intent(MainActivity.this, StudentMapActivity.class));
                finish();
                finishAffinity();
    }


    private void handleDriver() {
        progressDialog.dismiss();
            startActivity(new Intent(MainActivity.this,DriverMapActivity.class));
            finish();
            finishAffinity();
        }

    public void openregister(View view) {
        startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
    }

    public void resetPassword(View view) {
        startActivity(new Intent(MainActivity.this, ResetPasswordActivity.class));
    }
}
