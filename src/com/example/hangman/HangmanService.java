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

import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
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
	private Runnable timeoutHandler = null;
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
        startTimer();
        
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // Cancel the timer 
     
        // Tell the user we stopped.
        Toast.makeText(this, "Hangman service stopped", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

	private void startTimer(){
		
		if (inputTimer != null){
		  inputTimer.cancel();
		  timeoutTask.cancel();
		}
		
		timeoutHandler = new Runnable(){
			@Override
			public void run() {
				fetchWordsFromNet();
			}
		};

		timeoutTask = new TimerTask() {
			@Override
			public void run() {
				handler.post(timeoutHandler);
			}
		};
		inputTimer = new Timer();
		inputTimer.schedule(timeoutTask, 10000);
	}   
    
    /**
     * Show a notification while this service is running.
     */
    private void fetchWordsFromNet() {
         //Fetch words and start a timer
    	showToast("Fetched words");
        updateContentProvider();
    }
    
    private void updateContentProvider() {
    	showToast("Updated content provider");
    	startTimer();
    }
    
    private void showToast(String message){
    	Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}

