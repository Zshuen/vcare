package com.example.vaccare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class checkvaccinedetails extends AppCompatActivity {


    FirebaseFirestore bdb = FirebaseFirestore.getInstance();
    public static String contents = null;
    boolean valid= true;
    ImageView bckbtn;
    TextView VID,VNAME,VMDATETIME,VDISNAME,VDISDATE,VDTIME,VRNAME,VRDATE,VRTIME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkvaccinedetails);

        bckbtn= findViewById(R.id.checkvaccdetailbck);
        bckbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), login.class));
                finish();
            }
        });

    }

    public void custbrowsebtn(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1000);
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {

        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                try {
                    Bitmap bMap = selectedImage;
                    int[] intArray = new int[bMap.getWidth()*bMap.getHeight()];
                    bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());
                    LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);
                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                    Reader reader = new MultiFormatReader();
                    Result result = reader.decode(bitmap);
                    contents = result.getText();

                    Dialog cqrdialog = new Dialog(checkvaccinedetails.this);
                    cqrdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    cqrdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    cqrdialog.setContentView(R.layout.checkdetailslayout);
                    VID= cqrdialog.findViewById(R.id.vaccineid);
                    VID.setText(contents);

                    //getvacname,datetime
                    VNAME= cqrdialog.findViewById(R.id.vaccinename);
                    VMDATETIME= cqrdialog.findViewById(R.id.vmacdatetime);
                    bdb.collection("Vaccines")
                            .whereEqualTo("VaccineID", contents)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            String vname = document.getString("VaccineName");
                                            String vmdatetime = document.getString("VaccineManufactureDateTime");
                                            VNAME.setText(vname);
                                            VMDATETIME.setText(vmdatetime);

                                        }
                                    }
                                    checkField(VNAME);
                                    checkField(VMDATETIME);
                                    if (!valid){
                                        Toast.makeText(checkvaccinedetails.this, "Vaccine Information not found. Vaccine may be a counterfeit product!",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                    //get disname,date,time
                    VDISNAME= cqrdialog.findViewById(R.id.cdistributorname);
                    VDISDATE= cqrdialog.findViewById(R.id.cdistributiondate);
                    VDTIME= cqrdialog.findViewById(R.id.cdistributiontime);
                    bdb.collection("DistributionDetails")
                            .whereEqualTo("VaccineID", contents)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            String dname = document.getString("DistributorName");
                                            String ddate = document.getString("DistributionDate");
                                            String dtime = document.getString("DistributionTime");
                                            VDISNAME.setText(dname);
                                            VDISDATE.setText(ddate);
                                            VDTIME.setText(dtime);
                                        }
                                    }
                                    checkField(VDISNAME);
                                    checkField(VDISDATE);
                                    checkField(VDTIME);
                                    if (!valid){
                                        Toast.makeText(checkvaccinedetails.this, "Distribution Information not found. Vaccine may be a counterfeit product!",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                    //getretname, date, time
                    VRNAME=cqrdialog.findViewById(R.id.cretailername);
                    VRDATE=cqrdialog.findViewById(R.id.creceiveddate);
                    VRTIME=cqrdialog.findViewById(R.id.creceivedtime);
                    bdb.collection("RetailerDetails")
                            .whereEqualTo("VaccineID", contents)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            String rname = document.getString("RetailerName");
                                            String rdate = document.getString("ReceivedDate");
                                            String rtime = document.getString("ReceivedTime");
                                            VRNAME.setText(rname);
                                            VRDATE.setText(rdate);
                                            VRTIME.setText(rtime);
                                        }
                                    }
                                    checkField(VRNAME);
                                    checkField(VRDATE);
                                    checkField(VRTIME);
                                    if (!valid){
                                        Toast.makeText(checkvaccinedetails.this, "Retailer Information not found. Vaccine may be a counterfeit product!",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                    cqrdialog.show();



                    ImageView cimageviewclose = cqrdialog.findViewById(R.id.cclosedialog);
                    cimageviewclose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cqrdialog.dismiss();
                            Toast.makeText(checkvaccinedetails.this,"Dialog close", Toast.LENGTH_SHORT).show();
                        }
                    });



                }catch (Exception e){
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(checkvaccinedetails.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(checkvaccinedetails.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }




    }

    public boolean checkField(TextView textField){
        if(textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        }else {
            valid = true;
        }

        return valid;
    }

}