package com.dontrun.plz.besthj;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class UserAction {
	
	private final static int HTTP_ERROR = 0;
	private final static int HTTP_OK = 1;

	//private final static String loginURL = "https://gxapp.iydsj.com/api/v14/login";
	//private final static String logoutURL = "https://gxapp.iydsj.com/api/v6/user/logout";
	private final static String loginURL = "http://gxapp.iydsj.com/api/v14/login";
	private final static String logoutURL = "http://gxapp.iydsj.com/api/v6/user/logout";
	private final static String roomListURL = "http://gxapp.iydsj.com/api/v8/get/aboutrunning/list/";
	private final static String roomInfo = "http://gxapp.iydsj.com/api/v8/get/room/history/finished/record";

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
	
	public static JSONArray getRoomInfo(UserInfo userInfo) {
		
		
		
		
		
		JSONArray rooms;
		Map<String, String> header = getHeader();
		header.put("Content-Type", "application/json");
		header.put("uid", String.valueOf(userInfo.getUid()));
		header.put("token", userInfo.getToken());
		header.put("tokenSign", userInfo.getTokensign());
		String url = roomListURL + String.valueOf(userInfo.getUid()) +"/"+String.valueOf(userInfo.getUnid())+"/3";
		
		Map<String, String> result = sendGet(url, header);
		
		if (result.get("state").equals(String.valueOf(HTTP_OK))) {
			//Connect succeed
			try {
				JSONObject resJson = JSON.parseObject(result.get("result"));
				System.out.println("response = " + resJson.toString());

				int errorCode = (int) resJson.get("error");
				if (errorCode == 10000) {
					// Get room list succeed
					System.out.println("get room list succeed ");
					System.out.println(errorCode+"\n"+(String)resJson.get("message"));
					rooms = (JSONArray) resJson.get("data");
					return rooms;
				}else {
					// Get room list failed
					System.out.println("get room list failed ");
					System.out.println(errorCode+"\n"+(String)resJson.get("message"));
					return null;
				}
			}catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			//Connect Failed
			System.out.println("Connect Faild : " + result.get("state"));
		}
		return null;

	}

	public static JSONArray getRoomList(UserInfo userInfo) {
		JSONArray rooms;
		Map<String, String> header = getHeader();
		header.put("Content-Type", "application/json");
		header.put("uid", String.valueOf(userInfo.getUid()));
		header.put("token", userInfo.getToken());
		header.put("tokenSign", userInfo.getTokensign());
		String url = roomListURL + String.valueOf(userInfo.getUid()) +"/"+String.valueOf(userInfo.getUnid())+"/3";
		
		Map<String, String> result = sendGet(url, header);
		
		if (result.get("state").equals(String.valueOf(HTTP_OK))) {
			//Connect succeed
			try {
				JSONObject resJson = JSON.parseObject(result.get("result"));
				System.out.println("response = " + resJson.toString());

				int errorCode = (int) resJson.get("error");
				if (errorCode == 10000) {
					// Get room list succeed
					System.out.println("get room list succeed ");
					System.out.println(errorCode+"\n"+(String)resJson.get("message"));
					rooms = (JSONArray) resJson.get("data");
					return rooms;
				}else {
					// Get room list failed
					System.out.println("get room list failed ");
					System.out.println(errorCode+"\n"+(String)resJson.get("message"));
					return null;
				}
			}catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			//Connect Failed
			System.out.println("Connect Faild : " + result.get("state"));
		}
		return null;

	}

	public static boolean logout(UserInfo userInfo) {
		Map<String, String> header = getHeader();
		header.put("Content-Type", "application/json");
		header.put("token", userInfo.getToken());
		header.put("tokenSign", userInfo.getTokensign());
		header.put("uid", String.valueOf(userInfo.getUid()));

		Map<String, String> result = sendPost(logoutURL, header, null);

		if (result.get("state").equals(String.valueOf(HTTP_OK))) {
			//Connect succeed
			try {
				JSONObject resJson = JSON.parseObject(result.get("result"));
				System.out.println("response = " + resJson.toString());
				
				int errorCode = (int) resJson.get("error");
				if (errorCode == 10000) {
					// Logout succeed
					System.out.println("Logout scucceed ");
					System.out.println(errorCode+"\n"+(String)resJson.get("message"));	
					return true;
				}else {
					// Logout failed
					System.out.println("Logout failed ");
					System.out.println(errorCode+"\n"+(String)resJson.get("message"));
					return false;
				}
			}catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			//Connect Failed
			System.out.println("Connect Faild : " + result.get("state"));
		}
		return false;
	}
	
	public static UserInfo login(String userName,String password) {
		UserInfo userInfo =new UserInfo();
		Map<String, String> header = getHeader();
		String orig = userName+":"+password;
		String authorization = Base64.getEncoder().encodeToString(orig.getBytes(StandardCharsets.UTF_8));
		authorization = "Basic " + authorization;	
		header.put("Authorization", authorization);
		header.put("Content-Type", "application/json;charset=UTF-8");
		header.put("uid", "0");

		JSONObject body = new JSONObject(true);
		body.put("device_model", Config.getDeviceName());
		body.put("imei", Config.getImei());
		body.put("loginType", 1);
		body.put("mac_address", Config.getMacAddress());
		body.put("os_version", Config.getOsVersion());
		
		Map<String, String> result = sendPost(loginURL, header, body.toString());

		if (result.get("state").equals(String.valueOf(HTTP_OK))) {
			//Connect succeed

			try {
				JSONObject resJson = JSON.parseObject(result.get("result"));
				System.out.println("response = " + resJson.toString());
				
				int errorCode = (int) resJson.get("error");
				if (errorCode == 10000) {
					// Login succeed					
					userInfo.setLogined(true);
					System.out.println("Login Scucceed ");
					System.out.println(errorCode+"\n"+(String)result.get("message"));
					JSONObject dataJson = (JSONObject) resJson.get("data");
					userInfo.setUid((int)dataJson.get("uid"));
					userInfo.setUsername((String)dataJson.get("username"));
					userInfo.setName((String)dataJson.get("name"));
					userInfo.setUnid((int)dataJson.get("unid"));
					userInfo.setCampusName((String)dataJson.get("campusName"));
					userInfo.setSex((int)dataJson.get("sex"));
					userInfo.setIcon((String)dataJson.get("icon"));
					userInfo.setCampusId((String)dataJson.get("campusId"));
					userInfo.setDepart((String)dataJson.get("depart"));
					userInfo.setEnrollmentYear((int)dataJson.get("enrollmentYear"));
					userInfo.setNickname((String)dataJson.get("nickname"));
					userInfo.setRole((int)dataJson.get("role"));
					userInfo.setNameSecret((int)dataJson.get("nameSecret"));
					userInfo.setDepartmentSecret((int)dataJson.get("departmentSecret"));
					userInfo.setYearSecret((int)dataJson.get("yearSecret"));
					userInfo.setRegisted((boolean)dataJson.get("registed"));
					userInfo.setToken((String)dataJson.get("token"));
					userInfo.setAlias((String)dataJson.get("alias"));
					userInfo.setUserTag((String)dataJson.get("userTag"));
					userInfo.setDepartmentId((int)dataJson.get("departmentId"));
					userInfo.setInfoComplete((boolean)dataJson.get("infoComplete"));
					userInfo.setCompleteType((int)dataJson.get("completeType"));
					userInfo.setHasEnum((boolean)dataJson.get("hasEnum"));
					// TODO code blew cause a accident
					//userInfo.setHeight((float)dataJson.get("height"));
					//userInfo.setWeight((float)dataJson.get("weight"));
					userInfo.setBicon((String)dataJson.get("bicon"));
					userInfo.setPsign((String)dataJson.get("psign"));
				} else {
					// login failed
					// TODO Make a toast
					System.out.println("Login Faild ");
					System.out.println(errorCode+"\n"+(String)resJson.get("message"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			//Connect Failed
			System.out.println("Connect Faild : " + result.get("state"));
		}
		return userInfo;
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
			//System.out.println(responseCode +"==?" +HttpURLConnection.HTTP_OK);
			//System.out.println(responseCode == HttpURLConnection.HTTP_OK);

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
