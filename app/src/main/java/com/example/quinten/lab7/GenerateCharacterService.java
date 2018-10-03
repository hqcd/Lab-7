//Quinten Whitaker
//Lab 7 - Random Character Generator with Binder
//CS 4322
//Created on 10-3-18

package com.example.quinten.lab7;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Random;

public class GenerateCharacterService extends Service {

    private boolean isGeneratorOn;
    private char myRandomChar;
    private final String TAG = "RandomCharService: ";


    public GenerateCharacterService() {
    }

    class GenerateCharacterServiceBinder extends Binder{
        public GenerateCharacterService getService()
        {
            return GenerateCharacterService.this;
        }
    }

    private IBinder myBinder = new GenerateCharacterServiceBinder();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "In OnStartCommand Thread ID is "+Thread.currentThread().getId());
        isGeneratorOn = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                startRandomGenerator();
            }
        }).start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRandomGenerator();
        Log.i(TAG, "Service Destroyed");
    }

    private void stopRandomGenerator()
    {
        isGeneratorOn = false;
    }

    private void startRandomGenerator()
    {
        while(isGeneratorOn)
        {
            try
            {
                Thread.sleep(1000);
                if(isGeneratorOn)
                {
                    Random r = new Random();
                    char c = (char)(r.nextInt(26) + 'a');
                    myRandomChar = c;
                    Log.i(TAG, "Random char is " + myRandomChar);
                }
            }
            catch (InterruptedException e)
            {
                Log.i(TAG, "Thread Interrupted");
            }
        }
    }

    public char getRandom()
    {
        return myRandomChar;
    }

    @Nullable

    @Override
    public IBinder onBind(Intent intent)
    {
        Log.i(TAG, "In onBind ...");
        return myBinder;
    }
}
