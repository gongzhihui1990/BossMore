package org.gong.bmw.view

import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.BluetoothLeAdvertiser
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.ParcelUuid
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_blue.*
import net.gtr.framework.util.Loger
import net.gtr.framework.util.ToastUtil
import org.gong.bmw.R
import java.util.*


/**
 *
 * @author caroline
 * @date 2018/6/27
 */

class BLEActivity : BaseActivity() {
    override val layoutR: Int
        get() = R.layout.activity_blue

    val CLIENT_CHARACTERISTIC_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
    val UUID_LOST_SERVICE = UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb")
    val UUID_LOST_WRITE = UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb")
    val UUID_LOST_ENABLE = UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb")

    private var gattServer: BluetoothGattServer? = null
    private var mBluetoothLeAdvertiser: BluetoothLeAdvertiser? = null
    private var bluetoothDevice: BluetoothDevice? = null
    private lateinit var mBluetoothManager: BluetoothManager

    private lateinit var characterNotify: BluetoothGattCharacteristic
    override fun onCreate(savedInstanceState: Bundle?) {
        initBLE()
        setServer()
        super.onCreate(savedInstanceState)
    }

    override fun onViewLayout() {
        btnStart.setOnClickListener({ mBluetoothLeAdvertiser?.startAdvertising(createAdvSettings(true, 0), createAdvertiseData(), mAdvertiseCallback) })
        btnStop.setOnClickListener({ stopAdvertise() })
        btnSend.setOnClickListener({
            if (gattServer == null || bluetoothDevice == null) {
                ToastUtil.shortShow("gattServer==null  || bluetoothDevice==null")
            } else {
                characterNotify.setValue("HI World")
                gattServer?.notifyCharacteristicChanged(bluetoothDevice, characterNotify, false)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    /**
     * 初始化蓝牙
     */
    private fun initBLE() {
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "不支持BLE", Toast.LENGTH_LONG).show()
            finish()
        }
        mBluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        val mBluetoothAdapter = mBluetoothManager.adapter
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "蓝牙不支持", Toast.LENGTH_LONG).show()
            finish()
        }
        assert(mBluetoothAdapter != null)
        mBluetoothLeAdvertiser = mBluetoothAdapter.bluetoothLeAdvertiser
        if (mBluetoothLeAdvertiser == null) {
            Toast.makeText(this, "the device not support peripheral", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
    val mGattCallback = object : BluetoothGattCallback(){}

    /**
     * 添加服务，特征
     */
    private fun setServer() {
        //mBluetoothGatt = device.connectGatt(getContext(), false, mGattCallback)

        //读写特征
        val characterWrite = BluetoothGattCharacteristic(UUID_LOST_WRITE,
                BluetoothGattCharacteristic.PROPERTY_READ or BluetoothGattCharacteristic.PROPERTY_WRITE,
                BluetoothGattCharacteristic.PERMISSION_READ or BluetoothGattCharacteristic.PERMISSION_WRITE)
        //使能特征
        characterNotify = BluetoothGattCharacteristic(UUID_LOST_ENABLE,
                BluetoothGattCharacteristic.PROPERTY_NOTIFY,
                BluetoothGattCharacteristic.PERMISSION_READ)
        characterNotify.addDescriptor(
                BluetoothGattDescriptor(CLIENT_CHARACTERISTIC_CONFIG,
                        BluetoothGattDescriptor.PERMISSION_WRITE or BluetoothGattCharacteristic.PERMISSION_READ))
        //服务
        val gattService = BluetoothGattService(UUID_LOST_SERVICE,
                BluetoothGattService.SERVICE_TYPE_PRIMARY)
        //为服务添加特征
        gattService.addCharacteristic(characterWrite)
        gattService.addCharacteristic(characterNotify)
        //管理服务，连接和数据交互回调
        gattServer = mBluetoothManager.openGattServer(this,
                object : BluetoothGattServerCallback() {
                    @SuppressLint("SetTextI18n")
                    override fun onConnectionStateChange(device: BluetoothDevice, status: Int, newState: Int) {
                        super.onConnectionStateChange(device, status, newState)
                        bluetoothDevice = device
                        Loger.d("onConnectionStateChange:$device    $status   $newState");
                        runOnUiThread { txtDevice.text = device.address + "   " + device.name + "   " + status + "  " + newState; }
                    }

                    override fun onServiceAdded(status: Int, service: BluetoothGattService?) {
                        super.onServiceAdded(status, service)
                        Loger.d("onServiceAdded")
                    }

                    override fun onCharacteristicReadRequest(device: BluetoothDevice?, requestId: Int, offset: Int, characteristic: BluetoothGattCharacteristic?) {
                        super.onCharacteristicReadRequest(device, requestId, offset, characteristic)
                        gattServer?.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, characteristic?.value)
                        Loger.d("onCharacteristicReadRequest")
                    }

                    @SuppressLint("SetTextI18n")
                    override fun onCharacteristicWriteRequest(device: BluetoothDevice?, requestId: Int, characteristic: BluetoothGattCharacteristic?, preparedWrite: Boolean, responseNeeded: Boolean, offset: Int, value: ByteArray?) {
                        super.onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite, responseNeeded, offset, value)
                        Loger.d("onCharacteristicWriteRequest")
                        runOnUiThread { txtDevice.text = value!![0].toString() + "" }
                    }

                    override fun onNotificationSent(device: BluetoothDevice?, status: Int) {
                        super.onNotificationSent(device, status)
                        Loger.d("onNotificationSent")
                    }

                    override fun onMtuChanged(device: BluetoothDevice?, mtu: Int) {
                        super.onMtuChanged(device, mtu)
                        Loger.d("onMtuChanged")
                    }

                    override fun onDescriptorReadRequest(device: BluetoothDevice?, requestId: Int, offset: Int, descriptor: BluetoothGattDescriptor?) {
                        super.onDescriptorReadRequest(device, requestId, offset, descriptor)
                        gattServer?.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, characterNotify.getValue())
                        Loger.d("onDescriptorReadRequest")
                    }

                    override fun onExecuteWrite(device: BluetoothDevice?, requestId: Int, execute: Boolean) {
                        super.onExecuteWrite(device, requestId, execute)
                        Loger.d("onExecuteWrite")

                    }
                })
        gattServer?.addService(gattService)

    }

    private fun createAdvSettings(connectAble: Boolean, timeoutMillis: Int): AdvertiseSettings {
        val builder = AdvertiseSettings.Builder()
        builder.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
        builder.setConnectable(connectAble)
        builder.setTimeout(timeoutMillis)
        builder.setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
        val mAdvertiseSettings = builder.build()
        if (mAdvertiseSettings == null) {
            Toast.makeText(this, "mAdvertiseSettings == null", Toast.LENGTH_LONG).show();
        }
        return mAdvertiseSettings
    }

    private var mAdvertiseCallback = object : AdvertiseCallback() {
        override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
            super.onStartSuccess(settingsInEffect);
            if (settingsInEffect != null) {
                Loger.d("onStartSuccess TxPowerLv=" + settingsInEffect.getTxPowerLevel() + " mode=" + settingsInEffect.getMode()
                        + " timeout=" + settingsInEffect.getTimeout());
            } else {
                Loger.e("onStartSuccess, settingInEffect is null");
            }
            Loger.e("onStartSuccess settingsInEffect" + settingsInEffect);

        }

        override fun onStartFailure(errorCode: Int) {
            super.onStartFailure(errorCode);
            Loger.e("onStartFailure errorCode" + errorCode);

            if (errorCode == ADVERTISE_FAILED_DATA_TOO_LARGE) {
                Toast.makeText(getContext(), "R.string.advertise_failed_data_too_large", Toast.LENGTH_LONG).show();
                Loger.e("Failed to start advertising as the advertise data to be broadcasted is larger than 31 bytes.");
            } else if (errorCode == ADVERTISE_FAILED_TOO_MANY_ADVERTISERS) {
                Toast.makeText(getContext(), "R.string.advertise_failed_too_many_advertises", Toast.LENGTH_LONG).show();
                Loger.e("Failed to start advertising because no advertising instance is available.");
            } else if (errorCode == ADVERTISE_FAILED_ALREADY_STARTED) {
                Toast.makeText(getContext(), "R.string.advertise_failed_already_started", Toast.LENGTH_LONG).show();
                Loger.e("Failed to start advertising as the advertising is already started");
            } else if (errorCode == ADVERTISE_FAILED_INTERNAL_ERROR) {
                Toast.makeText(getContext(), "R.string.advertise_failed_internal_error", Toast.LENGTH_LONG).show();
                Loger.e("Operation failed due to an internal error");
            } else if (errorCode == ADVERTISE_FAILED_FEATURE_UNSUPPORTED) {
                Toast.makeText(getContext(), "R.string.advertise_failed_feature_unsupported", Toast.LENGTH_LONG).show();
                Loger.e("This feature is not supported on this platform");
            }
        }
    };

    //广播数据
    private fun createAdvertiseData(): AdvertiseData {
        val mDataBuilder = AdvertiseData.Builder()
        mDataBuilder.setIncludeDeviceName(true); //广播名称也需要字节长度
        mDataBuilder.setIncludeTxPowerLevel(true);
        mDataBuilder.addServiceData(ParcelUuid.fromString("0000fff0-0000-1000-8000-00805f9b34fb"), byteArrayOf(1, 2))
        val mAdvertiseData = mDataBuilder.build();
        if (mAdvertiseData == null) {
            Toast.makeText(getContext(), "mAdvertiseSettings == null", Toast.LENGTH_LONG).show();
        }
        return mAdvertiseData;
    }

    private fun stopAdvertise() {
        mBluetoothLeAdvertiser?.stopAdvertising(mAdvertiseCallback)
    }


}