package com.dontrun.plz.besthj;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class BlackBox {
	BlackBox() {
    }

	private final static String EQUAL_SIGN = "=";
	private final static String PARAMETERS_SEPARATOR = "&";

    static String getReturn(Map<String, Object> map, String salt) {
        return getMD5(mixSalt(cleanSource((Map<String, Object>) map), salt));
    }
    
     static String getMD5(String str) {
        int i = 0;
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            char[] toCharArray = str.toCharArray();
            byte[] bArr = new byte[toCharArray.length];
            for (int i2 = 0; i2 < toCharArray.length; i2++) {
                bArr[i2] = (byte) toCharArray[i2];
            }
            byte[] digest = instance.digest(bArr);
            StringBuilder stringBuilder = new StringBuilder();
            int length = digest.length;
            while (i < length) {
                int i3 = digest[i] & 255;
                if (i3 < 16) {
                    stringBuilder.append("0");
                }
                stringBuilder.append(Integer.toHexString(i3));
                i++;
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
    }


    //��ȥ�����е��޹���
    private static Map<String, Object> cleanSource(Map<String, Object> map) {
        Map<String, Object> hashMap = new HashMap<String, Object>();
        if (map == null || map.size() <= 0) {
            return hashMap;
        }
        //int i=0;
        for (String str : map.keySet()) {
            Object obj = map.get(str);
            if (!(obj == null || str.equalsIgnoreCase("signature"))) {
                hashMap.put(str, obj);
                
              //  System.out.println(i++ + str+"----"+obj);

            }
        }
        return hashMap;
    }

    private static String mixSalt(Map<String, Object> map, String salt) {
        List<String> keysList = new ArrayList<String>(map.keySet());
        Collections.sort(keysList);
        String ret = "";
        int i = 0;
        while (i < keysList.size()) {
            String key = (String) keysList.get(i);        
            //System.out.println(i + key);

            Object obj = map.get(key);
            key = i == keysList.size() + -1 ? ret + key + EQUAL_SIGN + obj : ret + key + EQUAL_SIGN + obj + PARAMETERS_SEPARATOR;
            i++;
            ret = key;
        }
        //System.out.println( ret + salt);
        return ret + salt;
    }
}