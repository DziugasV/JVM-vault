package passwordmanager.views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import passwordmanager.src.Backend.Comparitor;
import passwordmanager.src.Backend.DatabaseObject;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ComponentsDash {
    
    private String[][] data = {
        {"Dude 1", "Password", "Site"},
        {"Dude 2", "Password", "Site"},
        {"Dude 3", "Password", "Site"}
    };

    private String[] colName = {
        "Dude", "PasswordCol", "SiteCol"
    };

    private int WIDTH = 300;
    private int HEIGHT = 300;

    private Dimension elementDimension = new Dimension(150, 30); 
    private Border lineBorder = BorderFactory.createLineBorder(Color.black, 1);
    private Border margin = BorderFactory.createEmptyBorder(5, 5, 5, 5);
    private JTextField searchField;
    private JTextField passwordGen;

    private javax.swing.Timer timer;
    private Comparitor c = new Comparitor();

    private ArrayList<DatabaseObject.VaultData> databaseData;

    public JPanel bottomComponent(){

        JPanel bottomPanel = new JPanel();
        JTable table = new JTable(data, colName);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(
            20,
            30,
            30,
            30
        ));
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(Color.WHITE);

        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.add(scrollPane);

        return bottomPanel;

    }

    public JPanel topComponents(){

        JPanel topComponentPanel = new JPanel();
        JPanel subPasswordGenPanel = new JPanel();
        JPanel subSearchPanel = new JPanel();
        JTextField passwordGenField = customPasswordTF();
        JTextField searchField = customeSearchTF();
        JButton passwordGenButton = customButton("Generator");
        JLabel passwordGenLable = customizedLabel("Password generator");

        Dimension panelMaxDimension = new Dimension(Integer.MAX_VALUE, 40);

        subPasswordGenPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        subPasswordGenPanel.setMaximumSize(panelMaxDimension);
        subPasswordGenPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subPasswordGenPanel.setOpaque(false);
        
        subPasswordGenPanel.add(Box.createHorizontalGlue());
        subPasswordGenPanel.add(passwordGenButton);
        subPasswordGenPanel.add(Box.createHorizontalStrut(5)); 
        subPasswordGenPanel.add(passwordGenField);
        subPasswordGenPanel.add(Box.createHorizontalGlue());

        subSearchPanel.setLayout(new BoxLayout(subSearchPanel, BoxLayout.X_AXIS));
        subSearchPanel.setMaximumSize(panelMaxDimension);
        subSearchPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subSearchPanel.setOpaque(false);
        
        subSearchPanel.add(Box.createHorizontalGlue());
        subSearchPanel.add(Box.createHorizontalStrut(5)); 
        subSearchPanel.add(searchField);
        subSearchPanel.add(Box.createHorizontalGlue());

        topComponentPanel.setLayout(new BoxLayout(topComponentPanel, BoxLayout.Y_AXIS));
        topComponentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topComponentPanel.setOpaque(false);
        
        topComponentPanel.add(passwordGenLable);
        topComponentPanel.add(Box.createVerticalStrut(5));
        topComponentPanel.add(subPasswordGenPanel);
        topComponentPanel.add(Box.createVerticalStrut(10));
        topComponentPanel.add(subSearchPanel);

        return topComponentPanel;
    }

    public JButton customButton(String text){

        JButton customButton = new JButton(text);
        Color customColor = new Color(200, 200, 200);
        Color hoverCollor = new Color(190, 190, 190);
        Color pressedCollor = new Color(175, 175, 175);

        elementDimension = new Dimension(150 / 2, 20);
        customButton.setPreferredSize(elementDimension);
        customButton.setMaximumSize(elementDimension);
        
        customButton.setUI(new javax.swing.plaf.basic.BasicButtonUI());

        customButton.setBackground(customColor);
        customButton.setFocusPainted(false);
        customButton.setForeground(Color.BLACK);

        customButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        customButton.addChangeListener(e -> {
            if(customButton.getModel().isPressed()){
                customButton.setBackground(pressedCollor);
            }
            else if(customButton.getModel().isRollover()){
                customButton.setBackground(hoverCollor);
            }
            else{
                customButton.setBackground(customColor);
            }
        });

        return customButton;
    }

    public JTextField customPasswordTF(){

        JTextField customTextField = new JTextField();
        
        new GhostText(customTextField, "Password Generator");
        
        customTextField.setPreferredSize(elementDimension);
        customTextField.setMaximumSize(elementDimension);
        customTextField.setBorder(BorderFactory.createCompoundBorder(margin, lineBorder));

        return customTextField;
    }

    public JTextField customeSearchTF(){

        JTextField customTextField = new JTextField();

        new GhostText(customTextField, "Search");

        customTextField.setPreferredSize(elementDimension);
        customTextField.setMaximumSize(elementDimension);
        customTextField.setBorder(BorderFactory.createCompoundBorder(margin, lineBorder));
        customTextField.getDocument().addDocumentListener(setUpSearchListener(customTextField));

        return customTextField;
    }

    public DocumentListener setUpSearchListener(JTextField textField){
        
        return new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                typeEvent(textField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                typeEvent(textField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'changedUpdate'");
            }  
        };
    }

    public void typeEvent(String text){
        
        int setTime = 200;
        
        if(timer != null && timer.isRunning()){
            timer.stop();
        }

        ActionListener listener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(text.isEmpty() || text.equals("Search")){
                    databaseData = c.getAllDataFromDatabase();
                }
                else{
                    databaseData = c.searchDataFromDatabase(text);
                }
            }
        };

        timer = new Timer(setTime, listener);
        timer.setRepeats(false);
        timer.start();
    }
    
    public JLabel customizedLabel(String text){

        JLabel customizedLabel = new JLabel(text);

        customizedLabel.setPreferredSize(elementDimension);
        customizedLabel.setMaximumSize(elementDimension);
        customizedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        return customizedLabel;
    }

}

class GhostText implements FocusListener, PropertyChangeListener{

    private final JTextField textfield;
    private boolean isEmpty;
    private Color ghostColor;
    private Color foregroundColor;
    private final String ghostText;

    protected GhostText(final JTextField textfield, String ghostText) {
        super();
        this.textfield = textfield;
        this.ghostText = ghostText;
        this.ghostColor = Color.LIGHT_GRAY;
        textfield.addFocusListener(this);
        registerListeners();
        updateState();
        if (!this.textfield.hasFocus()) {
            focusLost(null);
        }
    }

    public void delete() {
        unregisterListeners();
        textfield.removeFocusListener(this);
    }

    private void registerListeners() {
        textfield.addPropertyChangeListener("foreground", this);
    }

    private void unregisterListeners() {
        textfield.removePropertyChangeListener("foreground", this);
    }

    public Color getGhostColor() {
        return ghostColor;
    }

    public void setGhostColor(Color ghostColor) {
        this.ghostColor = ghostColor;
    }

    private void updateState() {
        String text = textfield.getText();
        Color c = textfield.getForeground();
        isEmpty = text.length() == 0 || c.equals(Color.LIGHT_GRAY);
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (isEmpty) {
            unregisterListeners();
            try {
                textfield.setText("");
                textfield.setForeground(foregroundColor);
            } finally {
                registerListeners();
            }
        }

    }

    @Override
    public void focusLost(FocusEvent e) {
        updateState();
        if (isEmpty) {
            unregisterListeners();
            try {
                textfield.setText(ghostText);
                textfield.setForeground(ghostColor);
            }
            finally {
                registerListeners();
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}

