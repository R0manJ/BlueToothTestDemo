package com.rjstudio.bluetoothtestdemo.BluetoothOperateDemo.Socket;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import java.util.UUID;

/**
 * Created by r0man on 2017/7/2.
 */

public class ClientSocket extends Thread {

    private BluetoothDevice bluetoothDevice;
    private UUID uuid;
    private BluetoothSocket bluetoothSocket;
    private Handler handler;
    private SocketIOOperate socketIOOperate;

    public ClientSocket(BluetoothDevice bluetoothDevice, UUID uuid,Handler handler) {
        this.bluetoothDevice = bluetoothDevice;
        this.uuid = uuid;
        this.handler = handler;
    }


    private void ConnectToServer()
    {
        try
        {
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
            bluetoothSocket.connect();
            //TODO : 这里有两方法 ,先用第IO流操作个类,直接传入一个socket
            // 剩下的事情交给另外一个类来做
            socketIOOperate = new SocketIOOperate(bluetoothSocket,handler);
            socketIOOperate.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void sendMessage(String Content)
    {
        socketIOOperate.sendMessage(Content);
    }

    //关闭Socket
    public void close()
    {
        try
        {
            socketIOOperate.close();
            bluetoothSocket.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        ConnectToServer();
    }
}
