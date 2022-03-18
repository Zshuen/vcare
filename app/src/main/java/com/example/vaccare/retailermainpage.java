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

public class retailermainpage extends AppCompatActivity {

    Button retlogoutbtn, retreceivedbtn;
    public static TextView RPID, RDdate, RDname, RRname, RRtime, RRDate;
    FirebaseFirestore Rdb = FirebaseFirestore.getInstance();
    public static String rcontents = null;
    public static String digitalsign= "sampleds";
    boolean valid= true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailermainpage);

        //logoutbtn
        retlogoutbtn = findViewById(R.id.retlogout);
        retlogoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), login.class));
                finish();
            }
        });

    }

    public void rbrowsebtn(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1000);
    }

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
                    rcontents = result.getText();

                    Dialog retqrdialog = new Dialog(retailermainpage.this);
                    retqrdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    retqrdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    retqrdialog.setContentView(R.layout.retailerqrdialog);
                    //get product ID
                    RPID= retqrdialog.findViewById(R.id.retproductID);
                    RPID.setText(rcontents);

                    //get retailer's current date time
                    RRDate = retqrdialog.findViewById(R.id.retdate);
                    RRtime = retqrdialog.findViewById(R.id.rettime);
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdfdatetime = new SimpleDateFormat("dd-MM-yyyy");
                    SimpleDateFormat sdfdatetime1 = new SimpleDateFormat("hh:mm:ss a");
                    String date= sdfdatetime.format(calendar.getTime());
                    String time= sdfdatetime1.format(calendar.getTime());
                    RRDate.setText(date);
                    RRtime.setText(time);

                    //fetch distributor's data from firestore
                    RDname= retqrdialog.findViewById(R.id.retdisname);
                    RDdate= retqrdialog.findViewById(R.id.retdisdate);
                    Rdb.collection("DistributionDetails")
                            .whereEqualTo("VaccineID", rcontents)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            String rdisname = document.getString("DistributorName");
                                            String rdisdate = document.getString("DistributionDate");
                                            RDname.setText(rdisname);
                                            RDdate.setText(rdisdate);
                                        }
                                    }
                                }
                            });

                    //fetch retailer's data from firestore
                    RRname= retqrdialog.findViewById(R.id.retailername);
                    Rdb.collection("Vaccines")
                            .whereEqualTo("VaccineID", rcontents)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            String rname = document.getString("Retailer");
                                            RRname.setText(rname);
                                        }
                                    }
                                }
                            });

                    //add to retailer file & blockchain
                    retreceivedbtn= retqrdialog.findViewById(R.id.retailerreceivedbtn);
                    retreceivedbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            checkField(RRname);
                            checkField(RDname);
                            checkField(RDdate);

                            if(RRname.getText().toString().isEmpty() && RDname.getText().toString().isEmpty() && RDdate.getText().toString().isEmpty()){
                                Toast.makeText(retailermainpage.this,"Information not found, Vaccine may be a counterfeit product!", Toast.LENGTH_SHORT).show();
                            }
                            else if(RDname.getText().toString().isEmpty() && RDdate.getText().toString().isEmpty())
                            {
                                Toast.makeText(retailermainpage.this,"Vaccine hasn't distributed.", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                String rPID= RPID.getText().toString();
                                String rRname= RRname.getText().toString();
                                String rDate= RRDate.getText().toString();
                                String rTime= RRtime.getText().toString();
                                String ds= digitalsign;

                                retailerfile rfile = new retailerfile(rPID,rRname,rDate,rTime,ds);
                                rfile.retailerfile();
                                try {
                                    bcdemo.nextnextBlock();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(retailermainpage.this, "Vaccine Received", Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(getApplicationContext(), retailermainpage.class));
                                finish();
                            }

                        }

                    });

                    ImageView retimageviewclose = retqrdialog.findViewById(R.id.retailerclosedialog);
                    retimageviewclose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            retqrdialog.dismiss();
                            startActivity(new Intent(getApplicationContext(), retailermainpage.class));
                            //Toast.makeText(addproduct.this,"Dialog close", Toast.LENGTH_SHORT).show();
                        }
                    });

                    retqrdialog.show();

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(retailermainpage.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(retailermainpage.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

//    public static void retailerdistribute(String temp) {
//
//        String rpID = RPID.getText().toString();
//
//        try {
//            Files.write(Paths.get("/data/user/0/com.example.vaccare/files/bcledger/" + rpID + "ledger.txt"), temp.getBytes(), StandardOpenOption.CREATE);
//        } catch (IOException ex) {
//            Logger.getLogger(bcdemo.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

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