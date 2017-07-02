package com.rjstudio.bluetoothtestdemo.BluetoothOperateDemo.Socket;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by r0man on 2017/7/2.
 */

public class SocketIOOperate extends Thread{
    private BluetoothSocket bluetoothSocket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Handler handler;

    //---输入流需要的变量
    private int readIndex;
    private byte[] buff;
    private String receiveContent;
    private Message msg;

    public SocketIOOperate(BluetoothSocket bluetoothSocket , Handler handler) {
        this.bluetoothSocket = bluetoothSocket;
        this.handler = handler;
        init();
    }

    private void init()
    {
        buff = new byte[1024];
        readIndex = 0;
        receiveContent = null;

        try
        {
            inputStream  = bluetoothSocket.getInputStream();
            outputStream = bluetoothSocket.getOutputStream();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void listenerContent()
    {
        while(true)
        {
            try
            {
                //默认采用UTF-8的编码
                readIndex = inputStream.read(buff);
                receiveContent = new String(buff,0,readIndex);
                msg = new Message();
                msg.obj = receiveContent;
                handler.sendMessage(msg);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public String getReceiveContent()
    {
        return receiveContent;
    }

    public void sendMessage(String content)
    {
        byte[] sendContent = content.getBytes();
        try {
            outputStream.write(sendContent);
            outputStream.flush();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        listenerContent();
    }
}
