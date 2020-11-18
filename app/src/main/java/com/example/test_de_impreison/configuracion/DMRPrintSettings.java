package com.example.test_de_impreison.configuracion;

import java.io.Serializable;

/**Class to save the application settings**/

public class DMRPrintSettings implements Serializable {

	private static final long serialVersionUID = 4762643389154364957L;


	private String printerMAC;
	private int communicationMethod;
	private int selectedItemIndex;
	private String selectedPrintMode;//2018 PH
	private String communicationType;//2018
	private int configurado;//2018

	public int getConfigurado() {
		return configurado;
	}

	public void setConfigurado(int configurado) {
		this.configurado = configurado;
	}

	public String getIpwebservice() {
		return ipwebservice;
	}

	public void setIpwebservice(String ipwebservice) {
		this.ipwebservice = ipwebservice;
	}

	public String getWebservice() {
		return webservice;
	}

	public void setWebservice(String webservice) {
		this.webservice = webservice;
	}

	private String ipwebservice;
	private String webservice;

	public String getSuc() {
		return suc;
	}

	public void setSuc(String suc) {
		this.suc = suc;
	}

	private String suc;


	public String getCommunicationType() {
		return communicationType;
	}

	public void setCommunicationType(String communicationType) {
		this.communicationType = communicationType;
	}



	//2018 PH - Accessors for print mode
	public String getSelectedPrintMode() {
		return selectedPrintMode;
	}

	public void setSelectedPrintMode(String selectedPrintMode) {
		this.selectedPrintMode = selectedPrintMode;
	}

	//Accessors for selected item index;
	public int getSelectedItemIndex()
	{
		return selectedItemIndex;
	}
	public void setSelectedItemIndex(int value)
	{
		selectedItemIndex = value;
	}

	//Accessors for selected mode index;

	public String getPrinterMAC() {
		return printerMAC;
	}
	public void setPrinterMAC(String value) {
		printerMAC = value;
	}
	//Set and get printer's port


	public int getCommunicationMethod()
	{
		return communicationMethod;
	}
	public void setCommunicationMethod(int value)
	{
		communicationMethod = value;
	}
	//set and get folder path

	//Constructor
	public DMRPrintSettings(String mac, int commMethod, int metodo , String ipwebservic, String webservic , String sucursal, int config){

		printerMAC = mac;

		communicationMethod= commMethod;

		selectedItemIndex = metodo;

		ipwebservice = ipwebservic;

		webservice = webservic;

		suc = sucursal;

		configurado = config;
	}


}