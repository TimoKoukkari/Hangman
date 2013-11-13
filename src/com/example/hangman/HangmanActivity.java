package com.example.hangman;

import java.util.Timer;
import java.util.TimerTask;

import com.example.hangman.HangmanPicture;
import com.example.hangman.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
//import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
//import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HangmanActivity extends Activity implements OnKeyListener {

	private WordProcessor wp = null;
	private String name = "Default";
	private int level = 0;

	private Timer inputTimer;
	private TimerTask timeoutTask;
	private Runnable timeoutHandler;

	boolean hwKbd=false;
	
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
		EditText t = (EditText) findViewById(R.id.newWordField);		
		hwKbd = (getResources().getConfiguration().keyboard != Configuration.KEYBOARD_NOKEYS);
		if (!hwKbd) {
			setSwKbdListener(t);
			k.setScale(0.5F);
		} else {
			t.setOnKeyListener(this);
		}
		
		if (savedWord == null) {
			wp = new WordProcessor(getApplicationContext());
			wp.pickWord(); 
			// Restore saved state
		} else { 
			wp = new WordProcessor(getApplicationContext(), savedWord, savedLetters);	
		}

		TextView v = (TextView) findViewById(R.id.listItemWord);
		v.setText(wp.getMaskedWord());
        
        Toast.makeText(this, wp.getHint(),
                Toast.LENGTH_LONG).show();
		
		startTimer();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onKey(View arg0, int arg1, KeyEvent arg2) {

		char c = arg2.getDisplayLabel();
		// Ignore if they key is not a printable key
		if (c == 0) {
			return false;
		}
		if (arg2.getAction() != KeyEvent.ACTION_DOWN) {
			return true;
		}

        handleChar(c);
		return true;
	}

	void handleChar(char c) {
		char C = Character.toUpperCase(c);
		inputTimer.cancel();
		timeoutTask.cancel();

		FrameLayout frame = (FrameLayout) findViewById(R.id.area);
		HangmanPicture k = (HangmanPicture) frame.getChildAt(0);	
		if (!wp.addLetter(C)) {
			k.setLevel(++level);
		}
		String s = wp.getMaskedWord();
		TextView t = (TextView) findViewById(R.id.listItemWord);
		t.setText(s);
		// The user has guessed the word
		if (!s.contains("_")){
			level = 10;
			k.setLevel(level);
		}
		// Game over!
		if (level > 8) {	
			gameOver();
		} else {
			startTimer();
		}
	}
	
	private void setSwKbdListener(EditText t) {
		t.requestFocus();
		InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(t, InputMethodManager.SHOW_FORCED);

		t.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
	        @Override
	        public void beforeTextChanged(CharSequence s, int start, int count,
	                int after) {
	        }
			@Override
			public void afterTextChanged(Editable s) { 
				if (s.length()> 0) {
				  char c = s.charAt(s.length()-1);
				  handleChar(c);
				  s.clear();
				}
			}
		});
	}
	
	private void startTimer(){
		timeoutHandler = new Runnable(){
			@Override
			public void run() {
				level = 9;
				FrameLayout frame = (FrameLayout) findViewById(R.id.area);
				HangmanPicture k = (HangmanPicture) frame.getChildAt(0);
				k.setLevel(level);
				gameOver();
			}
		};

		timeoutTask = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(timeoutHandler);
			}
		};
		inputTimer = new Timer();
		inputTimer.schedule(timeoutTask, 30000);
	}
	
	private void gameOver() {
		TextView t = (TextView) findViewById(R.id.listItemWord);
		t = (TextView) findViewById(R.id.newWordField);
		t.setVisibility(View.INVISIBLE);
		t.setOnKeyListener(null); 
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
