package com.rjstudio.bluetoothtestdemo.BluetoothOperateDemo.Socket;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import java.util.UUID;

/**
 * Created by r0man on 2017/7/2.
 */

public class ServerSocket extends Thread {

    private BluetoothDevice bluetoothDevice;
    private UUID uuid;
    private BluetoothSocket bluetoothSocket;
    private Handler handler;
    private BluetoothServerSocket bluetoothServerSocket;
    private BluetoothAdapter bluetoothAdapter;
    private SocketIOOperate socketIOOperate;

    //private UUID uuid = UUID.fromString("8b66eab7-6f0e-42a2-870e-72a8141e308c"); //固定

    public ServerSocket(BluetoothAdapter bluetoothAdapter,UUID uuid,Handler handler)
    {
        this.bluetoothAdapter = bluetoothAdapter;
        this.uuid = uuid;
        this.handler = handler;
    }

    private void AcceptClient()
    {
        try
        {
            bluetoothServerSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord("MyBTServer",uuid);
            bluetoothSocket = bluetoothServerSocket.accept();
            //TODO : 这里有两方法 ,先用第IO流操作个类,直接传入一个socket
            // 剩下的事情交给另外一个类来做
            socketIOOperate = new SocketIOOperate(bluetoothSocket,handler);
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

            bluetoothSocket.close();
            bluetoothServerSocket.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        AcceptClient();
    }
}
