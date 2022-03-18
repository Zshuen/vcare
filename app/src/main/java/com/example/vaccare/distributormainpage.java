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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import block.bcdemo;

public class distributormainpage extends AppCompatActivity {

    Button  dislogoutbtn, disdistributebtn;
    public static TextView PID, bdate, bVname, bDname, bRname, btime;
    FirebaseFirestore bdb = FirebaseFirestore.getInstance();
    public static String contents = null;
    public static String bvname;
    boolean valid= true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distributormainpage);

        dislogoutbtn = findViewById(R.id.dislogout);
        dislogoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), login.class));
                finish();
            }
        });

    }

    //select image from gallery
    public void browsebtn(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1000);
    }

    //convert image to QRcode and fetch the result
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

                    //value stored in the QRcode
                    Result result = reader.decode(bitmap);
                    contents = result.getText();

                    Dialog bqrdialog = new Dialog(distributormainpage.this);
                    bqrdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    bqrdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    bqrdialog.setContentView(R.layout.distributorqrdialog);
                    PID= bqrdialog.findViewById(R.id.disproductID);
                    PID.setText(contents);

                    //get current date time
                    bdate = bqrdialog.findViewById(R.id.disdate);
                    btime = bqrdialog.findViewById(R.id.distime);
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdfdatetime = new SimpleDateFormat("dd-MM-yyyy");
                    SimpleDateFormat sdfdatetime1 = new SimpleDateFormat("hh:mm:ss a");
                    String date= sdfdatetime.format(calendar.getTime());
                    String time= sdfdatetime1.format(calendar.getTime());
                    bdate.setText(date);
                    btime.setText(time);

                    //fetch data from firestore according to the product/ vaccine ID
                    bDname= bqrdialog.findViewById(R.id.disname);
                    bRname= bqrdialog.findViewById(R.id.retname);
                    bdb.collection("Vaccines")
                            .whereEqualTo("VaccineID", contents)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            bvname = document.getString("VaccineName");
                                            String bdisname = document.getString("Distributor");
                                            String bretname = document.getString("Retailer");
                                            //bVname.setText(bvname);
                                            bDname.setText(bdisname);
                                            bRname.setText(bretname);
                                        }
                                    }
                                }
                            });

                    //distribute vaccine (add to file& blockchain)
                    disdistributebtn= bqrdialog.findViewById(R.id.distributeprodbtn);
                    disdistributebtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            checkField(bDname);
                            checkField(bRname);
                            if (!valid){
                                Toast.makeText(distributormainpage.this,"Information not found, Vaccine may be a counterfeit product!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                String dPID= PID.getText().toString();
                                //String dPName= bVname.getText().toString();
                                String dPName= bvname;
                                String dName= bDname.getText().toString();
                                String dDate= bdate.getText().toString();
                                String dTime= btime.getText().toString();
                                distributorfile dfile = new distributorfile(dPID,dPName,dName,dDate,dTime);
                                dfile.distributorfile();
                                try {
                                    bcdemo.nextBlock();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(distributormainpage.this, "Vaccine Distributed", Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(getApplicationContext(), distributormainpage.class));
                                finish();
                            }

                        }

                    });

                    ImageView disimageviewclose = bqrdialog.findViewById(R.id.distributorclosedialog);
                    disimageviewclose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bqrdialog.dismiss();
                            startActivity(new Intent(getApplicationContext(), distributormainpage.class));
                            //Toast.makeText(addproduct.this,"Dialog close", Toast.LENGTH_SHORT).show();
                        }
                    });

                    bqrdialog.show();



                    //Toast.makeText(getApplicationContext(),contents,Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
                //  image_view.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(distributormainpage.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(distributormainpage.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
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