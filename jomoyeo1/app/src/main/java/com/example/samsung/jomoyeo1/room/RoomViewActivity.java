package com.example.samsung.jomoyeo1.room;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.samsung.jomoyeo1.R;
import com.example.samsung.jomoyeo1.dbconnection.HttpRequestResult;
import com.example.samsung.jomoyeo1.dbconnection.ServerService;
import com.example.samsung.jomoyeo1.preference.UserPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RoomViewActivity  extends Activity{

    Context mContext = this;

    String masterId;
    String roomName;

    TextView roomNameView;
    HttpRequestResult requestResult;

    /* 현재 참여하는 유저 정보를 받는 필드 */
    ArrayList<String> al = new ArrayList<String>();
    int userCount = 0;
    JSONArray jsonArray;

    //정보를 저장하는 layout 변수
    TextView userCountView;
    TextView attendIdList;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_view);

        //앞에서 put 한 listView의 내용을 받음
        Intent intent = getIntent();
        masterId = intent.getStringExtra("id");   //방장
        roomName = intent.getStringExtra("room_name");  //방제목

        roomNameView = (TextView)findViewById(R.id.roomName);
        roomNameView.setText(roomName);

        attendIdList = (TextView)findViewById(R.id.attendIdList);

        //방장일 경우에만 방 삭제 버튼 보여주기.
        if(!UserPreference.getInstance().getId(mContext).equals(masterId)){
            findViewById(R.id.deleteRoomButton).setVisibility(View.GONE);
        }
        userCountView = (TextView)findViewById(R.id.userCountView);
        addRoomUser();

        /* UI에 참여자 명수와 목록을 띄운다. */
        setTextUserCount();
        setTextAttendIdView();
    }

    /* 참여 인원 목록 받아오기 */
    public void addRoomUser(){
        ServerService serverService = new ServerService(mContext);
        requestResult = serverService.getAttendUser(roomName);

        userCount = requestResult.getResultJsonArray().length();   // 참여 인원 명 수

        try {
            jsonArray = (JSONArray) requestResult.getResultJsonArray();//User 목록
            Log.d("출력을해보자",jsonArray.toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        for(int i = 0; i < userCount; i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                al.add(jsonObject.getString("attend_id"));
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /* 현재 몇 명이 방에 있는지 보여줌 */
    public void setTextUserCount(){
        userCountView.setText(userCount+"명");
    }

    /* 현재 참여자 목록을 보여줌 */
    public void setTextAttendIdView(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i< al.size(); i++) {
            sb.append(al.get(i)+"\n");
        }
        attendIdList.setText(sb.toString());
    }

    /* fresh buton 리스너 */
    public void freshButtonListener(View view){
        userCountView.setText("");
        attendIdList.setText("");
        addRoomUser();
        setTextUserCount();
        setTextAttendIdView();
    }

    /* 방 탈퇴시 DB에서 삭제하고 나가기 */
    public void deleteRoomUser(View view){
        ServerService deleteUser = new ServerService(mContext);

        if(isMasterUser()==true){   //방장이다.
                                    //방 삭제
        }
        else{
            requestResult = deleteUser.deleteAttendUser(roomName, UserPreference.getInstance().getId(mContext));    //나가는 유저 삭제
            Intent openTheDoor = new Intent(mContext, SearchActivity.class);
            startActivity(openTheDoor);
            finish();
        }
    }

    /* 방장인지 확인하기 */
    public boolean isMasterUser(){
        ServerService isMaster = new ServerService(mContext);
        requestResult = isMaster.isMasterUser(roomName, UserPreference.getInstance().getId(mContext));
        if(requestResult.getResultCode() == 200){
            Log.d("방장", "아님");
            return false;
        }
        else{
            return true;
        }
    }

    /* back 버튼 눌렀을 때 이전화면(방 목록)으로 돌아가기 */
    @Override public void onBackPressed() {
        Intent backIntent = new Intent(mContext, SearchActivity.class);
        startActivity(backIntent);
        finish();
    }
}
