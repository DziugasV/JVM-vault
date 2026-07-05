
package passwordmanager.db;

import java.io.File;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import passwordmanager.src.Backend.DatabaseObject;

public class dbSrc {

    private final String databasePath = "jdbc:sqlite:vault.db";
    // use MySqlLite

    public boolean databaseExist(){
        
        String path = databasePath.replace("jdbc:sqlite:", "");
        File dbFile = new File(path);

        if(dbFile.exists() && dbFile.length() > 0){
            return true;
        }
        else{
            return false;
        }
    }

    //Creates the basic layout of the DB
    public boolean databaseCreate(){

        var url = databasePath;

        var adminTable = "CREATE TABLE IF NOT EXISTS admin (" 
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "username TEXT,"
                        + "password TEXT,"
                        + "key TEXT"
                        + ");";

        var vaultTable = "CREATE TABLE IF NOT EXISTS vault ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "site TEXT,"
                        + "username TEXT,"
                        + "password TEXT,"
                        + "details TEXT"
                        + ");";

        try (var conn = DriverManager.getConnection(url)){
            var stmt = conn.createStatement();
            
            stmt.execute(adminTable);
            stmt.execute(vaultTable);

            System.out.println("Table creation [CREATED]");

            return true;

        } catch (SQLException e) {
            System.out.print("Table creation [FAILED]: " + e);
            return false;
        }
    }

    public boolean addToDatabase(String site, String username, String password, String details){

        boolean returnValue = false;
        var url = databasePath;
        
        var insertQuerry = "INSERT INTO vault(site, username, password, details) VALUES(?,?,?,?)";

        try(var conn = DriverManager.getConnection(url);
            var stmt = conn.prepareStatement(insertQuerry)){
            
            stmt.setString(1, site);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.setString(4, details);

            stmt.executeUpdate();

            returnValue = true;

        } catch (SQLException e) {
            System.out.print("Adding data failed: " + e);
            returnValue = false;
        }

        return returnValue;
    }

    public boolean delleteFromDatabase(int id){

        boolean returnValue = false;
        var url = databasePath;
        
        var deleteQuerry = "DELETE FROM vault WHERE id = ? ";

        try(var conn = DriverManager.getConnection(url);
            var stmt = conn.prepareStatement(deleteQuerry)) {
            
            stmt.setInt(1, id);

            int deleteCheck = stmt.executeUpdate();

            if(deleteCheck > 0){
                returnValue = true;
            }
            else{
                returnValue = false;
            }

        } catch (SQLException e) {
            System.out.print("Delete failed: " + e);
            returnValue = false;
        }

        return returnValue;

    }

    public boolean updateInDatabase(int id, String site, String username, String password, String details){
        
        boolean returnValue = false;
        var url = databasePath;

        var insertQuerry = "UPDATE vault SET site = ?," 
                        + "username = ?," 
                        + "password = ?," 
                        + "details = ? "
                        + "WHERE id = ?";

        try(var conn = DriverManager.getConnection(url);
            var stmt = conn.prepareStatement(insertQuerry)){
            
            stmt.setString(1, site);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.setString(4, details);
            stmt.setInt(5, id);

            stmt.executeUpdate();

            returnValue = true;

        } catch (SQLException e) {
            System.out.print("Adding data failed: " + e);
            returnValue = false;
        }

        return returnValue;
    }

    public void searchDatabase(String value){

    }

    //Instert to admin
    public boolean addToAdminDB(String username, String password, String key){

        boolean returnValue = false;
        var url = databasePath;
        
        var insertQuerry = "INSERT INTO admin(username, password, key) VALUES(?,?,?)";

        try(var conn = DriverManager.getConnection(url);
            var stmt = conn.prepareStatement(insertQuerry)){
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, key);

            stmt.executeUpdate();

            returnValue = true;

        } catch (SQLException e) {
            System.out.print("Adding data failed: " + e);
            returnValue = false;
        }

        return returnValue;
    }

    //Layout to get Data from Database Tables

    //Map
    @FunctionalInterface
    public interface DatabasesSetMap<T> {
        T map(ResultSet rs) throws SQLException;
    }

    //SQL boiler plate to get data
    public <T> ArrayList<T> boilerPlateGetFromDatabase(String sql, DatabasesSetMap<T> mapSet){

        ArrayList<T> list = new ArrayList<T>();
        var url = databasePath;

        try (var conn = DriverManager.getConnection(url);
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {

                T mapValues = mapSet.map(rs);
                list.add(mapValues);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching data: " + e);
        }

        return list;
    }

    //Specifc data to get methods
    public ArrayList<DatabaseObject.AdminData> getAdminCredentials(){

        String sql = "SELECT username, password FROM admin";

        return boilerPlateGetFromDatabase(sql, rs -> new DatabaseObject.AdminData(
            rs.getString("username"),
            rs.getString("password")
        ));

    }

    public ArrayList<String> getKeyValue(){

        String sql = "SELECT key FROM admin";

        return boilerPlateGetFromDatabase(sql, rs ->
            rs.getString("key")
        );
    }

    public ArrayList<DatabaseObject.VaultData> getDataFromVault(){

        String sql =  "SELECT  id, site, username, password, details FROM vault";

        return boilerPlateGetFromDatabase(sql, rs -> new DatabaseObject.VaultData(
            rs.getInt("id"),
            rs.getString("site"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("details")
        ));
    }

    public static void main(String[] args) throws ClassNotFoundException {
        
       dbSrc db = new dbSrc();

       ArrayList<DatabaseObject.AdminData> admin = new ArrayList<>(db.getAdminCredentials());
       DatabaseObject.AdminData aData = admin.get(0);
       String username = aData.getUsername();
       String password = aData.getPassword();

       System.out.println("Username: " + username);
       System.out.println("Password: " + password);

    }

}
