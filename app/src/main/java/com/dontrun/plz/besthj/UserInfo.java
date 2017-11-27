package com.dontrun.plz.besthj;

import java.util.HashMap;
import java.util.Map;

public class UserInfo {

    private int uid;
    private String username;
    private String name;
    private int unid;
    private String campusName;
    private int sex;
    private String icon;
    private String campusId;
    private String depart;
    private int enrollmentYear;
    private String nickname;
    private int role;
    private int nameSecret;    
    private int departmentSecret;
    private int yearSecret;
    private boolean registed;
    private String token;
    private String alias;
    private String userTag;
    private int departmentId;
    private boolean infoComplete;
    private int completeType;
    private boolean hasEnum;
    private float height;
    private float weight;
    private String bicon;
    private String psign;
    private boolean logined;
    public UserInfo() {
    	logined=false;
	}
    
    public boolean isLogined() {
		return logined;
	}
	public void setLogined(boolean logined) {
		this.logined = logined;
	}
	
	public String getTokensign() {
        Map<String, Object> hashMap= new HashMap<String, Object>();
        hashMap.put("timeStamp", Config.getTimeStamp());
        hashMap.put("token", token);
        hashMap.put("uid", uid);
        return BlackBox.getReturn(hashMap, Config.md5_sign_salt);
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getUnid() {
		return unid;
	}
	public void setUnid(int unid) {
		this.unid = unid;
	}
	public String getCampusName() {
		return campusName;
	}
	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getCampusId() {
		return campusId;
	}
	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getDepart() {
		return depart;
	}
	public void setDepart(String depart) {
		this.depart = depart;
	}
	public int getEnrollmentYear() {
		return enrollmentYear;
	}
	public void setEnrollmentYear(int enrollmentYear) {
		this.enrollmentYear = enrollmentYear;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	public int getNameSecret() {
		return nameSecret;
	}
	public void setNameSecret(int nameSecret) {
		this.nameSecret = nameSecret;
	}
	public int getDepartmentSecret() {
		return departmentSecret;
	}
	public void setDepartmentSecret(int departmentSecret) {
		this.departmentSecret = departmentSecret;
	}
	public int getYearSecret() {
		return yearSecret;
	}
	public void setYearSecret(int yearSecret) {
		this.yearSecret = yearSecret;
	}
	public boolean isRegisted() {
		return registed;
	}
	public void setRegisted(boolean registed) {
		this.registed = registed;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getUserTag() {
		return userTag;
	}
	public void setUserTag(String userTag) {
		this.userTag = userTag;
	}
	public int getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}
	public boolean isInfoComplete() {
		return infoComplete;
	}
	public void setInfoComplete(boolean infoComplete) {
		this.infoComplete = infoComplete;
	}
	public int getCompleteType() {
		return completeType;
	}
	public void setCompleteType(int completeType) {
		this.completeType = completeType;
	}
	public boolean isHasEnum() {
		return hasEnum;
	}
	public void setHasEnum(boolean hasEnum) {
		this.hasEnum = hasEnum;
	}
	public float getHeight() {
		return height;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	public float getWeight() {
		return weight;
	}
	public void setWeight(float weight) {
		this.weight = weight;
	}
	public String getBicon() {
		return bicon;
	}
	public void setBicon(String bicon) {
		this.bicon = bicon;
	}
	public String getPsign() {
		return psign;
	}
	public void setPsign(String psign) {
		this.psign = psign;
	}
}
