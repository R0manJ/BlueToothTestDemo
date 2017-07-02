package com.rjstudio.bluetoothtestdemo.Bluetooth.Client;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by r0man on 2017/7/1.
 */

public class BTConnectionClient extends Thread {
    private String TAG = "BTConnectionClient";
    private BluetoothSocket bluetoothSocket;
    private InputStream inputStream;
    private int bytes;
    private byte[] buff;
    private String receiveContent;
    private OutputStream outputStream;
   // private InputStream in;

    public BTConnectionClient(InputStream in,OutputStream os) {
        this.inputStream = in;
        this.outputStream = os;
    }

    public BTConnectionClient(BluetoothSocket bluetoothSocket) {
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
           // if (inputStream == null)
//            inputStream = bluetoothSocket.getInputStream();
            bytes = inputStream.read(buff);
            receiveContent = new String(buff,"utf-8");
            Log.d(TAG, "startListenClient: "+receiveContent);
        }
        catch (Exception e)
        {
            //e.printStackTrace();
           // Log.d(TAG, "startListenClient: 创建InputStream失败");
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
               // Log.d(TAG, "读取数据失败,退出监听.");
               // inputStream = null;
                //e.printStackTrace();

        }
    }

    public void sendMessage(String message)
    {
        byte[] temp = message.getBytes();
        try
        {
            //outputStream = bluetoothSocket.getOutputStream();
            outputStream.write(temp);
            outputStream.flush(); // TODO 求解释?
        }
        catch (Exception e)
        {
            Log.d(TAG, "sendMessage: 写入失败");
        }

    }
}
