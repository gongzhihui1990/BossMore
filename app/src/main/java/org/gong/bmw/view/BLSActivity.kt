package org.gong.bmw.view

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import org.gong.bmw.R

/**
 *
 * @author caroline
 * @date 2018/6/27
 */

class BLSActivity : BaseActivity() {
    override val layoutR: Int
        get() = R.layout.activity_blue

    private var bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    private lateinit var mBluetoothManager: BluetoothManager

    private val btReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBT()
    }

    override fun onViewLayout() {
    }

    override fun onDestroy() {
        super.onDestroy()
        finiBT()
    }


    /**
     * 初始蓝牙
     */
    private fun initBT() {
        //注册广播
        val mif = IntentFilter()
        mif.addAction("")
        mif.addAction(BluetoothDevice.ACTION_FOUND)
        registerReceiver(btReceiver, mif)
        //获取蓝牙服务
        mBluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = mBluetoothManager.adapter
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "蓝牙不支持", Toast.LENGTH_LONG).show()
            finish()
        }
        assert(bluetoothAdapter != null)
        //打开蓝牙模块
        if (!bluetoothAdapter.isEnabled) {
            bluetoothAdapter.enable()
        }
        //开始搜索蓝牙设备
        if (bluetoothAdapter.isDiscovering) {
            bluetoothAdapter.cancelDiscovery()
        }
        bluetoothAdapter.startDiscovery()
    }

    /**
     * 结束蓝牙
     */
    private fun finiBT() {
        //解放广播
        unregisterReceiver(btReceiver)
        //关闭蓝牙模块
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled) {
            bluetoothAdapter.disable()
        }
    }
}