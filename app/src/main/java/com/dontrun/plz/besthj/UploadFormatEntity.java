package com.dontrun.plz.besthj;

/**
 * Created by Bubble on 2017/11/25.
 * Format the Upload data
 * cc test
 */


public class UploadFormatEntity {
    private String allLocJson;
    private int avgStepFreq;
    private int calorie;
    private boolean complete;
    private String fivePointJson;
    private boolean getPrize;
    private boolean isUpload;
    private boolean more = true;
    private String prizeName;
    private String recordUrl;
    private int roomId;
    private int selDistance;
    private int selectedUnid;
    private String signature;
    private long speed;
    private int sportType;
    private long startTime;
    private int status;
    private long stopTime;
    private long totalDis;
    private int totalSteps;
    private long totalTime;
    private int uid;
    private int unCompleteReason;
    private String uuid;

    public int compareTo(UploadFormatEntity uploadFormatEntity) {
        return getStartTime() > uploadFormatEntity.getStartTime() ? -1 : getStartTime() < uploadFormatEntity.getStartTime() ? 1 : 0;
    }

    public String getAllLocJson() {
        return this.allLocJson;
    }

    int getAvgStepFreq() {
        return this.avgStepFreq;
    }

    int getCalorie() {
        return this.calorie;
    }

    boolean getComplete() {
        return this.complete;
    }

    String getFivePointJson() {
        return this.fivePointJson;
    }

    boolean getIsUpload() {
        return this.isUpload;
    }

    String getPrizeName() {
        return this.prizeName;
    }

    String getRecordUrl() {
        return this.recordUrl;
    }

    int getRoomId() {
        return this.roomId;
    }

    int getSelDistance() {
        return this.selDistance;
    }

    int getSelectedUnid() {
        return this.selectedUnid;
    }

    public String getSignature() {
        return this.signature;
    }

    long getSpeed() {
        return this.speed;
    }


    int getSportType() {
        return this.sportType;
    }

    long getStartTime() {
        return this.startTime;
    }

    int getStatus() {
        return this.status;
    }


    long getStopTime() {
        return this.stopTime;
    }

    long getTotalDis() {
        return this.totalDis;
    }

    int getTotalSteps() {
        return this.totalSteps;
    }

    long getTotalTime() {
        return this.totalTime;
    }

    int getUid() {
        return this.uid;
    }

    int getUnCompleteReason() {
        return this.unCompleteReason;
    }

    String getUuid() {
        return this.uuid;
    }

    boolean hasMore() {
        return this.more;
    }

    boolean isComplete() {
        return this.complete;
    }

    boolean isGetPrize() {
        return this.getPrize;
    }

    boolean isMore() {
        return this.more;
    }

    void setAllLocJson(String str) {
        this.allLocJson = str;
    }

    void setAvgStepFreq(int i) {
        this.avgStepFreq = i;
    }

    void setCalorie(int i) {
        this.calorie = i;
    }

    void setComplete(boolean z) {
        this.complete = z;
    }

    void setFivePointJson(String str) {
        this.fivePointJson = str;
    }

    void setGetPrize(boolean z) {
        this.getPrize = z;
    }

     void setIsUpload(boolean z) {
        this.isUpload = z;
    }

    void setMore(boolean z) {
        this.more = z;
    }

    void setPrizeName(String str) {
        this.prizeName = str;
    }

    void setRecordUrl(String str) {
        this.recordUrl = str;
    }

    void setRoomId(int i) {
        this.roomId = i;
    }

    void setSelDistance(int i) {
        this.selDistance = i;
    }

    void setSelectedUnid(int i) {
        this.selectedUnid = i;
    }

    void setSignature(String str) {
        this.signature = str;
    }

    void setSpeed(double d) {
        //this.speed = Math.round(1000.0d * d);
        this.speed = Math.round(d);

    }


    void setSportType(int i) {
        this.sportType = i;
    }

    void setStartTime(long j) {
        this.startTime = j;
    }

    void setStatus(int i) {
        this.status = i;
    }

    void setStopTime(long j) {
        this.stopTime = j;
    }

    void setTotalDis(double d) {
        //this.totalDis = Math.round(1000.0d * d);
        this.totalDis = Math.round( d);

    }

    void setTotalSteps(int i) {
        this.totalSteps = i;
    }

    void setTotalTime(long j) {
        this.totalTime = j;
    }

    void setUid(int i) {
        this.uid = i;
    }

    void setUnCompleteReason(int i) {
        this.unCompleteReason = i;
    }

    void setUuid(String str) {
        this.uuid = str;
    }

    public String toString() {
        return "UploadFormatEntity{allLocJson='" + this.allLocJson + '\'' + ", sportType=" + this.sportType + ", totalTime=" + this.totalTime + ", totalDis=" + this.totalDis + ", speed=" + this.speed + ", startTime=" + this.startTime + ", stopTime=" + this.stopTime + ", fivePointJson='" + this.fivePointJson + '\'' + ", complete=" + this.complete + ", selDistance=" + this.selDistance + ", unCompleteReason=" + this.unCompleteReason + ", recordUrl='" + this.recordUrl + '\'' + ", isUpload=" + this.isUpload + ", getPrize=" + this.getPrize + ", prizeName='" + this.prizeName + '\'' + ", status=" + this.status + ", uuid='" + this.uuid + '\'' + ", uid=" + this.uid + ", signature='" + this.signature + '\'' + ", roomId=" + this.roomId + ", more=" + this.more + ", totalSteps=" + this.totalSteps + ", avgStepFreq=" + this.avgStepFreq + ", stepsPerTenSec=" +  ", speedPerTenSec="  + ", selectedUnid=" + this.selectedUnid + '}';
    }
}