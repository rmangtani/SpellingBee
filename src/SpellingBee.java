import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, Ruchi Mangtani
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // completed: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        makeWords("", letters);
    }

    private void makeWords(String word, String letters) {
        if (letters.equals("")) {
            return;
        }
        for (int i = 0; i < letters.length(); i++) {
            words.add(word + letters.charAt(i));
            makeWords(word + letters.charAt(i), letters.substring(0, i) + letters.substring(i + 1));
        }
    }

    // Completed: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        words = mergeSort(words);
    }

    /**
     * Recursively implements merge sort
     * @param arr - the arraylist that will be sorted
     * @return a sorted arraylist
     */
    public ArrayList<String> mergeSort(ArrayList<String> arr) {
        if (arr.size() == 1)
            return arr;
        else {
            ArrayList<String> arr1 = new ArrayList<String>();
            ArrayList<String> arr2 = new ArrayList<String>();
            // Splits the array
            for (int i = 0; i < arr.size() / 2; i++)
                arr1.add(arr.get(i));
            for (int i = arr.size() / 2; i < arr.size(); i++)
                arr2.add(arr.get(i));
            // Sorts both halves
            arr1 = mergeSort(arr1);
            arr2 = mergeSort(arr2);
            return merge(arr1, arr2);
        }
    }

    /**
     *
     * @param arr1
     * @param arr2
     * @return
     */
    public static ArrayList<String> merge(ArrayList<String> arr1, ArrayList<String> arr2) {
        ArrayList<String> newArr = new ArrayList<String>();
        while (true) {
            if (arr1.size() == 0 && arr2.size() == 0)
                break;
            if (arr1.size() == 0) {
                for (int i = 0; i < arr2.size(); i++) {
                    newArr.add(arr2.remove(i));
                    i--;
                }
                break;
            }
            else if (arr2.size() == 0) {
                for (int i = 0; i < arr1.size(); i++) {
                    newArr.add(arr1.remove(i));
                    i--;
                }
                break;
            }
            else if (arr1.get(0).compareTo(arr2.get(0)) < 0) {
                newArr.add(arr1.get(0));
                arr1.remove(0);
            }
            else {
                newArr.add(arr2.get(0));
                arr2.remove(0);
            }
        }
        return newArr;
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // Complete: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        for (int i = 0; i < words.size(); i++) {
            if (!found(words.get(i), 0, DICTIONARY.length-1, DICTIONARY.length/2)) {
                words.remove(i);
                i--;
            }
        }
    }

    public boolean found(String s, int start, int end, int mid) {
        if (start > end) {
            return false;
        }
        if (s.equals(DICTIONARY[mid])) {
            return true;
        }
        else if (s.compareTo(DICTIONARY[mid]) < 0) {
            return found(s, start, mid-1, start + ((mid-start)/2));
        }
        else {
            return found(s, mid+1, end, start + ((end-mid)/2));
        }
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
