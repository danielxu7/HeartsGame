package HeartsPackage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

// Model contains all game logic
public class Model {
    
    // Attributes
    private static AI PlayerW;
    private static AI PlayerN;
    private static AI PlayerE;
    private static User PlayerU;
    private static Deck DeckCards;
    private static String FirstCard;
    
    // Constructor
    public Model() {
        // Initialize deck
        Deck Deck1 = new Deck();
        Model.DeckCards = Deck1;
        
        // Create 4 players -- AI and user inherits Player
        AI PlayerWest = new AI();
        AI PlayerNorth = new AI();
        AI PlayerEast = new AI();
        User PlayerUser = new User();

        // Make accessible outside of constructor
        Model.PlayerW = PlayerWest;
        Model.PlayerN = PlayerNorth;
        Model.PlayerE = PlayerEast;
        Model.PlayerU = PlayerUser;
    }
    
    // Assign cards to players
    public void assignCards(ArrayList<String> deck1,AI PlayerWest1,AI PlayerNorth1,AI PlayerEast1,User PlayerUser1)
    {
        // Loop through all cards in the deck and assign each card to a player
        for(int i=0; i < 13; i++) {
            // Assign first 13 cards to west player and next 13 to north player etc.
            PlayerWest1.addCard(deck1.get(i));
            PlayerNorth1.addCard(deck1.get(i+13));
            PlayerEast1.addCard(deck1.get(i+26));
            PlayerUser1.addCard(deck1.get(i+39));
        }
        
        // Copy values to players in this class
        PlayerW.setHand(PlayerWest1.getHand());
        PlayerN.setHand(PlayerNorth1.getHand());
        PlayerE.setHand(PlayerEast1.getHand());
        PlayerU.setHand(PlayerUser1.getHand());
    }
    
    // Method to determine winner of trick and calculate points
    public void findWinner()
    {
        // Convert cards JQKA back to numerical value as strings
        String Wcard = rewriteRankBack(HeartsGUI.getjButtonW().getText());
        String Ncard = rewriteRankBack(HeartsGUI.getjButtonN().getText());
        String Ecard = rewriteRankBack(HeartsGUI.getjButtonE().getText());
        String Ucard = rewriteRankBack(HeartsGUI.getjButtonU().getText());
        
        // Convert numerical values as strings to numerical values as ints
        int wCard = Integer.parseInt(Wcard.substring(1));
        int nCard = Integer.parseInt(Ncard.substring(1));
        int eCard = Integer.parseInt(Ecard.substring(1));
        int uCard = Integer.parseInt(Ucard.substring(1));
        
        // Check if user started the trick
        if(HeartsGUI.getWhoStarted().equals("user"))
        {
            // Compare the cards to find winner of trick
            compareCards(Ucard,Wcard,Ncard,Ecard,uCard,wCard,nCard,eCard,HeartsGUI.getWhoStarted());
        }
        
        // West started the trick
        else if(HeartsGUI.getWhoStarted().equals("west"))
        {
            compareCards(Wcard,Ncard,Ecard,Ucard,wCard,nCard,eCard,uCard,HeartsGUI.getWhoStarted());
        }
        
        // North started the trick
        else if(HeartsGUI.getWhoStarted().equals("north"))
        {
            compareCards(Ncard,Ecard,Ucard,Wcard,nCard,eCard,uCard,wCard,HeartsGUI.getWhoStarted());
        }
        
        // East started the trick
        else
        {
            compareCards(Ecard,Ucard,Wcard,Ncard,eCard,uCard,wCard,nCard,HeartsGUI.getWhoStarted());
        }
    }
    
    // Method for rewriting the rank of a card
    public String rewriteRank(String Card)
    {
        // Convert 11 to J, 12 to Q, etc.
        if(Card.substring(1).equals("11"))
        {
            Card = Card.substring(0,1) + "J";
        }
        else if(Card.substring(1).equals("12"))
        {
            Card = Card.substring(0,1) + "Q";
        }
        else if(Card.substring(1).equals("13"))
        {
            Card = Card.substring(0,1) + "K";
        }
        else if(Card.substring(1).equals("14"))
        {
            Card = Card.substring(0,1) + "A";
        }
        return Card;
    }
    
    // Method to convert JQKA to numerical value
    public String rewriteRankBack(String Card)
    {
        // Convert J to 11, Q to 12, etc.
        if(Card.substring(1).equals("J"))
        {
            Card = Card.substring(0,1) + "11";
        }
        else if(Card.substring(1).equals("Q"))
        {
            Card = Card.substring(0,1) + "12";
        }
        else if(Card.substring(1).equals("K"))
        {
            Card = Card.substring(0,1) + "13";
        }
        else if(Card.substring(1).equals("A"))
        {
            Card = Card.substring(0,1) + "14";
        }
        return Card;
    }
    
    // Method to compare cards to find winner of trick
    public void compareCards(String StartingCard,String NextCard1,String NextCard2,String NextCard3,int sCard1,int nCard1,int nCard2,int nCard3,String StartingPlayer)
    {
        // Track the highest card played
        // Temporarily set winner to who started the trick
        int highestCard = sCard1;
        HeartsGUI.setWhoWon(StartingPlayer);
        
        // Check if next player in turn played a card of same suit and higher value
        if(StartingCard.charAt(0)==(NextCard1.charAt(0)) && nCard1 > sCard1)
        {
            // Next player may have won the trick
            // Parameters: Person who was going to win trick, and number of clockwise rotations of players
            HeartsGUI.setWhoWon(nextPlayer(StartingPlayer,1));
            highestCard = nCard1;
        }

        // Repeat for other players
        if(StartingCard.charAt(0)==(NextCard2.charAt(0)) && nCard2 > highestCard)
        {
            HeartsGUI.setWhoWon(nextPlayer(StartingPlayer,2));
            highestCard = nCard2;
        }
        if(StartingCard.charAt(0)==(NextCard3.charAt(0)) && nCard3 > highestCard)
        {
            HeartsGUI.setWhoWon(nextPlayer(StartingPlayer,3));
        }

        // Check if anyone played queen of spades
        int points = playedQofS(StartingCard,NextCard1,NextCard2,NextCard3);

        // Check if anyone played a heart
        points = playedHeart(points,StartingCard,NextCard1,NextCard2,NextCard3);

        // Player who won the trick gets the points
        addToScore(HeartsGUI.getWhoWon(), points);
    }
    
    // Method to return the next player in clockwise order
    public String nextPlayer(String PreviousPlayer,int rotations)
    {
        // Set east to be next player by default
        String NextPlayer = "east";
        int position = 4;
        
        // Give each player a position
        // User will have 1, west will have 2, north will have 3 and east will have 4
        if(PreviousPlayer.equals("user"))
        {
            position = 1;
        }
        else if(PreviousPlayer.equals("west"))
        {
            position = 2;
        }
        else if(PreviousPlayer.equals("north"))
        {
            position = 3;
        }
        
        position = (position + rotations) %4;
        
        // Convert position back to strings
        if(position == 1)
        {
            NextPlayer = "user";
        }
        else if(position == 2)
        {
            NextPlayer = "west";
        }
        else if(position == 3)
        {
            NextPlayer = "north";
        }
        return NextPlayer;
    }
    
    // Method to check if anyone played queen of spades
    public int playedQofS(String Card1,String Card2,String Card3,String Card4)
    {
        // Track points
        int points = 0;
        
        // Check if any of the cards are queen of spades
        if(Card1.equals("♠12") || Card2.equals("♠12") || Card3.equals("♠12") || Card4.equals("♠12"))
        {
            points = points + 13;
        }
        return points;
    }
    
    // Method to check if anyone played a heart
    public int playedHeart(int points,String WCard,String NCard,String ECard,String UCard)
    {
        // Check if a player played a heart
        if(WCard.substring(0,1).equals("♥"))
        {
            points++;
        }
        if(NCard.substring(0,1).equals("♥"))
        {
            points++;
        }
        if(ECard.substring(0,1).equals("♥"))
        {
            points++;
        }
        if(UCard.substring(0,1).equals("♥"))
        {
            points++;
        }
        return points;
    }
    
    // Method to add points to whoever won the trick
    public void addToScore(String Winner, int points)
    {
        // Find who won the trick
        if(Winner.equals("west"))
        {
            PlayerW.setScore(PlayerW.getScore() + points);
        }
        else if(Winner.equals("north"))
        {
            PlayerN.setScore(PlayerN.getScore() + points);
        }
        else if(Winner.equals("east"))
        {
            PlayerE.setScore(PlayerE.getScore() + points);
        }
        else
        {
            PlayerU.setScore(PlayerU.getScore() + points);
        }
    }
    
    // Method to play and display AI's cards in first trick only
    public void playDisplayFirst(String PlayerName)
    {
        // West plays a card
        if(PlayerName.equals("west")) {
            HeartsGUI.getjButtonW().setText(PlayerW.playFirst(PlayerW.getHand()));
            HeartsGUI.getjButtonW().setVisible(true);
        }
        
        // North plays a card
        else if(PlayerName.equals("north")) {
            HeartsGUI.getjButtonN().setText(PlayerN.playFirst(PlayerN.getHand()));
            HeartsGUI.getjButtonN().setVisible(true);
        }
        
        // East plays a card
        else {
            HeartsGUI.getjButtonE().setText(PlayerE.playFirst(PlayerE.getHand()));
            HeartsGUI.getjButtonE().setVisible(true);
        }
        
    }
    
    public void updateColour()
    {
        // Change colour to black by default
        HeartsGUI.getjButtonW().setForeground(new java.awt.Color(0, 0, 0));
        HeartsGUI.getjButtonN().setForeground(new java.awt.Color(0, 0, 0));
        HeartsGUI.getjButtonE().setForeground(new java.awt.Color(0, 0, 0));
        HeartsGUI.getjButtonU().setForeground(new java.awt.Color(0, 0, 0));
        
        // Change colour of card if diamond or heart
        if(HeartsGUI.getjButtonW().getText().substring(0,1).equals("♦") || HeartsGUI.getjButtonW().getText().substring(0,1).equals("♥")) {
            HeartsGUI.getjButtonW().setForeground(new java.awt.Color(255, 0, 0));
        }
        // Repeat for all cards
        if(HeartsGUI.getjButtonN().getText().substring(0,1).equals("♦") || HeartsGUI.getjButtonN().getText().substring(0,1).equals("♥")) {
            HeartsGUI.getjButtonN().setForeground(new java.awt.Color(255, 0, 0));
        }
        if(HeartsGUI.getjButtonE().getText().substring(0,1).equals("♦") || HeartsGUI.getjButtonE().getText().substring(0,1).equals("♥")) {
            HeartsGUI.getjButtonE().setForeground(new java.awt.Color(255, 0, 0));
        }
        if(HeartsGUI.getjButtonU().getText().substring(0,1).equals("♦") || HeartsGUI.getjButtonU().getText().substring(0,1).equals("♥")) {
            HeartsGUI.getjButtonU().setForeground(new java.awt.Color(255, 0, 0));
        }
    }
    
    // Method to run for start of each trick
    public void startRound()
    {
        // Shuffle the deck
        DeckCards.shuffle(DeckCards.getDeck());
        
        // Deal cards to each player
        assignCards(DeckCards.getDeck(),PlayerW,PlayerN,PlayerE,PlayerU);
        
        // Sort the hands for each player
        PlayerU.setHand(DeckCards.sortHand(PlayerU.getHand()));
        PlayerW.setHand(DeckCards.sortHand(PlayerW.getHand()));
        PlayerN.setHand(DeckCards.sortHand(PlayerN.getHand()));
        PlayerE.setHand(DeckCards.sortHand(PlayerE.getHand()));
    }
    
    // Method for running the game
    // Parameters: User starts first trick, first trick or not
    public void nextMove(boolean uStartsFirst, boolean firstTrick) throws IOException
    {
        // Check if it's the first trick
        if(firstTrick == true)
        {
            // Check if user starts first
            // User has 2 of clubs
            if(uStartsFirst == true)
            {
                // Player west, north, and east goes
                playDisplayFirst("west");
                playDisplayFirst("north");
                playDisplayFirst("east");
                HeartsGUI.setWhoStarted("user");
                
                // Determine winner of trick and update scores
                findWinner();
                
                // Indicate that this trick ends
                HeartsGUI.setWhoStarted("finished");
            }
            // First trick and user does not start
            else
            {
                if(HeartsGUI.getWhoStarted().equals("west"))
                {
                    findWinner();
                    PlayerW.setStartFT(false);
                    HeartsGUI.setWhoStarted("finished");
                }
                // North previously started
                else if(HeartsGUI.getWhoStarted().equals("north"))
                {
                    playDisplayFirst("west");
                    findWinner();
                    // Reset who played value to start next trick
                    HeartsGUI.setWhoStarted("finished");
                }
                // East previously started
                else if(HeartsGUI.getWhoStarted().equals("east"))
                {
                    playDisplayFirst("west");
                    playDisplayFirst("north");
                    findWinner();
                    HeartsGUI.setWhoStarted("finished");
                }
                // First card hasn't been played yet
                else
                {
                    // AI with 2 of clubs goes first
                    // Gameplay flows in clockwise direction
                    // First determine who has 2 of clubs using its location in the deck
                    for(int i=0; i<52; i++)
                    {
                        // Find 2 of clubs in the deck
                        if(Model.DeckCards.getDeck().get(i).equals("♣2"))
                        {
                            // Player West has 2 of clubs
                            if(i<13)
                            {
                                PlayerW.setStartFT(true);
                            }
                            // Player North has 2 of clubs
                            else if(i<26)
                            {
                                PlayerN.setStartFT(true);
                            }
                            // Player East has 2 of clubs
                            else
                            {
                                PlayerE.setStartFT(true);
                            }
                        }
                    }

                    // AI with 2 of clubs plays a card
                    // West starts
                    if(PlayerW.isStartFT() == true)
                    {
                        playDisplayFirst("west");
                        playDisplayFirst("north");
                        playDisplayFirst("east");
                        FirstCard = HeartsGUI.getjButtonW().getText();
                        HeartsGUI.setWhoStarted("west");
                    }

                    // North starts
                    else if(PlayerN.isStartFT() == true)
                    {
                        playDisplayFirst("north");
                        playDisplayFirst("east");
                        FirstCard = HeartsGUI.getjButtonN().getText();
                        HeartsGUI.setWhoStarted("north");

                        // Exit this function and wait for user to press a button
                        // When user plays, will go back to where we left off and west will play
                    }
                    
                    // East starts
                    else
                    {
                        // E, U, then W N
                        playDisplayFirst("east");
                        FirstCard = HeartsGUI.getjButtonE().getText();
                        HeartsGUI.setWhoStarted("east");
                    }
                }
            }
        }
        
        // Not first trick
        else
        {
            // Check if user started first
            if(HeartsGUI.getWhoWon().equals("user"))
            {
                // Player west, north, and east goes
                HeartsGUI.getjButtonW().setText(PlayerW.play(PlayerW.getHand(),HeartsGUI.getjButtonU().getText()));
                HeartsGUI.getjButtonW().setVisible(true);
                HeartsGUI.getjButtonN().setText(PlayerN.play(PlayerN.getHand(),HeartsGUI.getjButtonU().getText(),HeartsGUI.getjButtonW().getText()));
                HeartsGUI.getjButtonN().setVisible(true);
                HeartsGUI.getjButtonE().setText(PlayerE.play(PlayerE.getHand(),HeartsGUI.getjButtonU().getText(),HeartsGUI.getjButtonW().getText(),HeartsGUI.getjButtonN().getText()));
                HeartsGUI.getjButtonE().setVisible(true);
                HeartsGUI.setWhoStarted("user");
                findWinner();
                
                // Indicate that this trick ends
                HeartsGUI.setWhoStarted("finished");
            }
            
            // User does not start first
            else {
                // West previously started
                if(HeartsGUI.getWhoStarted().equals("west"))
                {
                    findWinner();
                    HeartsGUI.setWhoStarted("finished");
                }
                // North previously started
                else if(HeartsGUI.getWhoStarted().equals("north"))
                {
                    HeartsGUI.getjButtonW().setText(PlayerW.play(PlayerW.getHand(),HeartsGUI.getjButtonN().getText(),HeartsGUI.getjButtonE().getText(),HeartsGUI.getjButtonU().getText()));
                    HeartsGUI.getjButtonW().setVisible(true);
                    findWinner();
                    // Reset who played value to start next trick
                    HeartsGUI.setWhoStarted("finished");
                }
                // East previously started
                else if(HeartsGUI.getWhoStarted().equals("east"))
                {
                    HeartsGUI.getjButtonW().setText(PlayerW.play(PlayerW.getHand(),HeartsGUI.getjButtonE().getText(),HeartsGUI.getjButtonU().getText()));
                    HeartsGUI.getjButtonW().setVisible(true);
                    HeartsGUI.getjButtonN().setText(PlayerN.play(PlayerN.getHand(),HeartsGUI.getjButtonE().getText(),HeartsGUI.getjButtonU().getText(),HeartsGUI.getjButtonW().getText()));
                    HeartsGUI.getjButtonN().setVisible(true);
                    findWinner();
                    HeartsGUI.setWhoStarted("finished");
                }
                // First card hasn't been played yet
                else {
                    // AI starts first
                    // West starts first
                    if(HeartsGUI.getWhoWon().equals("west"))
                    {
                        HeartsGUI.getjButtonW().setText(PlayerW.play(PlayerW.getHand()));
                        HeartsGUI.getjButtonW().setVisible(true);
                        HeartsGUI.getjButtonN().setText(PlayerN.play(PlayerN.getHand(),HeartsGUI.getjButtonW().getText()));
                        HeartsGUI.getjButtonN().setVisible(true);
                        HeartsGUI.getjButtonE().setText(PlayerE.play(PlayerE.getHand(),HeartsGUI.getjButtonW().getText(),HeartsGUI.getjButtonN().getText()));
                        HeartsGUI.getjButtonE().setVisible(true);
                        FirstCard = HeartsGUI.getjButtonW().getText();
                        HeartsGUI.setWhoStarted("west");
                    }
                    
                    // North starts first
                    else if(HeartsGUI.getWhoWon().equals("north"))
                    {
                        HeartsGUI.getjButtonN().setText(PlayerN.play(PlayerN.getHand()));
                        HeartsGUI.getjButtonN().setVisible(true);
                        HeartsGUI.getjButtonE().setText(PlayerE.play(PlayerE.getHand(),HeartsGUI.getjButtonN().getText()));
                        HeartsGUI.getjButtonE().setVisible(true);
                        FirstCard = HeartsGUI.getjButtonN().getText();
                        HeartsGUI.setWhoStarted("north");

                        // Exit this function and wait for user to press a button
                        // When user plays, will go back to where we left off and west will play
                    }
                    // East starts first
                    else
                    {
                        HeartsGUI.getjButtonE().setText(PlayerE.play(PlayerE.getHand()));
                        HeartsGUI.getjButtonE().setVisible(true);
                        FirstCard = HeartsGUI.getjButtonE().getText();
                        HeartsGUI.setWhoStarted("east");
                    }
                }
            }
            
            // Update colours of cards on screen
            updateColour();
        }
        
        // Enable all buttons
        for(int i=0; i<13; i++)
        {
            HeartsGUI.getArrayButtons()[i].setEnabled(true);
        }
        
        // Check if the trick has ended
        if(HeartsGUI.getWhoStarted().equals("finished"))
        {
            // Show ready button if the trick has ended
            HeartsGUI.getjButtonReady().setVisible(true);
            HeartsGUI.setTrickCounter(HeartsGUI.getTrickCounter()+1);
            
            // Disable all buttons
            for(int i=0; i<13; i++)
            {
                HeartsGUI.getArrayButtons()[i].setEnabled(false);
            }
            
            // Output scores and start new round if all tricks are done
            if(HeartsGUI.getTrickCounter() == 13) {
                // Display if user won
                if(PlayerU.getScore() <= PlayerW.getScore() && PlayerU.getScore() <= PlayerN.getScore() && PlayerU.getScore() <= PlayerE.getScore()) {
                    HeartsGUI.getjButtonReady().setText("Bravo, you win!");
                }
                else {
                    HeartsGUI.getjButtonReady().setText("Sorry, you lose!");
                }
                
                // Output scores to screen
                HeartsGUI.getjButtonU().setText("Score: " + Integer.toString(PlayerU.getScore()));
                HeartsGUI.getjButtonW().setText("Score: " + Integer.toString(PlayerW.getScore()));
                HeartsGUI.getjButtonN().setText("Score: " + Integer.toString(PlayerN.getScore()));
                HeartsGUI.getjButtonE().setText("Score: " + Integer.toString(PlayerE.getScore()));
                
                // Reset button colours
                // Change colour to black by default
                HeartsGUI.getjButtonW().setForeground(new java.awt.Color(0, 0, 0));
                HeartsGUI.getjButtonN().setForeground(new java.awt.Color(0, 0, 0));
                HeartsGUI.getjButtonE().setForeground(new java.awt.Color(0, 0, 0));
                HeartsGUI.getjButtonU().setForeground(new java.awt.Color(0, 0, 0));
                
                // Get the date
                // Create date format and object of imported date class
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                Date date = new Date();
                
                // Output dates and scores to a file
                try (BufferedWriter scoreOutput = new BufferedWriter(new FileWriter("PerformanceTracker.txt",true))) {
                    // Separate this data from previous records
                    scoreOutput.write("~~~~~~~~~~~~~~~~~~~~");
                    scoreOutput.newLine();
                    
                    // Write the date
                    scoreOutput.write(dateFormat.format(date));
                    scoreOutput.newLine();
                    
                    // Write the scores
                    scoreOutput.write("Your score is " + Integer.toString(PlayerU.getScore()));
                    scoreOutput.newLine();
                    scoreOutput.write("West's score is " + Integer.toString(PlayerW.getScore()));
                    scoreOutput.newLine();
                    scoreOutput.write("North's score is " + Integer.toString(PlayerN.getScore()));
                    scoreOutput.newLine();
                    scoreOutput.write("East's score is " + Integer.toString(PlayerE.getScore())); 
                    scoreOutput.newLine();
                    scoreOutput.newLine();
                    scoreOutput.flush();
                    scoreOutput.close();
                }
                catch (Exception e) {
                    alert("Error: " + e);
                }
            }
        }
        
        // Trick has not finished
        else {
            // Do not allow user to play hearts if hearts have not been broken
            if(HeartsGUI.isHeartsBroken() == false) {

                // Disable all hearts
                for(int i=0; i<13; i++) {
                    // Check if it's a heart
                    if(HeartsGUI.getArrayButtons()[i].getText().substring(0,1).equals("♥")) {
                        HeartsGUI.getArrayButtons()[i].setEnabled(false);
                    }
                }
            }
        
            // User must follow suit if AI started the trick first
            // User has card of same suit
            if(!AI.highestCardSuit(PlayerU.getHand(), FirstCard.substring(0,1)).equals("")) {

                // Disable all cards of different suit
                for(int i=0; i<13; i++) {
                    // Check if they are of different suit
                    if(!HeartsGUI.getArrayButtons()[i].getText().substring(0,1).equals(FirstCard.substring(0,1))) {
                        HeartsGUI.getArrayButtons()[i].setEnabled(false);
                    }
                }
            }

            // User does not have card of same suit
            else {
                // Enable all cards including hearts
                for(int i=0; i<13; i++) {
                    HeartsGUI.getArrayButtons()[i].setEnabled(true);
                }
            }
        }       
    }
    
    
    public static AI getPlayerW() {
        return PlayerW;
    }

    public static void setPlayerW(AI PlayerW1) {
        PlayerW = PlayerW1;
    }

    public static AI getPlayerN() {
        return PlayerN;
    }

    public static void setPlayerN(AI PlayerN1) {
        PlayerN = PlayerN1;
    }

    public static AI getPlayerE() {
        return PlayerE;
    }

    public static void setPlayerE(AI PlayerE1) {
        PlayerE = PlayerE1;
    }

    public static User getPlayerU() {
        return PlayerU;
    }

    public static void setPlayerU(User PlayerU1) {
        PlayerU = PlayerU1;
    }

    private void alert(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}