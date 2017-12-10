package com.dontrun.plz.besthj.Utils;

import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.nio.charset.StandardCharsets;
import android.util.Base64;
import android.util.Log;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import com.dontrun.plz.besthj.Cal.BlackBox;
import com.dontrun.plz.besthj.Utils.*;

import java.util.UUID;


public class UserAction {
	

	//private final static String loginURL = "https://gxapp.iydsj.com/api/v14/login";
	//private final static String logoutURL = "https://gxapp.iydsj.com/api/v6/user/logout";
	private final static String loginURL = "http://gxapp.iydsj.com/api/v14/login";
	private final static String logoutURL = "http://gxapp.iydsj.com/api/v6/user/logout";
	private final static String roomListURL = "http://gxapp.iydsj.com/api/v8/get/aboutrunning/list/";
	private final static String roomInfoURL = "http://gxapp.iydsj.com/api/v8/get/room/history/finished/record";
	private final static String getTestPointsURL = "http://gxapp.iydsj.com/api/v18/get/1/distance/1000";
	private final static String upLoadURL = "http://gxapp.iydsj.com//api/v14/runnings/save/record";


	@RequiresApi(api = Build.VERSION_CODES.N)
	public static boolean uploadRecord(UserInfo userInfo,
									JSONArray allLocJson,
									JSONArray fivePointJson) {
		Map<String, String> header = Net.getHeader();
		header.put("uid", String.valueOf(userInfo.getUid()));
		header.put("Content-Type", "application/json;charset=UTF-8");
		header.put("token", userInfo.getToken());
		header.put("tokenSign", userInfo.getTokenSign(header.get("timeStamp")));



		JSONArray allLoc = allLocJson;
		Long oldFlag = allLoc.getJSONObject(0).getLong("flag");
		Long totalTime= allLoc.getJSONObject(allLoc.size()-1).getLong("totalTime");
		Long newFlag= System.currentTimeMillis() - totalTime*1000;
		Long deltaTime = newFlag - oldFlag;
		Long currentTime = newFlag - oldFlag;
		for(int i = 0;i<fivePointJson.size();i++){
			JSONObject fpoint = fivePointJson.getJSONObject(i);
			fpoint.put("flag",newFlag);
		}

		JSONArray speedPerTenSec = new JSONArray();
		JSONArray stepsPerTenSec = new JSONArray();
		SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		float currentDis =0;
		int id=1;
		String newGainTime;
		Long newGainFlag = 0L;
		Log.d("allLoc","size="+allLoc.size()+allLoc.toJSONString());
		for(int i = 0;i<allLoc.size();i++){
			JSONObject loc = allLoc.getJSONObject(i);
			Log.d("loc",loc.toString());
			loc.put("flag",newFlag);
			float distance = loc.getFloat("totalDis") - currentDis;
			currentDis = loc.getFloat("totalDis");
			try {
				Date oldGainTime = format.parse(loc.getString("gainTime"));
				newGainFlag = deltaTime + oldGainTime.getTime();
				newGainTime = format.format(newGainFlag);
				loc.put("gainTime",newGainTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			JSONObject stepJson = new JSONObject(true);
			JSONObject speedJson = new JSONObject(true);
			stepJson.put("avgDiff",(float)(Math.random()*5)+10);
			stepJson.put("beginTime",currentTime);
			stepJson.put("endTime",newGainFlag);
			stepJson.put("flag",newFlag);
			stepJson.put("id",id);
			stepJson.put("maxDiff",(float)(Math.random()*5)+15);
			stepJson.put("maxDiff",(float)(Math.random()*3)+8);
			stepJson.put("stepsNum",(int)(distance/0.8));

			speedJson.put("beginTime",currentTime);
			speedJson.put("distance",distance);
			speedJson.put("flag",newFlag);
			speedJson.put("id",id);
			speedPerTenSec.add(speedJson);
			stepsPerTenSec.add(stepJson);
			currentTime = newGainFlag;
			id++;
		}
		JSONObject body = new JSONObject(true);

		body.put("getPrize",false);
		body.put("uuid",userInfo.getUuid());
		body.put("uid",userInfo.getUid());
		body.put("sportType",1);
		body.put("totalTime",totalTime);
		body.put("totalDis",(int)(currentDis));
		body.put("speed",(int)(1000/(currentDis/totalTime)/60*1000));
		body.put("startTime",newFlag);
		body.put("stopTime",currentTime);
		body.put("totalSteps",(int)(currentDis/1.2));
		body.put("avgStepFreq",(int)(currentDis/1.2/totalTime*60));
		body.put("selectedUnid",userInfo.getUnid());
		body.put("selDistance",1000);
		body.put("calorie",getCalorie(currentDis));
		body.put("complete",true);
		body.put("unCompleteReason",0);
		body.put("status",0);

		Map<String, Object> mapParams1 = body.getInnerMap();
		body.put("signature",BlackBox.getReturn(mapParams1, Config.md5_sign_salt_run));
		JSONObject a2 = new JSONObject(true);
		a2.put("allLocJson",allLoc);
		a2.put("useZip",false);
		body.put("allLocJson", a2);
		JSONObject f2 = new JSONObject(true);
		f2.put("fivePointJson",fivePointJson);
		f2.put("useZip",false);
		body.put("fivePointJson", f2);

		body.put("speedPerTenSec",speedPerTenSec);
		body.put("stepsPerTenSec",stepsPerTenSec);
		body.put("totalSteps",(int)(currentDis/1.2));
		body.put("selectedUnid",userInfo.getUnid());
		body.put("selDistance",1000);
		body.put("isUpload",false);
		body.put("more",true);
		body.put("roomId",0);

		Map<String, String> result = Net.sendPost(upLoadURL, header, body.toString());
		if (result.get("state").equals(String.valueOf(Net.HTTP_OK))) {
			//Connect succeed

			try {
				JSONObject resJson = JSON.parseObject(result.get("result"));
				int errorCode = (int) resJson.get("error");
				if (errorCode == 10000) {
					// Get RoomInfo succeed
					Log.d("uploadRecord","upload record succeed");
					Log.d("uploadRecord",errorCode+"\n"+(String)result.get("message"));
					JSONObject dataJson = (JSONObject) resJson.get("data");
					JSONArray roomersModelList = (JSONArray) dataJson.get("roomersModelList");
					return true;
				} else {
					// Get RoomInfo  failed
					// TODO Make a toast
					Log.d("getRoomersModelList","upload record Faild ");
					Log.d("getRoomersModelList",errorCode+"\n"+(String)resJson.get("message"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		else {
			//Connect Failed
			Log.d("getRoomersModelList","Connect Faild : " + result.get("state"));
		}
		return false;
	}

	public static JSONArray getRoomersModelList(UserInfo userInfo,String roomId) {
		
		Map<String, String> header = Net.getHeader();
		header.put("Content-Type", "application/json;charset=UTF-8");
		header.put("uid", String.valueOf(userInfo.getUid()));
		header.put("token", userInfo.getToken());
		header.put("tokenSign", userInfo.getTokenSign(header.get("timeStamp")));
		JSONObject body = new JSONObject(true);
		body.put("roomId", roomId);
		
		
		Map<String, String> result = Net.sendPost(roomInfoURL, header, body.toString());			
		if (result.get("state").equals(String.valueOf(Net.HTTP_OK))) {
			//Connect succeed

			try {
				JSONObject resJson = JSON.parseObject(result.get("result"));
					int errorCode = (int) resJson.get("error");
					if (errorCode == 10000) {
						// Get RoomInfo succeed					
						Log.d("getRoomersModelList","Get RoomInfo succeed");
						Log.d("getRoomersModelList",errorCode+"\n"+(String)resJson.get("message"));
						JSONObject dataJson = (JSONObject) resJson.get("data");
						JSONArray roomersModelList = (JSONArray) dataJson.get("roomersModelList");
						return roomersModelList;
					} else {
						// Get RoomInfo  failed
						// TODO Make a toast
						Log.d("getRoomersModelList","Get RoomInfo Faild ");
						Log.d("getRoomersModelList",errorCode+"\n"+(String)resJson.get("message"));
					}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		}
		else {
			//Connect Failed
			Log.d("getRoomersModelList","Connect Faild : " + result.get("state"));
		}
		return null;

	}

	public static Set<String> getRoomList(UserInfo userInfo) {
		Set<String> roomIds=new HashSet<String>();
		Map<String, String> header = Net.getHeader();
		
		header.put("Content-Type", "application/json");
		header.put("uid", String.valueOf(userInfo.getUid()));
		header.put("token", userInfo.getToken());
		header.put("tokenSign", userInfo.getTokenSign(header.get("timeStamp")));
		String url = roomListURL + String.valueOf(userInfo.getUid()) +"/"+String.valueOf(userInfo.getUnid())+"/3";
		
		Map<String, String> result = Net.sendGet(url, header);
		
		if (result.get("state").equals(String.valueOf(Net.HTTP_OK))) {
			//Connect succeed
			try {
				JSONObject resJson = JSON.parseObject(result.get("result"));

				int errorCode = (int) resJson.get("error");
				if (errorCode == 10000) {
					// Get room list succeed
					Log.d("getRoomList","get room list succeed ");
					Log.d("getRoomList",errorCode+"\n"+(String)resJson.get("message"));
					JSONArray rooms = (JSONArray) resJson.get("data");
					
					for(int i = 0;i<rooms.size();i++) {
						JSONObject room = (JSONObject) rooms.get(i);
						Log.d("Rooms ID",room.getString("roomId"));
						roomIds.add(room.getString("roomId"));
					}
					return roomIds;
				}else {
					// Get room list failed
					Log.d("getRoomList","get room list failed ");
					Log.d("getRoomList",errorCode+"\n"+(String)resJson.get("message"));
					return null;
				}
			}catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			//Connect Failed
			Log.d("getRoomList","Connect Faild : " + result.get("state"));
		}
		return null;
	}

	public static boolean logout(UserInfo userInfo) {
		Map<String, String> header = Net.getHeader();
		header.put("Content-Type", "application/json");
		header.put("token", userInfo.getToken());
		header.put("tokenSign", userInfo.getTokenSign(header.get("timeStamp")));
		header.put("uid", String.valueOf(userInfo.getUid()));

		Map<String, String> result = Net.sendPost(logoutURL, header, null);

		if (result.get("state").equals(String.valueOf(Net.HTTP_OK))) {
			//Connect succeed
			try {
				JSONObject resJson = JSON.parseObject(result.get("result"));

				int errorCode = (int) resJson.get("error");
				if (errorCode == 10000) {
					// Logout succeed
					Log.d("Logout","succeed");
					Log.d("Logout",errorCode+":"+resJson.getString("message"));
					return true;
				}else {
					// Logout failed
					Log.d("Logout","failed ");
					Log.d("Logout",errorCode+":"+resJson.getString("message"));
					return false;
				}
			}catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			//Connect Failed
			Log.d("Logout","Connect Faild : " + result.get("state"));
		}
		return false;
	}
	
	public static UserInfo login(String userName, String password) {
		UserInfo userInfo =new UserInfo();
		Map<String, String> header = Net.getHeader();
		String orig = userName+":"+password;
		String authorization = Base64.encodeToString(orig.getBytes(),Base64.DEFAULT);
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
		
		Map<String, String> result = Net.sendPost(loginURL, header, body.toString());

		if (result.get("state").equals(String.valueOf(Net.HTTP_OK))) {
			//Connect succeed

			try {
				JSONObject resJson = JSON.parseObject(result.get("result"));

				int errorCode = (int) resJson.get("error");
				if (errorCode == 10000) {
					// Login succeed					
					userInfo.setLogined(true);
                    Log.d("Login","Scucceed ");
                    Log.d("Login",errorCode+":"+(String)result.get("message"));
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
                    Log.d("Login","Faild ");
                    Log.d("Login",errorCode+":"+(String)resJson.get("message"));
                    return null;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			//Connect Failed
            Log.d("Login","Connect Faild : " + result.get("state"));
		}
		return userInfo;
	}

	public static Map<String, Location> getTestPoints(UserInfo userInfo){
		String str = "http://gxapp.iydsj.com/api/v18/get/1/distance/100005df15504f394eab8dd3ab8180006a83";
		Map<String, String> header = Net.getHeader();
		header.put("Content-Type", "application/json;charset=UTF-8");
		header.put("uid", String.valueOf(userInfo.getUid()));
		header.put("token", userInfo.getToken());
		header.put("tokenSign", userInfo.getTokenSign(header.get("timeStamp")));
		JSONObject body = new JSONObject(true);
		body.put("latitude",Config.getLatitude());
		body.put("selectedUnid",userInfo.getUnid());
		body.put("longitude",Config.getLongitude());
		body.put("sign", BlackBox.getMD5(str).toUpperCase());
		Map<String, Location> testPoints= new HashMap<String, Location>();
		Map<String, String> result = Net.sendPost(getTestPointsURL, header, body.toString());
		if (result.get("state").equals(String.valueOf(Net.HTTP_OK))) {
			//Connect succeed
			try {
				JSONObject resJson = JSON.parseObject(result.get("result"));
				Log.d("getTestPoints",resJson.toString());

				int errorCode = (int) resJson.get("error");
				if (errorCode == 10000) {
					// Get room list succeed
					Log.d("getTestPoints","get test points succeed ");
					Log.d("getTestPoints",errorCode+"\n"+(String)resJson.get("message"));
					JSONObject data =  resJson.getJSONObject("data");
					JSONArray pointsResModels = data.getJSONArray("pointsResModels");
					for(int i = 0;i<pointsResModels.size();i++) {
						JSONObject pointsResModel =  pointsResModels.getJSONObject(i);
						testPoints.put(pointsResModel.getString("pointName"),new Location(pointsResModel.getDouble("lat"),pointsResModel.getDouble("lon")));
						Log.d("TestPointInfo",pointsResModel.getString("pointName")+","+pointsResModel.getDouble("lat")+","+pointsResModel.getDouble("lon"));
					}
					return testPoints;
				}else {
					// Get room list failed
					Log.d("getTestPoints","get test points failed ");
					Log.d("getTestPoints",errorCode+"\n"+(String)resJson.get("message"));
					return null;
				}
			}catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			//Connect Failed
			Log.d("getTestPoints","Connect Faild : " + result.get("state"));
		}
		return  null;
	}
	private static int getCalorie(float f) {
		float f2 = 50.0f;
		//f2 = (float) (sex == 1 ? 60 : 50);
		return Math.round(f2 * (1.036f * f));
	}

}
