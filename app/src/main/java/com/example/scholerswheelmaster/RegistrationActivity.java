package com.example.scholerswheelmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity {
    CardView signUpBtn;
    EditText namebox, phonebox, emailbox, passwordbox;
    TextView toggleText;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch aSwitch;
    User userDetails;
    private ProgressDialog progressDialog;
    private String rName, rPhone, rEmail, rPassword;

    FirebaseAuth firebaseAuth;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();

        signUpBtn = findViewById(R.id.r_signup);
        namebox = findViewById(R.id.r_name);
        phonebox = findViewById(R.id.r_phone);
        emailbox = findViewById(R.id.r_email);
        passwordbox = findViewById(R.id.r_password);
        aSwitch = findViewById(R.id.toggleButton);
        toggleText = findViewById(R.id.ettoggle);

        userDetails = new User();

        progressDialog = new ProgressDialog(this);


        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggleText.setText("I am a Student");
                    userDetails.setUserDetails("Student");

                } else {
                    toggleText.setText("I am a Driver");
                    userDetails.setUserDetails("Driver");
                }
            }
        });

        /*FirebaseUser fuser = firebaseAuth.getCurrentUser();
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
                        } else {
                            handleStudent();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }*/
        /*signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setMessage("processing...");
                progressDialog.show();

                String rEmail = emailbox.getText().toString();
                String rPassword = passwordbox.getText().toString();
                String rPhone = phonebox.getText().toString();
                String rName = namebox.getText().toString();


                firebaseAuth.createUserWithEmailAndPassword(rEmail, rPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        sendEmailVerification();
                        Snackbar.make(v, "User Created", Snackbar.LENGTH_LONG).show();
                        String uid = firebaseAuth.getCurrentUser().getUid();
                        user.setUid(uid);
                        if (userDetails.getUserDetails() == "Driver") {
                            db.child("user").child("Driver").child(uid).setValue(user);
                        } else {
                            db.child("user").child("Student").child(uid).setValue(user);
                        }

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

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(v, "User creation failed", Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        });*/

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("processing...");
                progressDialog.show();
                if(validate()){

                    firebaseAuth.createUserWithEmailAndPassword(rEmail, rPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                sendEmailVerification();

                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(RegistrationActivity.this, "Registration Failed!", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }

            }
        });
    }

    private void sendEmailVerification(){
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        sendUserData();
                        firebaseAuth.signOut();
                        progressDialog.dismiss();
                        Toast.makeText(RegistrationActivity.this, "successfully registered and email verification mail sent", Toast.LENGTH_SHORT).show();
                        //firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(RegistrationActivity.this, "verification mail has not been sent!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void sendUserData(){
        User user = new User(rName, rPhone, rEmail, userDetails.getUserDetails(), userDetails.getUid());
        String uid = firebaseAuth.getCurrentUser().getUid();
        user.setUid(uid);
        if (userDetails.getUserDetails() == "Driver") {
            db.child("user").child("Driver").child(uid).setValue(user);
        } else {
            db.child("user").child("Student").child(uid).setValue(user);
        }

        /*db.child("user").child("Student").child(uid).addValueEventListener(new ValueEventListener() {
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
        });*/
    }

    private Boolean validate(){
        Boolean result = false;

        rName = namebox.getText().toString();
        rEmail = emailbox.getText().toString().trim();
        rPassword = passwordbox.getText().toString().trim();
        rPhone = phonebox.getText().toString().trim();

        if(rName.isEmpty()|| rPhone.isEmpty() || rEmail.isEmpty() || rPassword.isEmpty()){
            Toast.makeText(this, "input error", Toast.LENGTH_SHORT).show();

        }else{
            result = true;
        }
        return result;
    }


    /*private void handleStudent() {
        startActivity(new Intent(RegistrationActivity.this, StudentMapActivity.class));
        finish();
        finishAffinity();
    }

    private void handleDriver() {
        startActivity(new Intent(RegistrationActivity.this, DriverMapActivity.class));
        finish();
        finishAffinity();
    }*/

    public void goToLogin(View view) {
        startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
    }
}
