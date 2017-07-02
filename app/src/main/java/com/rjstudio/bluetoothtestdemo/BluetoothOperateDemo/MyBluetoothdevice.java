package com.rjstudio.bluetoothtestdemo.BluetoothOperateDemo;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by r0man on 2017/7/2.
 */

public class MyBluetoothdevice {
    private Context context;
    private BluetoothAdapter mBluetoothAdapter;
    private List<Bluetooth> mDeviceList;
    private boolean isBluetoothOpen;
    public MyBluetoothdevice(Context context) {
        this.context = context;
        init();

    }

    private void init()
    {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mDeviceList = new ArrayList<>();
        if ( !(isBluetoothOpen = mBluetoothAdapter.isEnabled()) )
        {
            //蓝牙设备没有打开的时候,询问用户是否打开蓝牙
            createAlertDialogToOpenBT(context,isBluetoothOpen);
        }

        if (!mBluetoothAdapter.isDiscovering())
        {
            openBluetoothDiscovery(context,mBluetoothAdapter,mDeviceList);
        }

    }

    //对话框:用于提示用户并让用户打开蓝牙;
    public void createAlertDialogToOpenBT(final Context context,final boolean isBluetoothOpen)
    {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(context);
        myDialog.setTitle("Open bluetooth device");
        myDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!isBluetoothOpen)
                {
                    //方式一:用意图
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    context.startActivity(intent);
                }
            }
        });
        myDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isBluetoothOpen)
                {
                    //方式二:调用方法
                    mBluetoothAdapter.disable();
                }
            }
        });
        myDialog.create().show();
    }

    //对话框:提示用户是否开启蓝牙被发现
    public void openBluetoothDiscovery(final Context context, final BluetoothAdapter bluetoothAdapter,List<Bluetooth> mDeviceList)
    {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(context);
        myDialog.setTitle("是否打开发现");
        myDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mBluetoothAdapter.startDiscovery();
            }
        });
        myDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mBluetoothAdapter.cancelDiscovery();
            }
        });
        mDeviceList = getBluetootDecvices(bluetoothAdapter);
    }

    //用户返回可用蓝牙设备列表 , 蓝牙设备名字与地址 , 这里不会刷新
    public List<Bluetooth> getBluetootDecvices(BluetoothAdapter bluetoothAdapter)
    {
        List<Bluetooth> mList = new ArrayList<Bluetooth>();
        Bluetooth bt;
        Set<BluetoothDevice> mSet = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : mSet)
        {
            bt = new Bluetooth();
            bt.setDeviceName(device.getName());
            bt.setDeviceAddaress(device.getAddress());
            mList.add(bt);
        }
        return mList;
    }

}
