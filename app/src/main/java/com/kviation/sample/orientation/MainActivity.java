package com.kviation.sample.orientation;

import android.annotation.TargetApi;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

@TargetApi(21)
public class MainActivity extends AppCompatActivity implements Orientation.Listener {

  private Orientation mOrientation;
  private Orientation mGyroSense;
  private AttitudeIndicator mAttitudeIndicator;
  private ProgressBar pb_x_acc;
  private ProgressBar pb_y_acc;
  private ProgressBar pb_z_acc;
  private ProgressBar pb_net_acc;
  private ProgressBar pb_x_gyro;
  private ProgressBar pb_y_gyro;
  private ProgressBar pb_z_gyro;
  private ProgressBar pb_net_gyro;
  private Button toggler;
  String filename_acc;
  String filename_gyro;
  File file_acc;
  OutputStream fos_acc;
  File file_gyro;
  OutputStream fos_gyro;
  TextView accel_x_tv;
  TextView accel_y_tv;
  TextView accel_z_tv;
  TextView accel_net_tv;
  TextView gyro_x_tv;
  TextView gyro_y_tv;
  TextView gyro_z_tv;
  TextView gyro_net_tv;
  int toggleval;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);


    mOrientation = new Orientation(this, Sensor.TYPE_ACCELEROMETER);
    mGyroSense = new Orientation(this, Sensor.TYPE_GYROSCOPE);
    toggler = (Button) findViewById(R.id.toggle);
    mAttitudeIndicator = (AttitudeIndicator) findViewById(R.id.attitude_indicator);
    pb_x_acc = (ProgressBar) findViewById(R.id.prog_x_accel);
    pb_y_acc = (ProgressBar) findViewById(R.id.prog_y_accel);
    pb_z_acc = (ProgressBar) findViewById(R.id.prog_z_accel);
    pb_net_acc = (ProgressBar) findViewById(R.id.prog_net_accel);
    pb_x_gyro = (ProgressBar) findViewById(R.id.prog_x_gyro);
    pb_y_gyro = (ProgressBar) findViewById(R.id.prog_y_gyro);
    pb_z_gyro = (ProgressBar) findViewById(R.id.prog_z_gyro);
    pb_net_gyro = (ProgressBar) findViewById(R.id.prog_net_gyro);
    accel_x_tv = (TextView) findViewById(R.id.x_accel_tv);
    accel_y_tv = (TextView) findViewById(R.id.y_accel_tv);
    accel_z_tv = (TextView) findViewById(R.id.z_accel_tv);
    accel_net_tv = (TextView) findViewById(R.id.net_accel_tv);
    gyro_x_tv = (TextView) findViewById(R.id.x_gyro_tv);
    gyro_y_tv = (TextView) findViewById(R.id.y_gyro_tv);
    gyro_z_tv = (TextView) findViewById(R.id.z_gyro_tv);
    gyro_net_tv = (TextView) findViewById(R.id.net_gyro_tv);

    filename_acc = "acceldata.txt";
    filename_gyro = "gyrodata.txt";
    try{
      file_acc = new File(this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),filename_acc);
      file_acc.createNewFile();
      try {
        fos_acc = new FileOutputStream(file_acc);
      }
      catch(Exception e2){
        e2.printStackTrace();
      }
    }
    catch(Exception e){
      e.printStackTrace();
    }

    try{
      file_gyro = new File(this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),filename_gyro);
      file_gyro.createNewFile();
      try {
        fos_gyro = new FileOutputStream(file_gyro);
      }
      catch(Exception e2){
        e2.printStackTrace();
      }
    }
    catch(Exception e){
      e.printStackTrace();
    }


    toggleval = 0;
    toggler.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if(toggleval==0) {
          mOrientation.startListening(MainActivity.this);
          mGyroSense.startListening(MainActivity.this);
          toggleval=1;
        }
        else{
          toggleval=0;
          mOrientation.stopListening();
          mGyroSense.stopListening();
        }
      }
    });

  }

  @Override
  protected void onStart() {
    super.onStart();
//    mOrientation.startListening(this);
//    mGyroSense.startListening(this);
  }

  @Override
  protected void onStop() {
    super.onStop();
    mOrientation.stopListening();
    mGyroSense.stopListening();
    try{
      fos_acc.close();
    }
    catch(Exception e){}
    try{
      fos_gyro.close();
    }
    catch(Exception e){}
  }

  @Override
  public void onOrientationChanged(float xaccel, float yaccel, float zaccel, float netaccel, int sensorType) {
    try {
      if(Sensor.TYPE_ACCELEROMETER==sensorType) {
        accel_x_tv.setText("Accelx(x*1000): " + xaccel);
        pb_x_acc.setProgress((int) (xaccel * 1000));
        accel_y_tv.setText("Accely(x*1000): " + yaccel);
        pb_y_acc.setProgress((int) (yaccel * 1000));
        accel_z_tv.setText("Accelz(x*1000): " + zaccel);
        pb_z_acc.setProgress((int) (zaccel * 1000));
        accel_net_tv.setText("Accelnet(x*1000): " + xaccel);
        pb_net_acc.setProgress((int) (netaccel * 1000));
        String tbs = (""+xaccel+","+yaccel+","+zaccel+"\n");//writes x,y,z accelerometer values in csv format
        fos_acc.write(tbs.getBytes());
      }
      else{
        gyro_x_tv.setText("gyrox(x*1000): " + xaccel);
        pb_x_gyro.setProgress((int) (xaccel * 1000));
        gyro_y_tv.setText("gyroy(x*1000): " + yaccel);
        pb_y_gyro.setProgress((int) (yaccel * 1000));
        gyro_z_tv.setText("gyroz(x*1000): " + zaccel);
        pb_z_gyro.setProgress((int) (zaccel * 1000));
        gyro_net_tv.setText("gyronet(x*1000): " + xaccel);
        pb_net_gyro.setProgress((int) (netaccel * 1000));
        String tbs = (""+xaccel+","+yaccel+","+zaccel+"\n");//writes x,y,z gyroscope values in csv format
        fos_gyro.write(tbs.getBytes());
      }
    }
    catch(NullPointerException e){
      e.printStackTrace();
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }
}
