package com.example.lawyerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Delayed;

public class LawyerProfileCreation extends AppCompatActivity {

    EditText edname, edspecs, edoff, ednumber, edemail;
    Button save;
    ProgressBar pb;
    ImageView pfp;
    Uri imageUri;
    UploadTask uploadTask;
    StorageReference storageReference;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    AllUsers member;
    String currentUserId;
    private static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_profile_creation);

        member = new AllUsers();

        pfp = findViewById(R.id.pfp_ed);
        edname = findViewById(R.id.name_ed_pf);
        edspecs = findViewById(R.id.specialization_ed_pf);
        edoff = findViewById(R.id.office_ed_pf);
        ednumber = findViewById(R.id.number_ed_pf);
        edemail = findViewById(R.id.email_ed_pf);
        pb = findViewById(R.id.pb_ed_pfp);
        save = findViewById(R.id.fin_ed_pf);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentUserId = user.getUid();

        documentReference = db.collection("user").document(currentUserId);
        storageReference = FirebaseStorage.getInstance().getReference("Profile images");
        databaseReference = database.getReference("All Users");

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });

        pfp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == PICK_IMAGE || resultCode == RESULT_OK ||
                    data != null || data.getData() != null){
                imageUri = data.getData();

                Picasso.get().load(imageUri).into(pfp);
            }
        }catch (Exception e){
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));

    }

    private void uploadData() {
        String name = edname.getText().toString();
        String specs = edspecs.getText().toString();
        String office = edoff.getText().toString();
        String number = ednumber.getText().toString();
        String email = edemail.getText().toString();

        if (!TextUtils.isEmpty(name) || !TextUtils.isEmpty(specs) || !TextUtils.isEmpty(office) || !TextUtils.isEmpty(number) || !TextUtils.isEmpty(email) || imageUri != null){
            pb.setVisibility(View.VISIBLE);
            final StorageReference reference = storageReference.child(System.currentTimeMillis()+"."+getFileExt(imageUri));
            uploadTask = reference.putFile(imageUri);

            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downlaodUri = task.getResult();

                        Map<String,String > profile = new HashMap<>();
                        profile.put("name", name);
                        profile.put("specs", specs);
                        profile.put("office", office);
                        profile.put("number", number);
                        profile.put("email", email);
                        profile.put("url", downlaodUri.toString());
                        profile.put("uid", currentUserId);
                        profile.put("Privacy", "Public");

                        member.setName(name);
                        member.setSpecs(specs);
                        member.setUid(currentUserId);
                        member.setUrl(downlaodUri.toString());

                        databaseReference.child(currentUserId).setValue(member);

                        documentReference.set(profile)
                                .addOnSuccessListener((new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        pb.setVisibility(View.INVISIBLE);
                                        Toast.makeText(LawyerProfileCreation.this, "Profile Saved", Toast.LENGTH_SHORT).show();

                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent intent = new Intent(LawyerProfileCreation.this, fragment1.class);
                                                startActivity(intent);
                                            }
                                        },2000);

                                    }
                                }));

                    }
                }
            });
        }else {
            Toast.makeText(this, "Please fill out all the fields", Toast.LENGTH_SHORT).show();
        }
    }
}