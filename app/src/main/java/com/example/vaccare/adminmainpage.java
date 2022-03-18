package com.example.vaccare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class adminmainpage extends AppCompatActivity implements View.OnClickListener {

    private CardView addproductcard, trackproductcard, adddistributorcard, addretailercard;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminmainpage);

        logout= findViewById(R.id.logout);
        addproductcard= (CardView) findViewById(R.id.addproductcard);
        adddistributorcard= (CardView) findViewById(R.id.adddistributorcard);

        logout.setOnClickListener(this);
        addproductcard.setOnClickListener(this);
        adddistributorcard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent i;

        switch(v.getId()){
            case R.id.addproductcard:
                i= new Intent(this, addproduct.class);
                startActivity(i);
                break;


            case R.id.adddistributorcard:
                i= new Intent(this, adduser.class);
                startActivity(i);
                break;


            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                i= new Intent(this, login.class);
                startActivity(i);
                finish();
                break;


        }

    }
}