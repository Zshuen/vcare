
package Cryptography;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.*;
import java.util.Base64;

public class KeyPairMaker
{
    private KeyPairGenerator keygen;
    private KeyPair keyPair;

    // constructor
    public KeyPairMaker() throws Exception
    {
        keygen = KeyPairGenerator.getInstance(Config.ALGORITHM);
        keygen.initialize(1024);
    }

    // utility operation
    public static void create()
    {
        try
        {
            KeyPairMaker maker = new KeyPairMaker();
            // generate keypair
            maker.keyPair = maker.keygen.generateKeyPair();
            // get public key
            PublicKey publicKey = maker.keyPair.getPublic();
            // get private key
            PrivateKey privateKey = maker.keyPair.getPrivate();
            // store keypair
            put(publicKey.getEncoded(), Config.PUBLICKEY_FILE);
            put(privateKey.getEncoded(), Config.PRIVATEKEY_FILE);
            // view
            System.out.println(Base64.getEncoder().encodeToString(publicKey.getEncoded()));
            System.out.println(Base64.getEncoder().encodeToString(privateKey.getEncoded()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // put
    private static void put(byte[] keyBytes, String loc)
    {
        File file = new File(loc);
        file.getParentFile().mkdirs();
        try
        {
            Files.write(Paths.get(loc), keyBytes, StandardOpenOption.CREATE);
            System.out.println("Done...");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
