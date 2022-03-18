//
//package block;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.ObjectOutputStream;
//import java.io.Serializable;
//import hasher.hasher;
//import java.util.Calendar;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import Transaction.Transaction;
//
//public class Block implements Serializable {
//
//    public Block(List<Transaction> data, String previoushash)
//    {
//        this.previoushash = previoushash;
//        this.data = data;
//        this.timestamp = Calendar.getInstance().getTimeInMillis();
//        this.currentHash = this.blockHashCode(Block.genByteArr(this.data), this.previoushash, this.timestamp);
//    }
//
//
//    private String currentHash;
//    private String previoushash;
//    private List<Transaction> data;
//    private long timestamp;
//
//    public List<Transaction> getData() {
//        return data;
//    }
//
//    public void setData(List<Transaction> data) {
//        this.data = data;
//    }
//
//    public String getCurrentHash() {
//        return currentHash;
//
//    }
//
//    public String getPrevioushash() {
//        return previoushash;
//    }
//
//    public void setCurrentHash(String currentHash) {
//        this.currentHash = currentHash;
//    }
//
//    public void setPrevioushash(String previoushash) {
//        this.previoushash = previoushash;
//    }
//
//    @Override
//    public int hashCode() {
//        return super.hashCode();
//    }
//
//
//    public String blockHashCode(byte[] data, String prehash, long tmestamp) {
//        return hasher.hash(
//                data + prehash + tmestamp,
//                "SHA-256");
//    }
//
//    //    private static byte[] genByteArr(Block b) {
//    private static byte[] genByteArr(List<Transaction> b) {
//        ByteArrayOutputStream boas = new ByteArrayOutputStream();
//        ObjectOutputStream out;
//        if (b != null) {
//            try {
//                out = new ObjectOutputStream(boas);
//                out.writeObject(b);
//                out.flush();
//            } catch (IOException ex) {
//                System.out.println(ex.getMessage());
//                return null;
//            }
//            return boas.toByteArray();
//        } else {
//            return null;
//        }
//    }
//
//    @Override
//    public String toString() {
//        return "Current Hash:" + this.currentHash + "\nPrevious Hash:" + this.previoushash + "\n";
//    }
//
//}
//
//
//
////package block;
////
////import java.io.ByteArrayOutputStream;
////import java.io.IOException;
////import java.io.ObjectOutputStream;
////import java.io.Serializable;
////import hasher.hasher;
////import java.util.Calendar;
////import java.util.List;
////import java.util.stream.Collectors;
////import org.javatuples.Decade;
////
////public class Block implements Serializable {
////
////    public Block(List<List<String>> data, String previoushash)
////    {
////        List<Decade<String, String, String, String, String, String, String, String, String, String>> hashes =
////                data.stream().map( elem -> Decade.fromCollection(elem) ).collect(Collectors.toList());
////        this.previoushash = previoushash;
////        this.data = hashes;
////        this.timestamp = Calendar.getInstance().getTimeInMillis();
////        this.currentHash = this.blockHashCode(Block.genByteArr(this.data), this.previoushash, this.timestamp);
////    }
////
////
////    private String currentHash;
////    private String previoushash;
////    private List<Decade<String, String, String, String, String, String, String, String, String, String>> data;
////    private long timestamp;
////
////    public List<Decade<String, String, String, String, String, String, String, String, String, String>> getData() {
////        return data;
////    }
////
////    public void setData(List<Decade<String, String, String, String, String, String, String, String, String, String>> data) {
////        this.data = data;
////    }
////
////    public String getCurrentHash() {
////        return currentHash;
////
////    }
////
////    public String getPrevioushash() {
////        return previoushash;
////    }
////
////    public void setCurrentHash(String currentHash) {
////        this.currentHash = currentHash;
////    }
////
////    public void setPrevioushash(String previoushash) {
////        this.previoushash = previoushash;
////    }
////
////    @Override
////    public int hashCode() {
////        return super.hashCode();
////    }
////
////
////    public String blockHashCode(byte[] data, String prehash, long tmestamp) {
////        return hasher.hash(
////                data + prehash + tmestamp,
////                "SHA-256");
////    }
////
////    //    private static byte[] genByteArr(Block b) {
////    private static byte[] genByteArr(List<Decade<String, String, String, String, String, String, String, String, String, String>> b) {
////        ByteArrayOutputStream boas = new ByteArrayOutputStream();
////        ObjectOutputStream out;
////        if (b != null) {
////            try {
////                out = new ObjectOutputStream(boas);
////                out.writeObject(b);
////                out.flush();
////            } catch (IOException ex) {
////                System.out.println(ex.getMessage());
////                return null;
////            }
////            return boas.toByteArray();
////        } else {
////            return null;
////        }
////    }
////
////    @Override
////    public String toString() {
////        return "Current Hash:" + this.currentHash + "\nPrevious Hash:" + this.previoushash + "\n";
////    }
////
////}
//


package block;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import hasher.hasher;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import org.javatuples.Decade;
import org.javatuples.Quintet;

public class Block implements Serializable {

    public Block(List<List<String>> data, String previoushash)
    {
        List<Quintet<String, String, String, String, String>> hashes =
                data.stream().map( elem -> Quintet.fromCollection(elem) ).collect(Collectors.toList());
        this.previoushash = previoushash;
        this.data = hashes;
        this.timestamp = Calendar.getInstance().getTimeInMillis();
        this.currentHash = this.blockHashCode(Block.genByteArr(this.data), this.previoushash, this.timestamp);
    }


    private String currentHash;
    private String previoushash;
    private List<Quintet<String, String, String, String, String>> data;
    private long timestamp;

    public List<Quintet<String, String, String, String, String>> getData() {
        return data;
    }

    public void setData(List<Quintet<String, String, String, String, String>> data) {
        this.data = data;
    }

    public String getCurrentHash() {
        return currentHash;

    }

    public String getPrevioushash() {
        return previoushash;
    }

    public void setCurrentHash(String currentHash) {
        this.currentHash = currentHash;
    }

    public void setPrevioushash(String previoushash) {
        this.previoushash = previoushash;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }


    public String blockHashCode(byte[] data, String prehash, long tmestamp) {
        return hasher.hash(
                data + prehash + tmestamp,
                "SHA-256");
    }

    private static byte[] genByteArr(List<Quintet<String, String, String, String, String>> b) {
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        ObjectOutputStream out;
        if (b != null) {
            try {
                out = new ObjectOutputStream(boas);
                out.writeObject(b);
                out.flush();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
                return null;
            }
            return boas.toByteArray();
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "Current Hash:" + this.currentHash + "\nPrevious Hash:" + this.previoushash + "\n";
    }

}
