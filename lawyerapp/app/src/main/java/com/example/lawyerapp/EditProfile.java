package com.example.lawyerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.squareup.picasso.Picasso;

public class EditProfile extends AppCompatActivity {

    EditText upname, upspecs, upoffice, upnumber, upemail;
    Button finup;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference;
    DocumentReference documentReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentuid = user.getUid();

        documentReference = db.collection("user").document(currentuid);

        upname = findViewById(R.id.name_up_pf);
        upemail = findViewById(R.id.email_up_pf);
        upoffice = findViewById(R.id.office_up_pf);
        upspecs = findViewById(R.id.specialization_up_pf);
        upnumber = findViewById(R.id.number_up_pf);
        finup = findViewById(R.id.fin_up_pf);

        finup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.getResult().exists()){

                            String nameResult = task.getResult().getString("name");
                            String specResult = task.getResult().getString("specs");
                            String officeResult = task.getResult().getString("office");
                            String numberResult = task.getResult().getString("number");
                            String emailResult = task.getResult().getString("email");
                            String url = task.getResult().getString("url");

                            upname.setText(nameResult);
                            upspecs.setText(specResult);
                            upoffice.setText(officeResult);
                            upnumber.setText(numberResult);
                            upemail.setText(emailResult);


                        }else {
                            Toast.makeText(EditProfile.this, "No Profile is Made Yet", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void updateProfile() {
        String name = upname.getText().toString();
        String specs = upspecs.getText().toString();
        String office = upoffice.getText().toString();
        String number = upnumber.getText().toString();
        String email = upemail.getText().toString();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentuid = user.getUid();

        final DocumentReference sDoc = db.collection("user").document(currentuid);

        db.runTransaction(new Transaction.Function<Void>() {
                    @Override
                    public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                        DocumentSnapshot snapshot = transaction.get(sDoc);


                        transaction.update(sDoc, "name", name);
                        transaction.update(sDoc, "specs", specs);
                        transaction.update(sDoc, "office", office);
                        transaction.update(sDoc, "number", number);
                        transaction.update(sDoc, "email", email);


                        // Success
                        return null;
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditProfile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfile.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}