package com.example.samsung.jomoyeo.room;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.samsung.jomoyeo.R;
import com.example.samsung.jomoyeo.main.MainActivity;
import com.example.samsung.jomoyeo.preference.UserPreference;
import com.example.samsung.jomoyeo.server.HttpRequestResult;
import com.example.samsung.jomoyeo.server.ServerService;

/*
 * 2018-03-27
 * 방 생성 화면
 */

public class CreationActivity extends Activity {

    Context mContext = this;
    EditText roomNameEditText;
    EditText roomPwdEditText;
    Button createButton;

    String roomName;
    String roomPwd;
    String id;
    HttpRequestResult requestResult;
    String backActivity;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation);

        Intent intent = getIntent();
        backActivity = intent.getStringExtra("backIntent");   //이전 액티비티
        roomNameEditText = (EditText)findViewById(R.id.roomNameEditText);
        roomPwdEditText = (EditText)findViewById(R.id.roomPwdEditText);
        createButton = (Button)findViewById(R.id.createButton);
        id = UserPreference.getInstance().getId(mContext);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                roomName = roomNameEditText.getText().toString();
                roomPwd = roomPwdEditText.getText().toString();

                if(roomName.equals("")){
                    Toast.makeText(mContext, "방 제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(roomPwd.equals("")){
                    Toast.makeText(mContext, "방 비밀번호를 입력해주세요.", Toast.LENGTH_LONG).show();
                }
                else {
                    //방제목 중복 검사
                    boolean roomNameDuplicate = false;
                    ServerService duplicateService = new ServerService(mContext);
                    requestResult = duplicateService.roomNameCheck(roomName);
                    if(requestResult.getResultCode()==200){
                        roomNameDuplicate = true;
                    }
                    else{
                        Toast.makeText(mContext, "중복된 방 제목입니다.", Toast.LENGTH_LONG).show();
                    }
                    //중복 검사 통과 후
                    if(roomNameDuplicate==true) {
                        ServerService serverService = new ServerService(mContext);
                        requestResult = serverService.createRoom(id, roomName, roomPwd);

                        //방을 생성한다.
                        if (requestResult.getResultCode() == 200) {
                            ServerService insertUser = new ServerService(mContext);
                            requestResult = insertUser.insertAttendUser(roomName, id, "1", id);  //master는 1로 저장.
                            Toast.makeText(mContext, "방 생성", Toast.LENGTH_LONG);
                            Intent Intent = new Intent(mContext, SearchActivity.class);
                            startActivity(Intent);
                            finish();
                        }
                    }
                }
            }
        });
    }

    /* back 버튼 눌렀을 때 이전화면(메인)으로 돌아가기 */
    @Override
    public void onBackPressed() {

        if(backActivity.equals("MainActivity")) {
            Intent backIntent = new Intent(mContext, MainActivity.class);
            startActivity(backIntent);
            finish();
        }
        else if(backActivity.equals("SearchActivity")){
            Intent backIntent = new Intent(mContext, SearchActivity.class);
            startActivity(backIntent);
            finish();
        }
        else{
            Intent backIntent = new Intent(mContext, AttendActivity.class);
            startActivity(backIntent);
            finish();
        }
    }
}
