package com.elmana.hackathon;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.ParcelUuid;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

/**
 * Created by ilvana on 10/16/2015.
 */
public class SlanjeStringa {


    private OutputStream outputStream;
    private InputStream inStream;


    public void SlanjeStringa()
    {}

    public void Konekcija( BluetoothDevice device)
    {
        try {
            init(device);
            run();
            write("1?");
        }
        catch (IOException e)
        {
            e.getStackTrace();
        }
    }
    private void init(BluetoothDevice device) throws IOException
    {
        BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();

        if (blueAdapter != null)
        {
            if (blueAdapter.isEnabled())
            {  Log.i("broj", "ADAPTER");
                Set<BluetoothDevice> bondedDevices = blueAdapter.getBondedDevices();
                Log.i("broj", String.valueOf(bondedDevices.size()));

                if(bondedDevices.size() > 0)
                {
                    Log.i("broj", String.valueOf(bondedDevices.size()));
                    ParcelUuid uuids[] = device.getUuids();
                    Log.i("USAO",uuids[0].getUuid().toString());
                    try {
                        BluetoothSocket socket = device.createInsecureRfcommSocketToServiceRecord(uuids[0].getUuid());
                        socket.connect();
                        Log.i("TefaTefa", "11");
                        outputStream = socket.getOutputStream();
                        Log.i("TefaTefa", "111");
                        inStream = socket.getInputStream();
                        Log.i("TefaTefa", "1111");
                    }
                    catch ( IOException e)
                    {
                        Log.i("Izutetak sam ja",e.toString());
                        e.printStackTrace();
                    }
                }
                Log.e("error", "No appropriate paired devices.");
            }
            else
            {
                //Intent upaliBlue=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                //startActivityForResult(upaliBlue, 1);
                Log.e("error", "Bluetooth is disabled.");
            }
        }
    }

    public void write(String s) throws IOException
    {
        Log.i("TefaTefa", s.getBytes().toString());
        outputStream.write(s.getBytes());
    }

    public void run()
    {
        final int BUFFER_SIZE = 1024;
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytes = 0;
        int b = BUFFER_SIZE;

        while (true)
        {
            try
            {
                Log.i("Prije Streama","prijee");
                bytes = inStream.read(buffer, bytes, BUFFER_SIZE - bytes);
                Log.i("Poslije Streama","poslije");
            }
            catch (IOException e)
            {
                Log.i("Izuzetak run",e.toString());
                e.printStackTrace();
            }
        }
    }
}
