package com.example.ddre;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pspdfkit.datastructures.TextSelection;
import com.pspdfkit.ui.special_mode.controller.TextSelectionController;
import com.pspdfkit.ui.special_mode.manager.TextSelectionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    protected static TCPClient client;

    //Toolbar Buttons
    protected Button btnSearchServerButton;
    protected static Button btnSendMessage;

    //Float Menu Buttons
    Button btnServerSettings;

    Fragment homeFragment;

    protected static TextView connectionInfo;
    protected static TextView chat;

    private DrawerLayout floatingMenu;
    private Menu toolbarMenu;


    public static final int SETTINGS_ACTIVITY = 1234;

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the TCPClient Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";


    //ON_ACTIVITY_RESULT
    final static int OPEN_PDF = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        floatingMenu = (DrawerLayout) findViewById(R.id.drawerLayout);
        homeFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentHome);
//
//        btnSearchServerButton = (Button) findViewById(R.id.connectServer);
//        connectionInfo = (TextView) homeFragment.getView().findViewById(R.id.textView);
        btnSendMessage = (Button) homeFragment.getView().findViewById(R.id.sendDataTest);
//        chat = (TextView) findViewById(R.id.textView2);
//
//
//

        initPreferences();





        client = new TCPClient(this, myHandler);

//        HomeFragment.btnSendMessage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    JSONObject obj = new JSONObject();
//                    obj.put("messageCode", 1001);
//                    obj.put("name", "foo");
//                    obj.put("num", new Integer(100));
//                    obj.put("balance", new Double(1000.21));
//                    obj.put("is_vip", new Boolean(true));
//                    obj.put("nickname", null);
//
//                    client.write(obj.toString().getBytes("utf-8"));
//                }catch (JSONException | UnsupportedEncodingException e){
//
//                }
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_bar_menu, menu);
        toolbarMenu = menu;
        return true;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.miBurgerMenu:
                floatingMenu.openDrawer(Gravity.RIGHT);
                return true;

            case R.id.miServerConnection:
                client.connect();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onStart(){
        super.onStart();
//        setButtonsListeners();


    }


    private final Handler myHandler = new Handler(){
        @SuppressLint({"HandlerLeak", "ResourceAsColor"})
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),Toast.LENGTH_SHORT).show();
//                    setContentView(R.layout.connection_failed);
//                    findViewById(R.id.connectionFailed).setOnTouchListener(firstTouch);
                    break;
                case MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case TCPClient.STATE_CONNECTED:
//                            connectionInfo.setText("Connected to server");
                            toolbarMenu.findItem(R.id.miServerConnection).getIcon().setColorFilter(getResources().getColor(R.color.server_connection_on), PorterDuff.Mode.SRC_ATOP);
                            Toast.makeText(getApplicationContext(), "Connected!",Toast.LENGTH_SHORT).show();

                            break;
                        case TCPClient.STATE_CONNECTING:
//                            connectionInfo.setText("Connecting...");
                            toolbarMenu.findItem(R.id.miServerConnection).getIcon().setColorFilter(getResources().getColor(R.color.server_connection___), PorterDuff.Mode.SRC_ATOP);
                            Toast.makeText(getApplicationContext(),"Connecting...",Toast.LENGTH_SHORT).show();

                            break;
                        case TCPClient.STATE_LISTEN:
//                            connectionInfo.setText("Listening...");
                            toolbarMenu.findItem(R.id.miServerConnection).getIcon().setColorFilter(getResources().getColor(R.color.server_connection___), PorterDuff.Mode.SRC_ATOP);
                            Toast.makeText(getApplicationContext(),"Listening...",Toast.LENGTH_SHORT).show();

                            break;
                        case TCPClient.STATE_CONNECTION_LOST:
//                            connectionInfo.setText("Connection lost...");
                            toolbarMenu.findItem(R.id.miServerConnection).getIcon().setColorFilter(getResources().getColor(R.color.server_connection_lost), PorterDuff.Mode.SRC_ATOP);
                            Toast.makeText(getApplicationContext(),"Connection lost.",Toast.LENGTH_SHORT).show();

                            break;
                        case TCPClient.STATE_CONNECTION_FAILED:
//                            connectionInfo.setText("Connection failed...");
                            toolbarMenu.findItem(R.id.miServerConnection).getIcon().setColorFilter(getResources().getColor(R.color.server_connection_lost), PorterDuff.Mode.SRC_ATOP);
                            Toast.makeText(getApplicationContext(),"Connection failed.",Toast.LENGTH_SHORT).show();

                            break;
                        case TCPClient.STATE_NONE:
//                            connectionInfo.setText("Waiting for action");
                            toolbarMenu.findItem(R.id.miServerConnection).getIcon().setColorFilter(getResources().getColor(R.color.server_connection_off), PorterDuff.Mode.SRC_ATOP);
                            Toast.makeText(getApplicationContext(),"NONE...",Toast.LENGTH_SHORT).show();

                            break;
                    }
                    break;
                case MESSAGE_WRITE:
//                    chat.setText(chat.getText() + "\n" + msg.getData().getString("MESSAGE"));
                    break;
                case MESSAGE_READ:

                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);

//                    chat.setText(chat.getText() + "\n" + readMessage);
                    break;
            }
        }
    };


    private void setButtonsListeners() {
//        btnSearchServerButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Toast.makeText(MainActivity.this, "Connect", Toast.LENGTH_SHORT).show();
//
////                client.connect();
////                client.connect();
////                client.connect();
////
////                client.write("ON essaye des choses");
//            }
//        });

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("messageCode", 1001);
                    obj.put("name", "foo");
                    obj.put("num", new Integer(100));
                    obj.put("balance", new Double(1000.21));
                    obj.put("is_vip", new Boolean(true));
                    obj.put("nickname", null);

                    client.write(obj.toString().getBytes("utf-8"));
                }catch (JSONException | UnsupportedEncodingException e){

                }
            }
        });
    }


    private void setFloatMenuButtonsListeners(){

        btnServerSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initPreferences(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();

        if(!sharedPref.contains(getString(R.string.ipAddress))){
            editor.putString(getString(R.string.ipAddress), "192.168.137.1");
            editor.commit();
        }
        if(!sharedPref.contains(getString(R.string.serverPort))){
            editor.putString(getString(R.string.serverPort), "3849");
            editor.commit();
        }
    }

    PDFFragment pdfFragment = new PDFFragment();

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        //Change fragment to fragment containing the PDF
        if(requestCode == 8 && resultCode == Activity.RESULT_OK){

            if(data != null) {
                Uri documentUri = data.getData();
//                openPDFView(uri);


                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
//                transaction.setReorderingAllowed(true);

//                pdfFragment = new PDFFragment(documentUri);
//               BlankFragment fragment = new BlankFragment();

                pdfFragment.setDocumentUri(documentUri);

//                Log.i("PPPPSDF", "Is inLayout : "+ getSupportFragmentManager().findFragmentById(R.id.fragmentHome).isInLayout());
//                transaction.replace(R.id.fragmentHome, new BlankFragment(), null);
                transaction.hide(getSupportFragmentManager().findFragmentById(R.id.fragmentHome));
//
                transaction.add(R.id.FrameLayoutContent, pdfFragment).addToBackStack("tag");


//                transaction.replace(R.id.FrameLayoutContent, fragment, null);
//                        .addToBackStack("tag");

//                transaction.add(android.R.id.content, new PDFFragment(documentUri));

                transaction.commit();

//

//
//                PdfFragment fragment;
//
//
//                 fragment = (PdfFragment) getSupportFragmentManager()
//                         .findFragmentById(R.id.fragment_pdf);
//
//
//                final PdfConfiguration configuration = new PdfConfiguration.Builder()
//                        .scrollDirection(PageScrollDirection.HORIZONTAL)
//                        .build();
//
//                if (fragment == null) {
//                    fragment = PdfFragment.newInstance(documentUri, configuration);
//                    getSupportFragmentManager()
//                            .beginTransaction()
//                            .replace(R.id.fragment_pdf, fragment)
//                            .commit();
//                }


//


//                setContentView(R.layout.fragment_pdf);







//            fragment.menu(true);



            }
        }
    }





}