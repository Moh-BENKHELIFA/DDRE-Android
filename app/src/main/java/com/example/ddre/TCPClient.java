package com.example.ddre;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TCPClient {

//    final static private String SERVER_IP = "192.168.137.1";
//    final static private String SERVER_IP = "192.168.4.14";
//    final static private String SERVER_IP = "192.168.43.29";
    static private String SERVER_IP ;
    static private int SERVER_PORT ;

//    final static private int SERVER_PORT = 3849;
    final static private String TAG = "TCPClient";


    private Context appContext;
    private Handler myHandler;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private int mState;

    private PrintWriter output;
    private BufferedReader input;
    private OutputStream outout;

//    private ClientThread clientThread;

    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device
    public static final int STATE_CONNECTION_LOST = 4;  // now connected to a remote device
    public static final int STATE_CONNECTION_FAILED = 5;  // now connected to a remote device

    public static final int EXIT_CMD = -1000;


    public TCPClient(Context c, Handler handler){
        this.appContext = c;
        this.myHandler = handler;
        this.mState = STATE_NONE;
    }


//    public void startClientThread(){
//        this.clientThread = new ClientThread();
//    }
//
//    public void sendMessage(String s){
//        this.clientThread.sendMessage(s);
//    }
//
//    class ClientThread implements Runnable {
//
//        private Socket socket;
//        private BufferedReader input;
//
//        @Override
//        public void run() {
//            try {
//                socket = new Socket(SERVER_IP, SERVER_PORT);
//
//                byte[] buffer = new byte[1024];
//                while (!Thread.currentThread().isInterrupted()){
//                    int bytes = socket.getInputStream().read(buffer);
//
//                       // Send the obtained bytes to the UI Activity
//                       myHandler.obtainMessage(MainActivity.MESSAGE_READ, bytes, -1, buffer)
//                               .sendToTarget();
//                }
//            }catch (IOException e){
//
//            }
//        }
//
//        void sendMessage(final String message) {
////            new Thread(new Runnable() {
//
//            try {
//                if (null != socket) {
//                    PrintWriter out = new PrintWriter(new BufferedWriter(
//                            new OutputStreamWriter(socket.getOutputStream())),
//                            true);
//                    out.println(message);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//



















    public void connect(){

        //Get server IP and Port in the preferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(appContext);
        SERVER_IP = sharedPref.getString(appContext.getString(R.string.ipAddress), null);
        SERVER_PORT = Integer.valueOf(sharedPref.getString(appContext.getString(R.string.serverPort), null));

        // Cancel any thread attempting to make a connection
        if (mState == STATE_CONNECTING) {
//            if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}
        }

        // Cancel any thread currently running a connection
//        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}

        // Start the thread to connect with the given device
        setState(STATE_CONNECTING);
        mConnectThread = new ConnectThread();
        mConnectThread.start();

    }

    public void connected(Socket socket){

//        try {
//            socket.getOutputStream().write("CONNECTED FUNCTION MOTHER FUCKER ".getBytes());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


//        // Cancel the thread that completed the connection
//        if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}
//
//        // Cancel any thread currently running a connection
//        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();

        setState(STATE_CONNECTED);
    }

    public void connectionFailed(){
        Message msg = myHandler.obtainMessage(MainActivity.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(MainActivity.TOAST, "Unable to connect device");
        msg.setData(bundle);
        myHandler.sendMessage(msg);

        setState(STATE_CONNECTION_FAILED);
    }

    public void connectionLost(){
        Message msg = myHandler.obtainMessage(MainActivity.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(MainActivity.TOAST, "Unable to connect device");
        msg.setData(bundle);
        myHandler.sendMessage(msg);

        setState(STATE_CONNECTION_LOST);
    }


    private synchronized void setState(int state) {
        mState = state;

        // Give the new state to the Handler so the UI Activity can update
        myHandler.obtainMessage(MainActivity.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
    }

//
    public void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (mState != STATE_CONNECTED) return;
            r = mConnectedThread;
        }
        // Perform the write unsynchronized
        r.write(out);
    }

    public void write(int out) {
        // Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (mState != STATE_CONNECTED) return;
            r = mConnectedThread;
        }
        // Perform the write unsynchronized
        r.write(out);
    }

    public void write(String text){

        ConnectedThread r;

        synchronized (this){
            if(mState != STATE_CONNECTED) return;
            r = mConnectedThread;
        }

        r.write("OMG CA FONCTIONNE ENFIN".getBytes(StandardCharsets.UTF_8));
    }


    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private class ConnectThread extends Thread {
        Socket mmSocket;

        public void run() {
            Log.i(TAG, "BEGIN mConnectThread");
            setName("ConnectThread");

            // Make a connection to the Server
            try {
                mmSocket = new Socket(SERVER_IP, SERVER_PORT);

                output = new PrintWriter(mmSocket.getOutputStream());
                input = new BufferedReader(new InputStreamReader(mmSocket.getInputStream()));
//                output = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);

                outout = mmSocket.getOutputStream();


                connected(mmSocket);


            } catch (IOException e) {
                connectionFailed();
                // Close the socket

                return;
            }

//            // Reset the ConnectThread because we're done
//            synchronized (TCPClient.this) {
//                mConnectThread = null;
//            }

            // Start the connected thread
        }


//        public void cancel() {
//            try {
//                mmSocket.close();
//            } catch (IOException e) {
//                Log.e(TAG, "close() of connect socket failed", e);
//            }
//        }
    }

    /**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {
        private final Socket mySocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(Socket socket) {
            Log.d(TAG, "create ConnectedThread");
            mySocket = socket;

            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();

            } catch (IOException e) {
                Log.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectedThread");
            byte[] buffer = new byte[1024];

            // Keep listening to the InputStream while connected
            while (true) {
                try {
                    // Read from the InputStream
                    int bytes = mmInStream.read(buffer);

                    // Send the obtained bytes to the UI Activity
                    myHandler.obtainMessage(MainActivity.MESSAGE_READ, bytes, -1, buffer)
                            .sendToTarget();



                        output.write("RUNNING THREAD");
                        output.flush();

                } catch (IOException e) {
                    Log.e(TAG, "disconnected", e);

                    connectionLost();
                    break;
                }
            }
        }

        /**
         * Write to the connected OutStream.
         * @param buffer  The bytes to write
         */
        public void write(byte[] buffer) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mySocket.getOutputStream().write(buffer);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Exception during write", e);

                    }
                    //            try {
                    //
                    ////                System.out.println(mmOutStream != null);
                    ////               mySocket.getOutputStream().write("hi".getBytes());
                    ////                mmOutStream.write(buffer);
                    //                output.write("Hello world!");
                    //                outout.write(3);
                    //                output.flush();
                    //                // Share the sent message back to the UI Activity
                    //
                    Message msg = myHandler.obtainMessage(MainActivity.MESSAGE_WRITE);
                    Bundle bundle = new Bundle();
                    bundle.putString("MESSAGE", new String(buffer));
                    msg.setData(bundle);
                    myHandler.sendMessage(msg);
                    //
                    //
                    //
                    ////                myHandler.obtainMessage(MainActivity.MESSAGE_WRITE, -1, -1, buffer)
                    ////                        .sendToTarget();
                    //            } catch (IOException e) {
                    //                Log.e(TAG, "Exception during write", e);
                    //            }
                }
            }).start();
        }

        public void write(int out) {
            try {
//                mmOutStream.write(out);
                outout.write(out);
                // Share the sent message back to the UI Activity
//                mHandler.obtainMessage(BluetoothChat.MESSAGE_WRITE, -1, -1, buffer)
//                        .sendToTarget();
            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
        }

        public void cancel() {
            try {
//                mmOutStream.write("BYE".getBytes(StandardCharsets.UTF_8));
                mySocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }


}
