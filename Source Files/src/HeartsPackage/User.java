package HeartsPackage;

import java.io.IOException;
import javax.swing.JButton;

public class User extends Player {
    
    // Detects that user is ready to start the trick
    public void startTrick(java.awt.event.ActionEvent evt) throws IOException {
        // Restart the game
        if(HeartsGUI.getTrickCounter() == 13) {
            // Reset counter and all variables
            HeartsGUI.setTrickCounter(0);
            HeartsGUI.setWhoStarted("");
            HeartsGUI.setWhoWon("");
            HeartsGUI.setHeartsBroken(false);
            
            // Reset scores
            Model.getPlayerU().setScore(0);
            Model.getPlayerW().setScore(0);
            Model.getPlayerN().setScore(0);
            Model.getPlayerE().setScore(0);
        }
        
        // Vanish cards after full trick
        HeartsGUI.vanish();
        
        // Make the button disappear and change text
        HeartsGUI.getjButtonReady().setVisible(false);
        HeartsGUI.getjButtonReady().setText("Next Round");
        
        // Start the trick if it is still first trick
        if(HeartsGUI.getTrickCounter() == 0)
        {
            // Reset all buttons and their colours
            for(int i=0; i<13; i++) {
                HeartsGUI.getArrayButtons()[i].setVisible(true);
                HeartsGUI.getArrayButtons()[i].setForeground(new java.awt.Color(0, 0, 0));
            }
            
            // Shuffle the deck, deal cards, and show cards on interface
            HeartsGUI.getModel().startRound();
            HeartsGUI.displayUserCards();
        }
        
        // Enable all buttons
        for(int i=0; i<13; i++)
        {
            HeartsGUI.getArrayButtons()[i].setEnabled(true);
        }
        
        // Variable to track if user starts during first trick
        boolean userTwoC = false;
        boolean firstTrick = true;
        
        // Check if user has two of clubs
        if(HeartsGUI.getArrayButtons()[0].getText().equals("♣2") && HeartsGUI.getArrayButtons()[0].isVisible() == true)
        {
            userTwoC = true;
            
            // Disable every card other than 2 of clubs
            for(int i=1; i<13; i++)
            {
                HeartsGUI.getArrayButtons()[i].setEnabled(false);
            }
        }
        
        // Start the game if user does not have 2 of clubs
        if(userTwoC == false)
        {   
            // Check if first trick or not
            if(HeartsGUI.getWhoStarted().equals("finished"))
            {
                // Not first trick
                firstTrick = false;
                
                // Check if user does not start next trick
                if(!HeartsGUI.getWhoWon().equals("user"))
                {
                    // Boolean parameters indicates if user starts and if first trick
                    HeartsGUI.getModel().nextMove(userTwoC, firstTrick);
                }
                
                // User starts next trick
                else {
                    // If user starts next trick, hearts have not been broken, and user has non-heart cards, disable all hearts
                    // Exception is when user has only hearts left and hearts have not been broken
                    if(HeartsGUI.isHeartsBroken() == false && (!AI.highestCardSuit(Model.getPlayerU().getHand(),"♣").equals("") || !AI.highestCardSuit(Model.getPlayerU().getHand(),"♦").equals("") || !AI.highestCardSuit(Model.getPlayerU().getHand(),"♠").equals(""))) {
                        
                        // Disable all hearts
                        for(int i=0; i<13; i++) {
                            // Check if it's a heart
                            if(HeartsGUI.getArrayButtons()[i].getText().substring(0,1).equals("♥")) {
                                HeartsGUI.getArrayButtons()[i].setEnabled(false);
                            }
                        }
                    }
                }
            }
            
            // First trick
            else {
                HeartsGUI.getModel().nextMove(userTwoC, firstTrick);
            }
        }
    }
    
    // Detects that user pressed one of the buttons
    public void playCard(java.awt.event.ActionEvent evt) throws IOException {
        // Text of which card user chose
        String SourceButtonText = "";
        
        // Get source of button pressed
        if (evt.getSource() instanceof JButton) {
            SourceButtonText = evt.getActionCommand();
        }
        
        // User played a card and should not be able to re-play the card
        // Trace which button it was
        for(int i=0; i<13; i++)
        {
            // Find the card user played
            if(HeartsGUI.getArrayButtons()[i].getText().equals(SourceButtonText))
            {
                // Make the card invisible
                HeartsGUI.getArrayButtons()[i].setVisible(false);
            }
            
            // Also disable every card until it is user's turn again
            HeartsGUI.getArrayButtons()[i].setEnabled(false);
        }
        
        // Copy the card played onto table and disable it but keep it on screen
        HeartsGUI.getjButtonU().setText(SourceButtonText);
        HeartsGUI.getjButtonU().setVisible(true);
        
        // Remove the card from player's hand
        Model.getPlayerU().removeCard(SourceButtonText);
        
        // Hearts are broken if user plays a heart
        if(SourceButtonText.equals("♥")) {
            HeartsGUI.setHeartsBroken(true);
        }
        
        // Parameters say that user starts first in the first trick
        boolean userStartsFT = false;
        boolean firstTrick = false;
            
        // Continue the game
        // If user has two of clubs, user must start
        if(SourceButtonText.equals("♣2"))
        {
            // Parameters say that user starts first in the first trick
            userStartsFT = true;
            firstTrick = true;
            HeartsGUI.getModel().nextMove(userStartsFT,firstTrick);
        }
        
        // First trick but AI started
        else if(Model.getPlayerW().getHand().size() == 13 || Model.getPlayerN().getHand().size() == 13 || Model.getPlayerE().getHand().size() == 13 || Model.getPlayerW().isStartFT() == true)
        {
            firstTrick = true;
            HeartsGUI.getModel().nextMove(userStartsFT, firstTrick);
        }
        
        // Not first trick
        else
        {
            HeartsGUI.getModel().nextMove(userStartsFT, firstTrick);
        }
    }
}