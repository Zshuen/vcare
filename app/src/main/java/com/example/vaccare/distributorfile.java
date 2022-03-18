package com.example.vaccare;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class distributorfile {

    private String ProductID;
    private String ProductName;
    private String DistributorName;
    private String DistributeDate;
    private String DistributeTime;


    public distributorfile(String ProductID,String ProductName,String DistributorName, String DistributeDate,String DistributeTime){
        this.ProductID=ProductID;
        this.ProductName=ProductName;
        this.DistributorName=DistributorName;
        this.DistributeDate=DistributeDate;
        this.DistributeTime=DistributeTime;
    }

    @Override
    public String toString()
    {
        return "Product ID= " + ProductID + "Product Name= " + ProductName +"Distributor Name= " + DistributorName + "Distribution Date= "+ DistributeDate+ "Distribution Time= "+ DistributeTime;
    }

    public void distributorfile()
    {
        File ProductF = new File ("/data/user/0/com.example.vaccare/files/vaccinedistributor.txt");
        try
        {
            FileWriter fw = new FileWriter(ProductF, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            String Line = ProductID + ";" + ProductName + ";" + DistributorName+ ";" +DistributeDate + ";" + DistributeTime + ";";
            bw.newLine();
            pw.write(Line);
            pw.close();
        }
        catch (IOException Ex)
        {

        }
    }
}
