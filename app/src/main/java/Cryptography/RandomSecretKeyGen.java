
package Cryptography;

import java.security.*;
import javax.crypto.*;

public class RandomSecretKeyGen {

    //ALGORITHM
    private static final String ALGORITHM = "AES";

    private static KeyGenerator keygen;

    public static Key genKey() {
        try {
            keygen = KeyGenerator.getInstance(ALGORITHM);
            keygen.init(128, new SecureRandom());
            return keygen.generateKey();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
