package com.example.bluetooth1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import java.io.IOException;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Button b1;
    Button b2;
    Button b3;
    Button b4;

    TextView t1;
    TextView t2;
    TextView t3;

    int state;
    int[] servo = new int [16];

    private Handler handler = new Handler();

    String address = null;
    String name = null;

    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    Set<BluetoothDevice> pairedDevices;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //f96867a2-9bfc-11ea-bb37-0242ac130002
    private final String DEVICE_ADDRESS="98:D3:11:FD:43:BC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            setw();
        } catch (Exception e) {
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setw() throws IOException {

        t1=findViewById(R.id.textView1);
        t2=findViewById(R.id.textView2);
        t3=findViewById(R.id.textView3);
        bluetooth_connect_device();
        b1 =findViewById(R.id.button1);
        b2 =findViewById(R.id.button2);
        b3 =findViewById(R.id.button3);
        b4 =findViewById(R.id.button4);


        b1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    state = 1;
                    try {
                        count();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    state = 0;
                }
                return true;
            }

        });

        b2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    state = 2;
                    try {
                        count();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    state = 0;
                }
                return true;
            }

        });

        b3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    state = 3;
                    try {
                        count();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    state = 0;
                }
                return true;
            }

        });

        b4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    state = 4;
                    try {
                        count();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    state = 0;
                }
                return true;
            }
        });
    }

    private void bluetooth_connect_device() throws IOException {
        try {

            myBluetooth = BluetoothAdapter.getDefaultAdapter();
            address = myBluetooth.getAddress();
            pairedDevices = myBluetooth.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice bt : pairedDevices) {
                    if(bt.getAddress().equals(DEVICE_ADDRESS)) {
                        address = bt.getAddress();
                        name = bt.getName();
                        Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
            else
            {
                t1.setText("Please pair the device first");
            }

        }
        catch(Exception we){}
        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice device = myBluetooth.getRemoteDevice(address);
        btSocket = device.createInsecureRfcommSocketToServiceRecord(myUUID);
        btSocket.connect();
        try {t1.setText("BT Name: "+name+"\nBT Adress: "+address);}
        catch(Exception e){}
    }

    private void count() throws IOException {
        try {
            switch (state)
            {
                case 0:
                {
                    btSocket.getOutputStream().write(20);
                    return;
                }
                case 1:
                {
                    if (servo[0] < 150)
                    {
                        servo[0]++;
                        t2.setText("Axis 1: "+servo[0]+"°");
                        btSocket.getOutputStream().write(1);
                        btSocket.getOutputStream().write(servo[0]);
                    }
                    else
                    {
                        t2.setText("Axis 1: "+servo[0]+"° (max)");
                    }
                    break;
                }
                case 2:
                {
                    if (servo[0] > 30)
                    {
                        servo[0]--;
                        t2.setText("Axis 1: "+servo[0]+"°");
                        btSocket.getOutputStream().write(1);
                        btSocket.getOutputStream().write(servo[0]);
                    }
                    else
                    {
                        t2.setText("Axis 1: "+servo[0]+"° (max)");
                    }
                    break;
                }
                case 3:
                {
                    if (servo[1] > 30)
                    {
                        servo[1]++;
                        t3.setText("Axis 2: "+servo[1]+"°");
                        btSocket.getOutputStream().write(2);
                        btSocket.getOutputStream().write(servo[1]);
                    }
                    else
                    {
                        t3.setText("Axis 2: "+servo[1]+"° (max)");
                    }
                    break;
                }
                case 4:
                {
                    if (servo[1] > 30)
                    {
                        servo[1]--;
                        t3.setText("Axis 2: "+servo[1]+"°");
                        btSocket.getOutputStream().write(2);
                        btSocket.getOutputStream().write(servo[1]);
                    }
                    else
                    {
                        t3.setText("Axis 2: "+servo[1]+"° (max)");
                    }
                    break;
                }
            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        count();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }, 75);
        }  catch (IOException e) {
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        StringBuilder str = new StringBuilder();
        for (int i = 0; i < servo.length; i++) {
            str.append(servo[i]).append(",");
        }

        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("value",str.toString());

        editor.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        String savedString = prefs.getString("value", "90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90");

        StringTokenizer st = new StringTokenizer(savedString, ",");
        for (int i = 0; i < servo.length; i++)
        {
            if (st.hasMoreTokens() == true) {
                servo[i] = Integer.parseInt(st.nextToken());
            }
        }
        t2.setText("Axis 1: "+servo[0]+"°");
        t3.setText("Axis 2: "+servo[1]+"°");
    }
}