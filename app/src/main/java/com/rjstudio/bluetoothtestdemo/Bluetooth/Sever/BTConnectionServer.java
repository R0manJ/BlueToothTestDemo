package com.rjstudio.bluetoothtestdemo.Bluetooth.Sever;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by r0man on 2017/7/1.
 */

public class BTConnectionServer extends Thread {
    String TAG = "BTConnectionServer";
    private BluetoothSocket bluetoothSocket;
    private InputStream inputStream;
    private int bytes;
    private byte[] buff;
    private String receiveContent;
    private OutputStream outputStream;


    public BTConnectionServer(InputStream inputStream , OutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    public BTConnectionServer(BluetoothSocket bluetoothSocket) {
        this.bluetoothSocket = bluetoothSocket;
        try
        {
            inputStream = bluetoothSocket.getInputStream();
            outputStream = bluetoothSocket.getOutputStream();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true)
        {
            //Log.d(TAG, "接受到的内容是"+receiveContent);
            startListenClient();
        }
    }

    private void startListenClient()
    {
        try
        {
            //inputStream = bluetoothSocket.getInputStream();
        }
        catch (Exception e)
        {
            Log.d(TAG, "startListenClient: 创建InputStream失败");
        }
        try
        {
            bytes = inputStream.read(buff);
            if (bytes == -1) {
                Log.d(TAG, "startListenClient: nothing");
            }
            receiveContent = new String(buff,"utf-8");
            //Log.d(TAG, "接受到的内容是"+receiveContent);
            //inputStream.close();
        }
        catch (Exception e)
        {
           // e.printStackTrace();
           // Log.d(TAG, "读取数据失败,退出监听.");
            //inputStream = null;
        }
    }

    public void sendMessage(String message)
    {
        byte[] temp = message.getBytes();
        try
        {
           // outputStream = bluetoothSocket.getOutputStream();
            outputStream.write(temp);
            outputStream.flush(); // TODO 求解释?
        }
        catch (Exception e)
        {
            Log.d(TAG, "sendMessage: 写入失败");
        }

    }
}
