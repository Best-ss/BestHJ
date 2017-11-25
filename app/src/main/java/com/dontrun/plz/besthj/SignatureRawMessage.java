package com.dontrun.plz.besthj;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bubble on 2017/11/25.
 *
 */

public class SignatureRawMessage {
    private int avgStepFreq;
    private int calorie;
    private boolean complete;
    private boolean getPrize;
    private int selDistance;
    private int selectedUnid;
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

    public SignatureRawMessage(UploadFormatEntity uploadFormatEntity) {
        this.sportType = uploadFormatEntity.getSportType();
        this.totalTime = uploadFormatEntity.getTotalTime();
        this.totalDis = uploadFormatEntity.getTotalDis();
        this.speed = uploadFormatEntity.getSpeed();
        this.startTime = uploadFormatEntity.getStartTime();
        this.stopTime = uploadFormatEntity.getStopTime();
        this.complete = uploadFormatEntity.getComplete();
        this.selDistance = uploadFormatEntity.getSelDistance();
        this.unCompleteReason = uploadFormatEntity.getUnCompleteReason();
        this.getPrize = uploadFormatEntity.isGetPrize();
        this.status = uploadFormatEntity.getStatus();
        this.uuid = uploadFormatEntity.getUuid();
        this.uid = uploadFormatEntity.getUid();
        this.avgStepFreq = uploadFormatEntity.getAvgStepFreq();
        this.totalSteps = uploadFormatEntity.getTotalSteps();
        this.selectedUnid = uploadFormatEntity.getSelectedUnid();
        this.calorie = uploadFormatEntity.getCalorie();
    }


    public Map<String, Object> getMapParams() {
        Exception e;
        int i = 0;
        Field[] declaredFields = getClass().getDeclaredFields();
        if (declaredFields == null || declaredFields.length == 0) {
            return Collections.emptyMap();
        }
        Map<String, Object> hashMap = new HashMap<>();
        try {
            for (Field field : declaredFields) {
                field.setAccessible(true);
                hashMap.put(field.getName(), String.valueOf(field.get(this)));
            }
        } catch (IllegalAccessException e2) {
            e = e2;
            e.printStackTrace();
            return hashMap;
        } catch (IllegalArgumentException e3) {
            e = e3;
            e.printStackTrace();
            return hashMap;
        }
        return hashMap;
    }
}
