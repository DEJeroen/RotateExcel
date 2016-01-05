package jav.accelerator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    //Initieer variabelen

private TextView textX, textY, textZ;
    private Sensor mySensor;
    private SensorManager SM;
    private  int score;
    private  boolean left;
    private  boolean right;
    private boolean changedLeft;
    private boolean changedRight = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        if (event.values[0] < -5 && changedLeft == true) {
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
