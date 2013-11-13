package com.example.hangman;

import com.example.hangman.R;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ContentActivity extends Activity {
   
    ListView mWordList;
    SimpleCursorAdapter mCursorAdapter;
    Cursor mCursor;
    
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
        
        
     // Creates a new SimpleCursorAdapter
        mCursorAdapter = new SimpleCursorAdapter(
            getApplicationContext(),               // The application's Context object
            R.layout.word_list,                  // A layout in XML for one row in the ListView
            mCursor,                               // The result from the query
            mWordListColumns,                      // A string array of column names in the cursor
            mWordListItems,                        // An integer array of view IDs in the row layout
            0);                                    // Flags (usually none are needed)

        // Sets the adapter for the ListView
        mWordList.setAdapter(mCursorAdapter);  
  
        /* Adding a listener to a ListView does not work...
        mWordList.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				long id = mWordList.getSelectedItemId();
		        Toast.makeText(ContentActivity.this, Integer.toString((int)id),
		                Toast.LENGTH_LONG).show();
			}
		});
		*/
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
		//Call content provider

		TextView hintInput = (TextView)findViewById(R.id.hintField);
		String hint = hintInput.getText().toString().toUpperCase();  
		
		
        ContentValues values = new ContentValues();
        values.put(HangmanContent.Words.COLUMN_NAME_WORD, word);
        values.put(HangmanContent.Words.COLUMN_NAME_HINT, hint);
        Uri uri = getContentResolver().insert(HangmanContent.Words.CONTENT_URI, values);
        
        Toast.makeText(this, word + ": " + uri,
                Toast.LENGTH_LONG).show();
        mCursor = getContentResolver().query(HangmanContent.Words.CONTENT_URI,null,null,null,null);
        mCursorAdapter.changeCursor(mCursor);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
    }
}