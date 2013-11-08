package com.example.hangman;

import java.util.Random;

import android.content.*;

public class WordProcessor {

	private String word = null;
	private String letters = "";
	private Context context = null;
	
	public WordProcessor (Context c) {
		context = c;
	}
	
	/**
	 * Selects the word randomly from a word list in resources.
	 */
	void pickWord() {
		String[] words = context.getResources().getStringArray(R.array.Words);
		Random r = new Random();
		int i = r.nextInt(words.length);
		word = words[i].toUpperCase();
	}
	
	/**
	 * Gets the word to be guessed.
	 * @return The word.
	 */
	String getWord() {
		return word;
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
