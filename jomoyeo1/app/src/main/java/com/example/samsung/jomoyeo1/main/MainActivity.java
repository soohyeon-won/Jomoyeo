package com.example.samsung.jomoyeo1.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.samsung.jomoyeo1.R;
import com.example.samsung.jomoyeo1.login.LoginActivity;
import com.example.samsung.jomoyeo1.preference.UserPreference;
import com.example.samsung.jomoyeo1.room.AttendActivity;
import com.example.samsung.jomoyeo1.room.CreationActivity;
import com.example.samsung.jomoyeo1.room.SearchActivity;
/*
 * 로그인 후 이동하는 메인 화면
 * 뒤로가기 누르면 종료 구현.
 */

public class MainActivity extends Activity {

    Context context = this;

    private BackPressCloseHandler backPressCloseHandler;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backPressCloseHandler = new BackPressCloseHandler(this);

    }

    /* 각 창으로 이동하는 메소드. */
    public void goRoom(View view){

        switch(view.getId()){
            /* 참가한 방으로 이동 */
            case R.id.attendRoom :
                Intent intent = new Intent(context, AttendActivity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent);
                finish();
                break;

            /* 스케쥴 생성으로 이동 */
            case R.id.scheduleRegistration:
                Intent intent2 = new Intent(context, ScheduleActivity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent2); // 다음 화면으로 넘어간다.
                finish();
                break;

             /* 방 검색으로 이동 */
            case R.id.listRoom:
                Intent intent3 = new Intent(context, SearchActivity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent3); // 다음 화면으로 넘어간다
                finish();
                break;

            /* 방 생성으로 이동 */
            case R.id.creationRoom:
                Intent intent4 = new Intent(context, CreationActivity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent4); // 다음 화면으로 넘어간다
                finish();
                break;

            /* logout 버튼 */
            case R.id.logoutButton:
                UserPreference.getInstance().setId(context, "-1");
                Log.d("logout", "로그아웃?"+UserPreference.getInstance().getId(context));
                Intent intent5 = new Intent(getApplicationContext(), LoginActivity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent5); // 다음 화면으로 넘어간다
                finish();
                break;

        }
    }

    /**
     * toast로 알리면서 종료
     */
    @Override public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

}