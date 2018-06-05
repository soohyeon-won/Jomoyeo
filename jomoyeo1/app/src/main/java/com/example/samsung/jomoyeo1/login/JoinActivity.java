package com.example.samsung.jomoyeo1.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.samsung.jomoyeo1.R;
import com.example.samsung.jomoyeo1.dbconnection.HttpRequestResult;
import com.example.samsung.jomoyeo1.dbconnection.ServerService;
import com.example.samsung.jomoyeo1.main.MainActivity;
import com.example.samsung.jomoyeo1.preference.UserPreference;

/**
 * Created by samsung on 2018-03-28.
 */

public class JoinActivity extends Activity {

    EditText idEditText;
    EditText pwdEditText;

    Context context = this;
    String id, pwd;
    HttpRequestResult requestResult;

    String checkId;
    boolean idCheckFlag=false;

    Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        idEditText = (EditText)(findViewById(R.id.idEditText));
        pwdEditText = (EditText)(findViewById(R.id.pwdEditText));
    }

    //회원 가입 메소드
    public void onClick(View view){
        id = idEditText.getText().toString();
        pwd = pwdEditText.getText().toString();
        if(!checkId.equals(id)){
            idCheckFlag = false;
            Toast.makeText(context, "id 중복확인을 완료해주세요.", Toast.LENGTH_SHORT).show();

        }
        else if(id.equals("")||pwd.equals("")){
            Toast.makeText(context, "모든 정보를 입력해주세요!", Toast.LENGTH_SHORT).show();
        }
        else if(!(id.equals("")&&pwd.equals(""))&&idCheckFlag ){
            ServerService serverService = new ServerService(context);
            requestResult = serverService.joinMemberInfo(id, pwd);

            if (requestResult.getResultCode() == 200) {
                Toast.makeText(context, "회원가입 성공!", Toast.LENGTH_SHORT).show();
                UserPreference.getInstance().setId(context, id);
                //회원가입이 성공하면 mainActivity로 이동한다.
                Intent intent = new Intent(JoinActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
            else if (requestResult.getResultCode() == 226) {
                Toast.makeText(context, "중복된 ID입니다!", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void checkDuplicate(View view){
        id = idEditText.getText().toString();
        checkId=id;
        ServerService serverService = new ServerService(context);
        requestResult = serverService.duplicateCheckService(id);
        if(!id.equals("")) {
            if (requestResult.getResultCode() == 200) {
                Toast.makeText(context, "사용할 수 있는 아이디입니다.", Toast.LENGTH_SHORT).show();
                idCheckFlag=true;
            }
            else if (requestResult.getResultCode() == 226) {
                Toast.makeText(context, "중복된 ID입니다!", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(context, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    /* back 버튼 눌렀을 때 이전화면(로그인창)으로 돌아가기 */
    @Override public void onBackPressed() {
        Intent backIntent = new Intent(mContext, LoginActivity.class);
        startActivity(backIntent);
        finish();
    }
}
