package com.example.samsung.jomoyeo1.dbconnection;

import org.json.JSONArray;
import org.json.JSONObject;

public class HttpRequestResult {

    private int resultCode;
    private JSONObject resultJson;
    private JSONArray resultJsonArray;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public JSONObject getResultJson() {
        return resultJson;
    }

    public void setResultJson(JSONObject resultJson) {
        this.resultJson = resultJson;
    }

    public JSONArray getResultJsonArray() {
        return resultJsonArray;
    }

    public void setResultJsonArray(JSONArray resultJsonArray) {
        this.resultJsonArray = resultJsonArray;
    }

    @Override
    public String toString() {
        return "HttpRequestResult{" +
                "resultCode=" + resultCode +
                ", resultJson=" + resultJson +
                ", resultJsonArray=" + resultJsonArray +
                '}';
    }
}