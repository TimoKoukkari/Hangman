package com.example.hangman;

import java.util.Random;
import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

public class WordProcessor {
   
	private String word = null;
	private String hint = null;
	private String letters = "";
	private Context context = null;
	private TaskReadyCallback callBack = null;
	
	public WordProcessor (Context c, TaskReadyCallback cb) {
		context = c;
		callBack = cb;
	}
	
	// Constructor that is used to restore saved state
	public WordProcessor (Context c, TaskReadyCallback cb,String restoreWord, String restoreLetters) {
		context = c;
		callBack = cb;
		word = restoreWord;
		letters = restoreLetters;
	}

	// Queries words from the content provider
	void pickWord() {		
		new QueryTask().execute(HangmanContent.Words.CONTENT_URI);		
	}
	
	// Selects a random word from the query results
	void onQueryReady(Cursor cursor){		
		
		Random r = new Random();	
		
        if ((cursor != null) && (cursor.getCount() > 0)) {
            int i = r.nextInt(cursor.getCount());
            cursor.moveToPosition(i);
            int index = cursor.getColumnIndex(HangmanContent.Words.COLUMN_NAME_WORD);
            word = cursor.getString(index).toUpperCase();
            index = cursor.getColumnIndex(HangmanContent.Words.COLUMN_NAME_HINT);
            hint = cursor.getString(index);
            cursor.close();
        } else {
    		String[] words = context.getResources().getStringArray(R.array.Words);
    		int i = r.nextInt(words.length);
    		word = words[i].toUpperCase();
    		hint = word;
        }		
        callBack.onTaskReady();
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
		if (letters.indexOf(c) >= 0){
			return true; // ignore, user has already guessed this letter!
		}
		letters += c;
		if (word.indexOf(c) >=0 )
			return true;
		return false;
	}
	
	
	 private class QueryTask extends AsyncTask<Uri, Integer, Cursor> {
	     protected Cursor doInBackground(Uri... uris) {
	    	publishProgress(0);
	 		Cursor cursor = context.getContentResolver().query(
					uris[0],null,null,null,null);
	 		publishProgress(100);
	        return cursor;
	     }

	     protected void onProgressUpdate(Integer... progress) {	         
	     }

	     protected void onPostExecute(Cursor result) {
	         onQueryReady(result);
	     }
	 }
	 
}
