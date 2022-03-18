package Transaction;


import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.vaccare.addproduct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import DigitalSignature.MySignature;
import hasher.hasher;
import java.io.Serializable;
import Cryptography.*;
import java.security.Key;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.example.vaccare.distributormainpage;

public class TransactionAdapter implements Serializable {



    /***public static void main(String[] args) {
        List<Transaction> trnxLst = TransactionAdapter.getTransactions();
        System.out.println(trnxLst);
    }*/

    //vaccine transactions
    public static List<Transaction> getTransactions() {
        List<String> trnxLst = Transaction.getAll();
        return trnxLst.stream()
                .map(record -> record.split(";"))
                .filter(arr -> !arr[0].isEmpty())
                .map(arr -> new Transaction(arr[0], arr[1], arr[2], arr[3], arr[4]))
                .collect(Collectors.toList());
    }

    //distributor transactions
    public static List<disTransaction> getdisTransactions() {
        List<String> trnxLst = disTransaction.getAll();
        return trnxLst.stream()
                .map(record -> record.split(";"))
                .filter(arr -> !arr[0].isEmpty())
                .map(arr -> new disTransaction(arr[0], arr[1], arr[2], arr[3], arr[4]))
                .collect(Collectors.toList());
    }

    //retailer transactions
    public static List<retTransaction> getretTransactions() {
        List<String> trnxLst = retTransaction.getAll();
        return trnxLst.stream()
                .map(record -> record.split(";"))
                .filter(arr -> !arr[0].isEmpty())
                .map(arr -> new retTransaction(arr[0], arr[1], arr[2], arr[3], arr[4]))
                .collect(Collectors.toList());
    }

    public static List<List<String>> getTransactionsCrypto() throws Exception
    {
        // asymmetric cryptography
        AsymmCrypto asymmCrypto = new AsymmCrypto();


        List<Transaction> trns = TransactionAdapter.getTransactions();
        List<List<String>> hashLstAll = new ArrayList<>();
        for (Transaction trnx : trns)
        {
            List<String> hashLst = new ArrayList<>();
            try
            {
                String s1 = asymmCrypto.encrypt(trnx.getVaccineID(), KeyAccess.publicKey());
                String s2 = asymmCrypto.encrypt(trnx.getVaccineName(), KeyAccess.publicKey());
                String s3 = asymmCrypto.encrypt(trnx.getVaccineDateTime(), KeyAccess.publicKey());
                String s4 = asymmCrypto.encrypt(trnx.getVaccineDistributor(), KeyAccess.publicKey());
                String s5 = asymmCrypto.encrypt(trnx.getVaccineRetailer(), KeyAccess.publicKey());
//                 String s10 = symmCrypto.encrypt(trnx.getProductID(), secretKey);
                hashLst.add( s1 );
                hashLst.add( s2 );
                hashLst.add( s3 );
                hashLst.add( s4 );
                hashLst.add( s5 );
//                 hashLst.add( s10 );


                hashLstAll.add(hashLst);

               toDecrypt(s1,s2,s3,s4,s5);


            } catch (Exception ex) {
                Logger.getLogger(TransactionAdapter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return hashLstAll;
    }

    //distributor transactioncrypto
    public static List<List<String>> getdisTransactionsCrypto() throws Exception
    {
        // asymmetric cryptography
        AsymmCrypto asymmCrypto = new AsymmCrypto();


        List<disTransaction> distrns = TransactionAdapter.getdisTransactions();
        List<List<String>> dishashLstAll = new ArrayList<>();
        for (disTransaction distrnx : distrns)
        {
            List<String> dishashLst = new ArrayList<>();
            try
            {
                String s1 = asymmCrypto.encrypt(distrnx.getVaccineID(), KeyAccess.publicKey());
                String s2 = asymmCrypto.encrypt(distrnx.getVaccineName(), KeyAccess.publicKey());
                String s3 = asymmCrypto.encrypt(distrnx.getDisName(), KeyAccess.publicKey());
                String s4 = asymmCrypto.encrypt(distrnx.getDisDate(), KeyAccess.publicKey());
                String s5 = asymmCrypto.encrypt(distrnx.getDisTime(), KeyAccess.publicKey());

                dishashLst.add( s1 );
                dishashLst.add( s2 );
                dishashLst.add( s3 );
                dishashLst.add( s4 );
                dishashLst.add( s5 );

                dishashLstAll.add(dishashLst);

               distoDecrypt(s1,s3,s4,s5);


            } catch (Exception ex) {
                Logger.getLogger(TransactionAdapter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return dishashLstAll;
    }

    //retailer transactioncrypto
    public static List<List<String>> getretTransactionsCrypto() throws Exception
    {
        // asymmetric cryptography
        AsymmCrypto asymmCrypto = new AsymmCrypto();
        MySignature signing = new MySignature();

        List<retTransaction> rettrns = TransactionAdapter.getretTransactions();
        List<List<String>> rethashLstAll = new ArrayList<>();
        for (retTransaction rettrnx : rettrns)
        {
            List<String> rethashLst = new ArrayList<>();
            try
            {
                String s1 = asymmCrypto.encrypt(rettrnx.getVaccineID(), KeyAccess.publicKey());
                String s2 = asymmCrypto.encrypt(rettrnx.getRetailerName(), KeyAccess.publicKey());
                String s3 = asymmCrypto.encrypt(rettrnx.getReceivedDate(), KeyAccess.publicKey());
                String s4 = asymmCrypto.encrypt(rettrnx.getReceivedTime(), KeyAccess.publicKey());
                String ds = signing.sign(s1,s2,s3,s4);

                rethashLst.add( s1 );
                rethashLst.add( s2 );
                rethashLst.add( s3 );
                rethashLst.add( s4 );
                rethashLst.add( ds );

                rethashLstAll.add(rethashLst);

                boolean isValid = signing.verify(s1,s2,s3,s4,ds);
                System.out.println(
                        (isValid)
                                ? "\nCorrect data!"
                                : "\nIncorrect data!"
                );

                rettoDecrypt(s1,s2,s3,s4,ds);


            } catch (Exception ex) {
                Logger.getLogger(TransactionAdapter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return rethashLstAll;
    }

    public static void toDecrypt(String s1, String s2, String s3, String s4, String s5) throws Exception
    {
        AsymmCrypto asymmCrypto = new AsymmCrypto();
        String VaccineName, VaccineID, VaccineMDatetime, VaccineDistributor, VaccineRetailer;
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        VaccineID= asymmCrypto.decrypt(s1, KeyAccess.privateKey());
        VaccineName= asymmCrypto.decrypt(s2, KeyAccess.privateKey());
        VaccineMDatetime= asymmCrypto.decrypt(s3, KeyAccess.privateKey());
        VaccineDistributor= asymmCrypto.decrypt(s4, KeyAccess.privateKey());
        VaccineRetailer= asymmCrypto.decrypt(s5, KeyAccess.privateKey());

        Map<String, Object> product = new HashMap<>();
        product.put("VaccineID", VaccineID.trim());
        product.put("VaccineName", VaccineName.trim());
        product.put("VaccineManufactureDateTime", VaccineMDatetime.trim());
        product.put("Distributor", VaccineDistributor.trim());
        product.put("Retailer", VaccineRetailer.trim());

        db.collection("Vaccines").add(product).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {

            }
        });

    }

    //decrypt distributor
    public static void distoDecrypt (String s1,String s2,String s3, String s4) throws Exception
    {
        AsymmCrypto asymmCrypto = new AsymmCrypto();
        String VaccineID, DistributorName, DistributorDate,DistributorTime;
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        VaccineID= asymmCrypto.decrypt(s1, KeyAccess.privateKey());
        DistributorName= asymmCrypto.decrypt(s2, KeyAccess.privateKey());
        DistributorDate= asymmCrypto.decrypt(s3, KeyAccess.privateKey());
        DistributorTime= asymmCrypto.decrypt(s4, KeyAccess.privateKey());


        Map<String, Object> distributor = new HashMap<>();
        distributor.put("VaccineID", VaccineID.trim());
        distributor.put("DistributorName", DistributorName.trim());
        distributor.put("DistributionDate", DistributorDate.trim());
        distributor.put("DistributionTime", DistributorTime.trim());

        db.collection("DistributionDetails").add(distributor).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {

            }
        });

    }

    //decrypt retailer
    public static void rettoDecrypt (String s1,String s2,String s3, String s4,String ds) throws Exception
    {
        AsymmCrypto asymmCrypto = new AsymmCrypto();
        String VaccineID, RetailerName, ReceivedDate,ReceivedTime,DigitalSign;
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        VaccineID= asymmCrypto.decrypt(s1, KeyAccess.privateKey());
        RetailerName= asymmCrypto.decrypt(s2, KeyAccess.privateKey());
        ReceivedDate= asymmCrypto.decrypt(s3, KeyAccess.privateKey());
        ReceivedTime= asymmCrypto.decrypt(s4, KeyAccess.privateKey());
//        DigitalSign= asymmCrypto.decrypt(s5, KeyAccess.privateKey());


        Map<String, Object> retailer = new HashMap<>();
        retailer.put("VaccineID", VaccineID.trim());
        retailer.put("RetailerName", RetailerName.trim());
        retailer.put("ReceivedDate", ReceivedDate.trim());
        retailer.put("ReceivedTime", ReceivedTime.trim());
        retailer.put("DigitalSignature", ds);

        db.collection("RetailerDetails").add(retailer).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {

            }
        });

    }


}