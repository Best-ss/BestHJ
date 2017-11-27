package com.dontrun.plz.besthj;

public class Config {


	final static String md5_sign_salt = "&wh2016_swcampus";
	final static String md5_sign_salt_run = "&ODJw#h03b_0EaV";
	final static String md5_key = "05df15504f394eab8dd3ab8180006a83";

	static String getDeviceID() {
		// TODO Change this to auto generation
		return "57a867f300dfaa04f6bf9f241b08d999";
	}
	
	static String getAppVersion() {
		// TODO Change this to auto generation
		return "1.5.0";
	}
	
	
	static String getOsVersion() {
		// TODO Change this to auto generation
		return "5.1";
	}
	
	static String getDeviceName() {
		// TODO Change this to auto generation
		return "Vivo V3M A";
	}
	
	static String getCustomDeviceId() {
		// TODO Change this to auto generation
		//return BlackBox.getMD5(getDeviceName());
		return "DF86666161BF4986A64D4F4110B77E93";
	}
	
	
	
	static String getUserAgent() {
		// TODO Change this to auto generation
		return "Dalvik/2.1.0 (Linux; U; Android 5.1; vivo V3M A Build/LMY47I)";
	}
	
	static String getTimeStamp() {
		return  String.valueOf(System.currentTimeMillis());
	}
	
	static String getImei() {
		// TODO Change this to auto generation
		return  "A1000052CEB8C2";
	}
	
	static String getMacAddress() {
		// TODO Change this to auto generation
		return  "9c:a5:c0:d0:63:31";
	}
	

}
