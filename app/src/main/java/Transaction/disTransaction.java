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
import com.example.vaccare.distributorfile;

public class disTransaction {

    private static final String FILENAME = "/data/user/0/com.example.vaccare/files/vaccinedistributor.txt";

    private String ProductID;
    private String ProductName;
    private String DistributorName;
    private String DistributeDate;
    private String DistributeTime;


    public disTransaction(String ProductID,String ProductName,String DistributorName, String DistributeDate,String DistributeTime){
        this.ProductID=ProductID;
        this.ProductName=ProductName;
        this.DistributorName=DistributorName;
        this.DistributeDate=DistributeDate;
        this.DistributeTime=DistributeTime;
    }

    disTransaction() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getVaccineID() {
        return ProductID;
    }
    public String getVaccineName() {
        return ProductName;
    }
    public String getDisName() {
        return DistributorName;
    }
    public String getDisDate() {
        return DistributeDate;
    }
    public String getDisTime() { return DistributeTime; }



    public static List<String> getAll(){
        try {
            return Files.readAllLines(Paths.get(FILENAME)).stream().collect(Collectors.toList());
        } catch (IOException ex) {
            Logger.getLogger(distributorfile.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    public static void empty(){
        try {
            FileChannel.open(Paths.get(FILENAME), StandardOpenOption.WRITE).truncate(0).close();
        } catch (IOException ex) {
            Logger.getLogger(disTransaction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String toString()
    {
        return "Distributors Details{" + "Product ID= " + ProductID + "Product Name= " + ProductName +"Distributor Name= " + DistributorName + "Distribution Date= "+ DistributeDate+ "Distribution Time= "+ DistributeTime +'}';
    }
}
