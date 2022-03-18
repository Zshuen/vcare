package com.example.vaccare;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class LoginTabFragment extends Fragment  {

    private Button loginbutton;
    EditText email, password;
    boolean valid = true;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    public LoginTabFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.login_tab_fragment, container, false);

        fAuth = FirebaseAuth.getInstance();
        fStore= FirebaseFirestore.getInstance();
        loginbutton = v.findViewById(R.id.login);
        email=v.findViewById(R.id.Email);
        password=v.findViewById(R.id.pass);
        password.setTransformationMethod(PasswordTransformationMethod.getInstance());

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //login();
                checkField(email);
                checkField(password);
                Log.d("TAG","onClick:" + email.getText().toString());


                if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Please Fill in the Required Field.", Toast.LENGTH_SHORT).show();
                }
                else{
                    fAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(getActivity(), "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            checkUserAccessLevel(authResult.getUser().getUid());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(),e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        return v;
    }


    //check user's role
    private void checkUserAccessLevel(String uid){

        DocumentReference df = fStore.collection("Users").document(uid);

        //extract data from doc
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onSuccess:" + documentSnapshot.getData());

                //identify user access level

                if(documentSnapshot.getString("isAdmin")!=null){
                    //user is admin
                    startActivity(new Intent(getActivity(),adminmainpage.class));
                    getActivity().finish();
                }

                if(documentSnapshot.getString("isDistributor")!=null){
                    //user is distributor
                    startActivity(new Intent(getActivity(),distributormainpage.class));
                    getActivity().finish();
                }

                if(documentSnapshot.getString("isRetailer")!=null){
                    //user is retailer
                    startActivity(new Intent(getActivity(), retailermainpage.class));
                    getActivity().finish();
                }

            }
        });
    }

    public boolean checkField(EditText textField){
        if(textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        }else {
            valid = true;
        }

        return valid;
    }



    public void onStart(){
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            DocumentReference df = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.getString("isAdmin")!=null){
                        startActivity(new Intent(getActivity(),adminmainpage.class));
                        getActivity().finish();
                    }

                    if(documentSnapshot.getString("isDistributor")!=null){
                        startActivity(new Intent(getActivity(),distributormainpage.class));
                        getActivity().finish();
                    }

                    if(documentSnapshot.getString("isRetailer")!=null){
                        startActivity(new Intent(getActivity(),retailermainpage.class));
                        getActivity().finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getActivity(),login.class));
                    getActivity().finish();
                }
            });
        }
    }



}




