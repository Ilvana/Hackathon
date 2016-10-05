package com.elmana.hackathon;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    ListView lista;
    BluetoothAdapter nasAdapter = BluetoothAdapter.getDefaultAdapter();
    ArrayAdapter<String> adapter;
    BluetoothDevice uredjaj;
    List<BluetoothDevice> listDevice = new ArrayList<BluetoothDevice>();
    ArrayList<String> s = new ArrayList<String>();
    ArrayList<String> sImena = new ArrayList<String>();
    ArrayList<String> elma = new ArrayList<String>();
    private static final String TAG = "bluetooth1";
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;
    final int RECIEVE_MESSAGE = 1;
    Handler h;
    public Timer timer = new Timer();
    int br=0;
    PendingIntent pi;
    Resources r ;
    Notification notification;
    NotificationManager notificationManager ;

    //private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    // MAC-address of Bluetooth module (you must edit this line)
    // private static String address = "20:15:06:28:04:69";
    private static String address = "20:15:05:27:69:97";

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        if (Build.VERSION.SDK_INT >= 10) {
            try {

                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[]{UUID.class});
                return (BluetoothSocket) m.invoke(device, MY_UUID);
            } catch (Exception e) {
                Log.e("TAG", "Could not create Insecure RFComm Connection", e);
            }
        }
        return device.createRfcommSocketToServiceRecord(MY_UUID);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();
        if (blueAdapter != null) {
            if (blueAdapter.isEnabled())
            {
                Intent dostupnostMob=new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                dostupnostMob.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,3600);
                startActivity(dostupnostMob);
            }
            else
            {
                Intent upaliBlue=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(upaliBlue, 1);
                Log.e("error", "Bluetooth is disabled.");
            }
        }
        /*notification = new NotificationCompat.Builder(this)
                .setTicker(("Paša, zaboravio si nešto!!!"))
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle("Notifikacija")
                .setContentText("Notifikacija")
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();
        r = getResources();
        pi = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);*/
    }
    private void sendData(String message) {
        byte[] msgBuffer = message.getBytes();

        Log.d(TAG, "...Send data: " + message + "...");

        try {
            outStream.write(msgBuffer);
        } catch (IOException e) {
            String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
            if (address.equals("00:00:00:00:00:00"))
                msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 35 in the java code";
            msg = msg + ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";

            errorExit("Fatal Error", msg);
        }
    }

    private void errorExit(String title, String message) {
        Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
        finish();
    }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);        // Get number of bytes and message in "buffer"
                    h.obtainMessage(RECIEVE_MESSAGE, bytes, -1, buffer).sendToTarget();     // Send to message queue Handler
                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String message) {
            Log.d(TAG, "...Data to send: " + message + "...");
            byte[] msgBuffer = message.getBytes();
            try {
                mmOutStream.write(msgBuffer);
            } catch (IOException e) {
                Log.d(TAG, "...Error data send: " + e.getMessage() + "...");
            }
        }


    }
    public void OtvoriReg(View v)
    {
        startActivity(new Intent(MainActivity.this, Registracija.class));
    }




    //f.show(getFragmentManager(), "dialog");

    public void Range(View v)
    {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.beep5);
                //mp.stop();
                try
                {
                    sendData("2");
                    Log.i("UsaoSamUtry","11111");
                }
                catch (Exception e)
                {
                    Log.i("UsaoSamUtry","222222");
                    //mp.start();
                    /*mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();
                        };
                    });*/

                    FireMissilesDialogFragment f=new FireMissilesDialogFragment();
                    if(br>=1) {
                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(400);
                        /*try{notificationManager.notify(0, notification);}
                        catch(Exception ea){ea.getStackTrace();}*/
                    }
                    br++;
                    if(br>=2)
                        timer.cancel();
                }
            }
        },2,3000);

        btAdapter = BluetoothAdapter.getDefaultAdapter();

        BluetoothDevice device = btAdapter.getRemoteDevice(address);


        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e1) {
            errorExit("Fatal Error", "In onResume() and socket create failed: " + e1.getMessage() + ".");
        }

        btAdapter.cancelDiscovery();
        try {
            btSocket.connect();
            Log.d(TAG, "...Connection ok...");
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
            }
        }
        try {
            outStream = btSocket.getOutputStream();
        } catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
        }

        // sendData("Evo ovo radi!!!");
        Log.i("onCreate", "Data sent!");
    }
    public void OtvoriTrg(View v)
    {
        startActivity(new Intent(MainActivity.this, Traganje.class));
    }


}
