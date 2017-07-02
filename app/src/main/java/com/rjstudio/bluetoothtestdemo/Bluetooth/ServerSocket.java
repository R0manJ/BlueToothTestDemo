package com.rjstudio.bluetoothtestdemo.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.rjstudio.bluetoothtestdemo.Bluetooth.Sever.BTConnectionServer;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by r0man on 2017/6/30.
 */

public class ServerSocket extends Thread{
    private BluetoothAdapter bluetoothAdapter;
    //private BluetoothDevice device;
    private UUID uuid;
    private BluetoothServerSocket bluetoothServerSocket;
    //创建服务器类
    private BluetoothSocket bluetoothSocket;
    //创建服务器监听对象;
    private String TAG = "Server";

    private InputStream inputStream;
    private OutputStream outputStream;

    private BTConnectionServer btConnectionServer;
    private ConnectionThread connectionThread;
    //读取操作所需要的数据


    public ServerSocket(BluetoothAdapter bluetoothAdapter, UUID uuid) {
        this.uuid = uuid;
        Log.d(TAG, "ServerSocket: UUID is "+ uuid.toString());
        try
        {
            this.bluetoothServerSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord("MyService",uuid);
            //需要权限
        }
        catch (Exception e)
        {
            this.bluetoothServerSocket = null;
            Log.d(TAG, "ServerSocket: 无法创建BluetoothServiceSocket");
        }
    }

    public UUID getUUIDFromServerSock()
    {
        return uuid;
    }

    public BluetoothSocket getBluetoothSocket()
    {
        return bluetoothSocket;
    }

    private void createServiceSocket()
    {
        //需要获取一个蓝牙服务器socket
        if (bluetoothSocket == null)
        {
            try
            {
                bluetoothSocket = bluetoothServerSocket.accept();
                Log.d(TAG, "createServiceSocket: 创建服务器Socket成功.");

                this.outputStream = bluetoothSocket.getOutputStream();
                this.inputStream = bluetoothSocket.getInputStream();
                sendMessage("这是服务器端,发送给客户端一条消息.");
                //btConnectionServer = new BTConnectionServer(bluetoothSocket);
                //btConnectionServer = new BTConnectionServer(inputStream,outputStream);

                //----方案二
                connectionThread = new ConnectionThread(bluetoothSocket);
                Log.d(TAG, "createServiceSocket: 创建服务器Socket成功.");
            }
            catch (Exception e)
            {
                Log.d(TAG, "createServiceSocket: 获取蓝牙Socket超时/失败.");
            }
        }
        else
        {
           // Log.d(TAG, "createServiceSocket: is null");
            return;
        }
    }

    public ConnectionThread getConnectionThread()
    {
        return connectionThread;
    }
    @Override
    public void run() {
        //已经创建好服务器ServiceSocket了
        //创建通讯Socket
        Log.d(TAG, "run: waiting......");
        createServiceSocket();
        Log.d(TAG, "S端连接成功!!");
        //btConnectionServer.start();
        connectionThread.start();
        Log.d(TAG, "在S端中 启动了数据读写线程.");

        try
        {
            bluetoothServerSocket.close();
            Log.d(TAG, "在S端中关闭了bluetoothServerSocktet.");
        }
        catch (Exception e)
        {
            Log.d(TAG, "BluetoothServerSocket 关闭失败");
        }


    }

    public BTConnectionServer getBTConnectionServer()
    {
        return btConnectionServer;
    }


    //用于关闭所有流的操作
    public void close()
    {
        try
        {
            bluetoothSocket.close();
            //bluetoothServerSocket.close();
        }
        catch (Exception e)
        {
            Log.d(TAG, "close: 失败");
        }
    }

    public void sendMessage(String message)
    {
        byte[] temp = message.getBytes();
        try
        {
            //OutputStream os = bluetoothSocket.getOutputStream();
            outputStream.write(temp);
            outputStream.flush();
           // outputStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.d(TAG, "sendMessage: 写入失败");
        }

    }

}
