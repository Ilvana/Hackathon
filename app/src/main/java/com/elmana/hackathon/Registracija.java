package com.elmana.hackathon;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;


public class Registracija extends AppCompatActivity {


    BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    ArrayList<String>uredjaji=new ArrayList<>();
    public static  ArrayAdapter<String> adapter1;
    Button dugme;
    ListView lista;
    static int br=0;
    Baza b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        b=new Baza(this);

        dugme=(Button)findViewById(R.id.button3);
        setContentView(R.layout.activity_registracija);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //adapter.startDiscovery();
        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        Log.i("BrojacIlvana", String.valueOf(br));
        registerReceiver(mReceiver,
                new IntentFilter(BluetoothDevice.ACTION_FOUND));


        lista=(ListView) findViewById(R.id.listView);
        Log.i("Brojac", String.valueOf(br));
        adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,uredjaji);
        Log.i("BrojadapterListaaaaa", String.valueOf(adapter1.getCount()));


        lista.setAdapter(adapter1);


        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent x=new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                startActivityForResult(x,1);
            }
        });

    }
    @Override
    public void onDestroy()
    {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

        public void Scan(View arg0)
        {
            // TODO Auto-generated method stub
            adapter1.clear();
            adapter.startDiscovery();
        }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action))
            {
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
            {

            }
            else if (BluetoothDevice.ACTION_FOUND.equals(action))
            {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                adapter1.add(device.getName() + "\n" + device.getAddress());
                br++;
            }
            Log.i("BrojadapterListaaaaaR", String.valueOf(adapter1.getCount()));
        }
    };
}
