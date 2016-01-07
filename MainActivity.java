package jav.accelerator;

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

    private CheckBox fBox;
    private CheckBox tBox;
    private TextView textView;
    private boolean timer = false;
    private OnClickListener checkBoxListener;

    private TextView textX, textY, textZ, time_textView;
    private Sensor mySensor;
    private SensorManager SM;
    private  int score;
    private int time = 10;
    private  boolean left;
    private  boolean right;
    private boolean changedLeft;
    private boolean changedRight = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fBox = (CheckBox) findViewById(R.id.freeplay_checkBox);
        tBox = (CheckBox) findViewById(R.id.timetrial_checkBox);

        checkBoxListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                textView = (TextView) findViewById(R.id.mode_textView);
                textView.setText("I'm playing");

                if (tBox.isChecked()) {
                    textView.setText(textView.getText().toString() + " " + tBox.getText().toString());
                    fBox.setChecked(false);
                    timer = true;
                }

                else if (fBox.isChecked()) {
                    textView.setText(textView.getText().toString() + " " + fBox.getText().toString());
                    tBox.setChecked(false);
                    timer = false;
                    time_textView.setText("");
                    time = 10;
                }

                if(timer == true)
                {
                    score = 0;
                    textY.setText("Score " +  score);
                    time_textView.setText("seconds remaining: " +  time);

                    new CountDownTimer(30000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            if(timer == true) {
                                time_textView.setText("seconds remaining: " + millisUntilFinished / 1000);
                            }
                        }

                        public void onFinish() {
                            if(timer == true) {
                                time_textView.setText("Time UP!");
                            }
                        }
                    }.start();
                }
            }
        };

        fBox.setOnClickListener(checkBoxListener);
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

        //Score management
        if (event.values[0] > 5 && changedRight == true) {
            left = true;
            right = false;
            changedLeft = true;
            changedRight = false;
            score ++;
        }

        if (event.values[0] < 1 && changedLeft == true) {
            right = true;
            left = false;
            changedRight = true;
            changedLeft = false;
            score++;
        }

        //End of Score management

        textY.setText("Score " +  score);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//gebruiken we niet, is voor error weg te halen als deze er niet is
    }
}
