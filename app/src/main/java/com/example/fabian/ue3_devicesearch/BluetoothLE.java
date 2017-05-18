package com.example.fabian.ue3_devicesearch;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;
import android.widget.ArrayAdapter;

/**
 * Created by Fabian on 15.05.2017.
 */

public class BluetoothLE extends BluetoothAbstract {

    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler = new Handler();
    private ArrayAdapter<String> adapter;
    int i=1;

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 5000;

    public BluetoothLE(BluetoothAdapter mBluetoothAdapter, ArrayAdapter<String> adapter){
        this.mBluetoothAdapter=mBluetoothAdapter;
        this.adapter=adapter;
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi,
                                     byte[] scanRecord) {
                    String deviceName = device.getName();
                    String deviceHardwareAddress = device.getAddress();
                    String entry;

                    //Manchmal ist der deviceName bzw. die deviceHardwareAdress null, desswegen muss unterschieden werden, was in der ListView angezeigt wird.
                    if(deviceName!=null && deviceHardwareAddress!=null){
                        entry = deviceName+" "+deviceHardwareAddress;
                    }else if (deviceName!=null && deviceHardwareAddress == null){
                        entry = deviceName;
                    }else if (deviceName==null && deviceHardwareAddress != null){
                        entry = deviceHardwareAddress;
                    }else{
                        entry = "Device"+i;
                        i++;
                    }

                    adapter.add(entry);
                }
            };

    @Override
    public void StartDiscover(){
        i = 1;
        adapter.clear();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScanning = false;
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            }
        }, SCAN_PERIOD);

        mScanning = true;
        mBluetoothAdapter.startLeScan(mLeScanCallback);
    }

    @Override
    void StopDiscover() {

    }

    @Override
    String getResultText() {
        return "Result of BluetoothLE search:";
    }
}
