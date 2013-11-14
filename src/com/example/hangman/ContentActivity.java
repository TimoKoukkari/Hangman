package com.example.hangman;

import com.example.hangman.R;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ContentActivity extends Activity {
   
    ListView mWordList;
    SimpleCursorAdapter mCursorAdapter;
    Cursor mCursor;
    Handler mHandler;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content);
        Button b = (Button) findViewById(R.id.AddWordButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	addWordToContent();
            }
        });
        
     // Defines a list of columns to retrieve from the Cursor and load into an output row
        String[] mWordListColumns =
        {
            HangmanContent.Words.COLUMN_NAME_WORD,   // Contract class constant containing the word column name
            HangmanContent.Words.COLUMN_NAME_HINT  // Contract class constant containing the locale column name
        };
        mWordList = (ListView)findViewById(R.id.wordList);
        
     // Defines a list of View IDs that will receive the Cursor columns for each row
        int[] mWordListItems = { R.id.listItemWord, R.id.listItemHint};
        
        mCursor = getContentResolver().query(HangmanContent.Words.CONTENT_URI,null,null,null,null);
      
        // Register to content change indications
        Handler handler = new Handler();
        WordObserver observer = new WordObserver(handler);
        getContentResolver().registerContentObserver( 
        		HangmanContent.Words.CONTENT_URI, true, observer );

        
     // Creates a new SimpleCursorAdapter
        mCursorAdapter = new SimpleCursorAdapter(
            getApplicationContext(),               // The application's Context object
            R.layout.word_list,                  // A layout in XML for one row in the ListView
            mCursor,                               // The result from the query
            mWordListColumns,                      // A string array of column names in the cursor
            mWordListItems,                        // An integer array of view IDs in the row layout
            0);                    // Flags (usually none are needed)                                    

        // Sets the adapter for the ListView
        mWordList.setAdapter(mCursorAdapter);  
        
        
        mWordList.setOnItemSelectedListener(new ListView.OnItemSelectedListener() {
        
        	@Override
			public void onItemSelected(AdapterView<?> parent,
					View view, int position, long id) {
				String message = "ID: " + Integer.toString((int)id) + " - ";
				message += "Pos: " + Integer.toString(position);
		        Toast.makeText(ContentActivity.this, message,
		                Toast.LENGTH_LONG).show();			
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub			
			}
		});
        
        mWordList.setOnItemClickListener(new ListView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				
				String idStr = Integer.toString((int)id);
				String message = "ID: " + idStr + ", ";

		        Uri uri = Uri.withAppendedPath(HangmanContent.Words.CONTENT_ID_URI_BASE, idStr);
		    	int count = getContentResolver().delete(uri,null,null);
		    	if (count > 0) {
		    		 message = "Deleted: " + idStr;
		    		 Toast.makeText(ContentActivity.this, message,
				                Toast.LENGTH_SHORT).show();
		    	     //mCursor = getContentResolver().query(HangmanContent.Words.CONTENT_URI,null,null,null,null);
		    	     //mCursorAdapter.changeCursor(mCursor);
		    	}
			}      	
        });
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save away the name so we still have it if the activity
        // needs to be killed while paused.
        super.onSaveInstanceState(outState);
    }

    private void addWordToContent() {
		TextView wordInput = (TextView)findViewById(R.id.newWordField);
		String word = wordInput.getText().toString().toUpperCase();  
		wordInput.setText("");
		TextView hintInput = (TextView)findViewById(R.id.hintField);
		String hint = hintInput.getText().toString();  
		hintInput.setText("");
		
        ContentValues values = new ContentValues();
        values.put(HangmanContent.Words.COLUMN_NAME_WORD, word);
        values.put(HangmanContent.Words.COLUMN_NAME_HINT, hint);
        
		// Check if exists:
		String selection = HangmanContent.Words.COLUMN_NAME_WORD + " = ?";
		String[] selectionArgs = {word};		
		Cursor cursor = getContentResolver().query(
				HangmanContent.Words.CONTENT_URI,null,selection,selectionArgs,null);
		// The word was found from the DB, update the existing word:
		if (cursor != null && cursor.moveToFirst()) {
			int count = getContentResolver().update(
					HangmanContent.Words.CONTENT_URI,values,selection,selectionArgs);
			Toast.makeText(this, "Updated:" + word,
	                Toast.LENGTH_LONG).show();
		// Not found, create a new entry:
		} else {		
	        Uri uri = getContentResolver().insert(HangmanContent.Words.CONTENT_URI, values);        
	        Toast.makeText(this, "Created:" + word + ": " + uri,
	                Toast.LENGTH_LONG).show();	
		}
        // Refsesh view
        //mCursor = getContentResolver().query(HangmanContent.Words.CONTENT_URI,null,null,null,null);
        //mCursorAdapter.changeCursor(mCursor);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
    }
    
    // Private class for handling Content Provider notifications
    private class WordObserver extends ContentObserver {

		public WordObserver(Handler handler) {
			super(handler);		
		}
		@Override
		public void onChange(boolean selfChange){
	        mCursor = getContentResolver().query(HangmanContent.Words.CONTENT_URI,null,null,null,null);
	        mCursorAdapter.changeCursor(mCursor);
		}
    	
    }
}