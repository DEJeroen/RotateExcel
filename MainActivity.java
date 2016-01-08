package jav.accelerator;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.Button;

import java.util.ArrayList;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    public Multiplayer mp = new Multiplayer();
    private ArrayList<Integer> highscores = new ArrayList<Integer>();
    private CheckBox tBox;
    private OnClickListener checkBoxListener;
    private CountDownTimer countDown;
    private TextView textX, textY, textZ, time_textView, textView, highscore_textView;
    private Sensor mySensor;
    private SensorManager SM;
    private  int score, scoreSpot, record;
    private  boolean left, right;
    private boolean timer = false;
    private boolean gameOver = false;
    private boolean changedLeft;
    private boolean changedRight = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tBox = (CheckBox) findViewById(R.id.timetrial_checkBox);

        Global.context=getApplicationContext();
        final Button listenButton = (Button) findViewById(R.id.recieveButton);
        listenButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                mp.startListening();
                textView = (TextView) findViewById(R.id.MpHighScore);
                textView.setText(Global.recievedMessageStr);
            }
        });

        checkBoxListener = new OnClickListener() {
            MediaPlayer vibes = MediaPlayer.create(MainActivity.this, R.raw.tripvibesrickdickert);

            @Override
            public void onClick(View v) {
                textView = (TextView) findViewById(R.id.mode_textView);
                textView.setText("I'm playing");
                vibes.start();

                if (tBox.isChecked()==true) {
                    textView.setText(textView.getText().toString() + " " + tBox.getText().toString());
                    timer = true;
                    gameOver = false;
                    vibes.pause();
                }

                else if (tBox.isChecked()==false) {
                    textView.setText(textView.getText().toString() + " Freeplay");
                    timer = false;
                    gameOver = true;
                    time_textView.setText("");
                    vibes.start();

                    if(gameOver == true) {
                        countDown.cancel();
                    }
                }

                if(timer == true && tBox.isChecked()) {
                    score = 0;
                    textY.setText("Score " + score);

                    countDown = new CountDownTimer(30000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        time_textView.setText("Seconds remaining: " + millisUntilFinished / 1000);
                    }

                    public void onFinish() {
                        time_textView.setText("Time UP!");
                        gameOver = true;

                        highscores.add(score);
                        highscore_textView = (TextView) findViewById(R.id.highscore_textView);
                        int i = 0;
                        while(highscores.size() > i) {
                            if(record < highscores.get(i))
                            {
                                record = highscores.get(i);
                                MediaPlayer coin = MediaPlayer.create(MainActivity.this, R.raw.coin);
                                coin.start();

                            }
                            highscore_textView.setText(record + "");
                            i++;
                        }
                        if(score > 50) {
                            MediaPlayer cheering = MediaPlayer.create(MainActivity.this, R.raw.cheering);
                            cheering.start();
                        }
                        else if(score > 40) {
                            MediaPlayer applause = MediaPlayer.create(MainActivity.this, R.raw.applause);
                            applause.start();
                        }
                        else if(score > 30) {
                            MediaPlayer woohoo = MediaPlayer.create(MainActivity.this, R.raw.woohoo);
                            woohoo.start();
                        }
                        else if(score > 20) {
                            MediaPlayer jollylaugh = MediaPlayer.create(MainActivity.this, R.raw.jollylaugh);
                            jollylaugh.start();
                        }
                        else if(score > 10) {
                            MediaPlayer laugh = MediaPlayer.create(MainActivity.this, R.raw.laugh);
                            laugh.start();
                        }
                        else {
                            MediaPlayer booo = MediaPlayer.create(MainActivity.this, R.raw.boooo);
                            booo.start();
                        }
                    }
                    }.start();
                }
            }
        };

        tBox.setOnClickListener(checkBoxListener);

        //Create sensor manager
        SM = (SensorManager)getSystemService(SENSOR_SERVICE);

        //Accelerometer sensor
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //Register SensorListener
        SM.registerListener(this,mySensor,SensorManager.SENSOR_DELAY_NORMAL);

        //Assign textviews
        textX = (TextView) findViewById(R.id.textX);
        textY = (TextView) findViewById(R.id.textY);
        time_textView = (TextView) findViewById(R.id.time_textView);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        textX.setText("X: " + event.values[0]);

        if(gameOver == false || timer == false) {
            //Score management
            if (event.values[0] > 5 && changedRight == true) {
                left = true;
                right = false;
                changedLeft = true;
                changedRight = false;
                score++;
                scoreSound();
            }

            if (event.values[0] < -5 && changedLeft == true) {
                right = true;
                left = false;
                changedRight = true;
                changedLeft = false;
                score++;
                scoreSound();
            }
        }
        //End of Score management

        textY.setText("Score " +  score);
    }

    public void scoreSound()
    {
        if(score > 50) {
            MediaPlayer shotgun = MediaPlayer.create(MainActivity.this, R.raw.shotgun);
            shotgun.start();
        }
        else if(score > 40) {
            MediaPlayer gunshot = MediaPlayer.create(MainActivity.this, R.raw.gunshot);
            gunshot.start();
        }
        else if(score > 30) {
            MediaPlayer guncock = MediaPlayer.create(MainActivity.this, R.raw.guncock);
            guncock.start();
        }
        else if(score > 20) {
            MediaPlayer punch = MediaPlayer.create(MainActivity.this, R.raw.punch);
            punch.start();
        }
        else if(score > 10) {
            MediaPlayer error = MediaPlayer.create(MainActivity.this, R.raw.error);
            error.start();
        }
        else {
            MediaPlayer point = MediaPlayer.create(MainActivity.this, R.raw.ding);
            point.start();
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//gebruiken we niet, is voor error weg te halen als deze er niet is
    }
}
