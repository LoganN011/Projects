package scrabble;

import java.util.ArrayList;

/**
 * A class that is used to calculate the score of a move in a game of scrabble
 *
 * @author Logan Nunno
 */
public class ScoreChecker {

    /**
     * Calculate the score of word in scrabble based on the default scoring and
     * rules of scrabble
     *
     * @param word       the word that was played on the board in the given bord squares
     * @param dictionary the list of valid words as a Trie
     * @param original   the original board that was played on and is used to check
     *                   what multiples need to be taken int account when scoring
     * @param bonus      a bounce that is added to the end of the score
     * @return the score if the word is valid and -1 if not
     */
    public static int checkScore(ArrayList<BoardSquare> word, Dictionary dictionary, Board original, int bonus) {
        int score = 0;
        String wordString = "";
        ArrayList<Integer> wordMultipliers = new ArrayList<>();

        for (int i = 0; i < word.size(); i++) {
            wordString += word.get(i).getLetter();
            score += word.get(i).getScore() * getBoardSquare(word, original, i).getLetterMultiplier();
            wordMultipliers.add(getBoardSquare(word, original, i).getWordMultiplier());
        }
        if (dictionary.getWords().isWord(wordString)) {
            if (wordMultipliers.isEmpty()) {
                return score;
            } else {
                int multiplier = 1;
                for (Integer multipliers : wordMultipliers) {
                    multiplier *= multipliers;
                }
                return (score * multiplier) + bonus;
            }
        }
        return -1;
    }

    /**
     * Gets the board square of the original board based on the board square
     * of the board square of the word and the index. used to check the multiples
     * in the original board
     *
     * @param word     word that was played on the board
     * @param original orginal board where a move was made
     * @param index    index of the letter in the word we are looking at
     * @return the original board square of a given index of the word that was played
     */
    private static BoardSquare getBoardSquare(ArrayList<BoardSquare> word, Board original, int index) {
        return original.getBoard()[word.get(index).getRow()][word.get(index).getCol()];
    }
}
