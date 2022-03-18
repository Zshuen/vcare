//package block;
//
//import com.google.gson.GsonBuilder;
//import java.util.LinkedList;
//import java.util.List;
//import Transaction.Transaction;
//import Transaction.TransactionAdapter;
//
//public class bcdemo {
//
//    //data structure
//    static LinkedList<Block> bchain = new LinkedList();
//
//    /**public static void main(String[] args) throws Exception {
//        firstBlock();
//        //nextBlock();
//
//    }*/
//
//    public static void firstBlock() throws Exception {
//
//        System.out.println("--- Transaction objects ---");
//        List<Transaction> trnxPool = TransactionAdapter.getTransactions();
//        trnxPool.stream().forEach(System.out::println);
//
//        Block b1 = new Block(trnxPool, "0"); //genesis block
//        bchain.add(b1);
//        //clear the bcfile.txt
//        Transaction.empty();
//        Blockchain.persist(bchain);
//        //distribute/display the linkedlist elements/blocks
//        out(bchain);
//    }
//
//    public static void nextBlock() throws Exception {
//
//        List<Transaction> trnxPool = TransactionAdapter.getTransactions();
//        bchain = Blockchain.get();
//        Block block = new Block(trnxPool, bchain.getLast().getCurrentHash());
//        bchain.add(block);
//        Transaction.empty();
//        Blockchain.persist(bchain);
//        out(bchain);
//    }
//
//    public static void out(LinkedList<Block> bchain) {
//        String temp = new GsonBuilder().setPrettyPrinting().create().toJson(bchain);
//        System.out.println(temp);
//        Blockchain.distribute(temp);
//    }
//
//}
//
//
//
////package block;
////
////import com.google.gson.GsonBuilder;
////import java.util.LinkedList;
////import java.util.List;
////import Transaction.Transaction;
////import Transaction.TransactionAdapter;
////
////public class bcdemo {
////
////    //data structure
////    static LinkedList<Block> bchain = new LinkedList();
////
////    public static void main(String[] args) throws Exception {
//////        firstBlock();
////        nextBlock();
////
////    }
////
////    public static void firstBlock() throws Exception {
////
////        System.out.println("--- Transaction objects ---");
////        List<Transaction> trnxPool = TransactionAdapter.getTransactions();
////        trnxPool.stream().forEach( System.out::println );
////
////        System.out.println("--- Transactions with hashes ---");
////        List<List<String>> trnxPool_hashes = TransactionAdapter.getTransactionsCrypto();
////        System.out.println( trnxPool_hashes );
////
////
////        Block b1 = new Block(trnxPool_hashes, "0"); //genesis block
////        bchain.add(b1);
////        //clear the bcfile.txt
////        Transaction.empty();
////        Blockchain.persist(bchain);
////        //distribute/display the linkedlist elements/blocks
////        out(bchain);
////    }
////
////    public static void nextBlock() throws Exception{
////        List<List<String>> trnxPool_hashes = TransactionAdapter.getTransactionsCrypto();
////        System.out.println("\nLedger:");
////        bchain = Blockchain.get();
////        Block block = new Block(trnxPool_hashes, bchain.getLast().getCurrentHash() );
////        bchain.add(block);
////        Transaction.empty();
////        Blockchain.persist(bchain);
////        out(bchain);
////    }
////
////    public static void out(LinkedList<Block> bchain){
////        String temp = new GsonBuilder().setPrettyPrinting().create().toJson(bchain);
////        System.out.println( temp );
////        Blockchain.distribute(temp);
////    }
////
////}
//


package block;

import com.example.vaccare.retailermainpage;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import Cryptography.KeyPairMaker;
import Transaction.Transaction;
import Transaction.disTransaction;
import Transaction.retTransaction;
import Transaction.TransactionAdapter;
import block.Blockchain;
import com.example.vaccare.distributormainpage;

public class bcdemo {

    //data structure
    static LinkedList<Block> bchain = new LinkedList();


    public static void firstBlock() throws Exception {

        //create new directory to store Keypair
        final File keystorefile = new File("/data/user/0/com.example.vaccare/files/keystore");
        keystorefile.mkdir();
        //Generate Keypair to encrypt data
        KeyPairMaker.create();
        //get encrypted vaccine details
        List<List<String>> trnxPool_hashes = TransactionAdapter.getTransactionsCrypto();

        //create new block (FIRST vaccine added by admin)
        Block b1 = new Block(trnxPool_hashes, "0"); //genesis block

        if (Blockchain.get()!=null){
            //create new block (second/third...vaccine added by admin)
            bchain = Blockchain.get();
            b1 = new Block(trnxPool_hashes, bchain.getLast().getCurrentHash() );
        }

        //add b1 into the Block
        bchain.add(b1);
        //clear the vaccineproduct.txt
        Transaction.empty();
        //update the binary file
        Blockchain.persist(bchain);
        //distribute/display the linkedlist elements/blocks
        out(bchain);
    }

    public static void nextBlock() throws Exception{
        List<List<String>> distrnxPool_hashes = TransactionAdapter.getdisTransactionsCrypto();
        bchain = Blockchain.get();
        Block block = new Block(distrnxPool_hashes, bchain.getLast().getCurrentHash() );
        bchain.add(block);
        disTransaction.empty();
        Blockchain.persist(bchain);
        out(bchain);
    }


    public static void nextnextBlock() throws Exception{
        List<List<String>> rettrnxPool_hashes = TransactionAdapter.getretTransactionsCrypto();
        bchain = Blockchain.get();
        Block rblock = new Block(rettrnxPool_hashes, bchain.getLast().getCurrentHash() );
        bchain.add(rblock);
        retTransaction.empty();
        Blockchain.persist(bchain);
        out(bchain);
    }

    public static void out(LinkedList<Block> bchain){
        String temp = new GsonBuilder().setPrettyPrinting().create().toJson(bchain);
        System.out.println( temp );
        Blockchain.distribute(temp);
    }

//    public static void disout(LinkedList<Block> bchain){
//        String temp = new GsonBuilder().setPrettyPrinting().create().toJson(bchain);
//        System.out.println( temp );
//        Blockchain.distribute(temp);
//
//    }
//
//    public static void retout(LinkedList<Block> bchain){
//        String temp = new GsonBuilder().setPrettyPrinting().create().toJson(bchain);
//        System.out.println( temp );
//        Blockchain.distribute(temp);
//
//    }

}


