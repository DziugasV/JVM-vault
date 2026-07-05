
package passwordmanager.src.Encryption;

import java.security.SecureRandom;

import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.modes.AEADBlockCipher;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;

import java.util.Arrays;

public class EncryptionService {

    private static final char[] masterKeySet = {'K', 'E', 'Y', 'V','A','L','U','E'};

    //Encrypts password using the MasterKey
    public String encryptData(char[] key, char[] password){
        
        String encryptedPass = "";
        //Clone values to use for this method
        char[] keyClone = key.clone();
        char[] passwordClone = password.clone();

        //Get bytes of masterKey
        byte[] verificationSalt = new byte[16];
        byte[] keyBytes = deriveKey(keyClone, verificationSalt);

        //Get new random salts
        SecureRandom random = new SecureRandom();
        byte[] nonce = new byte[12];
        random.nextBytes(nonce);

        //Cryptographic algorithm setup
        AEADBlockCipher cipher = GCMBlockCipher.newInstance(AESEngine.newInstance());
        
        AEADParameters params = new AEADParameters(
            new KeyParameter(keyBytes),
            128, 
            nonce,
            null
        );

        byte[] text = PBEParametersGenerator.PKCS5PasswordToUTF8Bytes(passwordClone);

        //The actual exucution of encrypting data
        try {
            cipher.init(true, params);

            byte[] cipherText = new byte[cipher.getOutputSize(text.length)];
            int outputLen = cipher.processBytes(text, 0, text.length, cipherText, 0);
            
            outputLen += cipher.doFinal(cipherText, outputLen);
            
            String encodedVector = java.util.Base64.getEncoder().encodeToString(nonce);
            String encodedCipher = java.util.Base64.getEncoder().encodeToString(cipherText);
            encryptedPass = encodedVector + ":" + encodedCipher;

        } catch (IllegalStateException | InvalidCipherTextException e) {
            System.out.println("Failed to encrypt: " + e);
        } finally{
            //Nulls data in memory 
            java.util.Arrays.fill(passwordClone, '\0');
            java.util.Arrays.fill(keyClone, '\0');
            java.util.Arrays.fill(keyBytes, (byte) 0);
            java.util.Arrays.fill(text, (byte) 0);
        }

        return encryptedPass;
    }

    public char[] decryptData(char[] key, String encryptedPassword){

        char[] decryptedPass = null;

        //Get key value
        char[] keyClone = key.clone();
        byte[] verificationSalt = new byte[16];
        byte[] keyBytes = deriveKey(keyClone, verificationSalt);
        
        //Seperate the data
        String[] splitData = encryptedPassword.split(":");
        String vector = splitData[0];
        String encryptedPass = splitData[1];

        byte[] vectorDecode = java.util.Base64.getDecoder().decode(vector);
        byte[] passDecode = java.util.Base64.getDecoder().decode(encryptedPass);

        //Setup the Cryptographic algorithm
        AEADBlockCipher cipher = GCMBlockCipher.newInstance(AESEngine.newInstance());
                
        AEADParameters params = new AEADParameters(
            new KeyParameter(keyBytes),
            256, 
            vectorDecode,
            null
        );

        //Get size of the password
        byte[] passTextBytes = new byte[cipher.getOutputSize(passDecode.length)];

        //The main decryption operation
        try {
            cipher.init(false, params);

            int passLen = cipher.processBytes(passDecode, 0, passDecode.length, passTextBytes, 0);
            
            passLen += cipher.doFinal(passTextBytes, passLen);

            java.nio.ByteBuffer byteBuffer = java.nio.ByteBuffer.wrap(passTextBytes, 0, passLen);
            java.nio.CharBuffer charBuffer = java.nio.charset.StandardCharsets.UTF_8.decode(byteBuffer);

            decryptedPass = new char[charBuffer.remaining()];
            charBuffer.get(decryptedPass);
            
            if(charBuffer.hasArray()){
                java.util.Arrays.fill(charBuffer.array(), '\0');
            }

        } catch (IllegalStateException | InvalidCipherTextException e ) {
            System.out.println("Failed to Decrypt: " + e);
        } finally {
            //Clear data
            java.util.Arrays.fill(keyClone, '\0');
            java.util.Arrays.fill(keyBytes, (byte) 0);
            java.util.Arrays.fill(passTextBytes, (byte) 0);
            java.util.Arrays.fill(vectorDecode, (byte) 0);
            java.util.Arrays.fill(passDecode, (byte) 0);        
        }

        return decryptedPass;
    }

    //Ensures Master key size fits SHA 256
    private byte[] deriveKey(char[] key, byte[] salt){

        int iterations = 20000;

        byte[] passBytes = PBEParametersGenerator.PKCS5PasswordToUTF8Bytes(key);
        byte[] derivedKeyBytes = null;
    
        try {
            PKCS5S2ParametersGenerator gen = new PKCS5S2ParametersGenerator(new SHA256Digest());
            gen.init(passBytes, salt, iterations);

            KeyParameter par = (KeyParameter) gen.generateDerivedMacParameters(256);
            derivedKeyBytes = par.getKey();

        } finally{
            Arrays.fill(passBytes, (byte) 0);
        }
        return derivedKeyBytes;
    }
    
    //Used to verify user Master key 
    public boolean verifyMasterKey(char[] key, String encryptedPassword){

        char[] decryptPassword = null;
        boolean isValid = false;

        //Clone and decrypt the masterKey phrase
        try {
            char[] keyClone = key.clone();

            decryptPassword = decryptData(keyClone, encryptedPassword);
            if(decryptPassword != null && java.util.Arrays.equals(decryptPassword, masterKeySet)){
                isValid = true;
            }

        } catch (Exception e) {
            System.out.println("Error: " + e);
        } finally{
            if(decryptPassword != null){
                java.util.Arrays.fill(decryptPassword, '\0');
            }
        }

        return isValid;
    }

    //Encryption of the master key
    public String setMasterKey(char[] key){
        //Encrypt masterKeySet using users special key term
        String masterKeyValidation = encryptData(key, masterKeySet);

        return masterKeyValidation;
    }

    public static void main(String[] args) {
        
        EncryptionService crypto = new EncryptionService();

        System.out.println("--- RUNNING CRYPTO PIPELINE TEST ---\n");

        // 1. Test setMasterKey
        char[] masterKey1 = {'M', 'a', 's', 't', 'e', 'r', '1', '2', '3'};
        String validationToken = crypto.setMasterKey(masterKey1);
        System.out.println("1. Master Key Verification String (Stored in DB):");
        System.out.println(validationToken + "\n");

        // 2. Test encryptData
        char[] masterKey2 = {'M', 'a', 's', 't', 'e', 'r', '1', '2', '3'};
        char[] secretData = {'M', 'y', 'S', 'e', 'c', 'r', 'e', 't', 'D', 'a', 't', 'a'};
        
        String encryptedResult = crypto.encryptData(masterKey2, secretData);
        System.out.println("2. Encrypted Password Data String (Stored in DB):");
        System.out.println(encryptedResult + "\n");

        // 3. Test decryptData
        char[] masterKey3 = {'M', 'a', 's', 't', 'e', 'r', '1', '2', '3'};
        char[] decryptedResult = crypto.decryptData(masterKey3, encryptedResult);
        
        System.out.println("3. Decrypted Password Result:");
        if (decryptedResult != null) {
            System.out.println(new String(decryptedResult));
        } else {
            System.out.println("Decryption failed (null returned).");
        }
        System.out.println();

        // 4. Verify match and perform final UI-side cleanup
        char[] expectedOriginal = {'M', 'y', 'S', 'e', 'c', 'r', 'e', 't', 'D', 'a', 't', 'a'};
        if (decryptedResult != null && Arrays.equals(decryptedResult, expectedOriginal)) {
            System.out.println("Verification Check: SUCCESS! Decrypted text matches the original secret.");
        } else {
            System.out.println("Verification Check: FAILURE! Data mismatch or corruption occurred.");
        }

        // Clean up the final plain-text array left over in our test runner
        if (decryptedResult != null) {
            Arrays.fill(decryptedResult, '\0');
        }

    }
}
