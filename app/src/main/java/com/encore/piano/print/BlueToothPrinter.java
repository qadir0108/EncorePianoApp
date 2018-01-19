package com.encore.piano.print;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.encore.piano.R;
import com.encore.piano.util.Alerter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Kamran on 24-Dec-17.
 */

public class BlueToothPrinter {

    // android built in classes for bluetooth operations
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    List<BluetoothDevice> btDevices = null;

    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;

    private void flushData() {
        try {
            if (mmSocket != null) {
                mmSocket.close();
                mmSocket = null;
            }

            if (mBluetoothAdapter != null) {
                mBluetoothAdapter.cancelDiscovery();
            }

            if (btDevices != null) {
                btDevices.clear();
                btDevices = null;
            }
            finalize();

        } catch (Exception ex) {
        } catch (Throwable e) {
        }

    }

    boolean findBT(Activity context) {

        try {

            flushData();

            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if(mBluetoothAdapter == null) {
                Alerter.error(context, "No bluetooth adapter available");
                return false;
            }

            if(!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                context.startActivityForResult(enableBluetooth, 0);
                return false;
            }

            // Discovering

//            new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
//                    .setTitleText("Discovering bluetooth printers.")
//                    .setContentText("Please wait...")
//                    .show();
//
//            IntentFilter btIntentFilter = new IntentFilter(
//                    BluetoothDevice.ACTION_FOUND);
//            context.registerReceiver(mBTReceiver, btIntentFilter);
//
//            if (mBluetoothAdapter.isDiscovering()) {
//                mBluetoothAdapter.cancelDiscovery();
//            }
//            mBluetoothAdapter.startDiscovery();

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if(pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {

                    // QSprinter is the name of the bluetooth printer device
                    // we got this name from the list of paired devices
                    if (device.getName().contains("printer")) {
                        mmDevice = device;
                        break;
                    }
                }
            }

            if(mmDevice == null) {
                Alerter.error(context, "No bluetooth device found with name printer");
                return false;
            }

        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

//    private final BroadcastReceiver mBTReceiver = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                BluetoothDevice device = intent
//                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//
//                try {
//                    if (btDevices == null) {
//                        btDevices = new ArrayList<BluetoothDevice>();
//                    }
//                    btDevices.add(device);
//
//                    if (device.getName().contains("printer")) {
//                        mmDevice = device;
//                    }
//
//                } catch (Exception ex) {
//                    // ex.fillInStackTrace();
//                }
//            }
//        }
//    };

    // tries to open a connection to the bluetooth printer device
    public OutputStream openBT(Activity context) {
        try {

            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();

            Log.d("BT", "Bluetooth Opened");
            //Alerter.success(context, "Bluetooth Opened");

        } catch (Exception e) {
            Alerter.error(context, "Can not open bluetooth.");
            e.printStackTrace();
        }
        return mmOutputStream;
    }

    /*
 * after opening a connection to bluetooth printer device,
 * we have to listen and check if a data were sent to be printed.
 */
    void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // this is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();

                            if (bytesAvailable > 0) {

                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; i++) {

                                    byte b = packetBytes[i];
                                    if (b == delimiter) {

                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );

                                        // specify US-ASCII encoding
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        // tell the user data were sent to bluetooth printer device
                                        handler.post(new Runnable() {
                                            public void run() {
                                                //myLabel.setText(data);
                                                Log.d("e", data);
                                            }
                                        });

                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // this will send text data to be printed by the bluetooth printer
    public void sendData(Activity context, String msg) {
        try {
            findBT(context);
            openBT(context);

            mmOutputStream.write(msg.getBytes());

            closeBT(context);
        } catch (IOException ex) {
            Alerter.error(context, ex.getMessage());
            ex.printStackTrace();
        } catch (Exception ex) {
            Alerter.error(context, ex.getMessage());
            ex.printStackTrace();
        }
    }

    // close the connection to bluetooth printer.
    void closeBT(Activity context) throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
            //Alerter.success(context, "Bluetooth Closed");
            Log.d("BT", "Bluetooth Closed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}