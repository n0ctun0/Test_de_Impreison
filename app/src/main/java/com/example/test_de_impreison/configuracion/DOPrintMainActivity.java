package com.example.test_de_impreison.configuracion;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.usb.UsbDevice;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test_de_impreison.R;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class DOPrintMainActivity extends AppCompatActivity {

    static final String FOLDER_PATH_KEY = "com.honeywell.doprint.Folder_Path_Key";
    static final String CONNECTION_MODE_KEY = "com.honeywell.doprint.Connection_Mode_Key";
    static final String PRINTER_IPADDRESS_KEY = "com.honeywell.doprint.PRINTER_IPAddress_Key";
    static final String PRINTER_TCPIPPORT_KEY = "com.honeywell.doprint.PRINTER_TCPIPPort_Key";
    static final String BLUETOOTH_DEVICE_ADDR_KEY = "com.honeywell.doprint.PRINTER_Bluetooth_Device_Addr_Key";
    static final String USB_DEVICENAME = "com.honeywell.dmrprint.USB_DEVICENAME";
    static final String USB_PRODUCTOID = "com.honeywell.doprint.USB_PRODUCTOID";


    private Context mContext;

    private String ipwebservice;
    private String webservice;
    private String numerodesucursal;

    private String m_selectedPath;
    private String m_printerIP = null;
    private String m_printerMAC = null;
    private int m_printerPort = 515;

    private int sucursaltext = 0;
    private String connectionType;
    //ConnectionBase conn = null;
    private int m_printHeadWidth = 384; //572

    private Button btnrubros;

    private DispositivoBT btconfig;
    private String g_PrintStatusStr;

    ArrayAdapter<CharSequence> adapter = null;

    //array to contain the filenames inside a directory
    List<String> filesList = new ArrayList<>();

    static final int CONFIG_CONNECTION_REQUEST = 0; // for Connection Settings
    private static final int REQUEST_PICK_FILE = 1; //for File browsing

    private Handler m_handler = new Handler(); // Main thread

    private String m_printerMode = "";
    Button m_saveButton;

    String m_devicename;
    String m_productoid;
    Button m_configConnectionButton;

    //EditText
    TextView m_connectionInfoStatus;
    TextView m_actionTextView;

    //Spinners
    Spinner m_connectionSpinner;
    Spinner m_printItemsSpinner;


    // Configuration files to load
    String ApplicationConfigFilename = "applicationconfigg.dat";

    DMRPrintSettings g_appSettings = new DMRPrintSettings("", 0, 0, "0", "0", "0",0);
    //2018 PH
    private IntentFilter mIntentFilter;//Se usa para el broadcast. Del lado del servicio asigno un setAction al intent que voy a enviar con sendBroadcast.
    // De este lado, al estar registrado el BroadcastReceiver con el IntentFilter, se "filtra" por esa acción del setAction y se produce el onReceive del BroadcastReceiver.
    Intent serviceIntent;
    public static final String mBroadcastStringAction = "broadcastStringAction";
    private String m_selectedPathFolder;
    Button m_iniciarTaskButton;
    int REQUEST_EXTERNAL_STORAGE_PERMISSION = 0;

    HashMap<String, UsbDevice> mDeviceList;
    Iterator<UsbDevice> mDeviceIterator;


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doprint_main);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int readExternalPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            int writeExternalPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (writeExternalPermission != PackageManager.PERMISSION_GRANTED || readExternalPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE_PERMISSION);
            }
        }

        mContext = this.getApplicationContext();

        DMRPrintSettings appSettings = ReadApplicationSettingFromFile();
        if (appSettings != null)

            g_appSettings = appSettings;
        //Get parameters from application settings
        m_printerMAC = g_appSettings.getPrinterMAC();
        m_printerMode = g_appSettings.getSelectedPrintMode();//2018 PH
        numerodesucursal = g_appSettings.getSuc();//2018 PH
        ipwebservice = g_appSettings.getIpwebservice();//2018 PH
        webservice = g_appSettings.getWebservice();//2018 PH

        //======Mapping UI controls from our activity xml===========//


        m_connectionInfoStatus = (TextView) findViewById(R.id.communication_status_information);
        m_connectionSpinner = (Spinner) findViewById(R.id.connection_spinner);
        m_configConnectionButton = (Button) findViewById(R.id.configConn_button);
        m_saveButton = (Button) findViewById(R.id.saveSettings_button);
        m_connectionSpinner.setSelection(g_appSettings.getCommunicationMethod());

        m_connectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                connectionType = m_connectionSpinner.getSelectedItem().toString();
                g_appSettings.setCommunicationType(connectionType);

                if (connectionType.equals("Bluetooth")) {
                    if (m_printerMAC.length() == 0) {
                        m_connectionInfoStatus.setText(R.string.connection_not_configured);
                    } else {
                        String printerInfo = "Printer's MAC Address: " + m_printerMAC;
                        m_connectionInfoStatus.setText(printerInfo);
                    }
                }

                g_appSettings.setCommunicationMethod(m_connectionSpinner.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        // ------------------------------------------------
        // Handles when user presses connection config button
        // -------------------------------------------------
        m_configConnectionButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent connSettingsIntent = new Intent("com.desarrollo.myapplication.ConnectionSettingsActivity");
                Spinner connectionSpinner = (Spinner) findViewById(R.id.connection_spinner);
                String connectionType = connectionSpinner.getSelectedItem().toString();
                connSettingsIntent.putExtra(CONNECTION_MODE_KEY, connectionType);
                connSettingsIntent.putExtra(PRINTER_IPADDRESS_KEY, m_printerIP);
                connSettingsIntent.putExtra(PRINTER_TCPIPPORT_KEY, m_printerPort);
                connSettingsIntent.putExtra(BLUETOOTH_DEVICE_ADDR_KEY, m_printerMAC);
                connSettingsIntent.putExtra(USB_DEVICENAME, m_devicename);
                connSettingsIntent.putExtra(USB_PRODUCTOID, m_productoid);
                startActivityForResult(connSettingsIntent, CONFIG_CONNECTION_REQUEST);
            }
        });


        m_saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                    g_appSettings.setIpwebservice(ipwebservice);
                    g_appSettings.setWebservice(webservice);
                    g_appSettings.setSuc(numerodesucursal);
                    g_appSettings.setConfigurado(1);
                    SaveApplicationSettingToFile();

            }
        });

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(mBroadcastStringAction);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            //get results from connection settings activity
            case CONFIG_CONNECTION_REQUEST: {
                if (resultCode == RESULT_OK) {
                    // get the bundle data from the TCP/IP Config Intent.
                    Bundle extras = data.getExtras();

                    if (extras != null) {
                        //===============Get data from Bluetooth configuration=================//
                        if (connectionType.equals("Bluetooth")) {
                            String btAddressString = extras.getString(BLUETOOTH_DEVICE_ADDR_KEY);
                            if (btAddressString != null)
                                m_printerMAC = btAddressString.toUpperCase(Locale.US);
                            if (!m_printerMAC.matches("[0-9A-fa-f:]{17}")) {
                                m_printerMAC = formatBluetoothAddress(m_printerMAC);
                            }

                            String printerInfo = "Printer's MAC Address: " + m_printerMAC;
                            m_connectionInfoStatus.setText(printerInfo);
                        }
                        //==============Get data from TCP/IP configuration===================//
                        else if (connectionType.equals("TCP/IP")) {

                            m_printerIP = extras.getString(PRINTER_IPADDRESS_KEY);
                            m_printerPort = extras.getInt(PRINTER_TCPIPPORT_KEY);

                            String printerInfo = "Printer's IP Address/Port: " + m_printerIP + ":" + Integer.toString(m_printerPort);
                            m_connectionInfoStatus.setText(printerInfo); //valid values are 3inch

                        }

                        g_appSettings.setPrinterMAC(m_printerMAC);


                    }
                }
                break;
            }

            case REQUEST_PICK_FILE: {
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        //========Get the file path===============//
                        m_selectedPath = extras.getString(FOLDER_PATH_KEY);

                        if (!filesList.contains(m_selectedPath))
                            filesList.add(m_selectedPath);
                        if (adapter != null) {
                            m_printItemsSpinner.setAdapter(adapter);

                            //2018 PH

                            //if item is not on the list, then add
                            if (adapter.getPosition(m_selectedPath) < 0) {
                                adapter.add(m_selectedPath);
                                adapter.notifyDataSetChanged();
                            }
                            //selects the selected one.
                            m_printItemsSpinner.setSelection(adapter.getPosition(m_selectedPath));
                        }
                    }
                }
                break;
            }
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //reload your ScrollBars by checking the newConfig

    }

    // -------------------------------------------------
    // Read the application configuration information from a file.
    // -------------------------------------------------

    DMRPrintSettings ReadApplicationSettingFromFile() {
        DMRPrintSettings ret = null;
        InputStream instream;
        try {
            //showToast("Loading configuration");
            showToast("Cargando configuración...");
            instream = openFileInput(ApplicationConfigFilename);
        } catch (FileNotFoundException e) {

            Log.e("DOPrint", e.getMessage(), e);
            //showToast("No configuration loaded");
            showToast("No hay configuración para cargar...");
            return null;
        }

        try {
            ObjectInputStream ois = new ObjectInputStream(instream);

            try {
                ret = (DMRPrintSettings) ois.readObject();
            } catch (ClassNotFoundException e) {
                Log.e("DOPrint", e.getMessage(), e);
                ret = null;
            }
        } catch (Exception e) {
            Log.e("DOPrint", e.getMessage(), e);
            ret = null;
        } finally {
            try {
                if (instream != null)
                    instream.close();
            } catch (IOException ignored) {
            }
        }
        return ret;
    }


    public boolean SaveApplicationSettingToFile() {

        boolean bRet = true;
        FileOutputStream fos = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            // write the object to the output stream object.
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(g_appSettings);

            // convert the output stream object to array of bytes
            byte[] buf = bos.toByteArray();

            // write the array of bytes to file output stream
            fos = openFileOutput(ApplicationConfigFilename,
                    Context.MODE_PRIVATE);
            fos.write(buf);

            File f = getDir(ApplicationConfigFilename, 0);
            Log.e("DOPrint", "Save Application settings to file: " + f.getName());
            //showToast("Application Settings saved");
            showToast("Configuración guardada correctamente.");

            finish();
        } catch (IOException ioe) {
            Log.e("DOPrint", "error", ioe);
            showToast(ioe.getMessage());
            bRet = false;
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException ioe) {

                showToast(ioe.getMessage());
            }
        }
        return bRet;
    }// SaveApplicationSettingToFile()

    public void showToast(final String toast) {
        Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
    }

    /**
     * Converts Bluetooth Address string from 00ABCDEF0102 format => 00:AB:CD:EF:01:02 format
     *
     * @param bluetoothAddr - Bluetooth Address string to convert
     */
    public String formatBluetoothAddress(String bluetoothAddr) {
        //Format MAC address string
        StringBuilder formattedBTAddress = new StringBuilder(bluetoothAddr);
        for (int bluetoothAddrPosition = 2; bluetoothAddrPosition <= formattedBTAddress.length() - 2; bluetoothAddrPosition += 3)
            formattedBTAddress.insert(bluetoothAddrPosition, ":");
        return formattedBTAddress.toString();
    }


    //2018 PH: SOBREESCRIBIENDO ESTE MÉTODO, AL PRESIONAR EL BOTÓN BACK EN LA ACTIVIDAD PRINCIPAL NO SE VUELVE AL LA PANTALLA
    //ANTERIOR (EN CASO DE EXISTIR) Y SE MANDA LA APLICACIÓN AL BACKGROUND (Y EL SERVICIO SIGUE FUNCIONANDO)
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (!m_printerMAC.equals("") && !numerodesucursal.equals("")) {
            finish();
        } else {
            showToast("Verifique configuración.");
        }
    }

    /**
     * Display printing status message to the Textview in the
     * DOPrinterDemoActivity
     *
     * @param MsgStr - message to display
     */
    public void DisplayPrintingStatusMessage(final String MsgStr, final String nombrePDF) {
        g_PrintStatusStr = MsgStr;
        m_handler.post(new Runnable() {
            public void run() {
                ((TextView) findViewById(R.id.printing_status_textview)).setText(g_PrintStatusStr);
                showToast(MsgStr);//2018 PH

            }// run()
        });
    }


    //2018 PH - implemento esto método para evaluar el estado del servicio mediante un booleano estático y para recargas los valores de la configuración
    @Override
    protected void onPostResume() {
        super.onPostResume();
        m_connectionSpinner.setSelection(g_appSettings.getCommunicationMethod());//cargo el spinner de la conexión según la configuración



    }


}

