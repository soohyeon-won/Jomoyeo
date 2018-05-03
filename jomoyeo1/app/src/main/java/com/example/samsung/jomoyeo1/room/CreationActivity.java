package com.example.samsung.jomoyeo1.room;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.samsung.jomoyeo1.R;
import com.example.samsung.jomoyeo1.dbconnection.HttpRequestResult;
import com.example.samsung.jomoyeo1.dbconnection.ServerService;
import com.example.samsung.jomoyeo1.main.MainActivity;
import com.example.samsung.jomoyeo1.preference.UserPreference;

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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation);

        roomNameEditText = (EditText)findViewById(R.id.roomNameEditText);
        roomPwdEditText = (EditText)findViewById(R.id.roomPwdEditText);
        createButton = (Button)findViewById(R.id.createButton);
        id = UserPreference.getInstance().getId(mContext);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                roomName = roomNameEditText.getText().toString();
                roomPwd = roomPwdEditText.getText().toString();

                ServerService serverService = new ServerService(mContext);
                requestResult = serverService.createRoom(id, roomName, roomPwd);

                if(requestResult.getResultCode() != 200){   // insert 못함.
                    Toast.makeText(mContext, "방 생성", Toast.LENGTH_LONG);
                }
                else{
                    Toast.makeText(mContext, "이미 존재하는 방 제목입니다.", Toast.LENGTH_LONG);
                }
                Intent Intent = new Intent(mContext, SearchActivity.class);
                startActivity(Intent);
                finish();
            }
        });
    }

    /* back 버튼 눌렀을 때 이전화면(메인)으로 돌아가기 */
    @Override public void onBackPressed() {
        Intent backIntent = new Intent(mContext, MainActivity.class);
        startActivity(backIntent);
        finish();
    }
}
