package com.example.hangman;

import com.example.hangman.HangmanPicture;
import com.example.hangman.R;

import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.os.Bundle;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class HangmanActivity extends Activity implements OnClickListener, OnKeyListener {
	
	private String name = "Default";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Read saved state
		if (savedInstanceState != null) {        
	       level = savedInstanceState.getInt("LEVEL");
		}		
		// Read input parameters from the bundle inside of the intent
		Intent intent = getIntent();
		name = intent.getStringExtra("NAME");
		
		setContentView(R.layout.activity_main);
        Button b = (Button) findViewById(R.id.button1);
        b.setOnClickListener(this);
        FrameLayout frame = (FrameLayout) findViewById(R.id.area);
        HangmanPicture k = new HangmanPicture(this);
        frame.addView(k);
        TextView t = (TextView) findViewById(R.id.textView1);
        t.setOnKeyListener(this);
        t.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

    private int level = 0;
	@Override
	public void onClick(View arg0) {
        FrameLayout frame = (FrameLayout) findViewById(R.id.area);
        HangmanPicture k = (HangmanPicture) frame.getChildAt(0);
		k.setLevel(++level);
		WordProcessor wp = new WordProcessor();
		wp.pickWord();
	}

	@Override
	public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
		System.out.println(arg1);
		WordProcessor wp = new WordProcessor();
		wp.pickWord();
		return false;
	}
	@Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save away the original text, so we still have it if the activity
        // needs to be killed while paused.
        outState.putInt("LEVEL", level);
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
