package com.example.lawyerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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

    TextInputEditText regemail, regpassword, regusername, regnumber;
    RadioButton lawyer, client;
    Button register;
    FirebaseAuth mAuth;
    ProgressBar progressbar;
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
        regusername = findViewById(R.id.regusername);
        regemail = findViewById(R.id.regemail);
        regnumber = findViewById(R.id.regnumber);
        regpassword = findViewById(R.id.regpassword);
        register = findViewById(R.id.register);
        progressbar =findViewById(R.id.progressbar);
        textView = findViewById(R.id.tologin);
        fStore = FirebaseFirestore.getInstance();

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivity(intent);
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressbar.setVisibility(view.VISIBLE);
                String username, email, number, password;
                username = String.valueOf(regusername.getText());
                email = String.valueOf(regemail.getText());
                number = String.valueOf(regnumber.getText());
                password = String.valueOf(regpassword.getText());

                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(signup.this, "Enter username", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(signup.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(number)) {
                    Toast.makeText(signup.this, "Enter number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(signup.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressbar.setVisibility(view.GONE);
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    DocumentReference df = fStore.collection("Users").document(user.getUid());
                                    Map<String,Object> userInfo = new HashMap<>();
                                    userInfo.put("UserName", regusername.getText().toString());
                                    userInfo.put("UserEmail", regemail.getText().toString());
                                    userInfo.put("PhoneNumber", regnumber.getText().toString());

                                    userInfo.put("isUser", "1");

                                    df.set(userInfo);

                                    Toast.makeText(signup.this, "Account Created.",
                                            Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    finish();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(signup.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });
    }
}