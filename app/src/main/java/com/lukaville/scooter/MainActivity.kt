package com.lukaville.scooter

import android.Manifest
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.lukaville.scooter.api.MijiaBluetoothApi
import com.lukaville.scooter.utils.toHexString
import com.polidea.rxandroidble.RxBleClient
import com.polidea.rxandroidble.RxBleConnection
import com.tbruyelle.rxpermissions.RxPermissions


class MainActivity : AppCompatActivity() {

    private lateinit var api: MijiaBluetoothApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RxPermissions(this)
                .request(Manifest.permission.ACCESS_COARSE_LOCATION)
                .filter { it == true }
                .flatMap {
                    RxBleClient.create(this).scanBleDevices()
                }.first {
            // TODO: change me
            // hardcoded bluetooth device mac address
            it.bleDevice.macAddress == "FE:03:4D:46:52:39"
        }.flatMap {
            it.bleDevice.establishConnection(this, false)
        }.doOnNext {
            onConnected(it)
        }.subscribe()

        findViewById(R.id.send).setOnClickListener {
            api.dumpMemory().subscribe({
                println("Memory dump: ${it.toHexString()}")
            }, Throwable::printStackTrace)
        }
    }

    private fun onConnected(connection: RxBleConnection) {
        api = MijiaBluetoothApi(connection)
    }

}