package com.example.samsung.jomoyeo1.room;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samsung.jomoyeo1.R;
import com.example.samsung.jomoyeo1.dbconnection.HttpRequestResult;
import com.example.samsung.jomoyeo1.dbconnection.ServerService;
import com.example.samsung.jomoyeo1.main.CombineSchedule;
import com.example.samsung.jomoyeo1.preference.UserPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RoomViewActivity  extends Activity{

    Context mContext = this;

    String masterId;
    String roomName;
    String backActivity;

    TextView roomNameView;
    HttpRequestResult requestResult;

    /* 현재 참여하는 유저 정보를 받는 필드 */
    ArrayList<String> al = new ArrayList<String>();
    int userCount=1;
    JSONArray jsonArray;

    //정보를 저장하는 layout 변수
    TextView userCountView;
    TextView attendIdList;
    TextView masterTextView;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_view);

        //앞에서 put 한 listView의 내용을 받음
        Intent intent = getIntent();
        masterId = intent.getStringExtra("id");   //방장
        roomName = intent.getStringExtra("room_name");  //방제목
        backActivity = intent.getStringExtra("back");   //이전 액티비티

        attendIdList = (TextView)findViewById(R.id.attendIdList);

        //방장일 경우에만 방 삭제 버튼 보여주기.
        if(!UserPreference.getInstance().getId(mContext).equals(masterId)){
            findViewById(R.id.deleteRoomButton).setVisibility(View.GONE);
        }
        else{
            findViewById(R.id.leaveRoomButton).setVisibility(View.GONE);
        }
        userCountView = (TextView)findViewById(R.id.userCountView);
        masterTextView = (TextView)findViewById(R.id.masterTextView);
        roomNameView = (TextView)findViewById(R.id.roomNameView);
        addRoomUser();

        /* UI에 참여자 명수와 목록을 띄운다. */
        setRoomNameTextView();
        setMasterTextView();
        setTextUserCount();
        setTextAttendIdView();
    }

    /* 참여 인원 목록 받아오기 */
    public void addRoomUser(){
        ServerService serverService = new ServerService(mContext);
        requestResult = serverService.getAttendUser(roomName);
        userCount = requestResult.getResultJsonArray().length();   // 참여 인원 명 수
        /* 방장이 나가서 방이 삭제되면 강제로 나가기함. */
        if(userCount == 0){
            Toast.makeText(mContext, "존재하지 않는 방입니다.", Toast.LENGTH_LONG).show();
            Intent restartIntent = new Intent(mContext, SearchActivity.class);
            startActivity(restartIntent);
            finish();
        }
        try {
            jsonArray = (JSONArray) requestResult.getResultJsonArray();//User 목록
            Log.d("출력을해보자",jsonArray.toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        al.clear();
        for(int i = 0; i < userCount; i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                al.add(jsonObject.getString("attend_id"));
                Log.d("출력al", al.toString());
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /* 현재 몇 명이 방에 있는지 보여줌 */
    public void setTextUserCount(){
        userCountView.setText("참여자 목록 "+"("+userCount+")");
    }

    /* master이름을 텍스트뷰에 띄워줌 */
    public void setMasterTextView(){
        masterTextView.setText(masterId);
    }

    /* room name을 텍스트뷰에 띄워줌 */
    public void setRoomNameTextView(){
        roomNameView.setText(roomName);
    }

    /* 현재 참여자 목록을 보여줌 */
    public String setTextAttendIdView(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i< al.size(); i++) {
            sb.append(al.get(i)+"\n");
        }
        sb.deleteCharAt(sb.length()-1);
        attendIdList.setText(sb.toString());
        return sb.toString();
    }

    /* fresh buton 리스너 */
    public void freshButtonListener(View view){
        addRoomUser();
        setTextUserCount();
        setTextAttendIdView();
    }

    /* 방 탈퇴시 DB에서 삭제하고 나가기 */
    public void leaveRoomUser(View view){
        ServerService deleteUser = new ServerService(mContext);
        if(isMasterUser()==true){   //방장이다.
            requestResult = deleteUser.deleteRoom(roomName, masterId);//방 삭제
            ServerService withdrawalService = new ServerService(mContext);
            requestResult = withdrawalService.deleteAllUser(roomName);      //방에 있는 모든 유저 삭제.
        }
        else{   //일반 유저일때.
            requestResult = deleteUser.deleteAttendUser(roomName, UserPreference.getInstance().getId(mContext));    //탈퇴하는 유저만 삭제
        }
        Intent openTheDoor = new Intent(mContext, SearchActivity.class);
        startActivity(openTheDoor);
        finish();
    }

    /* 방장인지 확인하기 */
    public boolean isMasterUser(){
        if(!UserPreference.getInstance().getId(mContext).equals(masterId)){
            Log.d("방장", "아님");
            return false;
        }
        else{
            return true;
        }
    }

    public void createRoomButtonListener(View view){
        Intent createIntent = new Intent(mContext, CreationActivity.class);
        startActivity(createIntent);
        finish();
    }

    public void combineScheduleButtonListener(View view){
        Intent combineScheduleIntent = new Intent(mContext, CombineSchedule.class);
        combineScheduleIntent.putExtra("AttendUserNameString", setTextAttendIdView());
        startActivity(combineScheduleIntent);
    }

    /* back 버튼 눌렀을 때 이전화면(방 목록)으로 돌아가기 */
    @Override
    public void onBackPressed() {
        Toast.makeText(mContext, "방 종료.", Toast.LENGTH_LONG).show();
        if(backActivity.equals("AttendActivity")){
            Intent backIntent = new Intent(mContext, AttendActivity.class);
            startActivity(backIntent);
            finish();
        }
        else {
            Intent backIntent = new Intent(mContext, SearchActivity.class);
            startActivity(backIntent);
            finish();
        }
    }
}
