package com.example.lyndamac.oscmusicunity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import java.net.*;
import java.util.*;
import com.illposed.osc.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class MainActivity extends AppCompatActivity {

    private String myIP = "192.168.0.169";
    private String remoteIP = "192.168.0.183";
    private int outgoingPort = 8000;
    private int incomingPort = 9000;

    private MediaPlayer media;
    private String songpath = "/sdcard/Music/Rose.mp3";
    // This is used to send messages
    private OSCPortOut oscPortOut;

    // This thread will contain all the code that pertains to OSC
    private Runnable oscThread = new Runnable() {
        @Override
        public void run() {
            // Log.d("TAG", "RUN");
      /* The first part of the run() method initializes the OSCPortOut for sending messages.
       *
       * For more advanced apps, where you want to change the address during runtime, you will want
       * to have this section in a different thread, but since we won't be changing addresses here,
       * we only have to initialize the address once.
       */

            try {
                // Connect to some IP address and port
                oscPortOut = new OSCPortOut(InetAddress.getByName(remoteIP), outgoingPort);
            } catch(UnknownHostException e) {
                // Error handling when your IP isn't found
                // Log.e("err", e.toString());
                return;
            } catch(Exception e) {
                // Error handling for any other errors
                // Log.e("err", e.toString());
                return;
            }

            Log.d("TAG", "RUN2");
      /* The second part of the run() method loops infinitely and sends messages every 500
       * milliseconds.
       */
            //while (true) {
                if (oscPortOut != null) {
                    ArrayList<Object> moreThingsToSend = new ArrayList<Object>();
                    moreThingsToSend.add("Hello World");

                    OSCMessage message2 = new OSCMessage("/OSCfromThread", moreThingsToSend);
                    //OSCMessage message2 = new OSCMessage(myIP, moreThingsToSend.toArray());

                    try {
                        // Send the messages
                        oscPortOut.send(message2);
                        Log.d("TAG", "SENT");
                        // Pause for half a second
                        // sleep(500);
                    } catch (Exception e) {
                        // Error handling for some error
                    }
                }
            //}
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        media = new MediaPlayer();

        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("STATE", "Button Pressed!");
                Thread thread = new Thread(oscThread);
                thread.start();
                playsong(songpath);
            }
        });

        // Start the thread that sends messages
        // oscThread.start();
    }

    private void playsong(String string) {
        try{
            media.reset();
            media.setDataSource(string);
            media.prepare();
            media.start();

        }catch(Exception e){

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
