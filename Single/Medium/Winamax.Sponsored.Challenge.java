// Winamax Sponsored Challenge 2015.12.09
import java.util.*;

class Solution {
    public static void main(String args[]) {
        List<Card> p1hand = new ArrayList<>();
        List<Card> p2hand = new ArrayList<>();
        
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();                   // Number of cards for player 1
        for (int i = 0; i < n; i++) {
            String cardp1 = in.next();          // The n cards of player 1
            int len = cardp1.length() - 1;
            //debug(i + ".cardp1:" + cardp1 + " (" + cardp1.substring(0, len) + "," + cardp1.substring(len) + ")");
            p1hand.add(new Card(cardp1.substring(0, len), cardp1.substring(len)));
        }
        debug("");
        int m = in.nextInt();                   // Number of cards for player 2
        for (int i = 0; i < m; i++) {
            String cardp2 = in.next();          // The m cards of player 2
            int len = cardp2.length() - 1;
            //debug(i + ".cardp2:" + cardp2 + " (" + cardp2.substring(0, len) + "," + cardp2.substring(len) + ")");
            p2hand.add(new Card(cardp2.substring(0, len), cardp2.substring(len)));
        }
        debug("\np1hand:"); for (Card card : p1hand) { debug (card.toString()); }
        debug("\np2hand:"); for (Card card : p2hand) { debug (card.toString()); }
        /*debug("\ncompare p1 cards:");
        for (int i = 0; i < p1hand.size() - 1; ++i) {
            int comp = p1hand.get(i).compareTo(p1hand.get(i + 1));
            boolean eq = p1hand.get(i).equals(p1hand.get(i + 1));
            debug(p1hand.get(i) + " compared to " + p1hand.get(i + 1) + ": " + comp + " (equals:" + eq + ")");
        }*/
        //debug("\ncompare p2 cards:"); for (int i = 0; i < p2hand.size() - 1; ++i) { debug(p2hand.get(i) + " compared to " + p2hand.get(i + 1) + ": " + p2hand.get(i).compareTo(p2hand.get(i + 1))); }
 
        debug("\noutput:");
        //for (Rank rank : Rank.values()) { debug(String.format("%-5s (id:%2s value:%2d)", rank, rank.id(), rank.value())); }
        //for (Suit suit : Suit.values()) { debug (suit + " (" + suit.id() + ")"); }
        System.out.println("PAT");              // If both first "PAT", otherwise "n r" where n:player (1 or 2), r:rounds
    } // main()
    
    public static void debug(String s) { System.err.println(s); }
} // class Solution

class Card implements Comparable<Card> {
    // Constructors
    Card(String r, String s) {
        for (Rank rank : Rank.values()) {
            if (r.equals(rank.id())) {
                this.rank = rank;
                break;
            }
        }
        for (Suit suit : Suit.values()) {
            if (s.equals(suit.id())) {
                this.suit = suit;
                break;
            }
        }
    }
    Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }  
    //Methods
    public int value() { return rank.value(); }
    @Override
    public int compareTo(Card other) { return (int)(Math.signum(this.value() - other.value())); } 
    @Override
    public boolean equals(Object other) { return this.value() == ((Card)other).value(); }
    @Override
    public String toString() { return "" + rank.id() + suit.id(); }
    
    private Rank rank;
    private Suit suit;
}

enum Rank { 
    TWO ("2", 0), THREE ("3", 1), FOUR ("4", 2), FIVE ("5", 3), SIX ("6", 4), 
    SEVEN ("7", 5), EIGHT ("8", 6), NINE ("9", 7), TEN ("10", 8), JACK ("J", 9),
    QUEEN ("Q", 10), KING ("K", 11), ACE ("A", 12);

    private final String id;
    private final int value;
    Rank(String id, int value) {
        this.id = id;
        this.value = value;
    }
    public String id() { return id; }
    public int value() { return value; }
}

enum Suit {
    DIAMONDS ("D"), HEARTS ("H"), CLUBS ("C"), SPADES ("S");

    private final String id;
    Suit(String id) { this.id = id; }
    public String id() { return id; }   
}