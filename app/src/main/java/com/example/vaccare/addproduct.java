package com.example.vaccare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;
import block.bcdemo;

public class addproduct extends AppCompatActivity {

    private static final String FILE_NAME = "vaccineproduct.txt";
    boolean valid= true;
    EditText ProductName;
    public static TextView ProductID, ProductDateTime;
    Button Addproductbtn, saveqrbtn;
    ImageView qrImage, apbckbtn;
    String inputValue;
    String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    Dialog dialog;
    Spinner spinner, spinner1;
    String TAG = "GenerateQRCode";
    OutputStream outputstream;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //bcdemo bcd= new bcdemo();

    UUID DataUUID= UUID.randomUUID(); //generate product ID
    public String ProductUUID= DataUUID.toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproduct);

        //back to admin main page
        apbckbtn=findViewById(R.id.addprodbck);
        apbckbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),adminmainpage.class));
            }
        });

        dialog = new Dialog(addproduct.this);

        //Set UUID as product ID
        ProductID=findViewById(R.id.productID);
        ProductID.setText(ProductUUID);

        //Generate Date Time and set as vaccine manufacture datetime
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdfdatetime = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        String datetime= sdfdatetime.format(calendar.getTime());
        ProductDateTime=findViewById(R.id.productDateTime);
        ProductDateTime.setText(datetime);

        //fetch distributor name from firestore and set in spinner
        List<String> disnames = new ArrayList<>();
        spinner = (Spinner) findViewById(R.id.distributorspinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, disnames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //get fullname from "Users" collection when field "isDistributor" =1 (user is distributor)
        db.collection("Users")
                .whereEqualTo("isDistributor", "1")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = document.getString("FullName");
                                disnames.add(name);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

        //fetch retailer name from firestore and set in spinner
        List<String> retnames = new ArrayList<>();
        spinner1 = (Spinner) findViewById(R.id.retailerspinner);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, retnames);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        //get fullname from "Users" collection when field "isRetailer" =1 (user is retailer)
        db.collection("Users")
                .whereEqualTo("isRetailer", "1")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String retname = document.getString("FullName");
                                retnames.add(retname);
                            }
                            adapter1.notifyDataSetChanged();
                        }
                    }
                });

        ProductName = findViewById(R.id.addproductname);
        Addproductbtn=findViewById(R.id.addproductbutton);

        Addproductbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkField(ProductName);
                if (valid){

                    addtoproductfile1();

                    //add product file info to the fist block of Blockchain
                    try {
                        bcdemo.firstBlock();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //open dialog with QR Code generated
                    openqrdialog();

                }
                else{
                    Toast.makeText(addproduct.this,"Please Fill in Product Name", Toast.LENGTH_SHORT).show();
                }
            }


        });
    }


    public void openqrdialog() {

        dialog.setContentView(R.layout.qrdialoglayout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        qrImage = (ImageView) dialog.findViewById(R.id.idQrcode);
        saveqrbtn = (Button) dialog.findViewById(R.id.saveqrbtn);
        //store Vaccine ID in QR code.
        inputValue =  ProductID.getText().toString().trim();

        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;

        qrgEncoder = new QRGEncoder(
                inputValue, null,
                QRGContents.Type.TEXT,
                smallerDimension);
        try {
            bitmap = qrgEncoder.encodeAsBitmap();
            qrImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Log.v(TAG, e.toString());
        }


        saveqrbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OutputStream outputStream ;
                Context context = getApplicationContext();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                {
                    ContentResolver resolver = context.getContentResolver();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME,ProductID.getText().toString().trim() + "//" + spinner.getSelectedItem().toString().trim() + "//" + spinner1.getSelectedItem().toString().trim()+".jpg");
                    contentValues.put(MediaStore.MediaColumns.MIME_TYPE,"image/jpeg");
                    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH,Environment.DIRECTORY_PICTURES + File.separator+"QRFolder");
                    Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
                    try {
                        outputStream =  resolver.openOutputStream(Objects.requireNonNull(imageUri) );
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                        Objects.requireNonNull(outputStream);
                        Toast.makeText(context, "Image Saved", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        Toast.makeText(context, "Image Not Not  Saved: \n "+e, Toast.LENGTH_SHORT).show();

                        e.printStackTrace();
                    }

                }

            }
        });


    ImageView imageviewclose = dialog.findViewById(R.id.closedialog);
        imageviewclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(getApplicationContext(), adminmainpage.class));
                finish();
            }
        });
        dialog.show();
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



    public void addtoproductfile1(){
        String Pname= ProductName.getText().toString();
        String PID= ProductID.getText().toString();
        String PDateTime= ProductDateTime.getText().toString();
        String PDistributor = spinner.getSelectedItem().toString();
        String PRetailer = spinner1.getSelectedItem().toString();
        String line= Pname + ";" + PID + ";" +  PDateTime + ";" + PDistributor + ";" + PRetailer + ";";

        FileOutputStream fos= null;
        try{
            fos= openFileOutput(FILE_NAME,MODE_APPEND);
            fos.write(line.getBytes());
            Toast.makeText(this, "Product Added to" + getFilesDir() + "/vaccineproduct.txt", Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos!=null){
                try{
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        }




//    public static void distribute( String temp ){
//        String PID= ProductID.getText().toString();
//        //create new directory to store blockchain ledger text file
//        final File newFile = new File("/data/user/0/com.example.vaccare/files/bcledger");
//        newFile.mkdir();
//        try {
//            Files.write(Paths.get("/data/user/0/com.example.vaccare/files/bcledger/"+PID+"ledger.txt"), temp.getBytes(), StandardOpenOption.CREATE);
//        } catch (IOException ex) {
//            Logger.getLogger(bcdemo.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
}