package com.example.fabian.ue3_devicesearch;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class SearchActivity extends AppCompatActivity {


    private static final int REGULARBLUETOOTH = 10;
    private static final int LEBLUETOOTH = 20;
    public BluetoothAdapter mBluetoothAdapter;
    BluetoothAbstract blue;


    ArrayAdapter<String> adapter;

    private final static IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);

    BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Switch bluetoothTypeSwitch = (Switch) findViewById(R.id.switch_ble);
        ListView devices = (ListView) findViewById(R.id.listView_devices);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, android.R.id.text1);
        devices.setAdapter(adapter);
        Button searchButton = (Button) findViewById(R.id.Button_Add);
        final TextView resulText = (TextView)findViewById(R.id.resultText);

        getBluetoothAdapter(REGULARBLUETOOTH);
        blue = new RegularBluetooth(mBluetoothAdapter, adapter, SearchActivity.this);

        bluetoothTypeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    getBluetoothAdapter(LEBLUETOOTH);
                    blue = new BluetoothLE(mBluetoothAdapter, adapter);
                }else{
                    getBluetoothAdapter(REGULARBLUETOOTH);
                    blue = new RegularBluetooth(mBluetoothAdapter, adapter, SearchActivity.this);
                }
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resulText.setText(blue.getResultText());
                blue.StartDiscover();
            }
        });

    }

    public void getBluetoothAdapter(int bluetoothType) {

        if (bluetoothType == REGULARBLUETOOTH) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        if (bluetoothType == LEBLUETOOTH) {
            final BluetoothManager bluetoothManager =
                    (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = bluetoothManager.getAdapter();
        }

        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Your device does not support Bluetooth", Toast.LENGTH_LONG).show();
        } else if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 10);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        blue.StopDiscover();
    }


    public void setmReceiver(BroadcastReceiver mReceiver) {
        this.mReceiver = mReceiver;
    }
}
