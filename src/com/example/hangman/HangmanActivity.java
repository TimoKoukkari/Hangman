package com.example.hangman;

import com.example.hangman.HangmanPicture;
import com.example.hangman.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

public class HangmanActivity extends Activity implements OnKeyListener {

	private WordProcessor wp = null;
	private String name = "Default";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		String savedWord = null;
		String savedLetters = null;
		super.onCreate(savedInstanceState);
		// Read saved state
		if (savedInstanceState != null) {        
	       level = savedInstanceState.getInt("LEVEL");
	       savedWord = savedInstanceState.getString("WORD");
	       savedLetters = savedInstanceState.getString("LETTERS");
		}		

		// Read input parameters from the bundle inside of the intent
		Intent intent = getIntent();
		name = intent.getStringExtra("NAME");
		
		setContentView(R.layout.activity_main);
		
		//Write user's name
		TextView user = (TextView)findViewById(R.id.User);
		user.setText(name);
		
        FrameLayout frame = (FrameLayout) findViewById(R.id.area);
        HangmanPicture k = new HangmanPicture(this);
        // Restore saved level
		if (level > 0) {
			k.setLevel(level);
		}
        frame.addView(k);
        EditText t = (EditText) findViewById(R.id.editText1);
        t.setOnKeyListener(this);
        
        if (savedWord == null) {
          wp = new WordProcessor(getApplicationContext());
          wp.pickWord(); 
        // Restore saved state
        } else { 
          wp = new WordProcessor(getApplicationContext(), savedWord, savedLetters);	
        }
        	      
        TextView v = (TextView) findViewById(R.id.textView1);
        v.setText(wp.getMaskedWord());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

    private int level = 0;
	@Override
	public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
		if (arg2.getAction() != KeyEvent.ACTION_DOWN) return true;
		
        FrameLayout frame = (FrameLayout) findViewById(R.id.area);
        HangmanPicture k = (HangmanPicture) frame.getChildAt(0);
		char c = arg2.getDisplayLabel();
		if (!wp.addLetter(c)) {
			k.setLevel(++level);
		}
		
		String s = wp.getMaskedWord();
		TextView t = (TextView) findViewById(R.id.textView1);
		t.setText(s);
		if (!s.contains("_"))
			k.setLevel(10);
		return true;
	}
	@Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("LEVEL", level);
        outState.putString("WORD", wp.getWord());
        outState.putString("LETTERS", wp.getLetters());
        super.onSaveInstanceState(outState);
    }
	
    @Override
    protected void onPause() {
        super.onPause();
        if (level != 0) {
          SharedPreferences.Editor editor = getPreferences(0).edit();
          editor.putInt("LEVEL", level);
          editor.commit();
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        /*        
        SharedPreferences prefs = getPreferences(0); 
        level = prefs.getInt("LEVEL", 0);
        FrameLayout frame = (FrameLayout) findViewById(R.id.area);
        HangmanPicture k = (HangmanPicture) frame.getChildAt(0);
		k.setLevel(level); */
    }
}
