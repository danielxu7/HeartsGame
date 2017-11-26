package HeartsPackage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;

// HeartsGUI functions as the view
public class HeartsGUI extends javax.swing.JFrame {

    // Make variables accessible by all functions
    private static Model model;
    // Track who started a trick and who won a trick
    private static String WhoStarted;
    private static String WhoWon;
    private static boolean heartsBroken = false;
    private static int trickCounter = 0;
    private static JButton[] arrayButtons;
    
    // User interface variables declared
    private javax.swing.JLabel cardsW;
    private javax.swing.JLabel cardsN;
    private javax.swing.JLabel cardsE;
    private javax.swing.JLabel labelWest;
    private javax.swing.JLabel labelNorth;
    private javax.swing.JLabel labelEast;
    private javax.swing.JLabel labelUser;
    private javax.swing.JLabel background;
    private static javax.swing.JButton jButtonW; 
    private static javax.swing.JButton jButtonN;
    private static javax.swing.JButton jButtonE;
    private static javax.swing.JButton jButtonU;
    private static javax.swing.JButton jButtonReady;
    
    public HeartsGUI() {
        // Create model to handle game logic
        model = new Model();
        this.WhoStarted = "";

        // Initialize visuals on GUI - This is the view
        // All jButtons, jLabels etc are created here
        initComponents();
        initComponents2();     
    }
    
    private void initComponents() {

        labelNorth = new javax.swing.JLabel();
        labelEast = new javax.swing.JLabel();
        labelWest = new javax.swing.JLabel();
        labelUser = new javax.swing.JLabel();
        jButtonW = new javax.swing.JButton();
        jButtonN = new javax.swing.JButton();
        jButtonE = new javax.swing.JButton();
        jButtonU = new javax.swing.JButton();
        jButtonReady = new javax.swing.JButton();
        cardsW = new javax.swing.JLabel();
        cardsE = new javax.swing.JLabel();
        cardsN = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Hearts");
        setMaximumSize(new java.awt.Dimension(1023, 608));
        setMinimumSize(new java.awt.Dimension(1023, 608));
        setPreferredSize(new java.awt.Dimension(1023, 608));
        setResizable(false);
        setSize(new java.awt.Dimension(1023, 608));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelNorth.setForeground(new java.awt.Color(255, 255, 255));
        labelNorth.setText("North");
        getContentPane().add(labelNorth, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 20, -1, -1));

        labelEast.setForeground(new java.awt.Color(255, 255, 255));
        labelEast.setText("East");
        getContentPane().add(labelEast, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 160, -1, -1));

        labelWest.setForeground(new java.awt.Color(255, 255, 255));
        labelWest.setText("West");
        getContentPane().add(labelWest, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 150, -1, -1));

        labelUser.setForeground(new java.awt.Color(255, 255, 255));
        labelUser.setText("You");
        getContentPane().add(labelUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 470, -1, -1));

        jButtonW.setVisible(false);
        getContentPane().add(jButtonW, new org.netbeans.lib.awtextra.AbsoluteConstraints(355, 260, -1, -1));

        jButtonN.setVisible(false);
        getContentPane().add(jButtonN, new org.netbeans.lib.awtextra.AbsoluteConstraints(485, 190, -1, -1));

        jButtonE.setVisible(false);
        getContentPane().add(jButtonE, new org.netbeans.lib.awtextra.AbsoluteConstraints(595, 260, -1, -1));

        jButtonU.setText("");
        jButtonU.setVisible(false);
        getContentPane().add(jButtonU, new org.netbeans.lib.awtextra.AbsoluteConstraints(485, 340, -1, -1));

        jButtonReady.setText("   Ready?   ");
        jButtonReady.addActionListener((java.awt.event.ActionEvent evt) -> {
            try {
                Model.getPlayerU().startTrick(evt);
            } catch (IOException ex) {
                Logger.getLogger(HeartsGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        getContentPane().add(jButtonReady, new org.netbeans.lib.awtextra.AbsoluteConstraints(465, 260, -1, -1));
        
        cardsW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/HeartsPackage/cards.jpg"))); // NOI18N
        getContentPane().add(cardsW, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 130, -1, -1));

        cardsE.setIcon(new javax.swing.ImageIcon(getClass().getResource("/HeartsPackage/cards.jpg"))); // NOI18N
        getContentPane().add(cardsE, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 130, -1, -1));

        cardsN.setIcon(new javax.swing.ImageIcon(getClass().getResource("/HeartsPackage/cardsH.jpg"))); // NOI18N
        getContentPane().add(cardsN, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 40, -1, -1));
    }
    // Create cards on GUI here
    private void initComponents2() {
        // Create array to store buttons
        // One button will be assigned for each card
        arrayButtons = new JButton[13];
        
        // Create user buttons using loop
        for(int i=0; i < 13; i++) {
            // Create a button and add it to array of buttons
            arrayButtons[i] = new javax.swing.JButton();
            // Temporarily set text to ???
            arrayButtons[i].setText("???");
            arrayButtons[i].setEnabled(false);
        }
        
        // Draw buttons for user
        for(int i=0; i < 13; i++) {
            getContentPane().add(arrayButtons[i], new org.netbeans.lib.awtextra.AbsoluteConstraints(180 + i*50, 500, -1, -1));
            
            // Action Listener for array of user's buttons
            arrayButtons[i].addActionListener((java.awt.event.ActionEvent evt) -> {
                try {
                    // Refers to controller method
                    Model.getPlayerU().playCard(evt);
                } catch (IOException ex) {
                    Logger.getLogger(HeartsGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }        
        
        // Draw background
        background = new javax.swing.JLabel();
        background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/HeartsPackage/background.jpg"))); 
        getContentPane().add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));
    }
    
    // Method to vanish cards
    public static void vanish()
    {
        jButtonW.setVisible(false);
        jButtonN.setVisible(false);
        jButtonE.setVisible(false);
        jButtonU.setVisible(false);
    }
    
    // Method for displaying user's cards to screen
    public static void displayUserCards()
    {
        // New arraylist for player's hand to convert 11 to J etc.
        ArrayList<String> newHand = model.getPlayerU().getHand();
        // Rewrite if rank was 11 or more
        for(int i=0; i < newHand.size(); i++) {
            newHand.set(i,model.rewriteRank(newHand.get(i)));
            
            // Change 13 user buttons to their card values
            arrayButtons[i].setText(newHand.get(i));
            
            // Change colour of card if diamond or heart
            if(newHand.get(i).substring(0,1).equals("♦") || newHand.get(i).substring(0,1).equals("♥")) {
                arrayButtons[i].setForeground(new java.awt.Color(255, 0, 0));
            }
        }
    }
    
    // Main for running an instance of the game
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new HeartsGUI().setVisible(true);
        });
    }
    
    // Getters and setters
        public static Model getModel() {
        return model;
    }

    public static void setModel(Model Model1) {
        model = Model1;
    }

    public static String getWhoStarted() {
        return WhoStarted;
    }

    public static void setWhoStarted(String WhoStarted1) {
        WhoStarted = WhoStarted1;
    }

    public static String getWhoWon() {
        return WhoWon;
    }

    public static void setWhoWon(String WhoWon1) {
        WhoWon = WhoWon1;
    }

    public static boolean isHeartsBroken() {
        return heartsBroken;
    }

    public static void setHeartsBroken(boolean HeartsBroken1) {
        heartsBroken = HeartsBroken1;
    }

    public static int getTrickCounter() {
        return trickCounter;
    }

    public static void setTrickCounter(int TrickCounter1) {
        trickCounter = TrickCounter1;
    }

    public static JButton[] getArrayButtons() {
        return arrayButtons;
    }

    public static void setArrayButtons(JButton[] ArrayButtons1) {
        arrayButtons = ArrayButtons1;
    }

    public static javax.swing.JButton getjButtonW() {
        return jButtonW;
    }

    public static void setjButtonW(javax.swing.JButton jButtonW1) {
        jButtonW = jButtonW1;
    }

    public static javax.swing.JButton getjButtonN() {
        return jButtonN;
    }

    public static void setjButtonN(javax.swing.JButton jButtonN1) {
        jButtonN = jButtonN1;
    }

    public static javax.swing.JButton getjButtonE() {
        return jButtonE;
    }

    public static void setjButtonE(javax.swing.JButton jButtonE1) {
        jButtonE = jButtonE1;
    }

    public static javax.swing.JButton getjButtonU() {
        return jButtonU;
    }

    public static void setjButtonU(javax.swing.JButton jButtonU1) {
        jButtonU = jButtonU1;
    }

    public static javax.swing.JButton getjButtonReady() {
        return jButtonReady;
    }

    public static void setjButtonReady(javax.swing.JButton jButtonReady1) {
        jButtonReady = jButtonReady1;
    }
}