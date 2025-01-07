package scrabble;

/**
 * A trie node is a used to make a prefix tree to store a list of words for
 * fast look up and searching of words
 * each edge represent a letter in a word and when following each edge if
 * the boolean is true that means that is a word
 * Adapted form https://www.geeksforgeeks.org/trie-insert-and-search/
 * @author Logan Nunno
 */
public class TrieNode {
    private TrieNode[] children;
    private boolean isWord;

    /**
     * Constructor of a trie node
     * assigns the children to be an array of tree nodes with 26 elements
     * one for each letter of the alphabet
     * and assigns the root to be not a word
     */
    public TrieNode() {
        children = new TrieNode[26];
        isWord = false;
    }

    /**
     * adds a word to the list of words
     *
     * @param word word that will be added to the list
     */
    public void add(String word) {
        if (!word.isEmpty()) {
            TrieNode root = this;
            for (char c : word.toCharArray()) {
                if (root.children[c - 'a'] == null) {
                    TrieNode newNode = new TrieNode();

                    root.children[c - 'a'] = newNode;
                }
                root = root.children[c - 'a'];
            }
            root.isWord = true;
        }
    }

    /**
     * check to see if a provided word is a word in the word list
     *
     * @param word the word that is being checked
     * @return if the provided word is in our word list or not
     */
    public boolean isWord(String word) {
        TrieNode root = this;
        for (char c : word.toLowerCase().toCharArray()) {
            if (root.children[c - 'a'] == null) {
                return false;
            }
            root = root.children[c - 'a'];
        }
        return root.isWord;
    }

    /**
     * get the trie node of the ending of the provided word or provided part
     * of the word
     * if the node does not exist in the tree return null
     * @param word the word that we are getting the node for
     * @return the trie node of the last letter of the word
     */
    public TrieNode lookUp(String word) {
        TrieNode root = this;
        for (char c : word.toLowerCase().toCharArray()) {
            if (root.children[c - 'a'] == null) {
                return null;
            }
            root = root.children[c - 'a'];
        }
        return root;
    }

    /**
     * given a char return the array of children trie nodes for that letter
     * based on the current node
     * @param c char that we are getting the children of
     * @return the children of trie nodes as an array
     */
    public TrieNode getChild(char c) {
        if (Character.isUpperCase(c)) {
            c = Character.toLowerCase(c);
        }
        return children[c - 'a'];
    }

    /**
     * gets the children of the current node
     *
     * @return the children of the current node as an array of Trie nodes
     */
    public TrieNode[] children() {
        return children;
    }

    /**
     * gets if the current node is a word or not
     * @return if the current node is a word or not
     */
    public boolean isWord() {
        return isWord;
    }
}
