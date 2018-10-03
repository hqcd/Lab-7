//Quinten Whitaker
//Lab 7 - Random Character Generator Service with Binder
//Created on 10-3-18
//CS 4322
package com.example.quinten.lab7;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn1, btn2, btn3, btn4, btn5;
    TextView TV1;
    Intent serviceIntent;
    boolean isServiceBound;


    private GenerateCharacterService myService;
    private ServiceConnection myServiceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1 = (Button)findViewById(R.id.startButton);
        btn2 = (Button)findViewById(R.id.stopButton);
        btn3 = (Button)findViewById(R.id.bindButton);
        btn4 = (Button)findViewById(R.id.unbindButton);
        btn5 = (Button)findViewById(R.id.getCharButton);
        TV1 = (TextView)findViewById(R.id.randomCharTV);

    }

    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.startButton:
                serviceIntent = new Intent(getApplicationContext(), GenerateCharacterService.class);
                startService(serviceIntent);
                Toast.makeText(getApplicationContext(), "Service Started", Toast.LENGTH_SHORT).show();
                break;
            case R.id.stopButton:
                serviceIntent = new Intent(getApplicationContext(), GenerateCharacterService.class);
                stopService(serviceIntent);
                Toast.makeText(getApplicationContext(), "Service Stopped", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bindButton:
                bindMyService();
                Toast.makeText(getApplicationContext(), "Service Bound", Toast.LENGTH_SHORT).show();
                break;
            case R.id.unbindButton:
                unbindMyService();
                Toast.makeText(getApplicationContext(), "Service Unbound", Toast.LENGTH_SHORT).show();
                break;
            case R.id.getCharButton:
                setRandomChar();
                break;
        }
    }


    private void bindMyService()
    {
        if(myServiceConnection == null){
            myServiceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    GenerateCharacterService.GenerateCharacterServiceBinder myServiceBinder = (GenerateCharacterService.GenerateCharacterServiceBinder)iBinder;
                    myService = myServiceBinder.getService();
                    isServiceBound = true;
                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    isServiceBound = false;
                }
            };
        }

        bindService(serviceIntent,myServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbindMyService()
    {
        if(isServiceBound)
        {
            unbindService(myServiceConnection);
            isServiceBound = false;
        }
    }

    private void setRandomChar()
    {
        if(isServiceBound)
        {
            TV1.setText("Random Character is " + myService.getRandom());
        }
        else
        {
            TV1.setText("Service not bound");
        }
    }
}
