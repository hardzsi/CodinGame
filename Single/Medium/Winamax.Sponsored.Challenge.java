// Winamax Sponsored Challenge
import java.util.*;

class Solution {
    public static void main(String args[]) {
        LinkedList<Card> p1hand = new LinkedList<>();
        LinkedList<Card> p2hand = new LinkedList<>();
        
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();                               // Number of cards for player 1
        for (int i = 0; i < n; i++) {
            String cardp1 = in.next();                      // The n cards of player 1
            int len = cardp1.length() - 1;
            p1hand.add(new Card(cardp1.substring(0, len), cardp1.substring(len)));
        }
        int m = in.nextInt();                               // Number of cards for player 2
        for (int i = 0; i < m; i++) {
            String cardp2 = in.next();                      // The m cards of player 2
            int len = cardp2.length() - 1;
            p2hand.add(new Card(cardp2.substring(0, len), cardp2.substring(len)));
        }
        debug("p1hand", p1hand);
        debug("p2hand", p2hand);
 
        int round = 0;
        boolean pat = false;
        Card card1, card2;
        LinkedList<Card> p1stack = new LinkedList<>();
        LinkedList<Card> p2stack = new LinkedList<>();
        
        while (p1hand.size() != 0 && p2hand.size() != 0) {  // Game
            card1 = p1hand.pop();                           // Move 1-1 card to each stack
            p1stack.add(card1);
            card2 = p2hand.pop();
            p2stack.add(card2);
            while (card1.equals(card2)) {                   // War
                if (p1hand.size() < 4 || p2hand.size() < 4) {
                    pat = true;                             // We have PAT
                    break;
                }
                for (int i = 0; i < 4; ++i) {               // Move 4-4 cards to each stack
                    card1 = p1hand.pop();
                    p1stack.add(card1);
                    card2 = p2hand.pop();
                    p2stack.add(card2);
                }
            }
            if (pat) { break; }                             // Let the game end
            p1stack.addAll(p2stack);
            if (card1.value() > card2.value()) {
                p1hand.addAll(p1stack);
            } else {
                p2hand.addAll(p1stack);
            }
            p1stack.clear();
            p2stack.clear();
            round++;
        }
        debug("\noutput:");
        String winner = p2hand.size() == 0 ? "1 " : "2 ";
        System.out.println(pat ? "PAT" : winner + round);   // If both first "PAT", otherwise "n r" where n:player (1 or 2), r:rounds
    }

    public static void debug(String s) { System.err.println(s); }

    public static void debug(String s, LinkedList<Card> cards) {
        StringBuffer result = new StringBuffer(s + ": ");
        for (Card card : cards) { result.append(card.toString()).append(", "); }
        System.err.println(result.toString());
    }
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