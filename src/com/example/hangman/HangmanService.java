/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.hangman;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;


/**
 * This is an example of implementing an application service that runs locally
 * in the same process as the application.  The {@link LocalServiceActivities.Controller}
 * and {@link LocalServiceActivities.Binding} classes show how to interact with the
 * service.
 *
 * <p>Notice the use of the {@link NotificationManager} when interesting things
 * happen in the service.  This is generally how background services should
 * interact with the user, rather than doing something more disruptive such as
 * calling startActivity().
 */

public class HangmanService extends Service {
 

    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private int NOTIFICATION = 1;

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();
    
	private Timer inputTimer = null;
	private TimerTask timeoutTask = null;
	private Runnable toastHandler = null;
	private Handler handler;
	
    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        HangmanService getService() {
            return HangmanService.this;
        }
    }
    
    @Override
    public void onCreate() {

      handler = new Handler();
    
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	Toast.makeText(this, "Hangman service started", Toast.LENGTH_SHORT).show();
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        startTimer(100);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
		if (inputTimer != null){
			  inputTimer.cancel();
			  timeoutTask.cancel();
			}
        // Tell the user we stopped.
        showToast("Hangman service stopped");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

	private void startTimer(long delay){
		
		if (inputTimer != null){
		  inputTimer.cancel();
		  timeoutTask.cancel();
		}
		
		toastHandler = new Runnable(){
			@Override
			public void run() {
				showToast("Hangman words updated");
			}
		};

		timeoutTask = new TimerTask() {
			@Override
			public void run() {
				int count = fetchWordsFromNet();
				// Show the toast only if something was added to the dictionary
				if (count > 0) {
				    handler.post(toastHandler);
				}
			}
		};
		inputTimer = new Timer();
		inputTimer.schedule(timeoutTask, delay);
	}   
    
    /**
     * Show a notification while this service is running.
     */
    private int fetchWordsFromNet() {
         //Fetch words and start a timer
		URL url = null;
		int insertCount=0;
		try {
			url = new URL("http://www.ideallearning.fi/sanat.xml");
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpURLConnection con = null;
		try {
			con = (HttpURLConnection) url.openConnection();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedReader reader = null;
		try {
			InputStreamReader isr = new InputStreamReader(con.getInputStream());
			reader = new BufferedReader(isr);
			Document xmlDoc = null;
			DocumentBuilder builder = null;
			try {
				builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				xmlDoc = builder.parse(con.getInputStream());
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Empty database
	    	//getContentResolver().delete(HangmanContent.Words.CONTENT_URI,null,null);
	    	
			NodeList words = xmlDoc.getElementsByTagName("word");
			for (int i=0; i<words.getLength(); ++i) {
	            if (words.item(i).getNodeType() != org.w3c.dom.Node.ELEMENT_NODE) continue;
	            Element element = (Element)words.item(i);
	            String word = element.getAttribute("value").toUpperCase();
	            String hint = ""; //element.getAttribute("hint");
				System.out.println(word);
		        ContentValues values = new ContentValues();
		        values.put(HangmanContent.Words.COLUMN_NAME_WORD, word);
		        values.put(HangmanContent.Words.COLUMN_NAME_HINT, hint);
				String selection = HangmanContent.Words.COLUMN_NAME_WORD + " = ?";
				String[] selectionArgs = {word};		

				Cursor cursor = getContentResolver().query(HangmanContent.Words.CONTENT_URI,null,selection,selectionArgs,null);
				if (cursor == null || !cursor.moveToFirst()) {
					getContentResolver().insert(HangmanContent.Words.CONTENT_URI, values);
				    insertCount++; 
				    }
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		startTimer(10000);
		return insertCount;
    }   
    
    private void showToast(String message){
    	Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}

