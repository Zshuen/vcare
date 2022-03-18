
package Cryptography;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class KeyAccess
{
    // get public key
    public static PublicKey publicKey() throws Exception
    {
        byte[] keyBytes = Files.readAllBytes(Paths.get(Config.PUBLICKEY_FILE));
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        return KeyFactory.getInstance(Config.ALGORITHM).generatePublic(spec);
    }

    // get private key
    public static PrivateKey privateKey() throws Exception
    {
        byte[] keyBytes = Files.readAllBytes(Paths.get(Config.PRIVATEKEY_FILE));
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        return KeyFactory.getInstance(Config.ALGORITHM).generatePrivate(spec);
    }
}
