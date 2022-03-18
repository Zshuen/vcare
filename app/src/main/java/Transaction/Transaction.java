
package Transaction;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
//import com.example.vaccare.productfile;


public class Transaction {

    private static final String FILENAME = "/data/user/0/com.example.vaccare/files/vaccineproduct.txt";

    private String ProductName;
    private String ProductID;
    private String ProductDateTime;
    private String ProductDistributor;
    private String ProductRetailer;

    public Transaction(String ProductName,String ProductID,String ProductDateTime, String ProductDistributor, String ProductRetailer){
        this.ProductName=ProductName;
        this.ProductID=ProductID;
        this.ProductDateTime=ProductDateTime;
        this.ProductDistributor=ProductDistributor;
        this.ProductRetailer=ProductRetailer;
    }

    Transaction() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getVaccineName() {
        return ProductName;
    }

    public String getVaccineID() {
        return ProductID;
    }

    public String getVaccineDateTime() { return ProductDateTime; }

    public String getVaccineDistributor() {
        return ProductDistributor;
    }

    public String getVaccineRetailer() {
        return ProductRetailer;
    }



    public static List<String> getAll(){
        try {
            return Files.readAllLines(Paths.get(FILENAME)).stream().collect(Collectors.toList());
        } catch (IOException ex) {
            //Logger.getLogger(productfile.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    public static void empty(){
        try {
            FileChannel.open(Paths.get(FILENAME), StandardOpenOption.WRITE).truncate(0).close();
        } catch (IOException ex) {
            Logger.getLogger(Transaction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String toString()
    {
        return "Product Details{" + "Product Name = " + ProductName + "Product ID= " + ProductID + "Manufacture Date Time= " + ProductDateTime +  "Product Distributor= " + ProductDistributor + "Product Retailer= " + ProductRetailer+ '}';
    }



}
