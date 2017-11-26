package HeartsPackage;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {

    private ArrayList<String> deck;
    
    // Constructor for making the deck
    public Deck()
    {
        // Create Arraylist of the deck
        ArrayList<String> deck1 = new ArrayList<>();

        // Create all the spades cards and add to deck
        for(int i=0; i < 13; i++) {
            // Create card of each suit and add to deck arraylist
            deck1.add("♣" + (i+2));
            deck1.add("♦" + (i+2));
            deck1.add("♠" + (i+2));
            deck1.add("♥" + (i+2));
        }
        
        this.deck = deck1;
    }
    
    // Method for shuffling the deck
    public void shuffle(ArrayList<String> deck1)
    {
        // Use built in function to shuffle an arraylist
        Collections.shuffle(deck1);
        this.deck = deck1;
    }        
    
    // Method for sorting cards
    public ArrayList<String> sortHand (ArrayList<String> hand1)
    {
        // Create mini arraylists to split cards
        ArrayList<String> spadesArrayList = new ArrayList<>();
        ArrayList<String> heartsArrayList = new ArrayList<>();
        ArrayList<String> clubsArrayList = new ArrayList<>();
        ArrayList<String> diamondsArrayList = new ArrayList<>();
        
        // Create final arraylist to be returned
        ArrayList<String> finalArrayList = new ArrayList<>();
        
        // Loop to displace each card
        for(int i=0; i < 13; i++) {
            // Check if card is a club
            if((hand1.get(i)).substring(0,1).equals("♣"))
            {
                // Add to mini array list
                clubsArrayList.add((hand1.get(i)).substring(1));
            }
            // Repeat for all other suits
            else if((hand1.get(i)).substring(0,1).equals("♦"))
            {
                diamondsArrayList.add((hand1.get(i)).substring(1));
            }
            else if((hand1.get(i)).substring(0,1).equals("♠"))
            {
                spadesArrayList.add((hand1.get(i)).substring(1));
            }
            else
            {
                heartsArrayList.add((hand1.get(i)).substring(1));
            }
        }
        
        // Convert arraylists into arrays before sorting
        int[] clubsArray = new int[clubsArrayList.size()];
        int[] diamondsArray = new int[diamondsArrayList.size()];
        int[] spadesArray = new int[spadesArrayList.size()];
        int[] heartsArray = new int[heartsArrayList.size()];
       
        // Take each value in each mini arraylist and convert to int
        for(int i=0;i<clubsArrayList.size();i++)
        {
            clubsArray[i] = Integer.parseInt((clubsArrayList.get(i)));
            
        }
        
        for(int i=0;i<diamondsArrayList.size();i++)
        {
            diamondsArray[i] = Integer.parseInt((diamondsArrayList.get(i)));
        }
        
        for(int i=0;i<spadesArrayList.size();i++)
        {
            spadesArray[i] = Integer.parseInt((spadesArrayList.get(i)));
        }
        
        for(int i=0;i<heartsArrayList.size();i++)
        {
            heartsArray[i] = Integer.parseInt((heartsArrayList.get(i)));
        }

        // Sort mini arrays
        mergeSort(clubsArray);
        mergeSort(diamondsArray);
        mergeSort(spadesArray);
        mergeSort(heartsArray);
        
        // Add the appropriate suit for each mini array
        for(int i=0; i < clubsArrayList.size(); i++)
        {
            finalArrayList.add("♣" + Integer.toString((clubsArray[i])));
        }
        for(int i=0; i < diamondsArrayList.size(); i++)
        {
            finalArrayList.add("♦" + Integer.toString((diamondsArray[i])));
        }
        for(int i=0; i < spadesArrayList.size(); i++)
        {
            finalArrayList.add("♠" + Integer.toString((spadesArray[i])));
        }
        for(int i=0; i < heartsArrayList.size(); i++)
        {
            finalArrayList.add("♥" + Integer.toString((heartsArray[i])));
        }
        
        return finalArrayList;
    }
    
    // Method for sorting the array
    public static void mergeSort(int[] arrayCards)
    {
        int size = arrayCards.length;
        
        // Check if base case has been reached
        if (size < 2)
            return;
        
        // Declare necessary numbers and arrays
        int middleNum = size / 2;
        int leftSize = middleNum;
        int rightSize = size - middleNum;
        int[] leftArray = new int[leftSize];
        int[] rightArray = new int[rightSize];
        
        // Create array to the left of middle number
        for (int i = 0; i < middleNum; i++) {
            leftArray[i] = arrayCards[i];

        }
        
        // Create array to the right of middle number
        for (int i = middleNum; i < size; i++) {
            rightArray[i - middleNum] = arrayCards[i];
        }
        
        // Sort left and right arrays and merge them
        mergeSort(leftArray);
        mergeSort(rightArray);
        merge(leftArray, rightArray, arrayCards);
    }

    // Method for merging the cards
    public static void merge(int[] left, int[] right, int[] newArrayCards)
    {    
        // Declare size of arrays and counters
        int leftSize = left.length;
        int rightSize = right.length;
        int i = 0;
        int j = 0;
        int k = 0;
        
        // Check if there are still cards left on both sides of array
        while(i < leftSize && j < rightSize) {
            
            // Assign the cards to new array
            if (left[i] <= right[j]) {
                newArrayCards[k] = left[i];
                i++;
                k++;
            }
            else {
                newArrayCards[k] = right[j];
                k++;
                j++;
            }
        }
        
        // Check again if there are still cards on the left of array
        while(i < leftSize) {
            newArrayCards[k] = left[i];
            k++;
            i++;
        }
        
        // Check for remaining cards on the right
        while(j < rightSize) {
            newArrayCards[k] = right[j];
            k++;
            j++;
        }
    }

    public ArrayList<String> getDeck() {
        return deck;
    }

    public void setDeck(ArrayList<String> deck) {
        this.deck = deck;
    }
}