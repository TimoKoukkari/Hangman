package com.example.hangman;

import com.example.hangman.HangmanPicture;
import com.example.hangman.R;

import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.os.Bundle;
import android.app.Activity;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class HangmanActivity extends Activity implements OnClickListener, OnKeyListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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

}
