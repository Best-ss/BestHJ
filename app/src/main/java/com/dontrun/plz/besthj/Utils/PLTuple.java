package com.dontrun.plz.besthj.Utils;

/**
 * Created by Bubble on 2017/12/9.
 */


public   class PLTuple<Set,JSONArray>{
    public  Set passPoints;
    public  JSONArray allLocJson;
    public PLTuple(Set passPoints,JSONArray allLocJson) {
        this.passPoints =passPoints;
        this.allLocJson = allLocJson;
    }
}