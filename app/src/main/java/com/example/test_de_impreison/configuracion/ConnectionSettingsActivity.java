package com.example.test_de_impreison.configuracion;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.test_de_impreison.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConnectionSettingsActivity extends AppCompatActivity {


	private UsbManager mUsbManager;
	private UsbDevice mDevice;
	private UsbDeviceConnection mConnection;
	private UsbInterface mInterface;
	private UsbEndpoint mEndPoint;
	private PendingIntent mPermissionIntent;
    private static Boolean forceCLaim = true;
	private DispositivoBT deviceBT;

	private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
	HashMap<String, UsbDevice> mDeviceList;
	Iterator<UsbDevice> mDeviceIterator;

	//PH 2018
	//SharedPreferences prefs;
	RadioGroup m_rdgListaImpresoras;
	RadioButton m_rdbApex3t,m_rdbApex4t,m_rdbApex3c,m_rdbApex4c;
	ListView listViewBT;
	ListaDispositivosBTAdapter lisAdapter;

	//Connection Type to be specified by user
	private String connectionType;
private Context mContext;
	//====Connection parameters===//
	private String m_printerIPAddr; //printer's Ip address
	private int m_printerPort; // printer's port.


	private String m_devicename; //printer's Ip address
	private String m_productoid; // printer's port.


	private String m_printerBluetoothAddr; // printer's MAC address

	//=======UI controls variables ============// 
	TextView m_connectionLabel;
	EditText m_connectionEditText;
	TextView m_portLabel;
	EditText m_portEditText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connection_settings);

		if (getSupportActionBar() != null)
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		//=======Associate UI variables to activity's UI control============//
		m_connectionLabel = (TextView) findViewById(R.id.connection_prompt_label);
		m_portLabel = (TextView) findViewById(R.id.port_textView);
		m_portEditText = (EditText) findViewById(R.id.port_editText);
		m_connectionEditText = (EditText) findViewById(R.id.connection_editText);

		//===========Retreive variable's values passed in from main activity=======//
		connectionType = getIntent().getExtras().getString(DOPrintMainActivity.CONNECTION_MODE_KEY);
		m_printerIPAddr = getIntent().getExtras().getString(DOPrintMainActivity.PRINTER_IPADDRESS_KEY);
		m_printerPort = getIntent().getExtras().getInt(DOPrintMainActivity.PRINTER_TCPIPPORT_KEY);
		m_printerBluetoothAddr = getIntent().getExtras().getString(DOPrintMainActivity.BLUETOOTH_DEVICE_ADDR_KEY);

		m_devicename = getIntent().getExtras().getString(DOPrintMainActivity.USB_DEVICENAME);
		m_productoid = getIntent().getExtras().getString(DOPrintMainActivity.USB_PRODUCTOID);





        mContext = this.getApplicationContext();
		//=======Display the proper connection parameters depending on the User's selection====//
		if (connectionType.equals("Bluetooth")) {
			m_connectionLabel.setText(R.string.enter_mac);
			m_connectionEditText.setHint(R.string.mac_hint);
			if (!m_printerBluetoothAddr.equals("Unknown"))
				m_connectionEditText.setText(m_printerBluetoothAddr);
			m_portLabel.setVisibility(View.GONE);
			m_portEditText.setVisibility(View.GONE);
		} else if (connectionType.equals("TCP/IP")) {
			m_connectionLabel.setText(R.string.enter_ip);
			m_connectionEditText.setHint(R.string.ip_hint);
			m_portEditText.setHint(R.string.port_hint);
			if (!m_printerIPAddr.equals("0.0.0.0") && m_printerPort != 0) {

				m_connectionEditText.setText(m_printerIPAddr);
				m_portEditText.setText(String.format("%d", m_printerPort));
			}
			m_portLabel.setVisibility(View.VISIBLE);
			m_portEditText.setVisibility(View.VISIBLE);
		}else if (connectionType.equals("USB") && m_devicename!=null){

			m_connectionLabel.setText("Enter USB");
			m_connectionEditText.setHint("USB id");
			m_portEditText.setHint("USB id2");
			if (m_devicename.length()>0 && m_productoid.length()> 0) {

				m_connectionEditText.setText(m_devicename);
				m_portEditText.setText(m_productoid);
			}

			m_portLabel.setVisibility(View.VISIBLE);
			m_portEditText.setVisibility(View.VISIBLE);
		}
		//2018 PH
		listViewBT = (ListView) findViewById(R.id.listaDispositivosBT);

		//==========================================================
		//handler for the save button click
		//==========================================================
		Button SavetButton = (Button) this.findViewById(R.id.save_button);
		SavetButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
					// create new intent
					Intent intent = new Intent();
					//Pass connection information back to main activity based on connection
					if (connectionType.equals("Bluetooth")) {
						m_printerBluetoothAddr = m_connectionEditText.getText().toString().toUpperCase(Locale.US);
						//validate if its a MAC address
						Pattern pattern = Pattern.compile("[0-9A-Fa-f]{12}");
						Matcher matcher = pattern.matcher(m_printerBluetoothAddr);
						if (matcher.matches())
							m_printerBluetoothAddr = formatBluetoothAddress(m_printerBluetoothAddr);

						if (!BluetoothAdapter.checkBluetoothAddress(m_printerBluetoothAddr))
							throw new Exception("Invalid Bluetooth Address format");
						intent.putExtra(DOPrintMainActivity.BLUETOOTH_DEVICE_ADDR_KEY, m_printerBluetoothAddr);
					} else if (connectionType.equals("TCP/IP")) {

						m_printerIPAddr = m_connectionEditText.getText().toString();
						//validate if its a IP Address
						Pattern pattern = Pattern.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
								"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
								"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
								"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
						Matcher matcher = pattern.matcher(m_printerIPAddr);
						if (!matcher.matches())
							throw new Exception("Invalid IP Address format");
						m_printerPort = Integer.parseInt(m_portEditText.getText().toString());

						intent.putExtra(DOPrintMainActivity.PRINTER_IPADDRESS_KEY, m_printerIPAddr);
						intent.putExtra(DOPrintMainActivity.PRINTER_TCPIPPORT_KEY, m_printerPort);


					} else if (connectionType.equals("USB")) {


						m_devicename = m_connectionEditText.getText().toString();
						m_productoid = m_portEditText.getText().toString();


						 intent.putExtra(DOPrintMainActivity.USB_DEVICENAME, m_devicename);
						 intent.putExtra(DOPrintMainActivity.USB_PRODUCTOID, m_productoid);
					}
					// return the value back to the caller
					setResult(RESULT_OK, intent);

					// close this Activity
					finish();
				}
				//=====Error handling============//
				catch (NumberFormatException nfe) {
					nfe.printStackTrace();
					AlertDialog.Builder builder = new AlertDialog.Builder(ConnectionSettingsActivity.this);
					builder.setTitle(R.string.BT_app_config_error)
							.setMessage(R.string.BT_app_config_error_message)
							.setCancelable(false)
							.setNegativeButton(R.string.close_label, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
								}
							});
					AlertDialog alert = builder.create();
					alert.show();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					AlertDialog.Builder builder = new AlertDialog.Builder(ConnectionSettingsActivity.this);
					builder.setTitle("Application Error")
							.setMessage(e.getMessage())
							.setCancelable(false)
							.setNegativeButton("Close", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
								}
							});
					AlertDialog alert = builder.create();
					alert.show();
				}
			}
		});

		//=================================
		//handler for the cancel button click
		//=====================================
		Button CancelButton = (Button) this.findViewById(R.id.cancel_button);
		CancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setResult(RESULT_CANCELED, null);
				finish();
			}
		});

		//m_connectionEditText.setText("0012f32d3577");//APEX4 taller PH 2018
		//m_connectionEditText.setText("0012f3318a49");//APEX3 taller PH 2018
		//m_connectionEditText.setText("0012f32d3913");//APEX4 cousa PH 2018
		//m_connectionEditText.setText("0012f3319103");//APEX3 cousa PH 2018

		cargarListaDispositivosConectados();

		//2018 PH
		m_rdgListaImpresoras = (RadioGroup) findViewById(R.id.rdgListaAPEX);
		m_rdgListaImpresoras.setVisibility(View.INVISIBLE);


		//==========================================================
		//handler del botón refresh
		//==========================================================
		Button RefreshButton = (Button) this.findViewById(R.id.refresh_button);
		RefreshButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
					cargarListaDispositivosConectados();
				}
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					AlertDialog.Builder builder = new AlertDialog.Builder(ConnectionSettingsActivity.this);
					builder.setTitle("Application Error")
							.setMessage(e.getMessage())
							.setCancelable(false)
							.setNegativeButton("Close", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
								}
							});
					AlertDialog alert = builder.create();
					alert.show();
				}
			}
		});
	}

	public void cargarListaDispositivosConectados()
	{
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		final ArrayList<DispositivoBT> listaBT = new ArrayList<DispositivoBT>();

		if (connectionType.equals("Bluetooth")) {

			//PH 2018

			for (BluetoothDevice bt : pairedDevices) {
				DispositivoBT deviceBT = new DispositivoBT();
				deviceBT.setNombreDispositivo(bt.getName());
				deviceBT.setMacDispositivo(bt.getAddress());
				listaBT.add(deviceBT);
				String deviceName = bt.getName();
				String deviceHardwareAddress = bt.getAddress();
				Log.e("Dispositivo: ", "Device name:" + deviceName + "Device Hardware/MAC Address:" + deviceHardwareAddress);
			}

		}

		lisAdapter = new ListaDispositivosBTAdapter(this, R.id.listaDispositivosBT, listaBT);
		listViewBT.setAdapter(lisAdapter);

		listViewBT.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				mDevice = ((DispositivoBT) listViewBT.getItemAtPosition(position)).getUsb();
				m_connectionEditText.setText(((DispositivoBT) listViewBT.getItemAtPosition(position)).getMacDispositivo());

                if (mDevice != null) {

					if (mUsbManager.hasPermission(mDevice)) {

						mInterface = mDevice.getInterface(0);
						mEndPoint = mInterface.getEndpoint(0);
						mConnection = mUsbManager.openDevice(mDevice);

						m_connectionEditText.setText(((DispositivoBT) listViewBT.getItemAtPosition(position)).getNombreDispositivo());
						m_portEditText.setText(((DispositivoBT) listViewBT.getItemAtPosition(position)).getMacDispositivo());

					}else{
						mPermissionIntent = PendingIntent.getBroadcast(ConnectionSettingsActivity.this, 0, new Intent(ACTION_USB_PERMISSION), 0);
						IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
						registerReceiver(mUsbReceiver, filter);
						mUsbManager.requestPermission(mDevice, mPermissionIntent);

					}

                }
			}
		});
	}

	final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (ACTION_USB_PERMISSION.equals(action)) {
				synchronized (this) {
					UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

					if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
						if (device != null) {

							//call method to set up device communication
							mInterface = device.getInterface(0);
							mEndPoint = mInterface.getEndpoint(0);
							mConnection = mUsbManager.openDevice(device);
							//setup();
						}
					} else {
						//Log.d("SUB", "permission denied for device " + device);
						Toast.makeText(context, "PERMISSION DENIED FOR THIS DEVICE", Toast.LENGTH_SHORT).show();
					}
				}
			}
		}
	};



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
			case android.R.id.home:
				onBackPressed();
				break;
			default:
				return super.onOptionsItemSelected(item);
		}
		return true;
	}

	/** Converts Bluetooth Address string from 00ABCDEF0102 format => 00:AB:CD:EF:01:02 format
	 * @param bluetoothAddr - Bluetooth Address string to convert
	 */
	public String formatBluetoothAddress(String bluetoothAddr)
	{
		//Format MAC address string
		StringBuilder formattedBTAddress = new StringBuilder(bluetoothAddr);
		for (int bluetoothAddrPosition = 2; bluetoothAddrPosition <= formattedBTAddress.length() - 2; bluetoothAddrPosition += 3)
			formattedBTAddress.insert(bluetoothAddrPosition, ":");
		return formattedBTAddress.toString();
	}


}
