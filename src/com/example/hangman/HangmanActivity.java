package com.example.hangman;

import com.example.hangman.HangmanPicture;
import com.example.hangman.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

public class HangmanActivity extends Activity implements OnClickListener, OnKeyListener {

	private WordProcessor wp = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        Button b = (Button) findViewById(R.id.button1);
        b.setOnClickListener(this);
        FrameLayout frame = (FrameLayout) findViewById(R.id.area);
        HangmanPicture k = new HangmanPicture(this);
        frame.addView(k);
        EditText t = (EditText) findViewById(R.id.editText1);
        t.setOnKeyListener(this);
        t.setOnClickListener(this);
        wp = new WordProcessor(getApplicationContext());
        wp.pickWord();
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
	public void onClick(View arg0) {
        FrameLayout frame = (FrameLayout) findViewById(R.id.area);
        HangmanPicture k = (HangmanPicture) frame.getChildAt(0);
		k.setLevel(++level);
		wp.pickWord();
	}

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

}
