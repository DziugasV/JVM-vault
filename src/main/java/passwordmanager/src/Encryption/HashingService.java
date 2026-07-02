
package passwordmanager.src.Encryption;

import java.security.SecureRandom;

import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

public class HashingService {
    
    //Method to Hash data
    public String HasingData(char[] password){
        
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        //Setup Bouncy Castle Argon2id
        Argon2BytesGenerator gen = new Argon2BytesGenerator();
        Argon2Parameters params = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
            .withVersion(Argon2Parameters.ARGON2_VERSION_13)
            .withIterations(3)
            .withMemoryAsKB(100000)
            .withParallelism(4)
            .withSalt(salt)
            .build();
        
        gen.init(params);

        byte[] hash = new byte[32];
        byte[] passwordBytes = PBEParametersGenerator.PKCS5PasswordToUTF8Bytes(password);

        //generate the hash
        try{
            gen.generateBytes(passwordBytes, hash);
        }
        //wipe sensitive data
        finally{
            java.util.Arrays.fill(password, '\0');
            java.util.Arrays.fill(passwordBytes, (byte) 0);
            java.util.Arrays.fill(hash, (byte) 0);
        }

        String encodeSalt = java.util.Base64.getEncoder().encodeToString(salt);
        String encodeHash = java.util.Base64.getEncoder().encodeToString(hash);
        
        return encodeSalt + ":" + encodeHash;
    }

    public Boolean HashDataVerify(char[] password, String credentials){

        //Get salt
        String[] hashedValues = credentials.split(":");

        String saltEncoded = hashedValues[0];
        byte[] hashCredentials = java.util.Base64.getDecoder().decode(hashedValues[1]);
        
        byte[] saltDecoded = java.util.Base64.getDecoder().decode(saltEncoded);

        //Setup Bouncy Castle Argon2id
        Argon2BytesGenerator gen = new Argon2BytesGenerator();
        Argon2Parameters params = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
            .withVersion(Argon2Parameters.ARGON2_VERSION_13)
            .withIterations(3)
            .withMemoryAsKB(100000)
            .withParallelism(4)
            .withSalt(saltDecoded)
            .build();
        
        gen.init(params);

        byte[] hash = new byte[32];
        byte[] passwordBytes = PBEParametersGenerator.PKCS5PasswordToUTF8Bytes(password);

        //Get hash value
        gen.generateBytes(passwordBytes, hash);

        //Wipe char[] memory
        java.util.Arrays.fill(password, '\0');
        java.util.Arrays.fill(passwordBytes, (byte) 0);

        //Compare if values are same in db and newly enterd
        return java.security.MessageDigest.isEqual(hash, hashCredentials);
    }

}


