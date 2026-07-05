package passwordmanager.views;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

import passwordmanager.src.Backend.Comparitor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

//Set up of multiple components and actions
//Ui component class for SignUp and Log in panels

public class ComponentsLS implements ActionListener, DocumentListener{

    private JTextField usernameField = new JTextField();
    private JPasswordField passwordField = new JPasswordField();
    private JPasswordField keyField = new JPasswordField();
    private JLabel returnMsg = new JLabel();
    private JButton button;

    private int height = 25;
    private int width = 200;
    private int gapSize = 15;
    private boolean validateUsernamme = false;
    private boolean validatePassword = false;
    private boolean validateKeyPhrase = false;
    
    //Gether methods
    public String getUsername(){
        return usernameField.getText();
    }

    public char[] getPassword(){
        return passwordField.getPassword();
    }

    public char[] getKey(){
        return keyField.getPassword();
    }

    public JButton addButton(String text, Container container, ActionListener lisiner){
        
        button = new JButton();

        //Add button
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMargin(new Insets(5, 5, 5, 5));
        button.setOpaque(false);
        button.setBackground(null);
        button.setForeground(Color.black);
        button.setText(text);
        button.setFont(new Font("Sarif", Font.PLAIN, 14));
        button.addActionListener(lisiner);

        container.add(button);
        container.add(Box.createRigidArea(new Dimension(gapSize, gapSize)));
        return button;
    }

    public void usernameTextField(Container container){

        //Set up text field
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameField.setHorizontalAlignment(JTextField.CENTER);
        usernameField.setMaximumSize(new Dimension(width, height));
        usernameField.setOpaque(false);

        //Set up place holder text
        new GhostText(usernameField, "Username");

        container.add(usernameField);
        container.add(Box.createRigidArea(new Dimension(gapSize, gapSize)));

    }

    public void passwordTextField(Container container){

        //Set up password text field
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.setHorizontalAlignment(JTextField.CENTER);
        passwordField.setMaximumSize(new Dimension(width, height));
        passwordField.setOpaque(false);

        //add ghost text
        new GhostText(passwordField, "Password");

        container.add(passwordField);
        container.add(Box.createRigidArea(new Dimension(gapSize, gapSize)));
    }

     public void keyTextField(Container container){

        //Set up key text field
        keyField.setAlignmentX(Component.CENTER_ALIGNMENT);
        keyField.setHorizontalAlignment(JTextField.CENTER);
        keyField.setMaximumSize(new Dimension(width, height));
        keyField.setOpaque(false);

        //add ghost text
        new GhostText(keyField, "Key Phrase");

        container.add(keyField);
        container.add(Box.createRigidArea(new Dimension(gapSize, gapSize)));
    }

    public void returnMsg(Container container){
        
        returnMsg.setAlignmentX(Component.CENTER_ALIGNMENT);
        returnMsg.setPreferredSize(new Dimension(100, 100));
        returnMsg.setOpaque(false);
        returnMsg.setBackground(Color.white);

        container.add(returnMsg);
        container.add(Box.createRigidArea(new Dimension(gapSize, gapSize)));
    }

    public void addAppName(Container container){
        
        JLabel appName = new JLabel("JVM Vault");
        appName.setFont(new Font("Serif", Font.PLAIN, 80));
        appName.setForeground(Color.black);
        appName.setAlignmentX(Component.CENTER_ALIGNMENT);

        container.add(appName);
        container.add(Box.createRigidArea(new Dimension(gapSize, gapSize)));

    }

    public void addRegisterDescription(Container container){

        JLabel title = new JLabel("Sign up");
        JLabel d1 = new JLabel("You need to Sign up");
        JLabel d2 = new JLabel("To gain the ability to store passwords");

        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        d1.setAlignmentX(Component.CENTER_ALIGNMENT);
        d2.setAlignmentX(Component.CENTER_ALIGNMENT);

        title.setFont(new Font("Sarif", Font.PLAIN, 20));
        d1.setFont(new Font("Sarif", Font.PLAIN, 14));
        d2.setFont(new Font("Sarif", Font.PLAIN, 14));
        
        title.setForeground(Color.black);
        d1.setForeground(Color.black);
        d2.setForeground(Color.black);

        container.add(title);
        container.add(Box.createRigidArea(new Dimension(10, 5)));
        container.add(d1);
        container.add(d2);
        container.add(Box.createRigidArea(new Dimension(10, 30)));

    }

    public void addSignInDescription(Container container){

        JLabel title = new JLabel("Sing in");
        JLabel d1 = new JLabel("Sign in to your acount");

        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        d1.setAlignmentX(Component.CENTER_ALIGNMENT);

        title.setFont(new Font("Sarif", Font.PLAIN, 20));
        d1.setFont(new Font("Sarif", Font.PLAIN, 14));

        title.setForeground(Color.BLACK);
        title.setForeground(Color.BLACK);

        container.add(title);
        container.add(Box.createRigidArea(new Dimension(10, 5)));
        container.add(d1);
        container.add(Box.createRigidArea(new Dimension(10, 40)));
    }

    public void addDocEventToPass(){
        passwordField.getDocument().addDocumentListener(this);
    
    }

    public void addDocEventToName(){
        usernameField.getDocument().addDocumentListener(this);
    }

    public void addDocEventToKey(){
        keyField.getDocument().addDocumentListener(this);
    }

    public void addDocFilterUsername(){
        
        Document doc = usernameField.getDocument();

        if(doc instanceof AbstractDocument){
            
            AbstractDocument aDoc = (AbstractDocument) doc;
            aDoc.setDocumentFilter(new InputFilter());
        }
    }

    public void addDocFilterPassword(){
        
        Document doc = passwordField.getDocument();

        if (doc instanceof AbstractDocument) {
            
            AbstractDocument aDoc = (AbstractDocument) doc;
            aDoc.setDocumentFilter(new InputFilter());
        }
    }

    public void addDocFillterKey(){

        Document doc = keyField.getDocument();

        if(doc instanceof AbstractDocument){

            AbstractDocument aDoc = (AbstractDocument) doc;
            aDoc.setDocumentFilter(new InputFilter());
        }
    }

    //Get input data
    @Override
    public void actionPerformed(ActionEvent e) {
        Comparitor c = new Comparitor();

        if (e.getSource() == button) {
            String username = usernameField.getText();
            char[] password = passwordField.getPassword();
            char[] key = keyField.getPassword();

            c.justPrintTheValues(username, password, key);
        }
    }
    
    //Refresh button
    public void buttonRefresh(){
        if (button != null){
            button.setEnabled(buttonUpdate());
        }
    }

    //Eneble disable button based on parameters
    public boolean buttonUpdate(){
        if(validatePassword && validateUsernamme && validateKeyPhrase){
            return true;
        }
        else{
            return false;
        }
    }

    //Input test validation requerments
    public void validateInputPass(DocumentEvent e){

        Document eDoc = e.getDocument();
        String pass = ""; //Chars

        try {
            pass = eDoc.getText(0, eDoc.getLength());
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }

        if(eDoc == passwordField.getDocument()){
            if(eDoc.getLength() < 8){
                validatePassword = false;
                returnMsg.setText("Password must be longer than 8 charecters");
            }
            else if(!pass.matches(".*[!@#$%^&*()_+~|?><:;,./\\\\-].*")){
                validatePassword = false;
                returnMsg.setText("Password must contain one special charecter (!, %, @, etc.)");
            }
            else if(pass.equals("Password") || pass.equals("asdf1234")){
                validatePassword = false;
                returnMsg.setText("Weak password plese make a stronger one");
            }
            else if(!pass.matches(".*[0-9].*")){
                validatePassword = false;
                returnMsg.setText("Password must contain atlest one number (0-9)");
            }
            else if(!pass.matches(".*[A-Z].*")){
                validatePassword = false;
                returnMsg.setText("Password must contain one uppercase letter (A-Z)");
            }
            else{
                validatePassword = true;
                returnMsg.setText(null);
            }
        }
    }

    public void validateInputUsername(DocumentEvent e){
        
        Document eDoc = e.getDocument();
        String user = "";

        try {
            user = eDoc.getText(0, eDoc.getLength());
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
        
        if(eDoc == usernameField.getDocument()){
            if(eDoc.getLength() >= 1 && !user.equals("Username")){
                validateUsernamme = true;
                returnMsg.setText(null);
            }
            else{
                validateUsernamme = false;
                returnMsg.setText("Username cant be empty");
            }
        }
    }

    public void validateInputKey(DocumentEvent e){

        Document eDoc = e.getDocument();
        String key = "";

        try {
            key = eDoc.getText(0, eDoc.getLength());
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
        
        if(eDoc == keyField.getDocument()){
            if(eDoc.getLength() >= 3){
                validateKeyPhrase = true;
                returnMsg.setText(null);
            }
            else{
                validateKeyPhrase = false;
                returnMsg.setText("This phrase will be how you get your password secured");
            }
        }
    }

    //Displayes messege to user about input validation
    @Override   
    public void insertUpdate(DocumentEvent e) {
        validateInputPass(e);
        validateInputUsername(e);
        validateInputKey(e);
        buttonRefresh();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        validateInputPass(e);
        validateInputUsername(e);
        validateInputKey(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        validateInputPass(e);
        validateInputUsername(e);
        validateInputKey(e);
    }

}

//Rules on the input 
class InputFilter extends DocumentFilter {
    
    public void replace(DocumentFilter.FilterBypass fb, int offset, int length, 
    String text, AttributeSet attrs) throws BadLocationException{

        if(text != null && text.equals("Username") || text.equals("Password")){
            super.replace(fb, offset, length, text, attrs);
            return;
        }

        int curentLenght = fb.getDocument().getLength() + text.length() - length;

        if(curentLenght <= 16){
            super.replace(fb, offset, length, text, attrs);
        }

    }
}

//Add Place Holder text
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
                if(textfield instanceof JPasswordField){
                    ((JPasswordField)textfield).setEchoChar('*');
                }
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
                if(textfield instanceof JPasswordField && isEmpty){
                    ((JPasswordField)textfield).setEchoChar((char) 0);
                }
            } finally {
                registerListeners();
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
