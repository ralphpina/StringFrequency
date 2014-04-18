/**
 * This class would presumably have other function besides
 * this method, but since this is just an exercise...
 * R.Pina 04152014
 * 
 * Assumptions: 
 * 1) If a null document is passed to the function, we will throw an error
 * 2) If an empty String is passed, it will return an empty list.
 * 3) If a string made up of spaces is passed, it will return an empty list, since
 * there are no words.
 * 4) If the number of words requested is less than the number of words in the string, 
 *    we'll just return all the words in the string sorted by number of appearance.
 * 5) If 0 is the number of words passed, we'll return an empty list.
 * 6) If two words have the same number of instances, I will order them alphabetically.
 * 
 * "Word" definition:
 * I am assuming a word is any collection of characters (more than 1) sepated by a space.
 * The exception to the rule above is the word "I".
 * If numbers and characters are mixed together, then that is a word. 
 * However:
 *   A word must begin with a letter, but can have numbers intertwined.
 *   So "i18n" is a word. So is "i985"
 *
 *   If numbers precede letters in a word, we ignore the numbers.
 *   "18ln" would be considered a word "ln".
 *
 *   If numbers and letters are mixed, consider the word to start at the first letter,
 *   and end with a white space.
 *   "9lsnsh9 91jn92mj 92f89k9" would be considered to have 3 words: "f89k9", "jn92mj", "lsnsh9".
 *
 *   A word does not include any symbols like $, !, unless it is between characters.
 *   If symbols are present in the beginning, we ignore them.
 *   So "$jifji! $j$ifji! fkji/  %ej-he** ie_l he8 he8he" has 7 words : 
 *   "jifji", "j$ifji", "fkji", "ej-he", "ie_l", "he8", "he8he"
 *
 *   And "$hehe, hehe!jj, hehe!" has 2 words "hehe" is twice and "hehe!jj".
 *   I am assuming that case does not matter, except for I. "I" != "i".
 *   However "Word" == "word" == "wOrD", etc.
 *
 *   Plurals are counted as different words. So "dog" != "dogs".
 */

import java.util.*;

public class StringFrequency {
  
    /*
    * Data we'll be parsing
    */
    String data;

    public StringFrequency() {
    }

    public StringFrequency(String data) {
        this.data = data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    /**
    * Method to get a sorted list of size numberOfWords made up 
    * of words in the document. They will be sorted in decreasing
    * number of appearance.
    * The Big O complexity of this method is O(n). The complexity of
    * different parts of the algorithm are commented in tlhe document.
    * They are O(n) + O(1) + nLog(n) + O(numberOfWords <- at worst n) = O(n)
    */ 
    public List<String> getSortedWordsByApperance(String document, int numberOfWords) {
        // return list
        List<String> sortedList = new ArrayList<String>();
        // hashmap to store words and their count
        HashMap<String, WordCount> wordMap = new HashMap<String,WordCount>();   
        // list to pass to Collections.sort()
        List<WordCount> sorterWordCountList = null;

        // check out input
        // is it a null pointer?
        if (document == null) {
            throw new IllegalArgumentException("Argument 'document' is null, please pass a valid object.");
        }
        // is the String empty
        if (document.length() == 0) {
            return sortedList;
        }
        // if the number of words requested is 0
        if (numberOfWords == 0) {
            return sortedList;
        }

        // parse the string -- O(n)
        parseString(document, wordMap);

        // O(n) or O(1) for HashMap.values() 
        // The Java 7 library seems to be O(n) since the docs say they are backed by 
        // a map and they iterated on to return 
        // (see http://docs.oracle.com/javase/7/docs/api/java/util/HashMap.html#values())
        // 
        // However, this seems like a bad implementation. I know for sure the 
        // Java 6 HashMap in Android is O(1) for values() 
        // (see https://android.googlesource.com/platform/libcore/+/refs/heads/master/luni/src/main/java/java/util/HashMap.java)
        sorterWordCountList = new ArrayList<WordCount>(wordMap.values());

        // nLog(n) for Collections.sort() 
        // (see http://docs.oracle.com/javase/7/docs/api/java/util/Collections.html#sort(java.util.List))
        Collections.sort(sorterWordCountList);

        int lengthOfWords = sorterWordCountList.size();

        for (int i = 0; i < numberOfWords; i++) {

            // if we ask for more words that there in
            // the document, just return the list we
            // have now that includes all of the available
            // words
            if (lengthOfWords == 0 || i == lengthOfWords) {
                break;
            }

            // for testing, I am printing to console as I add them
            System.out.println("\"" + sorterWordCountList.get(i).getWord() 
                             + "\" was found " + sorterWordCountList.get(i).getCount() + " times");

            sortedList.add(sorterWordCountList.get(i).getWord());
        }

        // for testing
        //System.out.println("\n");

        return sortedList;

    }

    /**
    * Parse the String and return a map we can sort
    */
    private void parseString(String s, HashMap<String, WordCount> wordMap) {
        StringBuilder builder;

        for (int index = 0; index < s.length(); index++) {
            char c = s.charAt(index);

            if (isAChar(c)) {

                builder = new StringBuilder();

                // add the first char
                builder.append(c);

                // increment the index
                index++;

                // make sure c was not the last char
                // in the document
                if (inBounds(s, index)) {

                    c = s.charAt(index);

                    // while next char is number or letter or
                    // symbol keep adding it to the 
                    // StringBuilder to get our word
                    while (isACharOrNumOrSym(c)) {
                        // if it is a symbol, only added if
                        // that symbol is not the last in the string
                        // and that symbol is followed by a character, 
                        // otherwise break
                        if (isASymbol(c)) {
                            if (!inBounds(s, index + 1) || (inBounds(s, index + 1) && !isAChar(s.charAt(index + 1)))) {
                                break;
                            }
                        }

                        builder.append(c);           
                        index++;
                        if (inBounds(s, index)) {
                            c = s.charAt(index);
                        } else {
                            break;
                        }
                    }
      
                    if (builder.length() > 1 || builder.charAt(0) == 'I') {
                        // I appears that the String.substring() functions are O(n) operation in Java 7,
                        // my local machine is compiling this with Java 6, so it would be an O(1) operation, 
                        // See http://stackoverflow.com/questions/4679746/time-complexity-of-javas-substring
                        // Looking at the StringBuilder.toString() it appears to be an O(1) operation
                        String newWord = builder.toString();
                        if (!newWord.equals("I")) {
                            newWord = newWord.toLowerCase();
                        }

                        WordCount wordCount;

                        if (wordMap.containsKey(newWord)) {

                            // our map already contains this word, so lets just increment.
                            wordCount = wordMap.get(newWord);
                            wordCount.increment();
                            wordMap.put(newWord, wordCount);

                        } else {

                            // we need to create a new word
                            wordCount = new WordCount(newWord);
                            wordMap.put(newWord, wordCount);

                        } 
                    }
                }
            }
        }
    }

    private boolean inBounds(String s, int index) {
        return index < s.length();
    }

    private boolean isAChar(char c) {
        return ( (65 <= c && c <= 90) || (97 <= c && c <= 122) );
    }

    private boolean isANumber(char c) {
        return 48 <= c && c <= 57;
    }

    private boolean isACharOrNum(char c) {
        return isAChar(c) || isANumber(c);
    }

    private boolean isASymbol(char c) {
        return ( (33 <= c && c <= 47) || (58 <= c && c <= 64) 
            || (91 <= c && c <= 96) || (123 <= c && c <= 126));
    }

    private boolean isACharOrNumOrSym(char c) {
        return isAChar(c) || isANumber(c) || isASymbol(c);
    }

    /**
    * Private inner utility class to hold the word
    * and the count for each word. We will be able to 
    * increment the word count with it and implement
    * Comparable to use the Collections.sort() method.
    */ 
    private class WordCount implements Comparable<WordCount> {

        final String word;
        int count;

        public WordCount(String word) {
            this.word = word;
            count = 1;
        }

        public String getWord() {
            return word;
        }

        public int getCount() {
            return count;
        }

        public void increment() {
            count++;
        }

        @Override
        public int compareTo(WordCount otherWordCount) {
            // if counts are different return in descending order
            if (otherWordCount.getCount() != this.count) {
                return otherWordCount.getCount() - this.count;
            } else {
                // else, if they have the same count, return in alphabetical order
                // I did this just to have consistent return ordering
                return this.word.compareToIgnoreCase(otherWordCount.getWord());
            }
        }
    }  
}