
package passwordmanager.src.Backend;

public class DatabaseObject {
    
    public static class VaultData{
        public int id;
        public String site;
        public String username;
        public String password;
        public String details;
        
        public VaultData(int id, String site, String username, String password, String details){
            this.id = id;
            this.site = site;
            this.username = username;
            this.password = password;
            this.details = details;
        }
    }

    public static class AdminData{

        public String username;
        public String password;

        public AdminData(String username, String password){
            this.username = username;
            this.password = password;
        }
    }
    
}

