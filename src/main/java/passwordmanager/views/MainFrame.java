package passwordmanager.views;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import passwordmanager.src.Backend.Comparitor;

//Main frame of the app, all Components go here
public class MainFrame extends JFrame{
    
    public boolean checkPanelsValue = false;
    public JPanel cPanel;
    public CardLayout cardLoader = new CardLayout();
    public static final int WIDTH = 400;
    public static final int HEIGHT = 500;
    public Comparitor backend = new Comparitor();

    private void setupCardPanel(){

        SignUpPanel signUpPanel = new SignUpPanel(this);
        SignInPanel signInPanel = new SignInPanel(this);
        DashboardPanel dashboardPanel = new DashboardPanel(this); 

        cPanel = new JPanel(cardLoader);
        cPanel.add(signUpPanel, "SignUpPanel");
        cPanel.add(signInPanel, "SignInPanel");
        cPanel.add(dashboardPanel, "DashboardPanel");
    }

    public MainFrame(){

        setupCardPanel();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setBackground(Color.DARK_GRAY);
        this.setSize(WIDTH, HEIGHT);
        this.setLocationRelativeTo(null);
        this.setTitle("JVM Vault");
        this.add(cPanel);
        this.setResizable(false);
        
        try {
            this.setIconImage(ImageIO.read(getClass().getResource("assets/key.png")));       
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Determen what frame to use
        boolean isDatabaseExist = false;
        boolean isUserCredentialsExist = false;

        isDatabaseExist = backend.databaseVerify();
        isUserCredentialsExist = backend.isUserCredentialsExists();

        if(isDatabaseExist == true && isUserCredentialsExist == true){
            cardLoader.show(cPanel, "SignInPanel");
        }
        if(isDatabaseExist == true && isUserCredentialsExist == false){
            cardLoader.show(cPanel, "SignUpPanel");    
        }

        cardLoader.show(cPanel, "DashboardPanel");

        this.setVisible(true);
    }

    public void cardSwitch(){
        cardLoader.show(cPanel, "Vault");
    }
}

//Set up Sign in Panel
class SignInPanel extends JPanel implements ActionListener {

    private MainFrame mFrame;
    private JButton button;
    private ComponentsLS c = new ComponentsLS();
    private Comparitor comparitor = new Comparitor();

    SignInPanel(MainFrame main){

        this.mFrame = main;
        this.setBackground(Color.WHITE);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        c.addDocFilterUsername();
        c.addDocFilterPassword();
        c.addAppName(this);
        c.addSignInDescription(this);
        c.usernameTextField(this);
        c.passwordTextField(this);
        this.button = c.addButton("Sign in", this, this);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.button) {
            
            if(comparitor.signInValidateCredentials(c.getUsername(), c.getPassword()) == true){
                mFrame.cardLoader.show(mFrame.cPanel, "DashboardPanel");
            }
            else{
                JOptionPane.showMessageDialog(this, "Incorect Username or Password");
            }
        }
    }

    
}

//Setup Sing up Panel
class SignUpPanel extends JPanel implements ActionListener {

    private MainFrame mFrame;
    private JButton button;
    private ComponentsLS c = new ComponentsLS();
    private Comparitor comparitor = new Comparitor();

    SignUpPanel(MainFrame main){
        //Get mainFrame cardswitching
        this.mFrame = main;
        this.setBackground(Color.WHITE);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        c.addDocFilterUsername();
        c.addDocFilterPassword();
        c.addDocFillterKey();
        c.addAppName(this);
        c.addRegisterDescription(this);
        c.usernameTextField(this);
        c.addDocEventToName();
        c.passwordTextField(this);
        c.addDocEventToPass();
        c.keyTextField(this);
        c.addDocEventToKey();
        this.button = c.addButton("Sign up", this, this);
        c.returnMsg(this);
        c.buttonRefresh();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == button) {

            String user = c.getUsername();
            char[] pass = c.getPassword();
            char[] key = c.getKey();

            if(comparitor.singUpDataHandeling(user, pass, key)){
                JOptionPane.showMessageDialog(this, "Acount created!");
                mFrame.cardLoader.show(mFrame.cPanel, "SignInPanel");
            }
            else{
                JOptionPane.showMessageDialog(this, "Opps failed to create acount!");
            }
            
        } 
    }

}

class DashboardPanel extends JPanel implements ActionListener{

    private MainFrame mFrame;
    private JButton button = new JButton();
    private ComponentsDash c = new ComponentsDash();
    private JPanel topPanel = c.topComponents();
    private JPanel bottomPanel = c.bottomComponent();

    DashboardPanel(MainFrame main){

        this.mFrame = main;
        this.setBackground(Color.WHITE);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        topPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        bottomPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        this.add(topPanel);
        this.add(bottomPanel);
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            mFrame.cardLoader.show(mFrame.cPanel, "DASHBOARD");
        } 
    }

}

