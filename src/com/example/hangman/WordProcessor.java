package com.example.hangman;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.content.*;
import android.database.Cursor;

public class WordProcessor {
  
	private String word = null;
	private String hint = null;
	private String letters = "";
	private Context context = null;
	
	public WordProcessor (Context c) {
		context = c;
	}
	
	// Constructor that is used to restore saved state
	public WordProcessor (Context c, String restoreWord, String restoreLetters) {
		context = c;
		word = restoreWord;
		letters = restoreLetters;
	}
	/**
	 * Selects the word randomly from a word list in resources.
	 */
	void pickWord() {
		
		String[] words = context.getResources().getStringArray(R.array.Words);		
		List<String> wordList = new ArrayList<String>();
		List<String> hintList = new ArrayList<String>();
		
		Random r = new Random();
		
		Cursor cursor = context.getContentResolver().query(
				HangmanContent.Words.CONTENT_URI,null,null,null,null);
        if (cursor != null) {
            while(cursor.moveToNext()) {
                int index = cursor.getColumnIndex(HangmanContent.Words.COLUMN_NAME_WORD);
                wordList.add(cursor.getString(index));
                index = cursor.getColumnIndex(HangmanContent.Words.COLUMN_NAME_HINT);
                hintList.add(cursor.getString(index));
            }
            cursor.close();
        }
        if (wordList.size() > 0) {
            int i = r.nextInt(wordList.size());
            word = wordList.get(i).toUpperCase();
            hint = hintList.get(i);
        } else {
    		int i = r.nextInt(words.length);
    		word = words[i].toUpperCase();
    		hint = word;
        }		
	}
	
	/**
	 * Gets the word to be guessed.
	 * @return The word.
	 */
	String getWord() {
		return word;
	}
	String getHint() {
		return hint;
	}
	String getLetters(){
		return letters;
	}
	
	/**
	 * Gets the word in masked form and spaces between letters.
	 * Guessed letters are shown and unknown replaced with '_'.
	 * @return
	 */
	String getMaskedWord() {
		String result = "";
		for (int i=0; i<word.length(); ++i) {
			if (letters.indexOf(word.charAt(i)) >= 0)
				result += word.charAt(i);
			else
				result += "_";
			if (i < (word.length()-1))
				result += " ";					
		}
		return result;
	}
	
	/**
	 * Adds letter to guessed letters list.
	 * @param c Guessed letter
	 * @return true if letter is in the word.
	 */
	boolean addLetter(char c) {
		letters += c;
		if (word.indexOf(c) >=0 )
			return true;
		return false;
	}
}
