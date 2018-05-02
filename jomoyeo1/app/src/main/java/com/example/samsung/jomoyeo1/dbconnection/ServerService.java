package com.example.samsung.jomoyeo1.dbconnection;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

public class ServerService {
    private String log = "Log - "+this.getClass().getSimpleName()+"";
    private final String basicDomain ="http://172.30.1.30:8080";
    public Context mContext;

    public ServerService(Context context) {
        this.mContext = context;
    }

    AsyncTaskActivity asyncTaskActivity = new AsyncTaskActivity();

    /* 스케쥴 불러오기 */
    public HttpRequestResult selectSchedule(String id){
        HttpRequestResult requestResult = null;
        HashMap<String,String> basic = new HashMap<>();
        HashMap<String,String> params = new HashMap<>();

        basic.put("url",basicDomain+"/schedule_select_api.do?");
        basic.put("method", "GET");

        params.put("id",id);

        try {
            requestResult = asyncTaskActivity.execute(basic,params).get();
        }catch (Exception e){
            e.printStackTrace();
        }
        return requestResult;
    }


    /* 스케쥴 존재하는지 검사 (타이틀로) */
    public HttpRequestResult selectScheduleTitle(String id, String x, String y){
        HttpRequestResult requestResult = null;
        HashMap<String,String> basic = new HashMap<>();
        HashMap<String,String> params = new HashMap<>();

        basic.put("url",basicDomain+"/schedule_select_title_api.do?");
        basic.put("method", "GET");

        params.put("id",id);
        params.put("x",x);
        params.put("y",y);

        try {
            requestResult = asyncTaskActivity.execute(basic,params).get();
        }catch (Exception e){
            e.printStackTrace();
        }
        return requestResult;
    }

    /* 스케쥴 추가 */
    public HttpRequestResult insertSchedule(String id, String scheduleTitle, String x, String y){
        HttpRequestResult requestResult = null;
        HashMap<String,String> basic = new HashMap<>();
        HashMap<String,String> params = new HashMap<>();

        basic.put("url",basicDomain+"/schedule_insert_api.do?");
        basic.put("method", "PUT");

        params.put("id",id);
        params.put("scheduleTitle",scheduleTitle);
        params.put("x",x+"");
        params.put("y",y+"");

        try {
            requestResult = asyncTaskActivity.execute(basic,params).get();
        }catch (Exception e){
            e.printStackTrace();
        }
        return requestResult;
    }

    /* 스케쥴 삭제 */
    public HttpRequestResult deleteSchedule(String id, String x, String y){
        HttpRequestResult requestResult = null;
        HashMap<String,String> basic = new HashMap<>();
        HashMap<String,String> params = new HashMap<>();

        basic.put("url",basicDomain+"/schedule_delete_api.do?");
        basic.put("method", "GET");

        params.put("id",id);
        params.put("x",x+"");
        params.put("y",y+"");

        try {
            requestResult = asyncTaskActivity.execute(basic,params).get();
        }catch (Exception e){
            e.printStackTrace();
        }
        return requestResult;
    }

    public HttpRequestResult getMemberInfo(String id, String pwd){
        HttpRequestResult requestResult = null;
        HashMap<String,String> basic = new HashMap<>();
        HashMap<String,String> params = new HashMap<>();

        basic.put("url",basicDomain+"/login_api.do?");
        basic.put("method","GET");

        params.put("id",id);
        params.put("pwd",pwd);

        try {
            requestResult = asyncTaskActivity.execute(basic,params).get();
        }catch (Exception e){
            e.printStackTrace();
        }

        Log.d(log,requestResult.toString());
        return requestResult;
    }

    public HttpRequestResult joinMemberInfo(String id, String pwd, String addr, String tel){
        HttpRequestResult requestResult = null;
        HashMap<String,String> basic = new HashMap<>();
        HashMap<String,String> params = new HashMap<>();

        basic.put("url",basicDomain+"/join_api.do?");
        basic.put("method","GET");

        params.put("id",id);
        params.put("pwd",pwd);
        params.put("addr",addr);
        params.put("tel",tel);

        try {
            requestResult = asyncTaskActivity.execute(basic,params).get();
        }catch (Exception e){
            e.printStackTrace();
        }

        Log.d(log,requestResult.toString());
        return requestResult;
    }

    public HttpRequestResult duplicateCheckService(String id){
        HttpRequestResult requestResult = null;
        HashMap<String,String> basic = new HashMap<>();
        HashMap<String,String> params = new HashMap<>();

        basic.put("url",basicDomain+"/idCheck_api.do?");
        basic.put("method","GET");

        params.put("id",id);

        try {
            requestResult = asyncTaskActivity.execute(basic,params).get();
        }catch (Exception e){
            e.printStackTrace();
        }

        Log.d(log,requestResult.toString());
        return requestResult;
    }

    public HttpRequestResult insertBoardService(String id, String title, String view){
        HttpRequestResult requestResult = null;
        HashMap<String,String> basic = new HashMap<>();
        HashMap<String,String> params = new HashMap<>();

        basic.put("url",basicDomain+"/board_api.do?");
        basic.put("method","PUT");

        params.put("writer",id);
        params.put("title",title);
        params.put("view",view);

        try {
            requestResult = asyncTaskActivity.execute(basic,params).get();
        }catch (Exception e){
            e.printStackTrace();
        }

        Log.d(log,requestResult.toString());
        return requestResult;
    }

    // 게시글 목록을 받아오는 메소드
    public HttpRequestResult selectBoardList(){
        HttpRequestResult requestResult = null;
        HashMap<String,String> basic = new HashMap<>();
        HashMap<String,String> params = new HashMap<>();

        basic.put("url",basicDomain+"/boardView_api.do?");
        basic.put("method","GET");

        try {
            requestResult = asyncTaskActivity.execute(basic,params).get();
        }catch (Exception e){
            e.printStackTrace();
        }

//        //json으로 list가 들어가야함.
//        Log.d(log,requestResult.toString());
        return requestResult;
    }

    // 게시글을 삭제하는 메소드
    public HttpRequestResult deleteBoard(String writer, String date){
        HttpRequestResult requestResult = null;
        HashMap<String,String> basic = new HashMap<>();
        HashMap<String,String> params = new HashMap<>();

        basic.put("url",basicDomain+"/deleteBoard_api.do?");
        basic.put("method","PUT");

        params.put("writer", writer);
        params.put("date", date);
        Log.d("date는?", date);
        try {
            requestResult = asyncTaskActivity.execute(basic,params).get();
        }catch (Exception e){
            e.printStackTrace();
        }

//        //json으로 list가 들어가야함.
//        Log.d(log,requestResult.toString());
        return requestResult;
    }

    //modifyBoard
    /* 게시글을 수정하는 메소드 */
    public HttpRequestResult modifyBoard(String number, String title, String view) {
        HttpRequestResult requestResult = null;
        HashMap<String, String> basic = new HashMap<>();
        HashMap<String, String> params = new HashMap<>();

        basic.put("url", basicDomain + "/modifyBoard_api.do?");
        basic.put("method", "PUT");

        params.put("title", title);
        params.put("view", view);
        params.put("number", number);
        try {
            requestResult = asyncTaskActivity.execute(basic, params).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestResult;
    }

}