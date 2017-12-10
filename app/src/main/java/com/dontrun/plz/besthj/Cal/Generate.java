package com.dontrun.plz.besthj.Cal;
import android.util.Log;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dontrun.plz.besthj.Utils.*;



public class Generate {

    static class LocationUtils {
        private static double EARTH_RADIUS = 6378.137;

        private static double rad(double d) {
            return d * Math.PI / 180.0;
        }

        /**
         * 通过经纬度获取距离(单位：米)
         * @param lat1
         * @param lng1
         * @param lat2
         * @param lng2
         * @return
         */
        static double getDistance(double lat1, double lng1, double lat2,
                                  double lng2) {
            double radLat1 = rad(lat1);
            double radLat2 = rad(lat2);
            double a = radLat1 - radLat2;
            double b = rad(lng1) - rad(lng2);
            double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                    + Math.cos(radLat1) * Math.cos(radLat2)
                    * Math.pow(Math.sin(b / 2), 2)));
            s = s * EARTH_RADIUS;
            s = Math.round(s * 10000d) / 10000d;
            s = s*1000;
            return s;
        }
    }

    private  static String isTestPoint(Map<String, Location> testPoint,double lat,double lng) {
    	for(String key:testPoint.keySet()) {
    		if(LocationUtils.getDistance(testPoint.get(key).lat,testPoint.get(key).lng,lat,lng)<50)
    			return key;
    	}
    	return null;
    }
   
   private static Set<String> isUsefulRount(Map<String, Location> testPoint,JSONArray allLocJson){
		Set<String> passPoints = new HashSet<String>();
		for(int i=0;i<allLocJson.size();i++) {
			JSONObject location = allLocJson.getJSONObject(i);
			String name = isTestPoint(testPoint,location.getDoubleValue("lat"),location.getDoubleValue("lng"));
			if(name!=null) {
				System.out.println(name);
				passPoints.add(name);
			}
		}
		if(passPoints.size()>=3) {
		  for(String str:passPoints){
		      Log.d("PassName",str);
          }
			return passPoints;
		}
		return null;
	}

   public static  PLTuple<Set<String>, JSONArray> getUsefulRount(UserInfo userInfo,Map<String, Location> testPoint){
	   Set<String>RoomIds = UserAction.getRoomList(userInfo);
	   if (RoomIds!=null) {
		   for (String RoomId : RoomIds) {
			   JSONArray roomersModelList = UserAction.getRoomersModelList(userInfo, RoomId);
			   if(null == roomersModelList)
			   	return null;
			   for (int i = 0; i < roomersModelList.size(); i++) {
				   JSONObject roomerModel = roomersModelList.getJSONObject(i);
				   if (roomerModel.getBooleanValue("finished")) {
					   JSONObject points = JSON.parseObject(roomerModel.get("points").toString());
					   JSONArray allLocJson = JSON.parseArray(points.getString("allLocJson"));
					   Set<String> passPoints = isUsefulRount(testPoint, allLocJson);
					   if (passPoints != null) {
						   return new PLTuple<Set<String>, JSONArray>(passPoints, allLocJson);
					   }
				   }
			   }
		   }
	   }
	   Log.d("PL","No match");
	   return null;
   }

	public static JSONArray getFivePointJSON(UserInfo userInfo,Map<String, Location> testPoint,Set<String> passPoints){
	   
	   JSONArray fivePoint = new JSONArray();
	   String flag = Config.getTimeStamp();
	   int id=1;
	   for(String pointName:passPoints) {
		   JSONObject point = new JSONObject(true);
		   point.put("flag", flag);
		   point.put("hasReward", false);
		   point.put("id", id);
		   if(id==2){
			   point.put("isFixed", 1);
		   }
		   else {
			   point.put("isFixed", 0);
		   }
		   point.put("isPass", true);
		   point.put("lat", testPoint.get(pointName).lat);
		   point.put("lon", testPoint.get(pointName).lng);
		   point.put("pointName", pointName);
		   //TODO 999
		   point.put("position", 999);
		   id++;
		   fivePoint.add(point);
	   }
	   if (fivePoint.size()<5){
		   for(String pointName:testPoint.keySet()){
				if(!passPoints.contains(pointName)){
					JSONObject point = new JSONObject(true);
					point.put("flag", flag);
					point.put("hasReward", false);
					point.put("id", id);
					point.put("isFixed", 0);
					point.put("isPass", false);
					point.put("lat", testPoint.get(pointName).lat);
					point.put("lon", testPoint.get(pointName).lng);
					point.put("pointName", pointName);
					point.put("position", 999);
					id++;
					fivePoint.add(point);
				}
			   if (fivePoint.size()>=5) {
					break;
			   }
		   }
	   }
//	   JSONObject fivePointJson = new JSONObject();
//	   fivePointJson.put("fivePointJson",fivePoint);
//	   fivePointJson.put("useZip",false);
	   return fivePoint;
   }


}
