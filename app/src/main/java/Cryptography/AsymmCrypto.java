
package Cryptography;

import javax.crypto.Cipher;
import java.security.*;
import java.util.Base64;

public class AsymmCrypto
{
    private Cipher cipher;
    // constructor
    public AsymmCrypto()
    {
        try
        {
            cipher = Cipher.getInstance(Config.ALGORITHM);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    // encrypt (String, PublicKey)
    public String encrypt ( String data, PublicKey key ) throws Exception
    {
        String cipherText = null;
        cipher.init(Cipher.ENCRYPT_MODE, key);
        // encrypt
        byte[] cipherBytes = cipher.doFinal(data.getBytes());
        cipherText = Base64.getEncoder().encodeToString(cipherBytes);
        return cipherText;
    }
    // decrypt (String, PrivateKey)
    public String decrypt ( String cipherText, PrivateKey key ) throws Exception
    {
        cipher.init(Cipher.DECRYPT_MODE, key);
        // decrypt
        byte[] dataBytes = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        return new String (dataBytes);
    }
}
