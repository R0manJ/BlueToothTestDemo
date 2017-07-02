package com.rjstudio.bluetoothtestdemo.Bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.rjstudio.bluetoothtestdemo.Bluetooth.Client.BTConnectionClient;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by r0man on 2017/6/30.
 */

public class ClientSocket extends Thread{

    private String TAG = "ClientSocket";
    private BluetoothDevice device;
    private  BluetoothSocket bluetoothSocket;
    private UUID uuid;
    private BTConnectionClient btConnectionClient;

    //-----------
    private OutputStream os;
    private InputStream is;

    public ClientSocket(BluetoothDevice device, UUID uuid) {
        this.device = device;
        this.uuid = uuid;
    }

    public void createBluetoothSocket()
    {
        try
        {
            this.bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid);
            Log.d(TAG, "createBluetoothSocket: 等待连接");
            bluetoothSocket.connect();
            this.os = bluetoothSocket.getOutputStream();
            this.is = bluetoothSocket.getInputStream();
            sendMessage("这是客户端,发送给服务器端一条消息.");
            btConnectionClient = new BTConnectionClient(bluetoothSocket);
            Log.d(TAG, "C端连接成功");
        }
        catch (Exception e )
        {
            e.printStackTrace();
            //bluetoothSocket = null;
            //Log.d(TAG, "createBluetoothSocket:创建Socket失败");
        }
    }


    @Override
    public void run() {
        //receiveMessage();
        
        createBluetoothSocket();
        if (bluetoothSocket == null)
        {
            return;
        }
        //this.btConnectionClient = new BTConnectionClient(is,os);
        btConnectionClient.start();
        Log.d(TAG, "在C端中 启动了数据读写线程.");
    }

    public BluetoothSocket getBluetoothSocket()
    {
        //TODO 逻辑不完整
        return bluetoothSocket;
    }

    public BTConnectionClient getBtConnectionClient()
    {
        return btConnectionClient;
    }

    public void sendMessage(String message)
    {
        byte[] temp = message.getBytes();
        try
        {
            //OutputStream os = bluetoothSocket.getOutputStream();
            os.write(temp);
            os.flush();
            //os.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.d(TAG, "sendMessage: 写入失败");
        }

    }


}
