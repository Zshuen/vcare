package com.example.vaccare;

import static android.widget.Toast.makeText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class adduser extends AppCompatActivity {

    EditText fullName, email, phone, password;
    Button registerBtn;
    CheckBox isDistributor, isRetailer;
    boolean valid= true;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    ImageView aubckbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adduser);



        fullName= findViewById(R.id.createfullname);
        email= findViewById(R.id.createemail);
        phone= findViewById(R.id.createcontactno);
        password= findViewById(R.id.createpass);
        registerBtn= findViewById(R.id.createacc);
        isDistributor=findViewById(R.id.isdistributor);
        isRetailer= findViewById(R.id.isretailer);
        aubckbtn=findViewById(R.id.adduserbck);

        aubckbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), adminmainpage.class));
            }
        });

        //checkbox logics - only one checkbox is allowed to be checked
        isDistributor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    isRetailer.setChecked(false);
                }
            }
        });

        isRetailer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    isDistributor.setChecked(false);
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                checkField(fullName);
                checkField(email);
                checkField(password);
                checkField(phone);

                if (fullName.getText().toString().isEmpty() || email.getText().toString().isEmpty() || password.getText().toString().isEmpty() || phone.getText().toString().isEmpty())
                {
                    Toast.makeText(adduser.this,"Please Fill in Required Field", Toast.LENGTH_SHORT).show();
                }
                else{
                    //start create acc process
                    fAuth= FirebaseAuth.getInstance();
                    fStore= FirebaseFirestore.getInstance();
                    fAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            FirebaseUser user = fAuth.getCurrentUser(); //getting info of user created
                            Toast.makeText(adduser.this, "Account Created", Toast.LENGTH_SHORT).show();
                            DocumentReference df = fStore.collection("Users").document(user.getUid()); 
                            Map<String,Object> userInfo = new HashMap<>();
                            userInfo.put("FullName", fullName.getText().toString());
                            userInfo.put("UserEmail", email.getText().toString());
                            userInfo.put("PhoneNumber", phone.getText().toString());

                            //ADD HASHED PASSWORD
                            String passwordtohash= password.getText().toString();
                            try {
                                String salt= getSalt();
                                String securePassword = get_SHA_256_SecurePassword(passwordtohash, salt);
                                userInfo.put("Hashed password", securePassword);

                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            }

                            //specify if user is distributor
                            if (isDistributor.isChecked()){
                                userInfo.put("isDistributor", "1");
                            }

                            if (isRetailer.isChecked()){
                                userInfo.put("isRetailer", "1");
                            }

                            df.set(userInfo);
                            //clear text after acc created
                            fullName.getText().clear();
                            email.getText().clear();
                            phone.getText().clear();
                            password.getText().clear();
                            isDistributor.setChecked(false);
                            isRetailer.setChecked(false);

                            //startActivity(new Intent(getApplicationContext(),adminmainpage.class));
                            //finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(adduser.this, "Failed to Create Account", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                //checkbox validation
                if (!(isDistributor.isChecked()||isRetailer.isChecked())){
                    Toast.makeText(adduser.this,"Select User Type", Toast.LENGTH_LONG).show();
                    return;
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

    //hash function for user's password
    private static String get_SHA_256_SecurePassword(String passwordToHash,
                                                     String salt) {
        String generatedPassword = null;
        try {

            //create msg digest using SHA-256 algorithm
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            //add the password bytes to digest
            md.update(salt.getBytes());
            //generate msg digest and get the hash's bytes
            byte[] bytes = md.digest(passwordToHash.getBytes());
            //convert the bytes in decimal format to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
                        .substring(1));
            }
            // get the hashed password in hex format
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        //return the hashed password w salt
        return generatedPassword;
    }


    // Add salt
    private static String getSalt() throws NoSuchAlgorithmException {
        //use secure random to generate salt as it supports "SHA1PRNG" algo (pseudo-random number generator algorithm)
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        //create array for salt
        byte[] salt = new byte[16];
        //generate random salt
        sr.nextBytes(salt);
        //return salt
        return salt.toString();
    }
}