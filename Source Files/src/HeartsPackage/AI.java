package HeartsPackage;

import java.util.ArrayList;

public class AI extends Player {
    
    // Variable to see whether or not a player starts the first trick
    private boolean startFT = false;
    
    // The computer algorithm used by AIs
    // Overloading used since algorithm depends on how many cards played
    // Parameters indicate order of cards played
    // No cards played and AI must start
    public String play(ArrayList<String> hand1)
    {
        // Find the lowest card of a suit for each suit
        String LowestC = lowestCardSuit(hand1,"♣");
        String LowestD = lowestCardSuit(hand1,"♦");
        String LowestS = lowestCardSuit(hand1,"♠");
        String LowestH = lowestCardSuit(hand1,"♥");
        String CardToPlay = "";
        
        // Check if hearts has been broken 
        if(HeartsGUI.isHeartsBroken() == true)
        {
            // Check if AI has queen of spades
            if(hand1.contains("♠12")) {
                CardToPlay = playHasQueenS(LowestC,LowestD,LowestS,LowestH);
            }
            
            // AI does not have queen of spades
            else {
                // Play spades if possible, but less than queen of spades
                CardToPlay = highestValueBelow(hand1,"♠12");

                // Play diamonds if not possible
                if(CardToPlay.equals("")) {
                   CardToPlay = LowestD;
                }

                // Play hearts if not possible, and if the heart is less than 8
                if(CardToPlay.equals("") && !LowestH.equals("") && Integer.parseInt(LowestH.substring(1)) < 8) {
                    CardToPlay = LowestH;
                }
                
                // If still not possible play clubs
                if(CardToPlay.equals("")) {
                    CardToPlay = LowestC;
                }
                
                // If still not possible play any heart
                if(CardToPlay.equals("")) {
                    CardToPlay = LowestH;
                }
                
                // If still not possible play any cards left, assuming queen of spades is still left in hand
                if(CardToPlay.equals("")) {
                    CardToPlay = LowestH;
                }
            }
        }
        
        // Hearts has not been broken
        else {
            // Check if AI has queen of spades
            if(hand1.contains("♠12")) {
                CardToPlay = playHasQueenS(LowestC,LowestD,LowestS,LowestH);
            }
            else {
                // Play spades if possible, but less than queen of spades
                CardToPlay = highestValueBelow(hand1,"♠12");

                // Play diamonds if not possible
                if(CardToPlay.equals("")) {
                   CardToPlay = LowestD;
                }
                
                // Play clubs if not possible
                if(CardToPlay.equals("")) {
                   CardToPlay = LowestC;
                }
                
                // AI is forced to play a high spade
                if(CardToPlay.equals("")) {
                    CardToPlay = LowestS;
                }
                
                // AI is forced to play hearts
                if(CardToPlay.equals("")) {
                   CardToPlay = LowestH;
                   HeartsGUI.setHeartsBroken(true);
                }
            }
        }

        // Return the card the player will play and remove it from the hand
        hand1.remove(CardToPlay);
        this.setHand(hand1);

        // Rewrite rank if it was 11 or more
        CardToPlay = HeartsGUI.getModel().rewriteRank(CardToPlay);
        return CardToPlay;
    }
    
    // Only one card has been played
    public String play(ArrayList<String> hand1,String Card1)
    {
        String CardToPlay = "";
        // Convert all cards back to numerical values
        Card1 = HeartsGUI.getModel().rewriteRankBack(Card1);
        
        // Choose a card based on hand, highest card played, and if point cards were played on non-point suit
        CardToPlay = chooseCard(hand1,Card1,false);
        return CardToPlay;
    }
    
    // Two cards played
    public String play(ArrayList<String> hand1,String Card1,String Card2)
    {
        String CardToPlay = "";
        
        boolean pointCardPlayed = false;
        Card1 = HeartsGUI.getModel().rewriteRankBack(Card1);
        Card2 = HeartsGUI.getModel().rewriteRankBack(Card2);
        String HighestCard = Card1;
        
        // Compare cards to find highest card played
        // Check if second card played was the same suit and greater than first card
        if(Card1.charAt(0) == Card2.charAt(0) && Integer.parseInt(Card2.substring(1)) > Integer.parseInt(Card1.substring(1)))
        {
            HighestCard = Card2;
        }
        
        // Check if a point card was played on a non-heart suit
        else if((!Card1.substring(0,1).equals("♥") && Card2.substring(0,1).equals("♥")) || Card2.equals("♠12")) {
            pointCardPlayed = true;
        }
        
        CardToPlay = chooseCard(hand1,HighestCard,pointCardPlayed);
        return CardToPlay;
    }
    
    // Three cards played
    public String play(ArrayList<String> hand1,String Card1,String Card2,String Card3)
    {
        String CardToPlay = "";
        
        boolean pointCardPlayed = false;
        Card1 = HeartsGUI.getModel().rewriteRankBack(Card1);
        Card2 = HeartsGUI.getModel().rewriteRankBack(Card2);
        Card3 = HeartsGUI.getModel().rewriteRankBack(Card3);
        String HighestCard = Card1;
        
        // Compare cards to find highest card played
        // Check if second card played was the same suit and greater than first card
        if(Card1.charAt(0) == Card2.charAt(0) && Integer.parseInt(Card2.substring(1)) > Integer.parseInt(Card1.substring(1)))
        {
            HighestCard = Card2;
        }
        
        // Check if third card played is also the same suit and the highest card
        if(HighestCard.charAt(0) == Card3.charAt(0) && Integer.parseInt(Card3.substring(1)) > Integer.parseInt(HighestCard.substring(1))) {
            HighestCard = Card3;
        }
        
        // Check if a point card was played on a non-heart suit
        else if((!Card1.substring(0,1).equals("♥") && (Card2.substring(0,1).equals("♥") || Card3.substring(0,1).equals("♥"))) || Card2.equals("♠12") || Card3.equals("♠12")) {
            pointCardPlayed = true;
        }
        
        CardToPlay = chooseCard(hand1,HighestCard,pointCardPlayed);
        return CardToPlay;
    }
    
    // Computer algorithm for first trick only
    public String playFirst(ArrayList<String> hand1)
    {
        // Type will be string to match substring datatype
        String ClubHighest = "";
        String CardToPlay = "";
        
        // Play 2 of clubs if possible
        if(hand1.get(0).equals("♣2"))
        {
            CardToPlay = "♣2";
        }
        
        // Does not have 2 of clubs
        else
        {
            // Play highest club possible if there is one
            ClubHighest = highestCardSuit(hand1,"♣");

            // There are clubs
            if(!ClubHighest.equals(""))
            {
                // Play highest club
                CardToPlay = ClubHighest;
            }
            
            // Player has no clubs, is allowed to play non-club suit
            else
            {
                // Play a diamond or spade, but not queen of spades or any heart
                // If player has queen of spades, no spades should be played
                if(hand1.contains("♠12"))
                {
                    // Play highest diamond possible
                    CardToPlay = highestCardSuit(hand1,"♦");
                    
                    // If no diamonds found, play lowest spade possible
                    // This increases chance of winning a trick with spades in later tricks
                    // In the future a more complex AI will take into consideration number of spades left etc.
                    if(CardToPlay.equals(""))
                    {
                        CardToPlay = lowestCardSuit(hand1,"♠");
                    }
                    
                    // Catch in case the player was dealt only hearts and queen of spades
                    if(CardToPlay.equals(""))
                    {
                        CardToPlay = "♠12";
                    }
                }
                
                // Play highest spade possible
                else
                {
                    // Find highest spade
                    CardToPlay = highestCardSuit(hand1,"♠");
                    
                    // If no spades in hand, play a diamond
                    if(CardToPlay.equals(""))
                    {
                        CardToPlay = highestCardSuit(hand1,"♦");
                    }
                    
                    // Catch if player was dealt only hearts                    
                    if(CardToPlay.equals(""))
                    {
                        CardToPlay = highestCardSuit(hand1,"♥");
                        HeartsGUI.setHeartsBroken(true);
                    }
                }
            }
        }
        
        // Return the card the player will play and remove it from the hand
        hand1.remove(CardToPlay);
        this.setHand(hand1);

        // Rewrite rank if it was 11 or more
        CardToPlay = HeartsGUI.getModel().rewriteRank(CardToPlay);
        return CardToPlay;
    }
    
    // Method for finding the highest card of a suit
    public static String highestCardSuit(ArrayList<String> hand1, String Suit)
    {
        String CardToBePlayed = "";
        
        // Loop to find the highest card in suit
        for(int i=0; i < hand1.size(); i++) {
            // Track the highest value
            if(hand1.get(i).substring(0,1).equals(Suit))
            {
                CardToBePlayed = Suit + hand1.get(i).substring(1);
            }
        }
        return CardToBePlayed;
    }
    
    // Method for finding the lowest card of a suit
    public String lowestCardSuit(ArrayList<String> hand1, String Suit)
    {
        String CardToBePlayed = "";
        
        // Loop to find the lowest card in suit
        for(int i = hand1.size()-1; i > -1; i--) {
            // Track the lowest value
            if(hand1.get(i).substring(0,1).equals(Suit))
            {
                CardToBePlayed = Suit + hand1.get(i).substring(1);
            }
        }
        return CardToBePlayed;
    }
    
    // Method for finding the highest card of a suit and under a certain number
    public String highestValueBelow(ArrayList<String> hand1,String Card1)
    {
        String CardToBePlayed = "";
        
        // Loop to find highest card in suit
        for(int i=0; i < hand1.size(); i++) {
            // Check if it's under a certain number
            if(hand1.get(i).substring(0,1).equals(Card1.substring(0,1)) && Integer.parseInt(hand1.get(i).substring(1)) < Integer.parseInt(Card1.substring(1))) {
                CardToBePlayed = Card1.substring(0,1) + hand1.get(i).substring(1);
            }
        }
        return CardToBePlayed;
    }
    
    // Method for finding number of cards with a certain suit
    public int numSuit(ArrayList<String> hand1,String Suit)
    {
        int numS = 0;
        
        // Loop to find number of cards
        for(int i=0; i < hand1.size(); i++) {
            // Check if suits match
            if(hand1.get(i).substring(0,1).equals(Suit)) {
                numS++;
            }
        }
        return numS;
    }
    
    // Method for choosing card based on hand, highest card played and if a points card was played
    public String chooseCard(ArrayList<String> hand1,String Card1,boolean pointCardPlayed)
    {
        String CardToChoose = "";
        
        // Play suit identical to the first card if possible
        if(!highestCardSuit(hand1,Card1.substring(0,1)).equals("") && (highestCardSuit(hand1,Card1.substring(0,1)).substring(0,1).equals(Card1.substring(0,1))))
        {
            // Play card below first card played
            CardToChoose = highestValueBelow(hand1,Card1);

            // Check if card played was less than 8
            // Only play this if it's safe and no hearts or queen of spades was played on this
            if(Integer.parseInt(Card1.substring(1)) < 8 && pointCardPlayed == false) {
                CardToChoose = highestValueBelow(hand1,Card1.substring(0,1)+"10");
            }
            
            // If AI doesn't have a card below first card played
            // Play the highest card
            if(CardToChoose.equals(""))
            {
                CardToChoose = highestCardSuit(hand1,Card1.substring(0,1));
            }
        }
        
        // AI does not have a card of the same suit 
        else {

            // Play queen of spades if possible
            if(hand1.contains("♠12")) {
                CardToChoose = "♠12";
            }
            
            // Play king and ace of spades if possible
            else if(hand1.contains("♠13")) {
                CardToChoose = "♠13";
            }
            else if(hand1.contains("♠14")) {
                CardToChoose = "♠14";
            }
            
            // Break hearts if there are many diamonds and clubs left
            else if((numSuit(hand1,"♣") > 2 || numSuit(hand1,"♦") > 2 ) && numSuit(hand1,"♥") != 0) {
                CardToChoose = highestCardSuit(hand1,"♥");
                HeartsGUI.setHeartsBroken(true);
            }
            
            // Otherwise, get rid of clubs and diamonds if there are only a few
            else {
                // Play the suit with the least cards
                if((numSuit(hand1,"♣") < numSuit(hand1,"♦") || numSuit(hand1,"♦") == 0) && numSuit(hand1,"♣") != 0) {
                    CardToChoose = highestCardSuit(hand1,"♣");
                }
                else {
                    // Play diamonds
                    CardToChoose = highestCardSuit(hand1,"♦");
                    
                    // If there are no diamonds, play a different card
                    if(CardToChoose.equals("")) {
                        CardToChoose = highestCardSuit(hand1,"♥");
                        HeartsGUI.setHeartsBroken(true);
                    }
                    
                    // If still not possible, play a spade
                    if(CardToChoose.equals("")) {
                        CardToChoose = highestCardSuit(hand1,"♠");
                    }
                    
                    // If still not possible, play a club
                    if(CardToChoose.equals("")) {
                        CardToChoose = highestCardSuit(hand1,"♣");
                    }
                }
            }
        }
        
        // Return the card the player will play and remove it from the hand
        this.removeCard(CardToChoose);
        // Rewrite rank if it was 11 or more
        CardToChoose = HeartsGUI.getModel().rewriteRank(CardToChoose);
        return CardToChoose;
    }
    
    // Method for choosing a card if AI has queen of spades
    public String playHasQueenS(String LowC,String LowD,String LowS,String LowH)
    {
        // Play lowest diamond if possible
        String CardToPlay = LowD;

        // Check if AI has no diamonds
        if(CardToPlay.equals("")) {
            // Play a club
            CardToPlay = LowC;
        }

        // Play hearts if still not possible
        if(CardToPlay.equals("")) {
            CardToPlay = LowH;
        }

        // Play a spade if still not possible
        if(CardToPlay.equals("")) {
            CardToPlay = LowS;   
        }
        
        return CardToPlay;
    }
    
    public boolean isStartFT() {
        return startFT;
    }

    public void setStartFT(boolean startFT) {
        this.startFT = startFT;
    }
}