package passwordmanager.views;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public class ComponentsDash {
    
    private String[][] data = {
        {"Dude 1", "Password", "Site", "Username"},
        {"Dude 2", "Password", "Site", "Username"},
        {"Dude 3", "Password", "Site", "Username"}
    };

    private String[] colName = {
        "Dude", "PasswordCol", "SiteCol", "UsernameCol"
    };

    public JScrollPane tableOfContents(){

        //Add data from db
        
        JTable table = new JTable(data, colName);
        JScrollPane scrollPane = new JScrollPane(table);

        return scrollPane;
    }

}
