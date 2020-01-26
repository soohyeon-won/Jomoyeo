package com.example.samsung.jomoyeo.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.samsung.jomoyeo.R;
import com.example.samsung.jomoyeo.main.BackPressCloseHandler;
import com.example.samsung.jomoyeo.main.MainActivity;
import com.example.samsung.jomoyeo.preference.UserPreference;
import com.example.samsung.jomoyeo.server.HttpRequestResult;
import com.example.samsung.jomoyeo.server.ServerService;

public class LoginActivity extends Activity {

    HttpRequestResult requestResult;

    Context context = this;
    EditText idEditText;
    EditText pwdEditText;
    Button loginButton;
    Button joinButton;

    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 유저 정보 있으면 메인으로 페이지 넘김
        if(!UserPreference.getInstance().getId(context).equals("-1")){
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
            finish();
        }

        idEditText = (EditText) (findViewById(R.id.idEditText));
        pwdEditText = (EditText) (findViewById(R.id.pwdEditText));

        loginButton = (Button) (findViewById(R.id.loginButton));
        joinButton = (Button)(findViewById(R.id.joinButton));

        backPressCloseHandler = new BackPressCloseHandler(this);
    }


    public void click(View view){
        switch(view.getId()){

            case R.id.joinButton :
                Intent goJoin = new Intent(context, JoinActivity.class);
                startActivity(goJoin);
                finish();
                break;

            case R.id.loginButton :
                String id = idEditText.getText().toString();
                String pwd = pwdEditText.getText().toString();
                if(id.equals("")){
                    Toast.makeText(context, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(!(id.equals("")) && pwd.equals("")){
                    Toast.makeText(context, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    /* login */
                    ServerService serverService = new ServerService(context);
                    requestResult = serverService.getMemberInfo(id, pwd);
                    int requestNumber = requestResult.getResultCode();
                    switch (requestNumber) {
                        /* 성공시 */
                        case 200:
                            Toast.makeText(context, "로그인 성공.", Toast.LENGTH_LONG).show();
                            /* user preference 에 저장 */
                            UserPreference.getInstance().setId(context, id);
                            Log.d("UserPreferenct.getId()", UserPreference.getInstance().getId(context));
                            /* main으로 이동 */
                            Intent intent = new Intent(context, MainActivity.class);
                            startActivity(intent);
                            finish();
                            break;

                        case 406:
                            Toast.makeText(context, "비밀번호가 틀렸습니다.", Toast.LENGTH_LONG).show();
                            Log.d("비번틀렸을때", UserPreference.getInstance().getId(context));
                            break;

                        case 204:
                            Toast.makeText(context, "존재하지 않는 아이디입니다.", Toast.LENGTH_LONG).show();
                            Log.d("존재하지 않을때", UserPreference.getInstance().getId(context));
                            break;
                    }
                    break;
                }
        }
    }

    /* 뒤로 가기 두번 클릭시 종료 */
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

    // hex string to byte[]
    public static byte[] hexToByteArray(String hex) {
        if (hex == null || hex.length() == 0) {
            return null;
        }
        byte[] ba = new byte[hex.length() / 2];
        for (int i = 0; i < ba.length; i++) {
            ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return ba;
    }


    // byte[] to hex sting
    public static String byteArrayToHex(byte[] ba) {
        if (ba == null || ba.length == 0) {
            return null;
        }
        StringBuffer sb = new StringBuffer(ba.length * 2);
        String hexNumber;
        for (int x = 0; x < ba.length; x++) {
            hexNumber = "0" + Integer.toHexString(0xff & ba[x]);

            sb.append(hexNumber.substring(hexNumber.length() - 2));
        }
        return sb.toString();
    }


}