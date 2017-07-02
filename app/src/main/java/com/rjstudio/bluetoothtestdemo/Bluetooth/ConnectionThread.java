package com.rjstudio.bluetoothtestdemo.Bluetooth;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by r0man on 2017/6/30.
 */

public class ConnectionThread extends Thread {
    private BluetoothSocket bluetoothSocket;
    private InputStream inputStream;
    private OutputStream outputStream;
    String TAG = "ConnectionThread";

    //用于接收信息的数据
    private String receiveContent;
    private byte[] buff;
    private int bytes;
    private InputStreamReader isr;
    private BufferedReader br;

    public ConnectionThread(BluetoothSocket bluetoothSocket) {

        this.bluetoothSocket = bluetoothSocket;
        inputStream = null;
        outputStream = null;

        try
        {
            this.inputStream = bluetoothSocket.getInputStream();

//            isr = new InputStreamReader(inputStream);
//            br = new BufferedReader(isr);
            Log.d(TAG, "ConnectionThread: 初始化成功.");
        }
        catch (Exception e)
        {
            Log.d(TAG, "ConnectionThread: 无法获取到输入/输出流.");
        }
    
    }

    @Override
    public void run() {
        buff = new byte[1024];

        while (true) {

            //if (br == null) continue;
            receiveMessage();
        }
            //因为要一直接受信息,所以要放在run方法里面
    }

    public void receiveMessage()
    {
        try
        {


            //receiveContent = br.readLine();
           // Log.d(TAG, "receiveMessage: newline"+receiveContent);
//                if (!receiveContent.endsWith("#"))
//                     Log.d(TAG, "receiveMessage: ");
//

            bytes = inputStream.read(buff);
            Log.d(TAG, "receiveMessage: bytes "+bytes);
            receiveContent = new String(buff,"utf-8");

            Log.d(TAG, "receiveMessage: "+receiveContent);

//            Log.d(TAG, "receiveMessage: "+receiveContent);
//            if (bytes == -1)
//            {
//                Log.d(TAG, "receiveMessage:-1 "+receiveContent);
//            }
            //单片机发送过来的信息以#号结束,比啊时接收完毕.

        }
        catch (Exception e)
        {
           Log.d(TAG, "receiveMessage: 无法读取信息");
        }
    }


    public void sendMessage(String message)
    {
        byte[] bytes = message.getBytes();
        try
        {
            outputStream = bluetoothSocket.getOutputStream();
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
//            outputStream.write(-1);
            //outputStream.flush();//这个方法有什么用?

//            OutputStreamWriter osw = new OutputStreamWriter(outputStream);
//            BufferedWriter bw = new BufferedWriter(osw);
//            bw.write(message);
//            bw.newLine();

            Log.d(TAG, "sendMessage: 发送的内容是"+new String(bytes,"utf-8"));
        }
        catch (Exception e)
        {
            Log.d(TAG, "sendMessage: 写入数据失败");
        }
    }
    
    public void closeResource()
    {
        try
        {
            inputStream.close();
            //outputStream.close();
            bluetoothSocket.close();
        }
        catch (Exception e)
        {
            Log.d(TAG, "closeResource: 关闭失败");
        }

    }
}
