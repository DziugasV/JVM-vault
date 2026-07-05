package passwordmanager.src.Backend;

import java.util.ArrayList;

import passwordmanager.db.dbSrc;
import passwordmanager.src.Encryption.EncryptionService;
import passwordmanager.src.Encryption.HashingService;

public class Comparitor {

    public void justPrintTheValues(String username, char[] passworrd, char[] key){
        System.out.println("These should be values of input");
        
        System.out.println(username);
        for(int i = 0; i < passworrd.length; i++){
            System.out.print(passworrd[i]);
        }
        System.out.println();
        for(int i = 0; i < key.length; i++){
            System.out.print(key[i]);
        }

    }

    //Check for database and make if dosent exist
    public boolean databaseVerify(){

        dbSrc database = new dbSrc();

        if(database.databaseExist() == true){
            return true;
        }
        if(database.databaseExist() == false){
            try {
                database.databaseCreate();
                System.out.println("Made database");
                return true;
            } catch (Exception e) {
                System.out.println("Cant find or make database: " + e);
                return false;
            }
        }
        else{
            return false;
        }
    }

    public boolean isUserCredentialsExists(){
        
        boolean flag = false;

        dbSrc database = new dbSrc();

        ArrayList<String> kData = new ArrayList<>(database.getKeyValue());
        ArrayList<DatabaseObject.AdminData> aData = new ArrayList<>(database.getAdminCredentials()); 

        if(aData.size() > 0 && kData.size() > 0){
            flag = true;
        }
        else{
            flag = false;
        }

        return flag;

    }

    //Add users credentials to database and encrypt
    public boolean singUpDataHandeling(String username, char[] password, char[] key){

        String hashedAdminPassword;
        String encryptedAdminKey;

        HashingService hash = new HashingService();
        EncryptionService encryption = new EncryptionService();
        dbSrc database = new dbSrc();

        hashedAdminPassword = hash.HasingData(password);
        encryptedAdminKey = encryption.setMasterKey(key);

        boolean flag = false;

        //Try to insert data to DB if exists
        try{
            if(database.addToAdminDB(username, hashedAdminPassword, encryptedAdminKey) == true){
                flag = true;
            }
        }
        catch(Exception e){
            System.out.println("Database error during sign up: " + e);
        }
        //Wipe key as encryption method dosent do so
        //Hasing password dose wipe the char[]
        finally{
            java.util.Arrays.fill(key, '\0');
        }

        return true;
    }

    //Veifys addmin credentials
    public boolean signInValidateCredentials(String username, char[] password){

        boolean flag = false;
        
        try{
            dbSrc database = new dbSrc();
            HashingService hash = new HashingService();
            
            ArrayList<DatabaseObject.AdminData> aData = new ArrayList<>(database.getAdminCredentials());
            DatabaseObject.AdminData adminData = aData.get(0);

            String storedUsername = adminData.getUsername();
            String storedPassword = adminData.getPassword();

            if(storedUsername.equals(username) && hash.HashDataVerify(password, storedPassword)){
                flag = true;
            }
        }
        finally{
            if(password != null){
                java.util.Arrays.fill(password, '\0');
            }
        }

        return flag;
    }

    //Adds data to vault db
    public boolean addValuesToVault(String site, String username, char[] password, String detials){

        EncryptionService encryption = new EncryptionService();
        boolean flag = false;

        try {

            //Gauna Key, check if true
            //jei true encrypt pass ir idet i db
            
            encryption.encryptData(password, password);

        } catch (Exception e) {
            System.out.println("Error while inserting multiple values to Vault: " + e);
        }
        finally{
            java.util.Arrays.fill(password, '\0');
        }

        return true;

    }

    public static void main(String[] args) {
        
        Comparitor c = new Comparitor();

        System.out.println(c.databaseVerify());
        
    }

    /*
        Gaunu username ir password string ir char
        Man reikia abu juos hasint
        kai hash pavyksta man reikia ji nusiust i db 
        is hash ir db turi buti 2 trues kad pavyko 
        tada galiu nusiusti i log in panel is sign in panel
    */

    // char[] or byte[] should be zeroed out after use

    /*  Sign up

        get user info from Sign up
        turn password and username to char[]
        turn password and username to byte[char size]
        send password and username to hash algo
        get password and username hash to a string
        send the hash values to db for storage
    */

    /*  Log in

        get user input
        turn password and username to char[]
        turn password and username to byte[char size]
        send password and username to hash algo
        get the new hash value compare whit old hash value
        send result true or falst if matched
        true or flase password and username seperate should give 2 values
        if password or username wrong give pop up try again
        (time limit the input amounts ????)
    */

    /*
        Main vault of password
        something something no fucking clue
    */
}
