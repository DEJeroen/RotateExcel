package jav.accelerator;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by jeroen on 1/7/16.
 */
public class Multiplayer {


        final String messageStr = "Hello ! This is your msg from server.";
        final int server_port = 50008;



        private void getBroadcastAddress() {

            WifiManager wifi = (WifiManager) Global.context.getSystemService(Context.WIFI_SERVICE);
            DhcpInfo dhcp = wifi.getDhcpInfo();
            // handle null somehow
            try {

                int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
                byte[] quads = new byte[4];
                for (int k = 0; k < 4; k++)
                    quads[k] = (byte) (broadcast >> (k * 8));
                Global.BroadcastAddress = InetAddress.getByAddress(quads);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }





    public void startListening() {

        getBroadcastAddress();


        new Thread(new Runnable() {
            public void run() {
                byte[] message = new byte[1500];
                try {
                    DatagramPacket p = new DatagramPacket(message, message.length);
                    DatagramSocket s = new DatagramSocket(server_port);
                    s.receive(p);
                    Global.recievedMessageStr = new String(message, 0, p.getLength());
                    Log.d("rockman", "message:" + Global.recievedMessageStr);
                    s.close();
                } catch (Exception e) {
                    Log.d("rockman", "error  " + e.toString());
                }
            }
        }).start();

    }


    public void broadcastScore() {


        AsyncTask<Object, Object, Object> task = new AsyncTask<Object, Object, Object>() {


            String currentBroadcast;
            @Override

            protected Object doInBackground(Object... params) {
                try {
                    String ScoreMessageGlobal =Global.sendScoreInt.toString();
                    getBroadcastAddress();
                    currentBroadcast = Global.BroadcastAddress.toString().substring(1);
                    Log.d("broadcast = ", currentBroadcast);

                    DatagramSocket s = new DatagramSocket();
                    InetAddress local = InetAddress.getByName(currentBroadcast);
                    int msg_length = ScoreMessageGlobal.length();
                    byte[] message = ScoreMessageGlobal.getBytes();
                    DatagramPacket p = new DatagramPacket(message, msg_length, local, server_port);
                    s.send(p);
                    Log.d("done", "message send");


                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            }

            ;
        };
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ((Object) null));
    }
    }



