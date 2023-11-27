package com.example.lawyerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class fragment1 extends Fragment implements View.OnClickListener{

    ImageView imageView;
    TextView pfname, pfspecs, pfoffice, pfnumber, pfemail;
    ImageButton edit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        imageView = getActivity().findViewById(R.id.pfp_f1);
        pfname = getActivity().findViewById(R.id.name_f1_pf);
        pfspecs = getActivity().findViewById(R.id.specialization_f1_pf);
        pfoffice = getActivity().findViewById(R.id.office_f1_pf);
        pfnumber = getActivity().findViewById(R.id.number_f1_pf);
        pfemail = getActivity().findViewById(R.id.email_f1_pf);

        edit = getActivity().findViewById(R.id.ib_edit_f1);
        edit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ib_edit_f1:
                Intent intent = new Intent(getActivity(), EditProfile.class);
                startActivity(intent);
                break;

        }
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentId = user.getUid();
        DocumentReference reference;
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        reference = firestore.collection("user").document(currentId);
        
        reference.get()
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

                            Picasso.get().load(url).into(imageView);
                            pfname.setText(nameResult);
                            pfspecs.setText(specResult);
                            pfoffice.setText(officeResult);
                            pfnumber.setText(numberResult);
                            pfemail.setText(emailResult);
                        }else {
                            Intent intent = new Intent(getActivity(), LawyerProfileCreation.class);
                            startActivity(intent);
                        }
                    }
                });

    }
}
