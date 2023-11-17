package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class signup extends AppCompatActivity {

    EditText username, password, retypepassword;
    Button btreg, btsignin;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        username =(EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        retypepassword = (EditText) findViewById(R.id.retypepassword);
        btreg = (Button) findViewById(R.id.btreg);
        btsignin =(Button) findViewById(R.id.btsignin);
        DB = new DBHelper(this);

        btreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String repass = retypepassword.getText().toString();

                if(user.equals("") ||pass.equals("") ||repass.equals(""))
                    Toast.makeText(signup.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                else{
                    if(pass.equals(repass)){
                        Boolean checkuser = DB.checkusername(user);
                        if (checkuser==false) {
                            Boolean insert = DB.insertData(user, pass);
                            if(insert==true){
                                Toast.makeText(signup.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent (getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(signup.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(signup.this, "Account is already registered", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(signup.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent (getApplicationContext(), login.class);
                startActivity(intent);

            }
        });
    }
}