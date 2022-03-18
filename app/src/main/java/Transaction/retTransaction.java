package Transaction;

import com.example.vaccare.retailerfile;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class retTransaction {

    private static final String FILENAME = "/data/user/0/com.example.vaccare/files/vaccineretailer.txt";

    private String ProductID;
    private String RetailerName;
    private String ReceivedDate;
    private String ReceivedTime;
    private String DigitalSign;


    public retTransaction(String ProductID,String RetailerName,String ReceivedDate, String ReceivedTime,String DigitalSign){
        this.ProductID=ProductID;
        this.RetailerName=RetailerName;
        this.ReceivedDate=ReceivedDate;
        this.ReceivedTime=ReceivedTime;
        this.DigitalSign=DigitalSign;
    }

    retTransaction() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getVaccineID() {
        return ProductID;
    }
    public String getRetailerName() {
        return RetailerName;
    }
    public String getReceivedDate() {
        return ReceivedDate;
    }
    public String getReceivedTime() {
        return ReceivedTime;
    }
    public String getDigitalSign() { return DigitalSign; }



    public static List<String> getAll(){
        try {
            return Files.readAllLines(Paths.get(FILENAME)).stream().collect(Collectors.toList());
        } catch (IOException ex) {
            Logger.getLogger(retailerfile.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    public static void empty(){
        try {
            FileChannel.open(Paths.get(FILENAME), StandardOpenOption.WRITE).truncate(0).close();
        } catch (IOException ex) {
            Logger.getLogger(retTransaction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String toString()
    {
        return "Retailer Details{" + "Product ID= " + ProductID + "Retailer Name= " + RetailerName +"Received Date= " + ReceivedDate + "Received Time= "+ ReceivedTime+ "Digital Signature= "+ DigitalSign +'}';
    }
}
