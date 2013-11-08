package com.example.hangman;

//import com.example.hangman.HangmanPicture;
import com.example.hangman.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
//import android.widget.FrameLayout;

public class IntroActivity extends Activity implements OnClickListener {
    
	private String name;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);
        Button b = (Button) findViewById(R.id.startButton);
        b.setOnClickListener(this);
        
        if (savedInstanceState != null) {
        	name = savedInstanceState.getString("NAME");
        	if (name != null) {
        		startHangman();
        	}       	
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {
       startHangman();
	}
	
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save away the name so we still have it if the activity
        // needs to be killed while paused.
        outState.putString("NAME", name);
        super.onSaveInstanceState(outState);
    }

    private void startHangman() {
		TextView nameInput = (TextView)findViewById(R.id.nameField);
		name = nameInput.getText().toString();
		Intent hangmanIntent = new Intent(this, HangmanActivity.class);
		hangmanIntent.putExtra("NAME", name);
		startActivity(hangmanIntent);  	
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if (name != null) {
          SharedPreferences.Editor editor = getPreferences(0).edit();
          editor.putString("NAME", name);
          editor.commit();
          
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getPreferences(0); 
        String restoredName = prefs.getString("NAME", null);
        if (restoredName != null) {
        	name = restoredName;
        }
    }
}
