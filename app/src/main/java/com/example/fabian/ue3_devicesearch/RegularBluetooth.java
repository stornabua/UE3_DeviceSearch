package com.example.fabian.ue3_devicesearch;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

/**
 * Created by Fabian on 15.05.2017.
 */

class RegularBluetooth extends BluetoothAbstract{

    private int i = 1;
    private ArrayAdapter<String> adapter;
    private final static IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
    private BluetoothAdapter mBluetoothAdapter;
    private Context context;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //region BroadcastReceiver Event
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
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
            //endregion
        }
    };

    public RegularBluetooth(BluetoothAdapter mBluetoothAdapter, ArrayAdapter<String> adapter, Context context){
        this.mBluetoothAdapter=mBluetoothAdapter;
        this.adapter=adapter;
        this.context=context;
    }

    @Override
    void StartDiscover() {
        if(mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
        }
        ((SearchActivity)context).setmReceiver(mReceiver);
        context.registerReceiver(mReceiver, filter);
        i = 1;
        adapter.clear();
        mBluetoothAdapter.startDiscovery();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run() {
                mBluetoothAdapter.cancelDiscovery();
                Log.d("Progrem spieking: ", "DiscoveryStoped");
            }}, 5000);
    }

    @Override
    void StopDiscover() {
        context.unregisterReceiver(mReceiver);
    }

    @Override
    String getResultText() {
        return "Result of regular Bluetooth search:";
    }
}
