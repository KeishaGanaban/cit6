package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class crud extends AppCompatActivity {

    EditText name, occupation, number, email;
    Button add, update, delete, profiles;
    DBHelper2 DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud);

        name = findViewById(R.id.name);
        occupation = findViewById(R.id.occupation);
        number = findViewById(R.id.number);
        email = findViewById(R.id.email);

        add = findViewById(R.id.add);
        update = findViewById(R.id.update);
        delete = findViewById(R.id.delete);
        profiles = findViewById(R.id.profiles);
        DB = new DBHelper2(this);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameTXT = name.getText().toString();
                String occupationTXT = occupation.getText().toString();
                String numberTXT = number.getText().toString();
                String emailTXT = email.getText().toString();

                Boolean checkinsertdata = DB.addprofiledata(nameTXT, occupationTXT, Integer.valueOf(numberTXT), emailTXT);
                if(checkinsertdata==true)
                    Toast.makeText(crud.this, "New Profile Created", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(crud.this, "Failed to Create New Profile", Toast.LENGTH_SHORT).show();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameTXT = name.getText().toString();
                String occupationTXT = occupation.getText().toString();
                String numberTXT = number.getText().toString();
                String emailTXT = email.getText().toString();

                Boolean checkupdatedata = DB.updateprofiledata(nameTXT, occupationTXT, Integer.valueOf(numberTXT), emailTXT);
                if(checkupdatedata==true)
                    Toast.makeText(crud.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(crud.this, "Failed to Update Profile", Toast.LENGTH_SHORT).show();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameTXT = name.getText().toString();

                Boolean checkdeletedata = DB.deletedata(nameTXT);
                if(checkdeletedata==true)
                    Toast.makeText(crud.this, "Profile Deleted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(crud.this, "Failed to Delete Profile", Toast.LENGTH_SHORT).show();
            }
        });

        profiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = DB.getdata();
                if(res.getCount()==0){
                    Toast.makeText(crud.this, "No Entry Exists", Toast.LENGTH_SHORT).show();
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while(res.moveToNext()){
                    buffer.append("Name :"+res.getString(0)+"\n");
                    buffer.append("Occupation :"+res.getString(1)+"\n");
                    buffer.append("Number :"+res.getString(2)+"\n");
                    buffer.append("Email :"+res.getString(3)+"\n\n");
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(crud.this);
                builder.setCancelable(true);
                builder.setTitle("Profiles");
                builder.setMessage(buffer.toString());
                builder.show();
            }
        });
    }
}