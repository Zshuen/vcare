//
//package block;
//
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.nio.file.StandardOpenOption;
//import java.util.LinkedList;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//
//public class Blockchain {
//
//    private static final String CHAIN_OBJFILE = "chainobj.dat";
//
//    public static void persist(LinkedList<Block> chain) {
//
//        try (FileOutputStream fos = new FileOutputStream(CHAIN_OBJFILE);
//             ObjectOutputStream out = new ObjectOutputStream(fos)) {
//            out.writeObject(chain);
//        } catch (Exception e) {
//        }
//
//    }
//
//    public static LinkedList<Block> get() {
//
//        try (FileInputStream fis = new FileInputStream(CHAIN_OBJFILE);
//             ObjectInputStream in = new ObjectInputStream(fis)) {
//            return (LinkedList<Block>) in.readObject();
//        } catch (Exception e) {
//            return null;
//        }
//
//    }
//
//    public static void distribute( String temp ){
//        try {
//            Files.write(Paths.get("/data/user/0/com.example.vaccare/files/vaccineledger.txt"), temp.getBytes(), StandardOpenOption.CREATE);
//        } catch (IOException ex) {
//            Logger.getLogger(bcdemo.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//}
//



package block;

import android.widget.TextView;

import com.example.vaccare.R;
import com.example.vaccare.addproduct;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Blockchain {

    private static final String CHAIN_OBJFILE = "/data/user/0/com.example.vaccare/files/chainobj.dat";

    public static void persist(LinkedList<Block> chain) {
        try (FileOutputStream fos = new FileOutputStream(CHAIN_OBJFILE);
             ObjectOutputStream out = new ObjectOutputStream(fos)) {
            out.writeObject(chain);
        } catch (Exception e) {
        }
    }

    public static LinkedList<Block> get() {
        try (FileInputStream fis = new FileInputStream(CHAIN_OBJFILE);
             ObjectInputStream in = new ObjectInputStream(fis)) {
            return (LinkedList<Block>) in.readObject();
        } catch (Exception e) {
            return null;
        }
    }

    public static void distribute( String temp ){
        //create new directory to store blockchain ledger text file
        final File newFile = new File("/data/user/0/com.example.vaccare/files/bcledger");
        newFile.mkdir();
        try {
            Files.write(Paths.get("/data/user/0/com.example.vaccare/files/bcledger/vaccineledger.txt"), temp.getBytes(), StandardOpenOption.CREATE);
        } catch (IOException ex) {
            Logger.getLogger(bcdemo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

  //  TextView ProductID;

//    public static void distribute( String temp ){
//
//        addproduct ap = new addproduct();
//       // File LedgerF = new File ("/data/user/0/com.example.vaccare/files/" +ap.getproductID()+"ledger.txt");
//      String productid = ap.ProductUUID;
//        try {
//            Files.write(Paths.get("/data/user/0/com.example.vaccare/files/"+productid+"ledger.txt"), temp.getBytes(), StandardOpenOption.CREATE);
//        } catch (IOException ex) {
//            Logger.getLogger(bcdemo.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
}

