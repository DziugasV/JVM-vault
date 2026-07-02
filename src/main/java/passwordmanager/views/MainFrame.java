package passwordmanager.views;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

//Main frame of the app, all Components go here
public class MainFrame extends JFrame{
    
    boolean checkPanelsValue = false;
    JPanel cPanel;
    CardLayout cardLoader = new CardLayout();

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
        this.setSize(400, 500);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setTitle("JVM Vault");
        this.add(cPanel);
        
        try {
            this.setIconImage(ImageIO.read(getClass().getResource("assets/key.png")));       
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(checkPanelsValue == false){
            cardLoader.show(cPanel, "SignInPanel");
        }
        if(checkPanelsValue == true){
            cardLoader.show(cPanel, "");
        }

        this.setVisible(true);
    }

    public void cardSwitch(){
        cardLoader.show(cPanel, "Vault");
    }
}

//Set up Sign in Panel
class SignInPanel extends JPanel implements ActionListener {

    private MainFrame mFrame;
    private JButton button = new JButton();
    private ComponentsLS c = new ComponentsLS();

    SignInPanel(MainFrame main){

        this.mFrame = main;
        this.setBackground(Color.WHITE);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        c.addDocFilterUsername();
        c.addDocFilterPassword();
        c.addAppName(this);
        c.addSignInDescription(this);
        c.usernameTextField(this);
        //DocEventUserName Wrong
        c.passwordTextField(this);
        //DocEventPassword Wrong
        c.addButton("Sign in", this);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            System.out.println("Login Login Login");
        }
    }

    
}

//Setup Sing up Panel
class SignUpPanel extends JPanel implements ActionListener {

    private MainFrame mFrame;
    private JButton button;
    private ComponentsLS c = new ComponentsLS();

    SignUpPanel(MainFrame main){
        //Get mainFrame cardswitching
        this.mFrame = main;
        this.setBackground(Color.WHITE);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        c.addDocFilterUsername();
        c.addDocFilterPassword();
        c.addAppName(this);
        c.addRegisterDescription(this);
        c.usernameTextField(this);
        c.addDocEventToName();
        c.passwordTextField(this);
        c.addDocEventToPass();
        this.button = c.addButton("Sign up", this);
        c.returnMsg(this);
        c.buttonRefresh();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == button) {
            mFrame.cardLoader.show(mFrame.cPanel, "SignInPanel");
        } 
    }

}

class DashboardPanel extends JPanel implements ActionListener{

    private MainFrame mFrame;
    private JButton button = new JButton();
    private ComponentsLS c = new ComponentsLS();

    DashboardPanel(MainFrame main){

        this.mFrame = main;
        this.setBackground(Color.WHITE);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        //Sign up page 
        JTextField text = new JTextField("DASHBOARD");
        mFrame.add(text);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            mFrame.cardLoader.show(mFrame.cPanel, "DASHBOARD");
        } 
    }

}

