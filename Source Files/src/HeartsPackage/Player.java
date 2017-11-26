package HeartsPackage;

import java.util.ArrayList;

public class Player
{
    // Create attributes
    private ArrayList<String> hand = new ArrayList<>();
    private int score = 0;
    
    // Getters and setters
    public ArrayList<String> getHand() {
        return hand;
    }
    
    public int getScore() {
        return score;
    }

    public void setHand(ArrayList<String> hand) {
        this.hand = hand;
    }

    public void setScore(int score) {
        this.score = score;
    }
    
    // Adding and removing from arraylists
    public void addCard(String Card) {
        this.hand.add(Card);
    }
    
    public void removeCard(String Card) {
        this.hand.remove(Card);
    }
}