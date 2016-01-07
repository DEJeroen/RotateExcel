package jav.accelerator;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;

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

    private CheckBox tBox;
    private OnClickListener checkBoxListener;
    private CountDownTimer countDown;
    private TextView textX, textY, textZ, time_textView, textView, highscore_textView;
    private Sensor mySensor;
    private SensorManager SM;
    private  int score, scoreSpot;
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
                        time_textView.setText("seconds remaining: " + millisUntilFinished / 1000);
                    }

                    public void onFinish() {
                        time_textView.setText("Time UP!");
                        gameOver = true;



                        highscore_textView = (TextView) findViewById(R.id.highscore_textView);
                        highscore_textView.setText( scoreSpot + ": " + score);


                        if(score > 50) {

                        }
                        else if(score > 40) {

                        }
                        else if(score > 30) {

                        }
                        else if(score > 20) {
                            MediaPlayer woohoo = MediaPlayer.create(MainActivity.this, R.raw.woohoo);
                            woohoo.start();
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
        textZ = (TextView) findViewById(R.id.textZ);
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

            if (event.values[0] < 1 && changedLeft == true) {
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
            MediaPlayer gunshot = MediaPlayer.create(MainActivity.this, R.raw.gunshot);
            gunshot.start();
        }
        else if(score > 40) {
            MediaPlayer gunshot = MediaPlayer.create(MainActivity.this, R.raw.gunshot);
            gunshot.start();
        }
        else if(score > 30) {
            MediaPlayer gunshot = MediaPlayer.create(MainActivity.this, R.raw.gunshot);
            gunshot.start();
        }
        else if(score > 20) {
            MediaPlayer guncock = MediaPlayer.create(MainActivity.this, R.raw.guncock);
            guncock.start();
        }
        else if(score > 10) {
            MediaPlayer punch = MediaPlayer.create(MainActivity.this, R.raw.punch);
            punch.start();
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
