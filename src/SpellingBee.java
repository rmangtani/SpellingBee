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

    /**
     * Generates all substrings and permutations of the letters
     */
    public void generate() {
        makeWords("", letters);
    }

    /**
     * Recursively finds all substrings and permutations of the letters
     * @param word - the current word
     * @param letters
     */
    private void makeWords(String word, String letters) {
        if (letters.equals("")) {
            return;
        }
        // Each substring and permutation is added to the ArrayList words
        for (int i = 0; i < letters.length(); i++) {
            words.add(word + letters.charAt(i));
            makeWords(word + letters.charAt(i), letters.substring(0, i) + letters.substring(i + 1));
        }
    }

    /**
     * Calls mergeSort to sort all words
     */
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
            ArrayList<String> newArr = new ArrayList<String>();
            return merge(arr1, arr2, newArr);
        }
    }

    /**
     * Recursively merges arr1 and arr2 together
     * @param arr1
     * @param arr2
     * @return the merged arrayList in sorted order
     */
    public static ArrayList<String> merge(ArrayList<String> arr1, ArrayList<String> arr2, ArrayList<String> newArr) {
        if (arr1.size() == 0 && arr2.size() == 0)
            return newArr;
        // Fills in newArr with the rest of the elements
        if (arr1.size() == 0) {
            for (int i = 0; i < arr2.size(); i++) {
                newArr.add(arr2.remove(i));
                i--;
            }
            return newArr;
        }
        else if (arr2.size() == 0) {
            for (int i = 0; i < arr1.size(); i++) {
                newArr.add(arr1.remove(i));
                i--;
            }
            return newArr;
        }
        // Compares the first elements of arr1 and arr2 and adds the smaller one to newArr
        else if (arr1.get(0).compareTo(arr2.get(0)) < 0) {
            newArr.add(arr1.get(0));
            arr1.remove(0);
            return merge(arr1, arr2, newArr);
        }
        else {
            newArr.add(arr2.get(0));
            arr2.remove(0);
            return merge(arr1, arr2, newArr);
        }
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

    /**
     * Removes every word in words that is not in the dictionary
     */
    public void checkWords() {
        for (int i = 0; i < words.size(); i++) {
            // Calls found to check if word is in DICTIONARY
            if (!found(words.get(i), 0, DICTIONARY.length-1, DICTIONARY.length/2)) {
                words.remove(i);
                i--;
            }
        }
    }

    /**
     * Implements binary search recursively to search for the word in the dictionary
     * @param word
     * @param start - starting index of the section of DICTIONARY that is being searched
     * @param end - ending index of the section
     * @param mid - index of the half point of the section
     * @return
     */
    public boolean found(String word, int start, int end, int mid) {
        if (start > end) {
            return false;
        }
        if (word.equals(DICTIONARY[mid])) {
            return true;
        }
        // Finding the next section of DICTIONARY that needs to be searched
        else if (word.compareTo(DICTIONARY[mid]) < 0) {
            return found(word, start, mid-1, start + ((mid-start)/2));
        }
        else {
            return found(word, mid+1, end, start + ((end-mid)/2));
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
