package com.example.samsung.jomoyeo.server;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

public class ServerService {

    private String log = "Log - "+this.getClass().getSimpleName()+"";
    private final String basicDomain ="http://192.168.0.102:8080";
    public Context mContext;

    public ServerService(Context context) {
        this.mContext = context;
    }

    AsyncTaskActivity asyncTaskActivity = new AsyncTaskActivity();

    /* 참여한 방 목록 불러오기 */
    public HttpRequestResult selectAttendRoomList(String attend_id){
        HttpRequestResult requestResult = null;
        HashMap<String,String> basic = new HashMap<>();
        HashMap<String,String> params = new HashMap<>();

        basic.put("url",basicDomain+"/attend_room_list_api.do?");
        basic.put("method", "GET");
        params.put("attend_id", attend_id);
        try {
            requestResult = asyncTaskActivity.execute(basic,params).get();
            //json으로 list가 들어가야함.
            Log.d("출력",requestResult.toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return requestResult;
    }

    /* 방장 탈퇴시 방에 있는 모든 유저 탈퇴 시킴. */
    public HttpRequestResult deleteAllUser(String room_name){
        HttpRequestResult requestResult = null;
        HashMap<String,String> basic = new HashMap<>();
        HashMap<String,String> params = new HashMap<>();

        basic.put("url",basicDomain+"/delete_room_alluser_api.do?");
        basic.put("method", "GET");
        params.put("room_name", room_name);

        try {
            requestResult = asyncTaskActivity.execute(basic,params).get();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return requestResult;
    }

    /* 방 삭제 */
    public HttpRequestResult deleteRoom(String room_name, String id){
        HttpRequestResult requestResult = null;
        HashMap<String,String> basic = new HashMap<>();
        HashMap<String,String> params = new HashMap<>();

        basic.put("url",basicDomain+"/delete_room_api.do?");
        basic.put("method", "GET");
        params.put("room_name", room_name);
        params.put("id", id);

        try {
            requestResult = asyncTaskActivity.execute(basic,params).get();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return requestResult;
    }


/* 방 참여 인원 불러오기 */
public HttpRequestResult deleteAttendUser(String room_name, String attend_id){
    HttpRequestResult requestResult = null;
    HashMap<String,String> basic = new HashMap<>();
    HashMap<String,String> params = new HashMap<>();

    basic.put("url",basicDomain+"/delete_attend_user_api.do?");
    basic.put("method", "PUT");
    params.put("room_name", room_name);
    params.put("attend_id", attend_id);

    try {
        requestResult = asyncTaskActivity.execute(basic,params).get();
    }
    catch (Exception e){
        e.printStackTrace();
    }
    return requestResult;
}

    /* 방 참여 인원 불러오기 */
    public HttpRequestResult getAttendUser(String room_name){
          HttpRequestResult requestResult = null;
          HashMap<String,String> basic = new HashMap<>();
          HashMap<String,String> params = new HashMap<>();

          basic.put("url",basicDomain+"/get_attend_user_api.do?");
          basic.put("method", "GET");
          params.put("room_name", room_name);

    try {
        requestResult = asyncTaskActivity.execute(basic,params).get();
    }
    catch (Exception e){
        e.printStackTrace();
    }
    return requestResult;
}

    /* 방 참여 인원 검사 */
    public HttpRequestResult isAttendUser(String room_name, String attend_id){
        HttpRequestResult requestResult = null;
        HashMap<String,String> basic = new HashMap<>();
        HashMap<String,String> params = new HashMap<>();

        basic.put("url",basicDomain+"/is_attend_user_api.do?");
        basic.put("method", "GET");
        params.put("room_name", room_name);
        params.put("attend_id", attend_id);

        try {
            requestResult = asyncTaskActivity.execute(basic,params).get();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return requestResult;
    }

    /* 방 참여 인원 등록 */
    public HttpRequestResult insertAttendUser(String room_name, String attend_id, String attend_master, String masterId){
        HttpRequestResult requestResult = null;
        HashMap<String,String> basic = new HashMap<>();
        HashMap<String,String> params = new HashMap<>();

        basic.put("url",basicDomain+"/insert_attend_user_api.do?");
        basic.put("method", "PUT");
        params.put("room_name", room_name);
        params.put("attend_id", attend_id);
        params.put("attend_master", attend_master);
        params.put("master_id", masterId);

        try {
            requestResult = asyncTaskActivity.execute(basic,params).get();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return requestResult;
    }

    /* 방 제목 중복 검사 */
    public HttpRequestResult roomNameCheck(String roomName){
        HttpRequestResult requestResult = null;
        HashMap<String,String> basic = new HashMap<>();
        HashMap<String,String> params = new HashMap<>();

        basic.put("url",basicDomain+"/room_name_check_api.do?");
        basic.put("method", "GET");
        params.put("room_name", roomName);

        try {
            requestResult = asyncTaskActivity.execute(basic,params).get();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return requestResult;
    }


    /* 방 목록 불러오기 */
    public HttpRequestResult selectRoomList(){
        HttpRequestResult requestResult = null;
        HashMap<String,String> basic = new HashMap<>();
        HashMap<String,String> params = new HashMap<>();

        basic.put("url",basicDomain+"/room_view_api.do?");
        basic.put("method", "GET");

        try {
            requestResult = asyncTaskActivity.execute(basic,params).get();
            //json으로 list가 들어가야함.
            Log.d("출력",requestResult.toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return requestResult;
    }

    /* 방 생성 */
    public HttpRequestResult createRoom(String id, String roomName, String roomPwd){
        HttpRequestResult requestResult = null;
        HashMap<String,String> basic = new HashMap<>();
        HashMap<String,String> params = new HashMap<>();

        basic.put("url",basicDomain+"/create_room_api.do?");
        basic.put("method", "PUT");

        params.put("id",id);
        params.put("room_name", roomName);
        params.put("room_pwd", roomPwd);

        try {
            requestResult = asyncTaskActivity.execute(basic,params).get();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return requestResult;
    }

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
    public HttpRequestResult insertSchedule(String id, String scheduleTitle, String x, String y, String color){
        HttpRequestResult requestResult = null;
        HashMap<String,String> basic = new HashMap<>();
        HashMap<String,String> params = new HashMap<>();

        basic.put("url",basicDomain+"/schedule_insert_api.do?");
        basic.put("method", "PUT");

        params.put("id",id);
        params.put("schedule_title",scheduleTitle);
        params.put("x",x+"");
        params.put("y",y+"");
        params.put("color", color);

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

    public HttpRequestResult joinMemberInfo(String id, String pwd){
        HttpRequestResult requestResult = null;
        HashMap<String,String> basic = new HashMap<>();
        HashMap<String,String> params = new HashMap<>();

        basic.put("url",basicDomain+"/join_api.do?");
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