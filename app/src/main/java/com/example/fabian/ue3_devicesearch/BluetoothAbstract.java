package com.example.fabian.ue3_devicesearch;

/**
 * Created by Fabian on 16.05.2017.
 */

public abstract class BluetoothAbstract {

    abstract void StartDiscover();
    abstract void StopDiscover();
    abstract String getResultText();
}
