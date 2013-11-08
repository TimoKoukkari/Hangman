package com.example.hangman;

public class WordProcessor {

	private String word = null;
	
	void pickWord() {
		int count = R.array.Words; //TODO tämä on taulukon ID, ei koko
		System.out.println(count);
	}
	
	String getWord() {
		return word;
	}
}
