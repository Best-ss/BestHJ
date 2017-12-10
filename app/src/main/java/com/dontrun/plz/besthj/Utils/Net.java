package com.dontrun.plz.besthj.Utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Net {

	public final static int HTTP_ERROR = 0;
	public final static int HTTP_OK = 1;
	
	
	public static Map<String, String> getHeader() {
		Map<String, String> header = new HashMap<>();
		header.put("Accept", "application/json");
		header.put("DeviceId", Config.getDeviceID());
		header.put("appVersion", Config.getAppVersion());
		header.put("osVersion", Config.getOsVersion());
		header.put("deviceName", Config.getDeviceName());
		header.put("osType", "0");
		header.put("CustomDeviceId", Config.getCustomDeviceId());
		header.put("timeStamp", Config.getTimeStamp());
		header.put("User-Agent", Config.getUserAgent());
		header.put("Host", "gxapp.iydsj.com");
		header.put("Connection", "Keep-Alice");
		header.put("Accept-Encoding", "gzip");
		return header;
	}
	
	public static Map<String,String> sendGet(String urlstr,Map<String,String> header){
        HttpURLConnection connection;
        Map<String,String> result = new HashMap<>();
		result.put("state", String.valueOf(HTTP_ERROR));
        try {
            URL url = new URL(urlstr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            for(Map.Entry<String,String> entry : header.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
            int responseCode = connection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                StringBuffer sb = new StringBuffer();
                String readLine;
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
                while((readLine = br.readLine())!=null){
                    sb.append(readLine+'\n');
                }
                br.close();
                result.put("result",sb.toString());
                result.put("state",String.valueOf(HTTP_OK));
            }else {
				result.put("state", String.valueOf(responseCode));
			}
        }catch (Exception e){
            e.printStackTrace();
            return result;
        }
        return result;
    }

	public static Map<String, String> sendPost(String urlstr, Map<String, String> header, String body) {
		HttpURLConnection connection;
		Map<String, String> result = new HashMap<>();
		result.put("state", String.valueOf(HTTP_ERROR));
		try {
			URL url = new URL(urlstr);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");

			for (Map.Entry<String, String> entry : header.entrySet()) {
				connection.setRequestProperty(entry.getKey(), entry.getValue());
			}
			connection.setRequestProperty("Content-Length","0");
			if (body != null) {
				byte[] requestBytes = body.getBytes("UTF-8");
				connection.setRequestProperty("Content-Length", String.valueOf(body.length()));
				System.out.println("Content-Length = " + String.valueOf(body.length()));
				OutputStream os = connection.getOutputStream();
				os.write(requestBytes);
				os.close();
			}

			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				StringBuffer sb = new StringBuffer();
				String readLine;
				BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
				while ((readLine = br.readLine()) != null) {
					sb.append(readLine + '\n');
				}
				br.close();
				result.put("result", sb.toString());
				result.put("state", String.valueOf(HTTP_OK));
			}else {
				result.put("state", String.valueOf(responseCode));
			}

		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		return result;
	}
}
