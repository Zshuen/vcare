
package DigitalSignature;

import java.security.Signature;
import java.util.Base64;
import Cryptography.*;

public class MySignature
{
    private Signature signature;

    private final String SIGNING_ALGORITHM = "SHA256WithRSA";

    public MySignature()
    {
        try
        {
            signature = Signature.getInstance(SIGNING_ALGORITHM);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    // sign
    public String sign(String s1, String s2, String s3, String s4) throws Exception
    {
        signature.initSign(KeyAccess.privateKey());
        signature.update(s1.getBytes());
        signature.update(s2.getBytes());
        signature.update(s3.getBytes());
        signature.update(s4.getBytes());
        return Base64.getEncoder().encodeToString(signature.sign());
    }
    // verify
    public boolean verify (String s1, String s2, String s3, String s4, String ds) throws Exception
    {
        signature.initVerify(KeyAccess.publicKey());
        signature.update(s1.getBytes());
        signature.update(s2.getBytes());
        signature.update(s3.getBytes());
        signature.update(s4.getBytes());
        return signature.verify(Base64.getDecoder().decode(ds));
    }
}
