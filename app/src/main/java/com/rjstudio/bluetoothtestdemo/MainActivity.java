package com.rjstudio.bluetoothtestdemo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rjstudio.bluetoothtestdemo.Bluetooth.Client.BTConnectionClient;
import com.rjstudio.bluetoothtestdemo.Bluetooth.ClientSocket;
import com.rjstudio.bluetoothtestdemo.Bluetooth.ConnectionThread;
import com.rjstudio.bluetoothtestdemo.Bluetooth.ServerSocket;
import com.rjstudio.bluetoothtestdemo.Bluetooth.Sever.BTConnectionServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView textView;
    private EditText editText;
    private Button bu_send;
    private Button bu_servce;
    private Button bu_client;

    private UUID uuid = UUID.fromString("8b66eab7-6f0e-42a2-870e-72a8141e308c");
    private BluetoothSocket bs;
    private ConnectionThread connectionThread;
    private ServerSocket ss;
    private ClientSocket cs;
    private ListView listView;
    private String bluetoothAddress;
    private BTConnectionClient btConnectionClient;
    private BTConnectionServer btConnectionServer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        SearchBlueTooth();

    }

    public void init()
    {
        Log.d(TAG, "init: 成功");
        textView = (TextView) findViewById(R.id.tv_show);
        editText = (EditText) findViewById(R.id.et_send);
        bu_send = (Button) findViewById(R.id.bu_send);
        bu_servce = (Button) findViewById(R.id.bu_s);
        bu_client = (Button) findViewById(R.id.bu_c);

        bu_send.setOnClickListener(this);
        bu_servce.setOnClickListener(this);
        bu_client.setOnClickListener(this);

        listView = (ListView) findViewById(R.id.lv_devices);
    }


    @Override
    public void onClick(View v) {
        //Toast.makeText(this, "Clicking", Toast.LENGTH_SHORT).show();
        switch (v.getId())
        {
            case R.id.bu_send:
                //Log.d(TAG, "onClick: send");
                //Toast.makeText(this, "send", Toast.LENGTH_SHORT).show();
                sendLogic();
                break;
            case R.id.bu_c:
                Log.d(TAG, "onClick: client");

                Toast.makeText(this, "client", Toast.LENGTH_SHORT).show();
                initClient();
                break;
            case R.id.bu_s:
                Log.d(TAG, "onClick: serve");
                Toast.makeText(this, "serve", Toast.LENGTH_SHORT).show();
                initServer();
                break;
        }
    }
    private String TAG = "Main";
    private boolean isClient = false;
    public void initClient()
    {
        textView.setBackgroundColor(getResources().getColor(R.color.client));
        //TODO 这里没有判断蓝牙是否开启
        BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
//        Set<BluetoothDevice> bluetoothDevices = ba.getBondedDevices();
//        for (BluetoothDevice d : bluetoothDevices)
//        {
//            Log.d(TAG, "initClient 蓝牙名称 : "+d.getName()+"--- 蓝牙地址 : "+d.getAddress());
//        }
        //G3588V 60:8F:5C:A5:4D:B0
        //S5830  D0:17:6A:E4:9C:AB

        //本机作为客户端
        BluetoothDevice clientDevice = ba.getRemoteDevice(bluetoothAddress);
        Log.d(TAG, "本机是"+clientDevice.getName()+"---"+clientDevice.getAddress()+"----"+clientDevice);
        cs = new ClientSocket(clientDevice,uuid);
        //开启线程,去申请与服务器连接
       // cs.start();
        cs.start();

        //获取一个socket 用于操作输出输入
        //bs = cs.getBluetoothSocket();
        btConnectionClient = cs.getBtConnectionClient();
        isClient = true;
        Log.d(TAG, "initClient: 客户端初始化完成");
    }

    public void initServer()
    {
        textView.setBackgroundColor(getResources().getColor(R.color.serve));
        BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
        ss = new ServerSocket(ba,uuid);
        ss.start();
        btConnectionServer = ss.getBTConnectionServer();
        isClient = false;
        Log.d(TAG, "initServer: 服务器端初始化完成");
    }

    public void sendLogic()
    {
        String tempContent = editText.getText().toString();
        Log.d(TAG, "sendLogic: " +tempContent);
        if (isClient)
        {
            cs.getConnectionThread().sendMessage(tempContent);
            //cs.sendMessage(tempContent);
            //btConnectionClient.sendMessage(tempContent);//NullPointException

        }
        else
        {
            ss.getConnectionThread().sendMessage(tempContent);
            //ss.sendMessage(tempContent);
            //btConnectionServer.sendMessage(tempContent); //NullPointException
        }
        editText.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connectionThread.closeResource();
        ss.close();
    }

    public void SearchBlueTooth()
    {
        BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> devices = ba.getBondedDevices();
        final List<String> devicesAddress = new ArrayList<>();
        for (BluetoothDevice bd : devices)
        {
            devicesAddress.add(bd.getAddress());
        }
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_list_item_1,devicesAddress);
        listView.setAdapter(aa);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bluetoothAddress = devicesAddress.get(position);
                Toast.makeText(MainActivity.this, bluetoothAddress, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
