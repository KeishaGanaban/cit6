package com.example.lawyerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class signup extends AppCompatActivity {

    EditText username, password, email, number;
    RadioButton lawyer, client;
    boolean valid = true;
    Button register;
    CheckBox aslawyer, asclient;
    FirebaseAuth mAuth;
    TextView textView;
    FirebaseFirestore fStore;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        username = findViewById(R.id.regusername);
        email = findViewById(R.id.regemail);
        number = findViewById(R.id.regnumber);
        password = findViewById(R.id.regpassword);
        register = findViewById(R.id.register);
        textView = findViewById(R.id.tologin);
        fStore = FirebaseFirestore.getInstance();
        aslawyer = findViewById(R.id.lawyer);
        asclient = findViewById(R.id.client);

        asclient.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    aslawyer.setChecked(false);
                }
            }
        });

        aslawyer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    asclient.setChecked(false);
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkField(username);
                checkField(email);
                checkField(number);
                checkField(password);

                if(!(aslawyer.isChecked() || asclient.isChecked())){
                    Toast.makeText(signup.this, "Select account type", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(valid){
                    mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            DocumentReference df = fStore.collection("Users").document(user.getUid());
                            Map<String,Object> userInfo = new HashMap<>();
                            userInfo.put("UserName", username.getText().toString());
                            userInfo.put("UserEmail", email.getText().toString());
                            userInfo.put("PhoneNumber", number.getText().toString());

                            if(aslawyer.isChecked()){
                                userInfo.put("isLawyer", "1");
                            }

                            if(asclient.isChecked()){
                                userInfo.put("isClient", "1");
                            }

                            df.set(userInfo);

                            Toast.makeText(signup.this, "Account Successfully Registered", Toast.LENGTH_SHORT).show();
                            if(aslawyer.isChecked()){
                                startActivity(new Intent(getApplicationContext(),lawyerdashboard.class));
                                finish();
                            }

                            if(asclient.isChecked()){
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(signup.this, "Failed to Create Account", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public boolean checkField(EditText textField){
        if(textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        } else {
            valid = true;
        }
        return valid;
    }
}