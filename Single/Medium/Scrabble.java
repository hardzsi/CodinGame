// Scrabble
import java.util.*;

class Solution {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int N = in.nextInt();
        in.nextLine();
        String alphabet = "abcdefghijklmnopqrstuvwxyz"; // 26 char
        String[] dictionary = new String[N];            // Store all authorized words
        List<String> words = new ArrayList<>();         // Store words matching LETTERS
        List<Score> scoredWords = new ArrayList<>();    // Store sortable score objects 
        for (int i = 0; i < N; i++) {
            String W = in.nextLine();
            dictionary[i] = W;
        }
        String LETTERS = in.nextLine();
        debug("letters:" + LETTERS + ", " + N + " authorized words");
        int[] numLetters = new int[alphabet.length()];
        for (int i = 0; i < LETTERS.length(); ++i) {
            numLetters[alphabet.indexOf(LETTERS.substring(i, i + 1))]++;
        }
        // Store each character of LETTERS - Note: Set removes recurring characters
        Set<String> letters = new HashSet<>(Arrays.asList(LETTERS.split("(?!^)")));
        // Fill words array
        for (String aWord : dictionary) {
            Set<String> word = new HashSet<>(Arrays.asList(aWord.split("(?!^)")));
            if (letters.containsAll(word)) { 
                int[] numWord = new int[alphabet.length()];
                for (int i = 0; i < aWord.length(); ++i) {
                    numWord[alphabet.indexOf(aWord.substring(i, i + 1))]++;
                }
                // Comparing numLetters with numWord for checking aWord validity
                // aWord should have the same or less of each letters as in LETTERS
                boolean valid = true;
                for (int i = 0; i < numLetters.length; ++i) {
                    if (numWord[i] > numLetters[i]) {
                        valid = false;                  // It's invalid
                        break;
                    }
                }
                if (valid) { words.add(aWord); }        // It's valid: add to words
            }
        }

        // Fill scoredWords array from words
        for (int i = 0; i < words.size(); ++i) {
            String word = words.get(i);
            scoredWords.add(new Score(word, i, getScore(word)));
        }

        Collections.sort(scoredWords);                  // Sort scoredWords
        debug("\nscored words sorted:");
        for (Score scoredWord : scoredWords) { debug(scoredWord.toString()); }        

        debug("\noutput:");
        System.out.println(scoredWords.get(0).getWord());
    }// main()
    
    // Return score of a word according to Scrabble rules 
    static int getScore(String word) {
        int score = 0;
        for (char ch : word.toCharArray()) {
            switch (ch) {
                case 'e': case 'a': case 'i': case 'o': case 'n':
                case 'r': case 't': case 'l': case 's': case 'u': score++; break;
                case 'd': case 'g': score += 2; break;
                case 'b': case 'c': case 'm': case 'p': score += 3; break;
                case 'f': case 'h': case 'v': case 'w': case 'y': score += 4; break;
                case 'k': score += 5; break;
                case 'j': case 'x': score += 8; break;
                case 'q': case 'z': score += 10; break;
            }
        }
        return score;
    }// getScore()

    static void debug(String str) {
        System.err.println(str);
    }
}// class Solution

// Sortable word with it's order and score
class Score implements Comparable<Score> {
    public Score(String word, int order, int score) {
        this.word = word;        
        this.order = order;
        this.score = score;
    }

    public String   getWord() { return word; }
    public int      getOrder() { return order; }    
    public int      getScore() { return score; }

    @Override   // Sorting rule: higher score and lower order if score is same
    public int      compareTo(Score other) {
        int result = other.getScore() - score;
        return result == 0 ? order - other.getOrder() : result;
    }

    @Override
    public String   toString() {
        return word + " (score:" + score + ", order:" + order + ")";
    }
    
    private String word;
    private int order;      // It's order of appearance in dictionary
    private int score;      // It's score according to Scrabble rules
}// class Score