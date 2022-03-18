package com.example.vaccare;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class retailerfile {

    private String ProductID;
    private String RetailerName;
    private String ReceivedDate;
    private String ReceivedTime;
    private String Digitalsign;


    public retailerfile(String ProductID,String RetailerName,String ReceivedDate, String ReceivedTime,String Digitalsign){
        this.ProductID=ProductID;
        this.RetailerName=RetailerName;
        this.ReceivedDate=ReceivedDate;
        this.ReceivedTime=ReceivedTime;
        this.Digitalsign=Digitalsign;
    }

    @Override
    public String toString()
    {
        return "Product ID= " + ProductID + "Retailer Name= " + RetailerName +"Received Date= " + ReceivedDate + "Received Time= "+ ReceivedTime+ "Digital Signature= "+ Digitalsign;
    }

    public void retailerfile()
    {
        File ProductF = new File ("/data/user/0/com.example.vaccare/files/vaccineretailer.txt");
        try
        {
            FileWriter fw = new FileWriter(ProductF, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            String Line = ProductID + ";" + RetailerName + ";" + ReceivedDate+ ";" +ReceivedTime + ";" + Digitalsign + ";";
            bw.newLine();
            pw.write(Line);
            pw.close();
        }
        catch (IOException Ex)
        {

        }
    }
}
